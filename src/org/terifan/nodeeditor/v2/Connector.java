package org.terifan.nodeeditor.v2;

import java.awt.Color;
import java.awt.Rectangle;


public class Connector
{
	public final static Color PURPLE = new Color(0x6363C7);
	public final static Color GRAY = new Color(0xA1A1A1);
	public final static Color YELLOW = new Color(0xC7C729);

	private final Rectangle mBounds;
	private final Direction mDirection;
	private Color mColor;
	protected RelationItem mRelationItem;


	public Connector(Direction aDirection)
	{
		this(aDirection, YELLOW);
	}


	public Connector(Direction aDirection, Color aColor)
	{
		mDirection = aDirection;
		mColor = aColor;
		mBounds = new Rectangle();
	}


	public Direction getDirection()
	{
		return mDirection;
	}


	public Color getColor()
	{
		return mColor;
	}


	public Rectangle getBounds()
	{
		return mBounds;
	}
}
