package examples;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.AbstractAction;
import org.terifan.nodeeditor.widgets.SliderProperty;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.terifan.nodeeditor.Context;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.ValueProperty;
import org.terifan.nodeeditor.NodeFunction;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.GRAY;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.PURPLE;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.YELLOW;
import org.terifan.nodeeditor.Styles.DefaultIcons;
import org.terifan.nodeeditor.Styles.DefaultNodeColors;
import org.terifan.nodeeditor.util.SimpleNodesFactory;
import org.terifan.nodeeditor.widgets.ButtonProperty;
import org.terifan.nodeeditor.widgets.ImageProperty;
import org.terifan.vecmath.Vec2i;
import org.terifan.vecmath.Vec4d;


public class MandelbrotExample
{
	private static NodeFunction mandelbrot = aContext ->
	{
		Vec2i coord = aContext.execute("coord");
		double size = aContext.execute("size");
		int limit = ((Number)aContext.execute("limit")).intValue();
		double sx = aContext.execute("x");
		double sy = aContext.execute("y");
		double zoom = aContext.execute("zoom");

		double x0 = sx + (2 * (2 * coord.x / size - 1)) / zoom;
		double y0 = sy + (2 * (2 * coord.y / size - 1)) / zoom;
		int iteration = 0;
		for (double x = 0, y = 0; x * x + y * y <= 4 && iteration < limit; iteration++)
		{
			double xtemp = x * x - y * y + x0;
			y = 2 * x * y + y0;
			x = xtemp;
		}

		return iteration >= limit ? -1 : iteration / (double)limit;
	};

	private static NodeFunction palette = aContext ->
	{
		double it = aContext.execute("iterations");
		if (it < 0)
		{
			return new Vec4d();
		}

		double rf = aContext.execute("rf");
		double gf = aContext.execute("gf");
		double bf = aContext.execute("bf");
		double sf = aContext.execute("sf");

		return new Vec4d(rf, gf, bf, 0).scale(sf * it).mod(1).add(0, 0, 0, 1);
	};

	private static NodeFunction buttonAction = aContext ->
	{
		new Thread(() ->
		{
			ImageProperty ip = aContext.find("image");
			ValueProperty cp = aContext.property("coord");
			int size = ((Double)aContext.execute("size")).intValue();

			BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

			ip.setImage(image);

			for (int y = 0; y < size; y++)
			{
				aContext.getNode().setTitle("Render %.1f%%".formatted((1 + y) * 100.0 / size));

				for (int x = 0; x < size; x++)
				{
					cp.setValue(new Vec2i(x, y));

					Context ctx = new Context(aContext.getEditor(), ip);
					Vec2i cp2 = ctx.execute("coord");
					Vec4d color = ctx.execute("color");
					ip.getImage().setRGB(cp2.x, cp2.y, color.intValue());

//					ip.execute(new Context(aContext.getEditor(), ip));
				}

				aContext.getEditor().repaint();
			}

			aContext.getNode().setTitle("Renderer");
		}).start();

		return null;
	};


