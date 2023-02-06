package org.terifan.boxcomponentpane;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import static org.terifan.nodeeditor.Styles.TITLE_HEIGHT_PADDED;


public class Test
{
	public static void main(String ... args)
	{
		try
		{
			BoxComponentModel model = new BoxComponentModel();
			model.add(new EmptyBoxComponent("test").setLocation(-120, -50));
			model.add(new EmptyBoxComponent("test").setLocation(20, -50));
			BoxComponentPane pane = new BoxComponentPane(model);

			JFrame frame = new JFrame();
			frame.add(pane);
			frame.setSize(1024, 768);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}


	static class EmptyBoxComponent extends BoxComponent
	{
		public EmptyBoxComponent(String aName)
		{
			super(aName);

			mBounds.setSize(100, 100);
		}


		@Override
		public void paintComponent(BoxComponentPane aPane, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected)
		{
			super.paintComponent(aPane, aGraphics, aWidth, aHeight, aSelected);

			aGraphics.setColor(Color.RED);
			aGraphics.drawRect(5+9, TITLE_HEIGHT_PADDED+6+4, aWidth-5-9-5-9, aHeight-TITLE_HEIGHT_PADDED-6-4-4-6);
		}
	}
}
