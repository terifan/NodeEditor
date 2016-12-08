package org.terifan.nodeeditor.v2;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JComponent;


public class RelationEditorPane extends JComponent
{
	private static final long serialVersionUID = 1L;

	private ArrayList<RelationBox> mBoxes;


	public RelationEditorPane()
	{
		mBoxes = new ArrayList<>();
	}


	public void add(RelationBox aBox)
	{
		mBoxes.add(aBox);
	}


	public void addConnection(RelationItem aFromItem, RelationItem aToItem)
	{
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		Graphics2D g = (Graphics2D)aGraphics;

		g.setColor(Styles.PANE_BACKGROUND_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (RelationBox box : mBoxes)
		{
			box.paintComponent(g);
		}
	}


	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(100,100);
	}
}
