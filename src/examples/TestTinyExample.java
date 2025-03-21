package examples;

import static java.awt.Color.GRAY;
import org.terifan.nodeeditor.widgets.SliderProperty;
import javax.swing.JFrame;
import static org.terifan.nodeeditor.Direction.IN;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.ValueProperty;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.DefaultColors.YELLOW;
import org.terifan.nodeeditor.widgets.ButtonProperty;
import org.terifan.nodeeditor.widgets.ImageProperty;


public class TestTinyExample
{
	public static void main(String... args)
	{
		try
		{
			NodeModel model = new NodeModel()
				.addNode(new Node("Colors")
					.setBounds(0, 0, 150, 0)
					.addProperty(new ValueProperty("Color").setProvides("r", "g", "b").addConnector(OUT, YELLOW).setId("color"))
					.addProperty(new SliderProperty("Red", 0, 1, 0).setId("r"))
					.addProperty(new SliderProperty("Green", 0, 1, 0.5).setId("g"))
					.addProperty(new SliderProperty("Blue", 0, 1, 0.75).setId("b"))
					.addProperty(new SliderProperty("Alpha", 0, 1, 0.5).setId("alpha").addConnector(OUT, GRAY)) // no setProvides(), the ID will be used
				)
				.addNode(new Node("RenderOutput")
					.setBounds(200, 0, 220, 0)
					.addProperty(new ValueProperty("Color").setId("rgb").addConnector(IN))
					.addProperty(new ValueProperty("Alpha").setId("alpha").addConnector(IN, GRAY))
					.addProperty(new ImageProperty("", 200, 200).setId("output").setConsumes("rgb", "alpha"))
					.addProperty(new ButtonProperty("Run").setIcon(Styles.DefaultIcons.RUN).setCommand("run"))
				)
				.addConnection(0, 0, 1, 0);

			NodeEditorPane editor = new NodeEditorPane(model)
				.center();

			editor.bind("run", ctx ->
			{
				ctx.getNode().getProperty("output").execute(ctx);
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
}
