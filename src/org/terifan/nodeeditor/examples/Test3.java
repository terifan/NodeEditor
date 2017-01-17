package org.terifan.nodeeditor.examples;

import java.awt.Color;
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


public class Test3
{
	public static void main(String... args)
	{
		try
		{
			NodeModel model = new NodeModel();

			model.addNode(new Node("Color")
				.setPrototype("color")
				.setSize(200, 0)
				.add(new TextNodeItem("Color")
					.addConnector(OUT, YELLOW))
				.add(new SliderNodeItem("Red", 0, 1, 0)
					.addConnector(IN, GRAY))
				.add(new SliderNodeItem("Green", 0, 1, 0.5)
					.addConnector(IN, GRAY))
				.add(new SliderNodeItem("Blue", 0, 1, 0.75)
					.addConnector(IN, GRAY))
				.add(new SliderNodeItem("Alpha", 0, 1, 0.5)
					.addConnector(IN, GRAY))
			);

			model.addNode(new Node("Texture")
				.add(new TextNodeItem("Color")
					.addConnector(OUT, YELLOW))
				.add(new TextNodeItem("Alpha")
					.addConnector(OUT, GRAY))
				.add(new ButtonNodeItem("Open"))
				.add(new ImageNodeItem("image", 200, 200))
				.add(new TextNodeItem("Vector")
					.addConnector(IN, PURPLE))
			);

			model.addNode(new Node("Output")
				.add(new ColorChooserNodeItem("Surface", new Color(0, 0, 0))
					.addConnector(IN, YELLOW))
				.add(new SliderNodeItem("Alpha", 0, 1, 0.75)
					.addConnector(IN, GRAY))
			);

			model.addNode(new Node("Alpha")
				.setSize(200, 0)
				.add(new SliderNodeItem("Alpha", 0, 1, 0.75)
					.addConnector(OUT, GRAY))
			);

			model.addNode(new Node("TextureCoordinate")
				.setSize(200, 0)
				.add(new TextNodeItem("UV")
					.addConnector(OUT, PURPLE))
			);

			model.addNode(new Node("Multiply")
				.setSize(200, 0)
				.setIdentity("math")
				.add(new TextNodeItem("Value")
					.setIdentity("result")
					.addConnector(OUT, GRAY))
				.add(new ComboBoxNodeItem("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Absolute", "Modulo", "Greater Than"))
				.add(new CheckBoxNodeItem("Clamp", false))
				.add(new SliderNodeItem("Value", 0.5, 0.01)
					.setIdentity("value1")
					.addConnector(IN, GRAY))
				.add(new SliderNodeItem("Value", 0.5, 0.01)
					.setIdentity("value2")
					.addConnector(IN, GRAY))
			);

			model.addNode(new Node("Mix")
				.setSize(200, 0)
				.add(new TextNodeItem("Color")
					.setIdentity("colorOut")
					.addConnector(OUT, YELLOW))
				.add(new SliderNodeItem("Fac", 0, 1, 0.5)
					.addConnector(IN, GRAY))
				.add(new ColorChooserNodeItem("Color", new Color(255, 0, 0))
					.setIdentity("colorIn1")
					.addConnector(IN, YELLOW))
				.add(new ColorChooserNodeItem("Color", new Color(0, 0, 255))
					.setIdentity("colorIn2")
					.addConnector(IN, YELLOW))
			);

			model.addConnection("texture.color", "mix.colorIn1");
			model.addConnection("color.color", "mix.colorIn2");
			model.addConnection("mix.colorOut", "output.surface");
			model.addConnection("alpha.alpha", "output.alpha");
			model.addConnection("alpha.alpha", "mix.fac");
			model.addConnection("texturecoordinate.uv", "math.value1");
			model.addConnection("math.result", "texture.vector");

			model.getNode("color").setLocation(0, 0);
			model.getNode("mix").setLocation(300, -50);
			model.getNode("alpha").setLocation(0, 200);
			model.getNode("output").setLocation(600, 100);
			model.getNode("texture").setLocation(0, -350);
			model.getNode("texturecoordinate").setLocation(-600, -200);
			model.getNode("math").setLocation(-300, -200);

			NodeModel model2 = NodeModel.unmarshal(model.marshal());

			NodeEditor editor = new NodeEditor(model2);

			editor.addResourceLoader("texture.image", e->ImageIO.read(Test3.class.getResource("slime.jpg")));

			editor.center();
			editor.setScale(1);

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
