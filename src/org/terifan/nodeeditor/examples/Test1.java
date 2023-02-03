package org.terifan.nodeeditor.examples;

import org.terifan.nodeeditor.SliderPropertyItem;
import org.terifan.nodeeditor.ImagePropertyItem;
import org.terifan.nodeeditor.ComboBoxPropertyItem;
import org.terifan.nodeeditor.CheckBoxPropertyItem;
import org.terifan.nodeeditor.ColorChooserNodeItem;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditor;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.TextPropertyItem;


public class Test1
{
	public static void main(String ... args)
	{
		try
		{
			TextPropertyItem item0 = new TextPropertyItem("node0").addConnector(new Connector(Direction.OUT));
			TextPropertyItem item1 = new TextPropertyItem("node1").addConnector(new Connector(Direction.OUT));
			TextPropertyItem item2 = new TextPropertyItem("node2").addConnector(new Connector(Direction.IN));
			TextPropertyItem item3 = new TextPropertyItem("node3").addConnector(new Connector(Direction.OUT));
			TextPropertyItem item4 = new TextPropertyItem("node4").addConnector(new Connector(Direction.IN));
			TextPropertyItem item5 = new TextPropertyItem("node5").addConnector(new Connector(Direction.IN));
			TextPropertyItem item6 = new TextPropertyItem("node6").addConnector(new Connector(Direction.OUT));
			TextPropertyItem item7 = new TextPropertyItem("node7").addConnector(new Connector(Direction.IN)).addConnector(new Connector(Direction.OUT));
			TextPropertyItem item8 = new TextPropertyItem("node8").addConnector(new Connector(Direction.OUT));
			TextPropertyItem item9 = new TextPropertyItem("node9").addConnector(new Connector(Direction.OUT, Connector.PURPLE));
			ImagePropertyItem item10 = new ImagePropertyItem("node10", 200, 200).addConnector(new Connector(Direction.OUT, Connector.PURPLE)).addConnector(new Connector(Direction.OUT, Connector.PURPLE)).addConnector(new Connector(Direction.OUT, Connector.PURPLE));
			ColorChooserNodeItem item11 = new ColorChooserNodeItem("node11", new Color(255,255,255)).addConnector(new Connector(Direction.IN, Connector.YELLOW));
			TextPropertyItem item12 = new TextPropertyItem("node12").addConnector(new Connector(Direction.IN, Connector.PURPLE));
			TextPropertyItem item13 = new TextPropertyItem("node13").addConnector(new Connector(Direction.IN, Connector.YELLOW));
			TextPropertyItem item14 = new TextPropertyItem("node14").addConnector(new Connector(Direction.IN, Connector.GRAY));
			TextPropertyItem item15 = new TextPropertyItem("node15").addConnector(new Connector(Direction.IN, Connector.GRAY));
			TextPropertyItem item16 = new TextPropertyItem("node16").addConnector(new Connector(Direction.IN, Connector.GRAY));
			TextPropertyItem item17 = new TextPropertyItem("node17").addConnector(new Connector(Direction.IN, Connector.GRAY));
			TextPropertyItem item18 = new TextPropertyItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
//			item18.getTextBox().setMaxWidth(300);
			SliderPropertyItem slider1 = new SliderPropertyItem("Roughness", 0, 1, 0);
			SliderPropertyItem slider2 = new SliderPropertyItem("Roughness", 0, 1, 0.5);
			SliderPropertyItem slider3 = new SliderPropertyItem("Roughness", 0, 1, 0.75);
			SliderPropertyItem slider4 = new SliderPropertyItem("Roughness", 0.0, 0.1);
			CheckBoxPropertyItem checkbox1 = new CheckBoxPropertyItem("Inverted", false);
			CheckBoxPropertyItem checkbox2 = new CheckBoxPropertyItem("Inverted", true);
			ComboBoxPropertyItem comboBoxNodeItem1 = new ComboBoxPropertyItem("A", 0, "GCX", "Beckmann");
			ComboBoxPropertyItem comboBoxNodeItem2 = new ComboBoxPropertyItem("B", 1, "Alpha", "Beta");
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
