package org.terifan.ui.relationeditor;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;


public class DefaultConnection implements Connection, Serializable
{
	private RelationLine mLineRenderer;
	private RelationItem mFrom;
	private RelationItem mTo;


	public DefaultConnection(RelationItem aFrom, RelationItem aTo)
	{
		mLineRenderer = new RelationLine();

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
	public void draw(Graphics2D aGraphics)
	{
		RelationEditor editor = RelationEditor.findEditor(mFrom.getComponent());

		if (editor == null)
		{
			return;
		}

		RelationBox fromBox = editor.getRelationBox(getFrom());
		RelationBox toBox = editor.getRelationBox(getTo());

		if (fromBox == null || toBox == null)
		{
			return;
		}

		Anchor[] anchorsFrom = fromBox.getConnectionAnchors(getFrom());
		Anchor[] anchorsTo = toBox.getConnectionAnchors(getTo());

		if (anchorsFrom == null || anchorsTo == null)
		{
			return;
		}

		Anchor bestFrom = null;
		Anchor bestTo = null;
		double dist = Integer.MAX_VALUE;

		for (Anchor fromAnchor : anchorsFrom)
		{
			for (Anchor toAnchor : anchorsTo)
			{
				Rectangle from = fromAnchor.getBounds();
				Rectangle to = toAnchor.getBounds();

				double dx = from.getCenterX() - to.getCenterX();
				double dy = from.getCenterY() - to.getCenterY();
				double dsqr = dx * dx + dy * dy;

				if (dsqr < dist)
				{
					dist = dsqr;
					bestFrom = fromAnchor;
					bestTo = toAnchor;
				}
			}
		}

		if (bestFrom != null)
		{
			mLineRenderer.render(aGraphics, bestFrom, bestTo);
		}
	}
}
