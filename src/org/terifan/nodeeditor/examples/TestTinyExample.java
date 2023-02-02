package org.terifan.nodeeditor.examples;

import java.awt.Color;
import org.terifan.nodeeditor.SliderNodeItem;
import javax.swing.JFrame;
import org.terifan.nodeeditor.ColorChooserNodeItem;
import static org.terifan.nodeeditor.Connector.YELLOW;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.NodeEditor;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.TextNodeItem;


public class TestTinyExample
{
	public static void main(String... args)
	{
		try
		{
			Node color = new Node("Input")
				.setBounds(0, 0, 150, 0)
				.add(new TextNodeItem("Color").addConnector(OUT, YELLOW))
				.add(new SliderNodeItem("Red", 0, 1, 0))
				.add(new SliderNodeItem("Green", 0, 1, 0.5))
				.add(new SliderNodeItem("Blue", 0, 1, 0.75))
				.add(new SliderNodeItem("Alpha", 0, 1, 0.5));

			Node output = new Node("Output")
				.setBounds(200, 0, 150, 0)
				.add(new ColorChooserNodeItem("Color", new Color(0, 0, 0)).addConnector(IN, YELLOW));

			NodeModel model = new NodeModel()
				.addNode(color)
				.addNode(output)
				.addConnection("Input.Color", "Output.Color");

			NodeEditor editor = new NodeEditor(model);
			editor.center();

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
