package org.terifan.nodeeditor.examples;

import javax.swing.JFrame;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Direction;
import org.terifan.ui.Utilities;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.NodeBox;
import org.terifan.nodeeditor.NodeItem;


public class Test1
{
	public static void main(String ... args)
	{
		try
		{
			Utilities.setSystemLookAndFeel();

			NodeEditorPane editor = new NodeEditorPane();

			NodeItem node0 = new NodeItem("node0", new Connector(Direction.OUT));
			NodeItem node1 = new NodeItem("node1", new Connector(Direction.OUT));
			NodeItem node2 = new NodeItem("node2", new Connector(Direction.IN));
			NodeItem node3 = new NodeItem("node3", new Connector(Direction.OUT));
			NodeItem node4 = new NodeItem("node4", new Connector(Direction.IN));
			NodeItem node5 = new NodeItem("node5", new Connector(Direction.IN));
			NodeItem node6 = new NodeItem("node6", new Connector(Direction.OUT));
			NodeItem node7 = new NodeItem("node7", new Connector(Direction.IN), new Connector(Direction.OUT));
			NodeItem node8 = new NodeItem("node8", new Connector(Direction.OUT));
			NodeItem node9 = new NodeItem("node9", new Connector(Direction.OUT, Connector.PURPLE));
			NodeItem node10 = new ImageRelationItem("node10", 200, 200, new Connector(Direction.OUT, Connector.PURPLE), new Connector(Direction.OUT, Connector.PURPLE), new Connector(Direction.OUT, Connector.PURPLE));
			NodeItem node11 = new NodeItem("node11", new Connector(Direction.IN, Connector.YELLOW));
			NodeItem node12 = new NodeItem("node12", new Connector(Direction.IN, Connector.PURPLE));
			NodeItem node13 = new NodeItem("node13", new Connector(Direction.IN, Connector.YELLOW));
			NodeItem node14 = new NodeItem("node14", new Connector(Direction.IN, Connector.GRAY));
			NodeItem node15 = new NodeItem("node15", new Connector(Direction.IN, Connector.GRAY));
			NodeItem node16 = new NodeItem("node16", new Connector(Direction.IN, Connector.GRAY));
			NodeItem node17 = new NodeItem("node17", new Connector(Direction.IN, Connector.GRAY));
			NodeItem node18 = new NodeItem("node18", new Connector(Direction.IN, Connector.GRAY));
			NodeBox nodeBox0 = new NodeBox("nodeBox0");
			NodeBox nodeBox1 = new NodeBox("nodeBox1");
			NodeBox nodeBox2 = new NodeBox("nodeBox2");
			NodeBox nodeBox3 = new NodeBox("nodeBox3");
			NodeBox nodeBox4 = new NodeBox("nodeBox4");
			NodeBox nodeBox5 = new NodeBox("nodeBox5");
			nodeBox0.addItem(node0);
			nodeBox0.addItem(node1);
			nodeBox1.addItem(node2);
			nodeBox1.addItem(node3);
			nodeBox2.addItem(node4);
			nodeBox2.addItem(node5);
			nodeBox2.addItem(node10);
			nodeBox2.addItem(node6);
			nodeBox3.addItem(node7);
			nodeBox4.addItem(node13);
			nodeBox4.addItem(node8);
			nodeBox4.addItem(node9);
			nodeBox4.addItem(node15);
			nodeBox4.addItem(node16);
			nodeBox4.addItem(node17);
			nodeBox4.addItem(node18);
			nodeBox5.addItem(node11);
			nodeBox5.addItem(node12);
			nodeBox5.addItem(node14);
			editor.add(nodeBox0);
			editor.add(nodeBox1);
			editor.add(nodeBox2);
			editor.add(nodeBox3);
			editor.add(nodeBox4);
			editor.add(nodeBox5);
			editor.addConnection(node0, node2);
			editor.addConnection(node3, node4);
			editor.addConnection(node8, node5);
			editor.addConnection(node6, node7);
			editor.addConnection(node1, node7);
			editor.addConnection(node9, node12);
			editor.addConnection(node7, node11);

			nodeBox0.setLocation(100, 50);
			nodeBox1.setLocation(300, 100);
			nodeBox2.setLocation(500, 150);
			nodeBox3.setLocation(800, 170);
			nodeBox4.setLocation(300, 200);
			nodeBox5.setLocation(1000, 200);
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
