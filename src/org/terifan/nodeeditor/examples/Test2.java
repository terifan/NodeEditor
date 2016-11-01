package org.terifan.nodeeditor.examples;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.terifan.ui.Utilities;
import org.terifan.nodeeditor.DefaultRelationItem;
import org.terifan.nodeeditor.AreaRelationBox;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.ListRelationBox;
import org.terifan.nodeeditor.RelationEditorPane;
import org.terifan.nodeeditor.StackedRelationBox;
import org.terifan.nodeeditor.StackedRelationItem;


public class Test2
{
	public static void main(String ... args)
	{
		try
		{
			Utilities.setSystemLookAndFeel();

			DefaultRelationItem itemA = new DefaultRelationItem("hello");
			DefaultRelationItem itemB = new DefaultRelationItem("world");
			DefaultRelationItem itemC = new DefaultRelationItem("HELLO");
			DefaultRelationItem itemD = new DefaultRelationItem("horsy");
			DefaultRelationItem itemE = new DefaultRelationItem("doggy");
			DefaultRelationItem itemF = new DefaultRelationItem("WORLD");
			DefaultRelationItem itemG = new DefaultRelationItem("horsy");
			DefaultRelationItem itemH1 = new DefaultRelationItem("doggy1");
			DefaultRelationItem itemH2 = new DefaultRelationItem("doggy2");
			DefaultRelationItem itemH3 = new DefaultRelationItem("doggy3");
			DefaultRelationItem itemI1 = new DefaultRelationItem("cat1");
			DefaultRelationItem itemI2 = new DefaultRelationItem("cat2");
			DefaultRelationItem itemI3 = new DefaultRelationItem("cat3");
			StackedRelationItem itemJ1 = new StackedRelationItem("stacked1", 20, null);
			StackedRelationItem itemJ2 = new StackedRelationItem("stacked2", 20, Direction.IN);
			StackedRelationItem itemJ3 = new StackedRelationItem("stacked3", 100, Direction.IN, 1.0);
			StackedRelationItem itemJ4 = new StackedRelationItem("stacked4", 20, Direction.OUT);
			DefaultRelationItem itemK1 = new DefaultRelationItem("k1");
			DefaultRelationItem itemK2 = new DefaultRelationItem("k2");

			ListRelationBox boxA = new ListRelationBox("Box A");
			boxA.addItem(itemA);
			boxA.addItem(itemB);

			ListRelationBox boxB = new ListRelationBox("Box B");
			boxB.addItem(itemC);
			boxB.addItem(itemD);

			ListRelationBox boxC = new ListRelationBox("Box C");
			boxC.addItem(itemE);
			boxC.addItem(itemF);

			ListRelationBox boxD = new ListRelationBox("Box D");
			boxD.addItem(itemG);
			boxD.addItem(itemH1);
			boxD.addItem(itemH2);
			boxD.addItem(itemH3);

			ListRelationBox boxG = new ListRelationBox("Box G");
			boxG.addItem(itemK1);
			boxG.addItem(itemK2);

			AreaRelationBox boxE = new AreaRelationBox("Box E");
			boxE.addItem(itemI1, new Rectangle(0,0,50,100));
			boxE.addItem(itemI2, new Rectangle(50,0,50,50));
			boxE.addItem(itemI3, new Rectangle(50,50,50,50));

			StackedRelationBox boxJ = new StackedRelationBox("Box F");
			boxJ.addItem(itemJ1);
			boxJ.addItem(itemJ2);
			boxJ.addItem(itemJ3);
			boxJ.addItem(itemJ4);
			boxJ.setResizableVertical(false);
			boxJ.setResizableHorizontal(false);

			RelationEditorPane editor = new RelationEditorPane();

			boxA.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyPressed(KeyEvent aEvent)
				{
					if (aEvent.getKeyCode() == KeyEvent.VK_INSERT)
					{
						ListRelationBox box = (ListRelationBox)editor.getSelectedBox();
						box.addItem(new DefaultRelationItem("test"));
						editor.invalidate();
						editor.validate();
						editor.repaint();
					}
				}
			});

			editor.add(boxA);
			editor.add(boxB);
			editor.add(boxC);
			editor.add(boxD);
			editor.add(boxE);
			editor.add(boxJ);
			editor.add(boxG);

			editor.addConnection(itemA, itemC);
			editor.addConnection(itemB, itemF);
			editor.addConnection(itemD, itemG);
			editor.addConnection(itemE, itemH1);
			editor.addConnection(itemH1, itemI1);
			editor.addConnection(itemH2, itemI2);
			editor.addConnection(itemH3, itemI3);
			editor.addConnection(itemI2, itemJ2);
			editor.addConnection(itemI3, itemJ3);
			editor.addConnection(itemJ3, itemK1);
			editor.addConnection(itemJ4, itemK2);

			editor.arrangeBoxes();

			boxA.setLocation(boxA.getLocation().x, 50);
			boxC.setLocation(boxB.getLocation().x, boxB.getLocation().y + boxB.getBounds().height + 10+100);
			boxD.setLocation(boxC.getLocation().x + 150, boxB.getLocation().y + 75);
			boxE.setLocation(boxD.getLocation().x + 150, boxD.getLocation().y + 150);

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
