package org.terifan.ui.relationeditor;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;


public class DefaultConnection implements Connection, Serializable
{
	private final static long serialVersionUID = 1L;
	private RelationItem mFrom;
	private RelationItem mTo;


	public DefaultConnection(RelationItem aFrom, RelationItem aTo)
	{
		mFrom = aFrom;
		mTo = aTo;
	}


	@Override
	public RelationItem getFrom()
	{
		return mFrom;
	}


	public void setFrom(RelationItem aFrom)
	{
		mFrom = aFrom;
	}


	@Override
	public RelationItem getTo()
	{
		return mTo;
	}


	public void setTo(RelationItem aTo)
	{
		mTo = aTo;
	}


	@Override
	public void draw(Graphics2D aGraphics, RelationEditor aRelationEditor)
	{
		RelationBox fromBox = aRelationEditor.getRelationBox(getFrom());
		RelationBox toBox = aRelationEditor.getRelationBox(getTo());

		if (fromBox == null || toBox == null)
		{
			return;
		}

		Rectangle[] anchorsFrom = fromBox.getAnchors(getFrom());
		Rectangle[] anchorsTo = toBox.getAnchors(getTo());

		if (anchorsFrom == null || anchorsTo == null)
		{
			return;
		}

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
