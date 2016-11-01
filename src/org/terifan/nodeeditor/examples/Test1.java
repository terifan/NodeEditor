package org.terifan.nodeeditor.examples;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.terifan.ui.Utilities;
import org.terifan.nodeeditor.RelationEditorPane;
import org.terifan.nodeeditor.StackedRelationBox;
import org.terifan.nodeeditor.StackedRelationItem;


public class Test1
{
	public static void main(String ... args)
	{
		try
		{
			Utilities.setSystemLookAndFeel();

			RelationEditorPane editor = new RelationEditorPane();

			StackedRelationItem node0 = new StackedRelationItem("node0", 20, StackedRelationItem.Anchors.RIGHT);
			StackedRelationItem node1 = new StackedRelationItem("node1", 20, StackedRelationItem.Anchors.RIGHT);
			StackedRelationItem node2 = new StackedRelationItem("node2", 20, StackedRelationItem.Anchors.LEFT);
			StackedRelationItem node3 = new StackedRelationItem("node3", 20, StackedRelationItem.Anchors.RIGHT);
			StackedRelationItem node4 = new StackedRelationItem("node4", 20, StackedRelationItem.Anchors.LEFT);
			StackedRelationItem node5 = new StackedRelationItem("node5", 20, StackedRelationItem.Anchors.LEFT);
			StackedRelationItem node6 = new StackedRelationItem("node6", 20, StackedRelationItem.Anchors.RIGHT);
			StackedRelationItem node7 = new StackedRelationItem("node7", 20, StackedRelationItem.Anchors.LEFT);
			StackedRelationItem node8 = new StackedRelationItem("node8", 20, StackedRelationItem.Anchors.RIGHT);
			StackedRelationItem node9 = new StackedRelationItem("node9", 20, StackedRelationItem.Anchors.RIGHT);
			StackedRelationItem node10 = new ImageRelationItem("node10", 100, StackedRelationItem.Anchors.NONE);
			StackedRelationBox nodeBox0 = new StackedRelationBox("nodeBox0");
			StackedRelationBox nodeBox1 = new StackedRelationBox("nodeBox1");
			StackedRelationBox nodeBox2 = new StackedRelationBox("nodeBox2");
			StackedRelationBox nodeBox3 = new StackedRelationBox("nodeBox3");
			StackedRelationBox nodeBox4 = new StackedRelationBox("nodeBox4");
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

			editor.arrangeBoxes();

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
