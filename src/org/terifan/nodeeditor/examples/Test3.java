package org.terifan.nodeeditor.examples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import org.terifan.nodeeditor.SliderNodeItem;
import org.terifan.nodeeditor.ImageNodeItem;
import javax.swing.JFrame;
import org.terifan.nodeeditor.ColorChooserNodeItem;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Direction;
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

			NodeBox nodeBox0 = editor.add(new NodeBox("Color")
				.setSize(200, 0)
				.setIdentity("color1")
				.add(new TextNodeItem("Color")
					.add(new Connector(Direction.OUT, Connector.YELLOW)))
				.add(new SliderNodeItem("Red", 0, 1, 0)
					.add(new Connector(Direction.IN, Connector.GRAY)))
				.add(new SliderNodeItem("Green", 0, 1, 0.5)
					.add(new Connector(Direction.IN, Connector.GRAY)))
				.add(new SliderNodeItem("Blue", 0, 1, 0.75)
					.add(new Connector(Direction.IN, Connector.GRAY)))
				.add(new SliderNodeItem("Alpha", 0, 1, 0.5)
					.add(new Connector(Direction.IN, Connector.GRAY)))
			);

			NodeBox nodeBox4 = editor.add(new NodeBox("Color")
				.setSize(200, 0)
				.setIdentity("color2")
				.add(new TextNodeItem("Color")
					.add(new Connector(Direction.OUT, Connector.YELLOW)))
				.add(new SliderNodeItem("Red", 0, 1, 0)
					.add(new Connector(Direction.IN, Connector.GRAY)))
				.add(new SliderNodeItem("Green", 0, 1, 0.5)
					.add(new Connector(Direction.IN, Connector.GRAY)))
				.add(new SliderNodeItem("Blue", 0, 1, 0.75)
					.add(new Connector(Direction.IN, Connector.GRAY)))
				.add(new SliderNodeItem("Alpha", 0, 1, 0.5)
					.add(new Connector(Direction.IN, Connector.GRAY)))
			);

			BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
			NodeBox nodeBox1 = editor.add(new NodeBox("Output")
				.add(new ImageNodeItem("node10", image, 200, 200)
					.add(new Connector(Direction.OUT, Connector.YELLOW)))
				.add(new ColorChooserNodeItem("Color", new Color(0, 0, 0))
					.add(new Connector(Direction.IN, Connector.YELLOW)))
				.add(new SliderNodeItem("Alpha", 0, 1, 0.75)
					.add(new Connector(Direction.IN, Connector.GRAY)))
			);

			NodeBox nodeBox2 = editor.add(new NodeBox("Alpha")
				.setSize(200, 0)
				.add(new SliderNodeItem("Alpha", 0, 1, 0.75)
					.add(new Connector(Direction.OUT, Connector.GRAY)))
			);

			NodeBox nodeBox3 = editor.add(new NodeBox("Mix")
				.setSize(200, 0)
				.add(new TextNodeItem("Color")
					.setIdentity("colorOut")
					.add(new Connector(Direction.OUT, Connector.YELLOW)))
				.add(new SliderNodeItem("Fac", 0, 1, 0.5)
					.add(new Connector(Direction.IN, Connector.GRAY)))
				.add(new ColorChooserNodeItem("Color", new Color(255, 0, 0))
					.setIdentity("colorIn1")
					.add(new Connector(Direction.IN, Connector.YELLOW)))
				.add(new ColorChooserNodeItem("Color", new Color(0, 0, 255))
					.setIdentity("colorIn2")
					.add(new Connector(Direction.IN, Connector.YELLOW)))
			);

			editor.addConnection("color1.color", "mix.colorIn1");
			editor.addConnection("color2.color", "mix.colorIn2");
			editor.addConnection("mix.colorOut", "output.color");
			editor.addConnection("alpha.alpha", "output.alpha");
			editor.addConnection("alpha.alpha", "mix.fac");

			nodeBox0.setLocation(0, 0);
			nodeBox1.setLocation(600, 100);
			nodeBox2.setLocation(0, 200);
			nodeBox3.setLocation(300, -50);
			nodeBox4.setLocation(0, -160);

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
