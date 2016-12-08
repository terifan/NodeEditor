package org.terifan.nodeeditor.v2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class RelationItem
{
	protected String mName;
	protected Rectangle mBounds;
	protected Dimension mSize;


	public RelationItem(String aName, Direction aDirection)
	{
		this(aName, 100, 20, aDirection, 0);
	}


	public RelationItem(String aName, int aWidth, int aHeight, Direction aDirection)
	{
		this(aName, aWidth, aHeight, aDirection, 0);
	}


	public RelationItem(String aName, int aWidth, int aHeight, Direction aDirection, double aWeight)
	{
		mName = aName;
		mSize = new Dimension(aWidth, aHeight);
		mBounds = new Rectangle();
	}


	protected void paintComponent(Graphics2D aGraphics)
	{
		aGraphics.setColor(Color.YELLOW);//Styles.ITEM_BACKGROUND_COLOR);
		aGraphics.fill(mBounds);
		aGraphics.setColor(Styles.BOX_FOREGROUND_COLOR);
		aGraphics.drawString(mName, mBounds.x, mBounds.y+16);
	}


	protected Rectangle getBounds()
	{
		return mBounds;
	}


	protected Dimension getPreferredSize()
	{
		return mSize;
	}
}
