package org.terifan.nodeeditor.v2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class RelationItem
{
	protected String mName;
	protected Rectangle mBounds;


	public RelationItem(String aName, int aHeight, Direction aDirection)
	{
		this(aName, aHeight, aDirection, 0);
	}


	public RelationItem(String aName, int aHeight, Direction aDirection, double aWeight)
	{
		mName = aName;
		mBounds = new Rectangle(0, 0, 150, aHeight);
	}


	protected void paintComponent(Graphics2D aGraphics)
	{
		aGraphics.setColor(Styles.ITEM_BACKGROUND_COLOR);
		aGraphics.fill(mBounds);
		aGraphics.setColor(Styles.BOX_FOREGROUND_COLOR);
		aGraphics.drawString(mName, mBounds.x, mBounds.y + 16);
	}


	protected Rectangle getBounds()
	{
		return mBounds;
	}
}