	public static void main(String... args)
	{
		try
		{
			NodeModel __model = new NodeModel()
				.addComponent(new Node("Mandelbrot")
					.setTitleBackground(DefaultNodeColors.BROWN)
					.setBounds(-400, 100, 150, 0)
					.addProperty(new ValueProperty("Iterations").addConnector(OUT, GRAY).setProducer("mandelbrot"))
					.addProperty(new ValueProperty("Coordinate").addConnector(IN, PURPLE).setId("coord"))
					.addProperty(new ValueProperty("Size").setId("size").addConnector(IN, GRAY))
					.addProperty(new SliderProperty("X").setRange(-2, 2, 0.0675945, 0.0000001).setId("x"))
					.addProperty(new SliderProperty("Y").setRange(-2, 2, 0.6349410, 0.0000001).setId("y"))
					.addProperty(new SliderProperty("Zoom", 500000, 1).setId("zoom"))
					.addProperty(new SliderProperty("Limit", 300000, 1).setId("limit"))
				)
				.addComponent(new Node("Palette")
					.setTitleBackground(DefaultNodeColors.GREEN)
					.setBounds(0, -20, 150, 0)
					.addProperty(new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer("palette"))
					.addProperty(new SliderProperty("Red", 0.0, 0.01).setId("rf"))
					.addProperty(new SliderProperty("Green", 13.42, 0.01).setId("gf"))
					.addProperty(new SliderProperty("Blue", 30.26, 0.01).setId("bf"))
					.addProperty(new SliderProperty("Scale", 20.5, 0.01).setId("sf"))
					.addProperty(new ValueProperty("Iterations").setId("iterations").addConnector(IN, GRAY))
				)
				.addComponent(new Node("RenderOutput")
					.setTitleBackground(DefaultNodeColors.DARKRED)
					.setBounds(400, 100, 530, 0)
					.addProperty(new ValueProperty("Color").setId("color").addConnector(IN))
					.addProperty(new ValueProperty("Coordinate").setId("coord").addConnector(IN, PURPLE))
					.addProperty(new ImageProperty("", 512, 512).setId("image").bind("image"))
				)
				.addComponent(SimpleNodesFactory.createSourceColor().setLocation(-250, -90))
				.addComponent(SimpleNodesFactory.createIntermediateMath().setLocation(-250, -250))
				.addComponent(SimpleNodesFactory.createIntermediateColorMix().setLocation(220, -140))
				.addComponent(new Node("Renderer")
					.setTitleBackground(DefaultNodeColors.BROWN)
					.setBounds(-600, 400, 150, 0)
					.addProperty(new ValueProperty("Coordinate").setId("coord").addConnector(OUT, PURPLE))
					.addProperty(new SliderProperty("Size").setRange(0, 4096, 512, 1).setId("size").addConnector(OUT, GRAY))
					.addProperty(new ButtonProperty("Render").setIcon(DefaultIcons.RUN).bind("run"))
				)
				.addConnection(6, 1, 0, 2)
				.addConnection(6, 0, 0, 1)
				.addConnection(6, 0, 2, 1)
				.addConnection(0, 0, 4, 4)
				.addConnection(0, 0, 1, 5)
				.addConnection(3, 0, 5, 2)
				.addConnection(4, 0, 5, 1)
				.addConnection(1, 0, 5, 3)
				.addConnection(5, 0, 2, 0);

			__model.print();

			// -- debugging only, serialize/deserialize model to ensure it's stateless
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ObjectOutputStream dos = new ObjectOutputStream(baos))
			{
				dos.writeObject(__model);
			}
			NodeModel model;
			try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())))
			{
				model = (NodeModel)ois.readObject();
			}
			// --

			NodeEditorPane editor = new NodeEditorPane(model)
				.bind("palette", palette)
				.bind("mandelbrot", mandelbrot)
				.bind("run", buttonAction)
				.center();

			SimpleNodesFactory.install(editor);

			JToolBar toolbar = new JToolBar();

			toolbar.add(new AbstractAction("Math")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addComponent(SimpleNodesFactory.createIntermediateMath());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Mix")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addComponent(SimpleNodesFactory.createIntermediateColorMix());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Alpha")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addComponent(SimpleNodesFactory.createSourceAlpha());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Color")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addComponent(SimpleNodesFactory.createSourceColor());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("RGB")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addComponent(SimpleNodesFactory.createSourceColorRGB());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("RGBA")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addComponent(SimpleNodesFactory.createSourceColorRGBA());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Value")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addComponent(SimpleNodesFactory.createSourceValue());
					editor.repaint();
				}
			});

//			toolbar.add(new AbstractAction("SeparateColor")
//			{
//				@Override
//				public void actionPerformed(ActionEvent aE)
//				{
//					model.addComponent(SimpleNodesFactory.createSourceValue());
//					editor.repaint();
//				}
//			});
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(toolbar, BorderLayout.NORTH);
			panel.add(editor, BorderLayout.CENTER);

			JFrame frame = new JFrame();
			frame.add(panel);
			frame.setSize(1600, 1000);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}
}
