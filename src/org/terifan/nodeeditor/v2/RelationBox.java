package org.terifan.nodeeditor.v2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.terifan.util.log.Log;


public class RelationBox
{
	private String mName;
	private Rectangle mBounds;
	private ArrayList<RelationItem> mItems;


	public RelationBox(String aName)
	{
		mName = aName;
		mItems = new ArrayList<>();
		mBounds = new Rectangle(0, 30);
	}


	public void addItem(RelationItem aItem)
	{
		mItems.add(aItem);
	}


	public void setLocation(int aX, int aY)
	{
		mBounds.setLocation(aX, aY);
	}


	protected void paintComponent(Graphics2D aGraphics)
	{
		layout();

		aGraphics.setColor(Styles.BOX_BACKGROUND_COLOR);
		aGraphics.fill(mBounds);
		aGraphics.setColor(Styles.BOX_BORDER_COLOR);
		aGraphics.draw(mBounds);
		aGraphics.setColor(Styles.BOX_FOREGROUND_COLOR);
		aGraphics.drawString(mName, mBounds.x, mBounds.y + 15);

		for (RelationItem item : mItems)
		{
			item.paintComponent(aGraphics);
		}
	}


	protected void layout()
	{
		mBounds.width = 0;
		mBounds.height = 25;

		for (RelationItem item : mItems)
		{
			Dimension size = item.getPreferredSize();

			mBounds.width = Math.max(mBounds.width, size.width + 10);
			mBounds.height += size.height;
		}

		int y = 20;

		for (RelationItem item : mItems)
		{
			Rectangle bounds = item.getBounds();
			Dimension size = item.getPreferredSize();

			bounds.setSize(mBounds.width-10, size.height);
			bounds.setLocation(mBounds.x + 5, mBounds.y + y);

			y += bounds.height;
		}
	}


	protected Rectangle getBounds()
	{
		return mBounds;
	}
}
