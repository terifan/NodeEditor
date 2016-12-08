package org.terifan.nodeeditor.v2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;


public class RelationBox
{
	private String mName;
	private Rectangle mBounds;
	private ArrayList<RelationItem> mItems;


	public RelationBox(String aName)
	{
		mName = aName;
		mBounds = new Rectangle();
		mItems = new ArrayList<>();
	}


	public void addItem(RelationItem aItem)
	{
		mItems.add(aItem);

		Dimension size = aItem.getBounds().getSize();

		mBounds.width = Math.max(mBounds.width, size.width);
		mBounds.height += size.height;
	}


	public void setLocation(int aX, int aY)
	{
		mBounds.setLocation(aX, aY);
	}


	protected void paintComponent(Graphics2D aGraphics)
	{
		aGraphics.setColor(Styles.BOX_BACKGROUND_COLOR);
		aGraphics.fill(mBounds);
		aGraphics.setColor(Styles.BOX_BORDER_COLOR);
		aGraphics.draw(mBounds);

		int y = 20;
		for (RelationItem item : mItems)
		{
			Rectangle bounds = item.getBounds();
			bounds.setLocation(0, y);
			y += bounds.height;
		}

		for (RelationItem item : mItems)
		{
			aGraphics.translate(item.mBounds.x, item.mBounds.y);

			item.paintComponent(aGraphics);

			aGraphics.translate(-item.mBounds.x, -item.mBounds.y);
		}
	}


	protected Dimension getPreferredSize()
	{
		Dimension d = mBounds.getSize();
		d.width += 10;
		d.height += 30;
		return d;
	}
}
