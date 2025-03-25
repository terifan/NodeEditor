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
import org.terifan.nodeeditor.Direction;
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
import org.terifan.vecmath.Vec2d;
import org.terifan.vecmath.Vec4d;


public class MandelbrotExample
{
	private static NodeFunction mandelbrot = (aContext, self) ->
	{
		Vec2d coord = (Vec2d)self.getNode().getProperty("coord").execute(aContext);
		int limit = ((Number)self.getNode().getProperty("limit").execute(aContext)).intValue();
		double sx = (Double)self.getNode().getProperty("x").execute(aContext);
		double sy = (Double)self.getNode().getProperty("y").execute(aContext);
		double zoom = (Double)self.getNode().getProperty("zoom").execute(aContext);

		double x0 = sx + (2 * (2*coord.x - 1))/zoom;
		double y0 = sy + (2 * (2*coord.y - 1))/zoom;
		int iteration = 0;
		for (double x = 0, y = 0; x * x + y * y <= 4 && iteration < limit; iteration++)
		{
			double xtemp = x * x - y * y + x0;
			y = 2 * x * y + y0;
			x = xtemp;
		}

		return iteration >= limit ? -1 : iteration / (double)limit;
	};

	private static NodeFunction palette = (aContext, self) ->
	{
		double it = (Double)self.getNode().getProperty("iterations").execute(aContext);
		if (it < 0)
		{
			return new Vec4d();
		}

		double rf = (Double)self.getNode().getProperty("rf").execute(aContext);
		double gf = (Double)self.getNode().getProperty("gf").execute(aContext);
		double bf = (Double)self.getNode().getProperty("bf").execute(aContext);
		double sf = (Double)self.getNode().getProperty("sf").execute(aContext);

		return new Vec4d(rf, gf, bf, 0).scale(sf * it).mod(1).add(0, 0, 0, 1);
	};

	private static NodeFunction buttonAction = (aContext, self) ->
	{
		ImageProperty ip = aContext.getEditor().getModel().getProperty("image");
		ValueProperty cp = aContext.getEditor().getModel().getProperty("coordinate");

		ip.setImage(new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB));

		for (int y = 0; y < 400; y++)
		{
			for (int x = 0; x < 400; x++)
			{
				cp.setValue(new Vec2d(x / 400.0, y / 400.0));

				Vec2d coord = (Vec2d)self.getNode().getProperty("coord").execute(aContext);
				Vec4d argb = (Vec4d)self.getNode().getProperty("argb").execute(aContext);

				ip.getImage().setRGB((int)(coord.x * 400 + 0.5), (int)(coord.y * 400 + 0.5), argb.intValue());
			}
		}

		aContext.getEditor().repaint();
		return null;
	};


	public static void main(String... args)
	{
		try
		{
			NodeModel __model = new NodeModel()
				.addComponent(new Node("Mandelbrot")
					.setTitleBackground(DefaultNodeColors.BROWN)
					.setBounds(-500, 100, 150, 0)
					.addProperty(new ValueProperty("Iterations").addConnector(OUT, GRAY).setProducer("mandelbrot"))
					.addProperty(new ValueProperty("Coordinate").setId("coord").addConnector(OUT, PURPLE).bind("coordinate"))
					.addProperty(new SliderProperty("X").setRange(-2, 2, 0.06755, 0.0001).setId("x"))
					.addProperty(new SliderProperty("Y").setRange(-2, 2, 0.635, 0.0001).setId("y"))
					.addProperty(new SliderProperty("Zoom", 30000, 1).setId("zoom"))
					.addProperty(new SliderProperty("Limit", 200000, 1).setId("limit"))
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
					.setBounds(400, 100, 420, 0)
					.addProperty(new ValueProperty("Color").setId("argb").addConnector(IN))
					.addProperty(new ValueProperty("Coordinate").setId("coord").addConnector(IN, PURPLE))
					.addProperty(new ImageProperty("", 400, 400).bind("image"))
					.addProperty(new ButtonProperty("Run").setIcon(DefaultIcons.RUN).bind("run"))
				)
				.addComponent(SimpleNodesFactory.createSourceColor().setLocation(-250, -90))
				.addComponent(SimpleNodesFactory.createIntermediateMath().setLocation(-250, -250))
				.addComponent(SimpleNodesFactory.createIntermediateColorMix().setLocation(220, -140))
				.addComponent(SimpleNodesFactory.createSourceColorRGBA().setLocation(-250, 250))
				.addConnection(0, 0, 4, 4)
				.addConnection(0, 1, 2, 1)
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
