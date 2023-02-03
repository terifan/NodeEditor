package org.terifan.boxcomponentpane;

import javax.swing.JFrame;


public class Test
{
	public static void main(String ... args)
	{
		try
		{
			BoxComponentModel model = new BoxComponentModel();
			model.add(new EmptyBoxComponent("test").setBounds(-120, -50, 100, 100));
			model.add(new EmptyBoxComponent("test").setBounds(20, -50, 100, 100));
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
}
