package org.terifan.nodeeditor.examples;

import org.terifan.nodeeditor.SliderNodeItem;
import org.terifan.nodeeditor.ImageNodeItem;
import org.terifan.nodeeditor.ComboBoxNodeItem;
import org.terifan.nodeeditor.CheckBoxNodeItem;
import org.terifan.nodeeditor.ColorChooserNodeItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.NodeBox;
import org.terifan.nodeeditor.TextNodeItem;


public class Test1
{
	public static void main(String ... args)
	{
		try
		{
			NodeEditorPane editor = new NodeEditorPane();

			TextNodeItem node0 = new TextNodeItem("node0", new Connector(Direction.OUT));
			TextNodeItem node1 = new TextNodeItem("node1", new Connector(Direction.OUT));
			TextNodeItem node2 = new TextNodeItem("node2", new Connector(Direction.IN));
			TextNodeItem node3 = new TextNodeItem("node3", new Connector(Direction.OUT));
			TextNodeItem node4 = new TextNodeItem("node4", new Connector(Direction.IN));
			TextNodeItem node5 = new TextNodeItem("node5", new Connector(Direction.IN));
			TextNodeItem node6 = new TextNodeItem("node6", new Connector(Direction.OUT));
			TextNodeItem node7 = new TextNodeItem("node7", new Connector(Direction.IN), new Connector(Direction.OUT));
			TextNodeItem node8 = new TextNodeItem("node8", new Connector(Direction.OUT));
			TextNodeItem node9 = new TextNodeItem("node9", new Connector(Direction.OUT, Connector.PURPLE));
			ImageNodeItem node10 = new ImageNodeItem("node10", new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB), 200, 200, new Connector(Direction.OUT, Connector.PURPLE), new Connector(Direction.OUT, Connector.PURPLE), new Connector(Direction.OUT, Connector.PURPLE));
			ColorChooserNodeItem node11 = new ColorChooserNodeItem("node11", new Color(255,255,255), new Connector(Direction.IN, Connector.YELLOW));
			TextNodeItem node12 = new TextNodeItem("node12", new Connector(Direction.IN, Connector.PURPLE));
			TextNodeItem node13 = new TextNodeItem("node13", new Connector(Direction.IN, Connector.YELLOW));
			TextNodeItem node14 = new TextNodeItem("node14", new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem node15 = new TextNodeItem("node15", new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem node16 = new TextNodeItem("node16", new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem node17 = new TextNodeItem("node17", new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem node18 = new TextNodeItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
			node18.getTextBox().setMaxWidth(300);
			SliderNodeItem slider1 = new SliderNodeItem("Roughness", 0, 1, 0);
			SliderNodeItem slider2 = new SliderNodeItem("Roughness", 0, 1, 0.5);
			SliderNodeItem slider3 = new SliderNodeItem("Roughness", 0, 1, 0.75);
			SliderNodeItem slider4 = new SliderNodeItem("Roughness", 0.0, 0.1);
			CheckBoxNodeItem checkbox1 = new CheckBoxNodeItem("Inverted", false);
			CheckBoxNodeItem checkbox2 = new CheckBoxNodeItem("Inverted", true);
			ComboBoxNodeItem comboBoxNodeItem1 = new ComboBoxNodeItem("GCX");
			ComboBoxNodeItem comboBoxNodeItem2 = new ComboBoxNodeItem("Beckmann");
			NodeBox nodeBox0 = new NodeBox("nodeBox0", node0, node1, comboBoxNodeItem1, comboBoxNodeItem2);
			NodeBox nodeBox1 = new NodeBox("nodeBox1", node2, slider1, slider2, slider3, slider4, checkbox1, checkbox2, node3);
			NodeBox nodeBox2 = new NodeBox("nodeBox2", node4, node5, node10, node6);
			NodeBox nodeBox3 = new NodeBox("nodeBox3", node7);
			NodeBox nodeBox4 = new NodeBox("nodeBox4", node13, node8, node9, node18, node15, node16, node17);
			NodeBox nodeBox5 = new NodeBox("nodeBox5", node11, node12, node14);
			nodeBox2.setMinSize(new Dimension(230,0));
			editor.add(nodeBox0).addConnection(node0, node2).addConnection(node1, node7);
			editor.add(nodeBox1).addConnection(node3, node4).addConnection(node9, node12);
			editor.add(nodeBox2).addConnection(node6, node7);
			editor.add(nodeBox3).addConnection(node7, node11);
			editor.add(nodeBox4).addConnection(node8, node5);
			editor.add(nodeBox5);

			nodeBox0.setLocation(100, 50);
			nodeBox1.setLocation(300, 0);
			nodeBox2.setLocation(500, 150);
			nodeBox3.setLocation(800, 170);
			nodeBox4.setLocation(100, 250);
			nodeBox5.setLocation(1000, 200);
			editor.center();
			editor.setScale(2);

			JFrame frame = new JFrame();
			frame.add(editor);
			frame.setSize(2*1600, 2*1000);
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
