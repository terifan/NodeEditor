package org.terifan.nodeeditor.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.terifan.nodeeditor.SliderNodeItem;
import org.terifan.nodeeditor.ImageNodeItem;
import javax.swing.JFrame;
import org.terifan.nodeeditor.ColorChooserNodeItem;
import static org.terifan.nodeeditor.Connector.GRAY;
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

			editor.add(new NodeBox("Color1")
				.setSize(200, 0)
				.setIdentity("color1")
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

			editor.add(new NodeBox("Color2")
				.setSize(200, 0)
				.setIdentity("color2")
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
			editor.add(new NodeBox("Output")
				.add(new ImageNodeItem("node10", image, 200, 200)
					.addConnector(OUT, YELLOW))
				.add(new ColorChooserNodeItem("Color", new Color(0, 0, 0))
					.addConnector(IN, YELLOW))
				.add(new SliderNodeItem("Alpha", 0, 1, 0.75)
					.addConnector(IN, GRAY))
			);

			editor.add(new NodeBox("Alpha")
				.setSize(200, 0)
				.add(new SliderNodeItem("Alpha", 0, 1, 0.75)
					.addConnector(OUT, GRAY))
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

			editor.addConnection("color1.color", "mix.colorIn1");
			editor.addConnection("color2.color", "mix.colorIn2");
			editor.addConnection("mix.colorOut", "output.color");
			editor.addConnection("alpha.alpha", "output.alpha");
			editor.addConnection("alpha.alpha", "mix.fac");

			editor.getNode("color1").setLocation(0, 0);
			editor.getNode("color2").setLocation(0, -160);
			editor.getNode("mix").setLocation(300, -50);
			editor.getNode("alpha").setLocation(0, 200);
			editor.getNode("output").setLocation(600, 100);

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
