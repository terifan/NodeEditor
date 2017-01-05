package org.terifan.nodeeditor.examples;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
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

			BufferedImage image = new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB);

			ImageNodeItem node1 = new ImageNodeItem("node10", image, 200, 200);

			TextNodeItem node2 = new TextNodeItem("RGB-out", new Connector(Direction.OUT, Connector.YELLOW));

			SliderNodeItem slider0 = new SliderNodeItem("Alpha", 0, 1, 0.5).setOnChange((e,f)->{e.fireOnChange();});
			SliderNodeItem slider1 = new SliderNodeItem("Red", 0, 1, 0);
			SliderNodeItem slider2 = new SliderNodeItem("Green", 0, 1, 0.5);
			SliderNodeItem slider3 = new SliderNodeItem("Blue", 0, 1, 0.75);

			TextNodeItem node0 = new TextNodeItem("RGB-in", new Connector(Direction.IN, Connector.YELLOW));
			SliderNodeItem slider4 = new SliderNodeItem("Alpha-2", 0, 1, 0.75, new Connector(Direction.IN, Connector.GRAY));

			SliderNodeItem slider5 = new SliderNodeItem("Alpha-2-out", 0, 1, 0.75, new Connector(Direction.OUT, Connector.GRAY));

			NodeBox nodeBox0 = new NodeBox("Input", slider1, slider2, slider3, slider0, node2).setSize(new Dimension(200,0));
			NodeBox nodeBox1 = new NodeBox("Output", node0, slider4, node1).setOnInputChange((e,f)->{
				double a = slider0.getValue() * slider4.getValue();
				double r = slider1.getValue();
				double g = slider2.getValue();
				double b = slider3.getValue();
				for (int y = 0; y < 200; y++) for (int x = 0; x < 200; x++) image.setRGB(x, y, ((int)(255*a)<<24)+((int)(255*r)<<16)+((int)(255*g)<<8)+(int)(255*b));
			});
			NodeBox nodeBox2 = new NodeBox("Input", slider5);

			editor.addConnection(node2, node0);
			editor.add(nodeBox0);
			editor.add(nodeBox1);
			editor.add(nodeBox2);

			nodeBox0.setLocation(100, 0);
			nodeBox1.setLocation(400, 100);
			nodeBox2.setLocation(100, 200);
			editor.center();
			editor.setScale(1);

			JFrame frame = new JFrame();
			frame.add(editor);
			frame.setSize(1600*(int)editor.getScale(), 1000*(int)editor.getScale());
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
