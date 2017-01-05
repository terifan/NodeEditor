package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.stream.Stream;


public class Connector
{
	public final static Color PURPLE = new Color(0x6363C7);
	public final static Color GRAY = new Color(0xA1A1A1);
	public final static Color YELLOW = new Color(0xC7C729);

	protected Rectangle mBounds = new Rectangle();
	protected Direction mDirection;
	protected Color mColor;
	protected NodeItem mItem;


	public Connector(Direction aDirection)
	{
		this(aDirection, YELLOW);
	}


	public Connector(Direction aDirection, Color aColor)
	{
		mDirection = aDirection;
		mColor = aColor;
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


	Point getConnectorPoint()
	{
		Rectangle bounds = mItem.mNodeBox.getBounds();

		return new Point(bounds.x + mBounds.x + mBounds.width / 2, bounds.y + mBounds.y + mBounds.height / 2);
	}
	
	
	public Stream<NodeItem> getConnectedItems()
	{
		return mItem.mNodeBox.mEditorPane.getConnectionsTo(this);
	}
}
