package org.terifan.nodeeditor.examples;

import javax.swing.JFrame;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.NodeBox;
import org.terifan.nodeeditor.NodeItem;


public class Test2
{
	public static void main(String ... args)
	{
		try
		{
			NodeEditorPane editor = new NodeEditorPane();

			editor.add(
				new NodeBox("nodeBox0",
					new NodeItem("node0-0", new Connector(Direction.IN)),
					new NodeItem("node0-1", new Connector(Direction.OUT)),
					new NodeItem("node0-2", new Connector(Direction.OUT))
				)
			);
			editor.add(
				new NodeBox("nodeBox1",
					new NodeItem("node1-0", new Connector(Direction.IN)),
					new NodeItem("node1-1", new Connector(Direction.OUT)),
					new NodeItem("node1-2", new Connector(Direction.OUT))
				)
			);
			editor.add(
				new NodeBox("nodeBox2",
					new NodeItem("node2-0", new Connector(Direction.IN)),
					new NodeItem("node2-1", new Connector(Direction.OUT)),
					new NodeItem("node2-2", new Connector(Direction.OUT))
				)
			);
			editor.add(
				new NodeBox("nodeBox3",
					new NodeItem("node3-0", new Connector(Direction.IN)),
					new NodeItem("node3-1", new Connector(Direction.OUT)),
					new NodeItem("node3-2", new Connector(Direction.OUT))
				)
			);
			editor.add(
				new NodeBox("nodeBox4",
					new NodeItem("node4-0", new Connector(Direction.IN)),
					new NodeItem("node4-1", new Connector(Direction.OUT)),
					new NodeItem("node4-2", new Connector(Direction.OUT))
				)
			);
			editor.add(
				new NodeBox("nodeBox5",
					new NodeItem("node5-0", new Connector(Direction.IN)),
					new NodeItem("node5-1", new Connector(Direction.OUT)),
					new NodeItem("node5-2", new Connector(Direction.OUT))
				)
			);
			editor.add(
				new NodeBox("nodeBox6",
					new NodeItem("node6-0", new Connector(Direction.IN)),
					new NodeItem("node6-1", new Connector(Direction.OUT)),
					new NodeItem("node6-2", new Connector(Direction.OUT))
				)
			);

			editor.addConnection("node0-1", "node1-0");
			editor.addConnection("node1-1", "node2-0");
			editor.addConnection("node2-1", "node3-0");
			editor.addConnection("node2-2", "node4-0");
			editor.addConnection("node4-1", "node5-0");
			editor.addConnection("node1-2", "node6-0");
			editor.addConnection("node6-1", "node4-0");

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
