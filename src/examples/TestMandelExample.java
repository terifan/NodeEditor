package examples;

import java.awt.image.BufferedImage;
import org.terifan.nodeeditor.widgets.SliderProperty;
import javax.swing.JFrame;
import org.terifan.nodeeditor.Context;
import static org.terifan.nodeeditor.Direction.IN;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.ValueProperty;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.DefaultColors.GRAY;
import static org.terifan.nodeeditor.Styles.DefaultColors.YELLOW;
import org.terifan.nodeeditor.Styles.DefaultNodeColors;
import org.terifan.nodeeditor.widgets.ButtonProperty;
import org.terifan.nodeeditor.widgets.ImageProperty;
import org.terifan.nodeeditor.widgets.ProducerProperty;


public class TestMandelExample
{
	public static void main(String... args)
	{
		try
		{
			ProducerProperty mandelbrot = new ProducerProperty("Result")
			{
				@Override
				public Object produce(Context aContext)
				{
					double x = (Double)aContext.params.get("x");
					double y = (Double)aContext.params.get("y");
					double d = (double)iterate(x, y, 1000);
					return d;
				}
			}.addConnector(OUT, GRAY);

			NodeModel model = new NodeModel()
				.addNode(new Node("Mandelbrot")
					.setTitleBackground(DefaultNodeColors.GREEN)
					.setBounds(-200, -50, 150, 0)
					.addProperty(new SliderProperty("Limit", 1000, 1).setId("limit"))
					.addProperty(mandelbrot)
				)
				.addNode(new Node("Colors")
					.setBounds(0, -50, 150, 0)
					.addProperty(new ValueProperty("Color").setProvides("r", "g", "b").addConnector(OUT, YELLOW).setId("rgb"))
					.addProperty(new SliderProperty("Red", 0, 1, 0).setId("r").addConnector(IN, GRAY))
					.addProperty(new SliderProperty("Green", 0, 1, 0.5).setId("g").addConnector(IN, GRAY))
					.addProperty(new SliderProperty("Blue", 0, 1, 0.75).setId("b").addConnector(IN, GRAY))
				)
				.addNode(new Node("RenderOutput")
					.setTitleBackground(DefaultNodeColors.DARKRED)
					.setBounds(200, 0, 220, 0)
					.addProperty(new ValueProperty("Color").setId("rgb").addConnector(IN))
//					.addProperty(new ValueProperty("Coordinate").setId("rgb").addConnector(IN, Styles.DefaultColors.PURPLE))
					.addProperty(new ImageProperty("", 200, 200).setId("output").setConsumes("rgb"))
					.addProperty(new ButtonProperty("Run").setIcon(Styles.DefaultIcons.RUN).setCommand("run"))
				)
//				.addNode(new Node("Coordinates")
//					.setTitleColor(DefaultNodeColors.PURPLE)
//					.setBounds(-200, 150, 220, 0)
//					.addProperty(new ValueProperty("Vector").setId("rgb").addConnector(OUT, Styles.DefaultColors.PURPLE))
//				)
				.addConnection(0, 1, 1, 1)
				.addConnection(0, 1, 1, 2)
				.addConnection(0, 1, 1, 3)
				.addConnection(1, 0, 2, 0)
//				.addConnection(3, 0, 2, 1)
				;

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


	private static int iterate(double aX, double aY, int max_iteration)
	{
		double x0 = -2 + (2.47 * aX); //scaled x coordinate of pixel (scaled to lie in the Mandelbrot X scale (-2.00, 0.47))
		double y0 = -1.12 + (2.24 * aY); //scaled y coordinate of pixel (scaled to lie in the Mandelbrot Y scale (-1.12, 1.12))
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
