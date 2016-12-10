package org.terifan.nodeeditor.v2.examples;

import javax.swing.JFrame;
import org.terifan.nodeeditor.v2.Connector;
import org.terifan.nodeeditor.v2.Direction;
import org.terifan.ui.Utilities;
import org.terifan.nodeeditor.v2.NodeEditorPane;
import org.terifan.nodeeditor.v2.RelationBox;
import org.terifan.nodeeditor.v2.RelationItem;


public class Test1
{
	public static void main(String ... args)
	{
		try
		{
			Utilities.setSystemLookAndFeel();

			NodeEditorPane editor = new NodeEditorPane();

			RelationItem node0 = new RelationItem("node0", new Connector(Direction.OUT));
			RelationItem node1 = new RelationItem("node1", new Connector(Direction.OUT));
			RelationItem node2 = new RelationItem("node2", new Connector(Direction.IN));
			RelationItem node3 = new RelationItem("node3", new Connector(Direction.OUT));
			RelationItem node4 = new RelationItem("node4", new Connector(Direction.IN));
			RelationItem node5 = new RelationItem("node5", new Connector(Direction.IN));
			RelationItem node6 = new RelationItem("node6", new Connector(Direction.OUT));
			RelationItem node7 = new RelationItem("node7", new Connector(Direction.IN), new Connector(Direction.OUT));
			RelationItem node8 = new RelationItem("node8", new Connector(Direction.OUT));
			RelationItem node9 = new RelationItem("node9", new Connector(Direction.OUT, Connector.PURPLE));
			RelationItem node10 = new ImageRelationItem("node10", 200, 200, new Connector(Direction.OUT, Connector.PURPLE), new Connector(Direction.OUT, Connector.PURPLE), new Connector(Direction.OUT, Connector.PURPLE));
			RelationItem node11 = new RelationItem("node11", new Connector(Direction.IN, Connector.YELLOW));
			RelationItem node12 = new RelationItem("node12", new Connector(Direction.IN, Connector.PURPLE));
			RelationItem node13 = new RelationItem("node13", new Connector(Direction.IN, Connector.YELLOW));
			RelationItem node14 = new RelationItem("node14", new Connector(Direction.IN, Connector.GRAY));
			RelationItem node15 = new RelationItem("node15", new Connector(Direction.IN, Connector.GRAY));
			RelationItem node16 = new RelationItem("node16", new Connector(Direction.IN, Connector.GRAY));
			RelationItem node17 = new RelationItem("node17", new Connector(Direction.IN, Connector.GRAY));
			RelationItem node18 = new RelationItem("node18", new Connector(Direction.IN, Connector.GRAY));
			RelationBox nodeBox0 = new RelationBox("nodeBox0");
			RelationBox nodeBox1 = new RelationBox("nodeBox1");
			RelationBox nodeBox2 = new RelationBox("nodeBox2");
			RelationBox nodeBox3 = new RelationBox("nodeBox3");
			RelationBox nodeBox4 = new RelationBox("nodeBox4");
			RelationBox nodeBox5 = new RelationBox("nodeBox5");
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
