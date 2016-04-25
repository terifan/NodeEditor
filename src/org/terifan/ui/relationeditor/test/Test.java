package org.terifan.ui.relationeditor.test;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.terifan.ui.Utilities;
import org.terifan.ui.relationeditor.DefaultRelationItem;
import org.terifan.ui.relationeditor.DefaultConnection;
import org.terifan.ui.relationeditor.RelationAreaBox;
import org.terifan.ui.relationeditor.RelationListBox;
import org.terifan.ui.relationeditor.RelationEditor;
import org.terifan.util.log.Log;


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

			RelationListBox boxA = new RelationListBox("Box A");
			boxA.add(itemA);
			boxA.add(itemB);

			RelationListBox boxB = new RelationListBox("Box B");
			boxB.add(itemC);
			boxB.add(itemD);

			RelationListBox boxC = new RelationListBox("Box C");
			boxC.add(itemE);
			boxC.add(itemF);

			RelationListBox boxD = new RelationListBox("Box D");
			boxD.add(itemG);
			boxD.add(itemH1);
			boxD.add(itemH2);
			boxD.add(itemH3);

			RelationAreaBox boxE = new RelationAreaBox("Box E");
			boxE.add(itemI1, new Rectangle(0,0,50,100));
			boxE.add(itemI2, new Rectangle(50,0,50,50));
			boxE.add(itemI3, new Rectangle(50,50,50,50));

			RelationEditor editor = new RelationEditor();

			editor.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyPressed(KeyEvent aEvent)
				{
					if (aEvent.getKeyCode() == KeyEvent.VK_DELETE && editor.getSelectedConnection() != null)
					{
						editor.removeConnection(editor.getSelectedConnection());
						editor.repaint();
					}
					if (aEvent.getKeyCode() == KeyEvent.VK_INSERT && editor.getSelectedBox() != null)
					{
						if (editor.getSelectedBox() instanceof RelationListBox)
						{
							RelationListBox box = (RelationListBox)editor.getSelectedBox();
							box.add(new DefaultRelationItem("test"));
							editor.repaint();
						}
					}
				}
			});
			boxA.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mousePressed(MouseEvent aEvent)
				{
					aEvent.getComponent().requestFocusInWindow();
				}
			});
			boxA.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyPressed(KeyEvent aEvent)
				{
					if (aEvent.getKeyCode() == KeyEvent.VK_INSERT)
					{
						RelationListBox box = (RelationListBox)editor.getSelectedBox();
						box.add(new DefaultRelationItem("test"));
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

			editor.addConnection(new DefaultConnection(itemA, itemC));
			editor.addConnection(itemB, itemF);
			editor.addConnection(itemD, itemG);
			editor.addConnection(itemE, itemH1);
			editor.addConnection(itemH1, itemI1);
			editor.addConnection(itemH2, itemI2);
			editor.addConnection(itemH3, itemI3);

			editor.arrangeBoxes();

			boxA.setLocation(boxA.getLocation().x, boxA.getLocation().y = 50);
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
