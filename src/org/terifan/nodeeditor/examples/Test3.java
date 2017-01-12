package org.terifan.nodeeditor.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
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
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.NodeBox;
import org.terifan.nodeeditor.TextNodeItem;


public class Test3
{
	public static void main(String... args)
	{
		try
		{
			NodeEditorPane editor = new NodeEditorPane();

			editor.add(new NodeBox("Color")
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

			BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
			editor.add(new NodeBox("Texture")
				.add(new TextNodeItem("Color")
					.addConnector(OUT, YELLOW))
				.add(new TextNodeItem("Alpha")
					.addConnector(OUT, GRAY))
				.add(new ButtonNodeItem("Open", ImageIO.read(Test3.class.getResource("directory.png")), System.out::println))
				.add(new ImageNodeItem("image", image, 200, 200))
				.add(new TextNodeItem("Vector")
					.addConnector(IN, PURPLE))
			);

			editor.add(new NodeBox("Output")
				.add(new ColorChooserNodeItem("Surface", new Color(0, 0, 0))
					.addConnector(IN, YELLOW))
				.add(new SliderNodeItem("Alpha", 0, 1, 0.75)
					.addConnector(IN, GRAY))
			);

			editor.add(new NodeBox("Alpha")
				.setSize(200, 0)
				.add(new SliderNodeItem("Alpha", 0, 1, 0.75)
					.addConnector(OUT, GRAY))
			);

			editor.add(new NodeBox("TextureCoordinate")
				.setSize(200, 0)
				.add(new TextNodeItem("UV")
					.addConnector(OUT, PURPLE))
			);

			editor.add(new NodeBox("Multiply")
				.setSize(200, 0)
				.setIdentity("math")
				.add(new TextNodeItem("Value")
					.setIdentity("result")
					.addConnector(OUT, GRAY))
				.add(new ComboBoxNodeItem("Multiply"))
				.add(new CheckBoxNodeItem("Clamp", false))
				.add(new SliderNodeItem("Value", 0.5, 0.01)
					.setIdentity("value1")
					.addConnector(IN, GRAY))
				.add(new SliderNodeItem("Value", 0.5, 0.01)
					.setIdentity("value2")
					.addConnector(IN, GRAY))
			);

			editor.add(new NodeBox("Mix")
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

			editor.addConnection("texture.color", "mix.colorIn1");
			editor.addConnection("color.color", "mix.colorIn2");
			editor.addConnection("mix.colorOut", "output.surface");
			editor.addConnection("alpha.alpha", "output.alpha");
			editor.addConnection("alpha.alpha", "mix.fac");
			editor.addConnection("texturecoordinate.uv", "math.value1");
			editor.addConnection("math.result", "texture.vector");

			editor.getNode("color").setLocation(0, 0);
			editor.getNode("mix").setLocation(300, -50);
			editor.getNode("alpha").setLocation(0, 200);
			editor.getNode("output").setLocation(600, 100);
			editor.getNode("texture").setLocation(0, -350);
			editor.getNode("texturecoordinate").setLocation(-600, -200);
			editor.getNode("math").setLocation(-300, -200);

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
