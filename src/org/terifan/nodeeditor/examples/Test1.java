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
import org.terifan.nodeeditor.NodeEditor;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.TextNodeItem;


public class Test1
{
	public static void main(String ... args)
	{
		try
		{
			TextNodeItem node0 = new TextNodeItem("node0").add(new Connector(Direction.OUT));
			TextNodeItem node1 = new TextNodeItem("node1").add(new Connector(Direction.OUT));
			TextNodeItem node2 = new TextNodeItem("node2").add(new Connector(Direction.IN));
			TextNodeItem node3 = new TextNodeItem("node3").add(new Connector(Direction.OUT));
			TextNodeItem node4 = new TextNodeItem("node4").add(new Connector(Direction.IN));
			TextNodeItem node5 = new TextNodeItem("node5").add(new Connector(Direction.IN));
			TextNodeItem node6 = new TextNodeItem("node6").add(new Connector(Direction.OUT));
			TextNodeItem node7 = new TextNodeItem("node7").add(new Connector(Direction.IN)).add(new Connector(Direction.OUT));
			TextNodeItem node8 = new TextNodeItem("node8").add(new Connector(Direction.OUT));
			TextNodeItem node9 = new TextNodeItem("node9").add(new Connector(Direction.OUT, Connector.PURPLE));
			ImageNodeItem node10 = new ImageNodeItem("node10", new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB), 200, 200).add(new Connector(Direction.OUT, Connector.PURPLE)).add(new Connector(Direction.OUT, Connector.PURPLE)).add(new Connector(Direction.OUT, Connector.PURPLE));
			ColorChooserNodeItem node11 = new ColorChooserNodeItem("node11", new Color(255,255,255)).add(new Connector(Direction.IN, Connector.YELLOW));
			TextNodeItem node12 = new TextNodeItem("node12").add(new Connector(Direction.IN, Connector.PURPLE));
			TextNodeItem node13 = new TextNodeItem("node13").add(new Connector(Direction.IN, Connector.YELLOW));
			TextNodeItem node14 = new TextNodeItem("node14").add(new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem node15 = new TextNodeItem("node15").add(new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem node16 = new TextNodeItem("node16").add(new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem node17 = new TextNodeItem("node17").add(new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem node18 = new TextNodeItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
			node18.getTextBox().setMaxWidth(300);
			SliderNodeItem slider1 = new SliderNodeItem("Roughness", 0, 1, 0);
			SliderNodeItem slider2 = new SliderNodeItem("Roughness", 0, 1, 0.5);
			SliderNodeItem slider3 = new SliderNodeItem("Roughness", 0, 1, 0.75);
			SliderNodeItem slider4 = new SliderNodeItem("Roughness", 0.0, 0.1);
			CheckBoxNodeItem checkbox1 = new CheckBoxNodeItem("Inverted", false);
			CheckBoxNodeItem checkbox2 = new CheckBoxNodeItem("Inverted", true);
			ComboBoxNodeItem comboBoxNodeItem1 = new ComboBoxNodeItem("A", System.out::println, 0, "GCX", "Beckmann");
			ComboBoxNodeItem comboBoxNodeItem2 = new ComboBoxNodeItem("B", System.out::println, 1, "Alpha", "Beta");
			Node nodeBox0 = new Node("nodeBox0", node0, node1, comboBoxNodeItem1, comboBoxNodeItem2);
			Node nodeBox1 = new Node("nodeBox1", node2, slider1, slider2, slider3, slider4, checkbox1, checkbox2, node3);
			Node nodeBox2 = new Node("nodeBox2", node4, node5, node10, node6);
			Node nodeBox3 = new Node("nodeBox3", node7);
			Node nodeBox4 = new Node("nodeBox4", node13, node8, node9, node18, node15, node16, node17);
			Node nodeBox5 = new Node("nodeBox5", node11, node12, node14);
			nodeBox2.setMinSize(new Dimension(230,0));

			NodeModel model = new NodeModel();

			model.add(nodeBox0);
			model.add(nodeBox1);
			model.add(nodeBox2);
			model.add(nodeBox3);
			model.add(nodeBox4);
			model.add(nodeBox5);
			model.addConnection(node0, node2);
			model.addConnection(node1, node7);
			model.addConnection(node3, node4);
			model.addConnection(node9, node12);
			model.addConnection(node6, node7);
			model.addConnection(node7, node11);
			model.addConnection(node8, node5);

			NodeEditor editor = new NodeEditor(model);

			nodeBox0.setLocation(100, 50);
			nodeBox1.setLocation(300, 0);
			nodeBox2.setLocation(500, 150);
			nodeBox3.setLocation(800, 170);
			nodeBox4.setLocation(100, 250);
			nodeBox5.setLocation(1000, 200);
			editor.center();
			editor.setScale(1);

			JFrame frame = new JFrame();
			frame.add(editor);
			frame.setSize((int)(editor.getScale()*1600), (int)(editor.getScale()*1000));
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
