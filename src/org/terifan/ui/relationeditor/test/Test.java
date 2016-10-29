package org.terifan.ui.relationeditor.test;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.terifan.ui.Utilities;
import org.terifan.ui.relationeditor.DefaultRelationItem;
import org.terifan.ui.relationeditor.DefaultConnection;
import org.terifan.ui.relationeditor.AreaRelationBox;
import org.terifan.ui.relationeditor.ListRelationBox;
import org.terifan.ui.relationeditor.RelationEditorPane;
import org.terifan.ui.relationeditor.StackedRelationBox;
import org.terifan.ui.relationeditor.StackedRelationItem;


public class Test
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
			StackedRelationItem itemJ1 = new StackedRelationItem("stacked1", 20, StackedRelationItem.Anchors.NONE);
			StackedRelationItem itemJ2 = new StackedRelationItem("stacked2", 20, StackedRelationItem.Anchors.LEFT);
			StackedRelationItem itemJ3 = new StackedRelationItem("stacked3", 100, StackedRelationItem.Anchors.LEFT, 1.0);
			StackedRelationItem itemJ4 = new StackedRelationItem("stacked4", 20, StackedRelationItem.Anchors.RIGHT);
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
//			boxJ.setResizableVertical(false);
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

//			editor.add(boxA);
//			editor.add(boxB);
//			editor.add(boxC);
//			editor.add(boxD);
//			editor.add(boxE);
//			editor.add(boxJ);
//			editor.add(boxG);

			editor.addConnection(new DefaultConnection(itemA, itemC));
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
