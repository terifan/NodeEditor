package org.terifan.boxcomponentpane;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JFrame;


public class Test
{
	public static void main(String... args)
	{
		try
		{
			BoxComponentModel model = new BoxComponentModel();
			model.addComponent(new EmptyBoxComponent("test1").setLocation(-120, -50));
			model.addComponent(new EmptyBoxComponent("test2").setLocation(20, -50));
			model.addComponent(new EmptyBoxComponent("test3").setLocation(-120, 50));
			model.addComponent(new EmptyBoxComponent("test4").setLocation(20, 50));
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
			aGraphics.drawRect(mInsets.left, mInsets.top, aWidth - mInsets.left - mInsets.right, aHeight - mInsets.top - mInsets.bottom);
		}
	}
}
