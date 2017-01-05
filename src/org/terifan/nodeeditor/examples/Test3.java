package org.terifan.nodeeditor.examples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import org.terifan.nodeeditor.SliderNodeItem;
import org.terifan.nodeeditor.ImageNodeItem;
import javax.swing.JFrame;
import org.terifan.nodeeditor.ColorChooserNodeItem;
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

			//

			SliderNodeItem slider0 = new SliderNodeItem("Alpha", 0, 1, 0.5).addConnector(new Connector(Direction.IN, Connector.GRAY));
			SliderNodeItem slider1 = new SliderNodeItem("Red", 0, 1, 0).addConnector(new Connector(Direction.IN, Connector.GRAY));
			SliderNodeItem slider2 = new SliderNodeItem("Green", 0, 1, 0.5).addConnector(new Connector(Direction.IN, Connector.GRAY));
			SliderNodeItem slider3 = new SliderNodeItem("Blue", 0, 1, 0.75).addConnector(new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem node2 = new TextNodeItem("Color")
				.addConnector(new Connector(Direction.OUT, Connector.YELLOW))
//				.setValueProvider(e->{
//					double a = slider0.getValue();
//					double r = slider1.getValue();
//					double g = slider2.getValue();
//					double b = slider3.getValue();
//					return new double[]{r,g,b,a};
//				})
				;

			NodeBox nodeBox0 = new NodeBox("Color", node2, slider1, slider2, slider3, slider0).setSize(new Dimension(200,0));

			//

			SliderNodeItem slider10 = new SliderNodeItem("Alpha", 0, 1, 0.5).addConnector(new Connector(Direction.IN, Connector.GRAY));
			SliderNodeItem slider11 = new SliderNodeItem("Red", 0, 1, 0).addConnector(new Connector(Direction.IN, Connector.GRAY));
			SliderNodeItem slider12 = new SliderNodeItem("Green", 0, 1, 0.5).addConnector(new Connector(Direction.IN, Connector.GRAY));
			SliderNodeItem slider13 = new SliderNodeItem("Blue", 0, 1, 0.75).addConnector(new Connector(Direction.IN, Connector.GRAY));
			TextNodeItem node12 = new TextNodeItem("Color", new Connector(Direction.OUT, Connector.YELLOW));

			NodeBox nodeBox4 = new NodeBox("Color", node12, slider11, slider12, slider13, slider10).setSize(new Dimension(200,0));

			//

			ColorChooserNodeItem node0 = new ColorChooserNodeItem("Color", new Color(0,0,0)).addConnector(new Connector(Direction.IN, Connector.YELLOW));
			SliderNodeItem slider4 = new SliderNodeItem("Alpha", 0, 1, 0.75).addConnector(new Connector(Direction.IN, Connector.GRAY));
			BufferedImage image = new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB);
			ImageNodeItem imageNodeItem = new ImageNodeItem("node10", image, 200, 200).addConnector(new Connector(Direction.OUT, Connector.YELLOW));

			NodeBox nodeBox1 = new NodeBox("Output", imageNodeItem, node0, slider4).setOnInputChange((e,f)->{
//				System.out.println(node0.getValueProvider().getValue(e));
				double a = slider0.getValue() * slider4.getValue();
//				for (int y = 0; y < 200; y++) for (int x = 0; x < 200; x++) image.setRGB(x, y, ((int)(255*a)<<24)+((int)(255*r)<<16)+((int)(255*g)<<8)+(int)(255*b));
			});

			//

			SliderNodeItem slider5 = new SliderNodeItem("Alpha", 0, 1, 0.75).addConnector(new Connector(Direction.OUT, Connector.GRAY));

			NodeBox nodeBox2 = new NodeBox("Alpha", slider5).setSize(new Dimension(200,0));;

			//

			ColorChooserNodeItem node3 = new ColorChooserNodeItem("Color", new Color(255,0,0)).addConnector(new Connector(Direction.IN, Connector.YELLOW));
			ColorChooserNodeItem node4 = new ColorChooserNodeItem("Color", new Color(0,0,255)).addConnector(new Connector(Direction.IN, Connector.YELLOW));
			TextNodeItem node5 = new TextNodeItem("Color").addConnector(new Connector(Direction.OUT, Connector.YELLOW));
			SliderNodeItem slider6 = new SliderNodeItem("Fac", 0, 1, 0.5).addConnector(new Connector(Direction.IN, Connector.GRAY));

			NodeBox nodeBox3 = new NodeBox("Mix", node5, slider6, node3, node4).setSize(new Dimension(200,0));;

			//

			editor.addConnection(node2, node4);
			editor.addConnection(node12, node3);
			editor.addConnection(node5, node0);
			editor.addConnection(slider5, slider4);

			editor.add(nodeBox0);
			editor.add(nodeBox1);
			editor.add(nodeBox2);
			editor.add(nodeBox3);
			editor.add(nodeBox4);

			nodeBox0.setLocation(0, 0);
			nodeBox1.setLocation(600, 100);
			nodeBox2.setLocation(0, 200);
			nodeBox3.setLocation(300, -50);
			nodeBox4.setLocation(0, -160);
			editor.center();
			editor.setScale(2);

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
