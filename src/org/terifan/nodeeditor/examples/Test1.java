package org.terifan.nodeeditor.examples;

import org.terifan.nodeeditor.SliderNodeItem;
import org.terifan.nodeeditor.ImageNodeItem;
import org.terifan.nodeeditor.ComboBoxNodeItem;
import org.terifan.nodeeditor.CheckBoxNodeItem;
import org.terifan.nodeeditor.ColorChooserNodeItem;
import java.awt.Color;
import java.awt.Dimension;
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
			TextNodeItem item0 = new TextNodeItem("node0").add(new Connector(Direction.OUT));
			TextNodeItem item1 = new TextNodeItem("node1").add(new Connector(Direction.OUT));
			TextNodeItem item2 = new TextNodeItem("node2").add(new Connector(Direction.IN));
			TextNodeItem item3 = new TextNodeItem("node3").add(new Connector(Direction.OUT));
			TextNodeItem item4 = new TextNodeItem("node4").add(new Connector(Direction.IN));
			TextNodeItem item5 = new TextNodeItem("node5").add(new Connector(Direction.IN));
			TextNodeItem item6 = new TextNodeItem("node6").add(new Connector(Direction.OUT));
			TextNodeItem item7 = new TextNodeItem("node7").add(new Connector(Direction.IN)).add(new Connector(Direction.OUT));
			TextNodeItem item8 = new TextNodeItem("node8").add(new Connector(Direction.OUT));
			TextNodeItem item9 = new TextNodeItem("node9").add(new Connector(Direction.OUT, Connector.PURPLE));
			ImageNodeItem item10 = new ImageNodeItem("node10", 200, 200).add(new Connector(Direction.OUT, Connector.PURPLE)).add(new Connector(Direction.OUT, Connector.PURPLE)).add(new Connector(Direction.OUT, Connector.PURPLE));
			ColorChooserNodeItem item11 = new ColorChooserNodeItem("node11", new Color(255,255,255)).add(new Connector(Direction.IN, Connector.YELLOW));
			TextNodeItem item12 = new TextNodeItem("node12").add(new Connector(Direction.IN, Connector.PURPLE));
			TextNodeItem item13 = new TextNodeItem("node13").add(new Connector(Direction.IN, Connector.YELLOW));
			TextNodeItem item14 = new TextNodeItem("node14").add(new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem item15 = new TextNodeItem("node15").add(new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem item16 = new TextNodeItem("node16").add(new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem item17 = new TextNodeItem("node17").add(new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem item18 = new TextNodeItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
			item18.getTextBox().setMaxWidth(300);
			SliderNodeItem slider1 = new SliderNodeItem("Roughness", 0, 1, 0);
			SliderNodeItem slider2 = new SliderNodeItem("Roughness", 0, 1, 0.5);
			SliderNodeItem slider3 = new SliderNodeItem("Roughness", 0, 1, 0.75);
			SliderNodeItem slider4 = new SliderNodeItem("Roughness", 0.0, 0.1);
			CheckBoxNodeItem checkbox1 = new CheckBoxNodeItem("Inverted", false);
			CheckBoxNodeItem checkbox2 = new CheckBoxNodeItem("Inverted", true);
			ComboBoxNodeItem comboBoxNodeItem1 = new ComboBoxNodeItem("A", 0, "GCX", "Beckmann");
			ComboBoxNodeItem comboBoxNodeItem2 = new ComboBoxNodeItem("B", 1, "Alpha", "Beta");
			Node node0 = new Node("nodeBox0", item0, item1, comboBoxNodeItem1, comboBoxNodeItem2);
			Node node1 = new Node("nodeBox1", item2, slider1, slider2, slider3, slider4, checkbox1, checkbox2, item3);
			Node node2 = new Node("nodeBox2", item4, item5, item10, item6);
			Node node3 = new Node("nodeBox3", item7);
			Node node4 = new Node("nodeBox4", item13, item8, item9, item18, item15, item16, item17);
			Node node5 = new Node("nodeBox5", item11, item12, item14);
			node2.setMinSize(new Dimension(230,0));

			NodeModel model = new NodeModel();

			model.addNode(node0);
			model.addNode(node1);
			model.addNode(node2);
			model.addNode(node3);
			model.addNode(node4);
			model.addNode(node5);
			model.addConnection(item0, item2);
			model.addConnection(item1, item7);
			model.addConnection(item3, item4);
			model.addConnection(item9, item12);
			model.addConnection(item6, item7);
			model.addConnection(item7, item11);
			model.addConnection(item8, item5);

			NodeEditor editor = new NodeEditor(model);

			node0.setLocation(100, 50);
			node1.setLocation(300, 0);
			node2.setLocation(500, 150);
			node3.setLocation(800, 170);
			node4.setLocation(100, 250);
			node5.setLocation(1000, 200);
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
