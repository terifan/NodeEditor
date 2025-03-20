package examples;

import java.awt.Color;
import org.terifan.nodeeditor.widgets.SliderProperty;
import javax.swing.JFrame;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.ColorChooserProperty;
import org.terifan.nodeeditor.widgets.ComboBoxProperty;
import org.terifan.nodeeditor.widgets.TextProperty;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import static org.terifan.nodeeditor.Styles.YELLOW;


public class TestTinyExample
{
	public static void main(String... args)
	{
		try
		{
			NodeModel model = new NodeModel()
				.addNode(new Node("Input")
					.setBounds(0, 0, 150, 0)
					.addProperty(new TextProperty("Color").addConnector(OUT, YELLOW))
					.addProperty(new SliderProperty("Red", 0, 1, 0))
					.addProperty(new SliderProperty("Green", 0, 1, 0.5))
					.addProperty(new SliderProperty("Blue", 0, 1, 0.75))
					.addProperty(new SliderProperty("Alpha", 0, 1, 0.5))
				)
				.addNode(new Node("Output")
					.setBounds(200, 0, 150, 0)
					.addProperty(new ComboBoxProperty("Function", 0, "Add", "Subtract"))
					.addProperty(new ColorChooserProperty("Color", new Color(0, 0, 0))
						.addConnector(IN, YELLOW)))
				.addConnection(0, 0, 1, 1);

			NodeEditorPane editor = new NodeEditorPane(model)
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
