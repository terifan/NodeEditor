package examples;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.terifan.nodeeditor.widgets.SliderProperty;
import javax.swing.JFrame;
import static org.terifan.nodeeditor.Direction.IN;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.ValueProperty;
import static org.terifan.nodeeditor.Direction.OUT;
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

		double x0 = -2 + (4 * coord.x);
		double y0 = -2 + (4 * coord.y);
		int iteration = 0;
		for (double x = 0, y = 0; x * x + y * y <= 4 && iteration < limit;)
		{
			double xtemp = x * x - y * y + x0;
			y = 2 * x * y + y0;
			x = xtemp;
			iteration++;
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
		Vec4d v = new Vec4d(it, it, it, 0).scale(sf).scale(rf, gf, bf, 0).add(0, 0, 0, 1);
		v.x %= 1;
		v.y %= 1;
		v.z %= 1;
		return v;
	};

	private static NodeFunction buttonAction = (aContext, self) ->
	{
		ImageProperty ip = aContext.getEditor().getModel().getProperty("image");
		ValueProperty cp = aContext.getEditor().getModel().getProperty("coordinate");

		ip.setImage(new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB));

		for (int y = 0; y < 200; y++)
		{
			for (int x = 0; x < 200; x++)
			{
				cp.setValue(new Vec2d(x / 200.0, y / 200.0));

				Vec2d coord = (Vec2d)self.getNode().getProperty("coord").execute(aContext);
				Vec4d argb = (Vec4d)self.getNode().getProperty("argb").execute(aContext);

				ip.getImage().setRGB((int)(coord.x * 200 + 0.5), (int)(coord.y * 200 + 0.5), argb.intValue());
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
				.addNode(new Node("Mandelbrot")
					.setTitleBackground(DefaultNodeColors.GREEN)
					.setBounds(-200, 0, 150, 0)
					.addProperty(new ValueProperty("Iterations").addConnector(OUT, GRAY).setProducer("mandelbrot"))
					.addProperty(new ValueProperty("Coordinate").setId("coord").addConnector(OUT, PURPLE).bind("coordinate"))
					.addProperty(new SliderProperty("Limit", 5000, 1).setId("limit"))
				)
				.addNode(new Node("Palette")
					.setBounds(0, -150, 150, 0)
					.addProperty(new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer("palette"))
					.addProperty(new SliderProperty("Red", 2.3, 0.01).setId("rf"))
					.addProperty(new SliderProperty("Green", 9.2, 0.01).setId("gf"))
					.addProperty(new SliderProperty("Blue", 17.5, 0.01).setId("bf"))
					.addProperty(new SliderProperty("Scale", 20.5, 0.01).setId("sf"))
					.addProperty(new ValueProperty("Iterations").setId("iterations").addConnector(IN, GRAY))
				)
				.addNode(new Node("RenderOutput")
					.setTitleBackground(DefaultNodeColors.DARKRED)
					.setBounds(200, 50, 220, 0)
					.addProperty(new ValueProperty("Color").setId("argb").addConnector(IN))
					.addProperty(new ValueProperty("Coordinate").setId("coord").addConnector(IN, PURPLE))
					.addProperty(new ImageProperty("", 200, 200).bind("image"))
					.addProperty(new ButtonProperty("Run").setIcon(DefaultIcons.RUN).bind("run"))
				)
				.addNode(SimpleNodesFactory.createSourceColorRGBA())
				.addNode(SimpleNodesFactory.createIntermediateMath())
				.addNode(SimpleNodesFactory.createIntermediateColorMix())
				.addConnection(0, 0, 1, 5)
				.addConnection(0, 1, 2, 1)
				.addConnection(1, 0, 2, 0);

			// -- serialize/deserialize model to ensure it's stateless
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

			JFrame frame = new JFrame();
			frame.add(editor);
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
