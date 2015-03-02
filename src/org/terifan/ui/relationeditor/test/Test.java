package org.terifan.ui.relationeditor.test;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.terifan.ui.ColumnLayout;
import org.terifan.ui.Utilities;
import org.terifan.ui.relationeditor.DefaultRelationItem;
import org.terifan.ui.relationeditor.DefaultConnection;
import org.terifan.ui.relationeditor.RelationListBox;
import org.terifan.ui.relationeditor.RelationEditor;


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

			RelationEditor editor = new RelationEditor();

			editor.add(boxA);
			editor.add(boxB);
			editor.add(boxC);
			editor.add(boxD);

			editor.addRelationship(new DefaultConnection(itemA, itemC));
			editor.addRelationship(itemB, itemF);
			editor.addRelationship(itemD, itemG);
			editor.addRelationship(itemE, itemH1);

			editor.arrangeBoxes();

			boxA.getBounds().y += 50;
			boxC.getBounds().x = boxB.getBounds().x;
			boxC.getBounds().y = boxB.getBounds().y + boxB.getBounds().height + 10+100;
			boxD.getBounds().x = boxC.getBounds().x + 150;
			boxD.getBounds().y = boxB.getBounds().y + 75;

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
