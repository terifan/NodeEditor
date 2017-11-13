package org.terifan.nodeeditor.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.terifan.nodeeditor.SliderNodeItem;
import org.terifan.nodeeditor.ImageNodeItem;
import javax.swing.JFrame;
import org.terifan.nodeeditor.ButtonNodeItem;
import org.terifan.nodeeditor.CheckBoxNodeItem;
import org.terifan.nodeeditor.ColorChooserNodeItem;
import org.terifan.nodeeditor.ComboBoxNodeItem;
import static org.terifan.nodeeditor.Connector.GRAY;
import static org.terifan.nodeeditor.Connector.PURPLE;
import static org.terifan.nodeeditor.Connector.YELLOW;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.NodeEditor;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.TextNodeItem;
import org.terifan.util.cache.Cache;


public class Test4
{
	public static void main(String... args)
	{
		try
		{
			String code = "{'box1', 'box1.item1', {'box2', 'box2.item1', {}}, 'box1.item2', {}}";

			
			
			NodeModel model = new NodeModel();

			model.addNode(new Node("Texture")
				.add(new TextNodeItem("Color")
					.addConnector(OUT, YELLOW))
			);

			model.addNode(new Node("Output")
				.add(new TextNodeItem("Surface")
					.addConnector(IN, YELLOW))
			);

			model.addConnection("texture.color", "output.surface");

			NodeEditor editor = new NodeEditor(model);
	
			editor.center();
			editor.setScale(2);

			JFrame frame = new JFrame();
			frame.add(editor);
			frame.setSize((int)(1600 * editor.getScale()), (int)(1000 * editor.getScale()));
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
