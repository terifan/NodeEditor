package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.terifan.ui.Utilities;


public class RelationEditor extends JPanel
{
	private final static long serialVersionUID = 1L;

//	private ArrayList<RelationBox> mBoxes;
	private ArrayList<Connection> mRelationships;
	private RelationEditorLayoutManager mRelationEditorLayoutManager;
	private JComponent mSelectedComponent;


	public RelationEditor()
	{
		mRelationEditorLayoutManager = new RelationEditorLayoutManager(this);

		setLayout(mRelationEditorLayoutManager);
		setBackground(new Color(68,68,68));

		mRelationships = new ArrayList<>();

//		RelationEditorMouseListener ml = new RelationEditorMouseListener(this);
//		addMouseListener(ml);
//		addMouseMotionListener(ml);
	}


//	public ArrayList<RelationBox> getBoxes()
//	{
//		return mBoxes;
//	}
//
//
//	public void addBox(RelationBox aBox)
//	{
//		mBoxes.add(aBox);
//	}


	public void addRelationship(Connection aRelationship)
	{
		mRelationships.add(aRelationship);
	}


	public void addRelationship(RelationItem aFrom, RelationItem aTo)
	{
		mRelationships.add(new DefaultConnection(aFrom, aTo));
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		Graphics2D g = (Graphics2D)aGraphics;

		Utilities.enableTextAntialiasing(g);
		Utilities.enableAntialiasing(g);

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		drawRelationLines(g);
	}


	protected RelationBox getRelationBox(RelationItem aItem)
	{
		for (int i = 0; i < getComponentCount(); i++)
		{
			RelationBox box = (RelationBox)getComponent(i);
			for (int j = 0; j < box.getComponentCount(); j++)
			{
				if (box.getComponent(j) == aItem)
				{
					return box;
				}
			}
		}

		return null;
	}


	protected void drawRelationLines(Graphics2D aGraphics)
	{
		for (Connection relationship : mRelationships)
		{
			RelationBox fromBox = getRelationBox(relationship.getFrom());
			RelationBox toBox = getRelationBox(relationship.getTo());

			if (fromBox == null || toBox == null)
			{
				continue;
			}

			Rectangle[] anchorsFrom = fromBox.getAnchors(relationship.getFrom());
			Rectangle[] anchorsTo = toBox.getAnchors(relationship.getTo());

			Rectangle bestFrom = null;
			Rectangle bestTo = null;
			boolean fromLeft = true;
			boolean toLeft = true;
			double dist = Integer.MAX_VALUE;

			for (Rectangle from : anchorsFrom)
			{
				for (Rectangle to : anchorsTo)
				{
					int dx = from.x - to.x;
					int dy = from.y + from.height / 2 - to.y + to.height / 2;
					double d = Math.sqrt(dx*dx+dy*dy);
					if (d < dist)
					{
						dist = d;
						bestFrom = from;
						bestTo = to;
						fromLeft = from.x < fromBox.getBounds().getCenterX();
						toLeft = to.x < toBox.getBounds().getCenterX();
					}
				}
			}

			if (bestFrom != null)
			{
				new RelationLine().render(aGraphics, bestFrom, bestTo, fromLeft, toLeft);
			}
		}
	}


	protected void setSelectedComponent(JComponent aElement)
	{
		mSelectedComponent = aElement;

//		if (aElement != null)
//		{
//			remove(aElement);
//			add(aElement, 0);
//			repaint();
//		}
	}


	public JComponent getSelectedComponent()
	{
		return mSelectedComponent;
	}


	public void arrangeBoxes()
	{
		int x = 10;
		int y = 10;

		for (int i = 0; i < getComponentCount(); i++)
		{
			RelationBox box  = (RelationBox)getComponent(i);

			Dimension d = box.getPreferredSize();

			d.width = Math.max(d.width, 80);

			box.setBounds(x, y, d.width, d.height);

			x += d.width + 50;
			y += 20;
		}
	}
}
