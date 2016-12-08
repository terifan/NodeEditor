package org.terifan.nodeeditor.v2.examples;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.terifan.ui.Utilities;
import org.terifan.nodeeditor.v2.Direction;
import org.terifan.nodeeditor.v2.RelationEditorPane;
import org.terifan.nodeeditor.v2.RelationBox;
import org.terifan.nodeeditor.v2.RelationItem;


public class Test1
{
	public static void main(String ... args)
	{
		try
		{
			Utilities.setSystemLookAndFeel();

			RelationEditorPane editor = new RelationEditorPane();

			RelationItem node0 = new RelationItem("node0", 20, Direction.OUT);
			RelationItem node1 = new RelationItem("node1", 20, Direction.OUT);
			RelationItem node2 = new RelationItem("node2", 20, Direction.IN);
			RelationItem node3 = new RelationItem("node3", 20, Direction.OUT);
			RelationItem node4 = new RelationItem("node4", 20, Direction.IN);
			RelationItem node5 = new RelationItem("node5", 20, Direction.IN);
			RelationItem node6 = new RelationItem("node6", 20, Direction.OUT);
			RelationItem node7 = new RelationItem("node7", 20, Direction.IN);
			RelationItem node8 = new RelationItem("node8", 20, Direction.OUT);
			RelationItem node9 = new RelationItem("node9", 20, Direction.OUT);
			RelationItem node10 = new ImageRelationItem("node10", 100, null);
			RelationBox nodeBox0 = new RelationBox("nodeBox0");
			RelationBox nodeBox1 = new RelationBox("nodeBox1");
			RelationBox nodeBox2 = new RelationBox("nodeBox2");
			RelationBox nodeBox3 = new RelationBox("nodeBox3");
			RelationBox nodeBox4 = new RelationBox("nodeBox4");
			nodeBox0.addItem(node0);
			nodeBox0.addItem(node1);
			nodeBox1.addItem(node2);
			nodeBox1.addItem(node3);
			nodeBox2.addItem(node4);
			nodeBox2.addItem(node5);
			nodeBox2.addItem(node10);
			nodeBox2.addItem(node6);
			nodeBox3.addItem(node7);
			nodeBox4.addItem(node8);
			nodeBox4.addItem(node9);
			editor.add(nodeBox0);
			editor.add(nodeBox1);
			editor.add(nodeBox2);
			editor.add(nodeBox3);
			editor.add(nodeBox4);
			editor.addConnection(node0, node2);
			editor.addConnection(node3, node4);
			editor.addConnection(node8, node5);
			editor.addConnection(node6, node7);

			nodeBox0.setLocation(100, 50);
			nodeBox1.setLocation(300, 100);
			nodeBox2.setLocation(500, 150);
			nodeBox3.setLocation(800, 350);
			nodeBox4.setLocation(300, 200);

			JFrame frame = new JFrame();
			frame.add(new JScrollPane(editor));
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
