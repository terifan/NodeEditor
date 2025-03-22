package examples;

import java.awt.image.BufferedImage;
import org.terifan.nodeeditor.widgets.SliderProperty;
import javax.swing.JFrame;
import static org.terifan.nodeeditor.Direction.IN;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.ValueProperty;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.DefaultColors.GRAY;
import static org.terifan.nodeeditor.Styles.DefaultColors.GREEN;
import static org.terifan.nodeeditor.Styles.DefaultColors.YELLOW;
import org.terifan.nodeeditor.Styles.DefaultNodeColors;
import org.terifan.nodeeditor.widgets.ButtonProperty;
import org.terifan.nodeeditor.widgets.ImageProperty;


public class TestMandelExample
{
	public static void main(String... args)
	{
		try
		{
			BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
			for (int y = 0, m = 1000; y < 200; y++)
			{
				for (int x = 0; x < 200; x++)
				{
					int c = iterate(x, y, m);
					image.setRGB(x, y, c == m ? 0 : 0xFF000000 | c);
				}
			}

			Node n;
			Property p;

			NodeModel model = new NodeModel()
				.addNode(new Node("Mandelbrot")
					.setTitleColor(DefaultNodeColors.GREEN)
					.setBounds(-200, -50, 150, 0)
					.addProperty(new SliderProperty("Limit", 1000, 1).setId("r"))
					.addProperty(new ValueProperty("Iterations").setProvides("r").addConnector(OUT, GRAY))
				)
				.addNode(n=new Node("Colors")
					.setBounds(0, -50, 150, 0)
					.addProperty(new ValueProperty("Color").setProvides("r", "g", "b").addConnector(OUT, YELLOW).setId("rgb"))
					.addProperty(p=new SliderProperty("Red", 0, 1, 0).setId("r").addConnector(IN, GRAY))
					.addProperty(new SliderProperty("Green", 0, 1, 0.5).setId("g").addConnector(IN, GRAY))
					.addProperty(new SliderProperty("Blue", 0, 1, 0.75).setId("b").addConnector(IN, GRAY))
				)
				.addNode(new Node("RenderOutput")
					.setTitleColor(DefaultNodeColors.DARKRED)
					.setBounds(200, 0, 220, 0)
					.addProperty(new ValueProperty("Color").setId("rgb").addConnector(IN))
					.addProperty(new ValueProperty("Coordinate").setId("rgb").addConnector(IN, Styles.DefaultColors.PURPLE))
					.addProperty(new ImageProperty("", 200, 200).setId("output").setConsumes("rgb"))
					.addProperty(new ButtonProperty("Run").setIcon(Styles.DefaultIcons.RUN).setCommand("run"))
				)
				.addNode(new Node("Coordinates")
					.setTitleColor(DefaultNodeColors.PURPLE)
					.setBounds(-200, 150, 220, 0)
					.addProperty(new ValueProperty("XY").setId("rgb").addConnector(OUT, Styles.DefaultColors.PURPLE))
				)
				.addConnection(0, 1, 1, 1)
				.addConnection(0, 1, 1, 2)
				.addConnection(0, 1, 1, 3)
				.addConnection(1, 0, 2, 0)
				.addConnection(3, 0, 2, 1)
				;

			System.out.println(p.getConnector(IN).getConnectedProperties());
			System.out.println(model.getPropertiesConnectingTo(n.getProperty(1)));

			NodeEditorPane editor = new NodeEditorPane(model)
				.center();

			editor.bind("run", ctx ->
			{
				System.out.println("run");

				ImageProperty ip = ctx.getNode().getProperty("output");

				ip.execute(ctx);
			});

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


	private static int iterate(int aX, int aY, int max_iteration)
	{
		double x0 = -2 + (2.47 * aX / 200.0); //scaled x coordinate of pixel (scaled to lie in the Mandelbrot X scale (-2.00, 0.47))
		double y0 = -1.12 + (2.24 * aY / 200.0); //scaled y coordinate of pixel (scaled to lie in the Mandelbrot Y scale (-1.12, 1.12))
		double x = 0.0;
		double y = 0.0;
		int iteration = 0;
		while (x * x + y * y <= 4 && iteration < max_iteration)
		{
			double xtemp = x * x - y * y + x0;
			y = 2 * x * y + y0;
			x = xtemp;
			iteration++;
		}
		return iteration;
	}
}
