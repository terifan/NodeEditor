package org.terifan.nodeeditor.examples;

import java.awt.Dimension;
import org.terifan.nodeeditor.SliderNodeItem;
import org.terifan.nodeeditor.ImageNodeItem;
import javax.swing.JFrame;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.NodeBox;
import org.terifan.nodeeditor.TextNodeItem;


public class Test3
{
	public static void main(String ... args)
	{
		try
		{
			NodeEditorPane editor = new NodeEditorPane();

			TextNodeItem node0 = new TextNodeItem("RGB", new Connector(Direction.IN, Connector.YELLOW));
			ImageNodeItem node1 = new ImageNodeItem("node10", 200, 200);
			SliderNodeItem slider1 = new SliderNodeItem("Red", 0, 1, 0);
			SliderNodeItem slider2 = new SliderNodeItem("Green", 0, 1, 0.5);
			SliderNodeItem slider3 = new SliderNodeItem("Blue", 0, 1, 0.75);
			TextNodeItem node2 = new TextNodeItem("RGB", new Connector(Direction.OUT, Connector.YELLOW));
			NodeBox nodeBox0 = new NodeBox("Input", slider1, slider2, slider3, node2).setSize(new Dimension(200,0));
			NodeBox nodeBox1 = new NodeBox("Output", node0, node1);

			editor.addConnection(node2, node0);
			editor.add(nodeBox0);
			editor.add(nodeBox1);
			
			nodeBox0.setLocation(100, 50);
			nodeBox1.setLocation(400, 0);
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
