package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.stream.Stream;


public class Connector
{
	public final static Color PURPLE = new Color(0x6363C7);
	public final static Color GRAY = new Color(0xA1A1A1);
	public final static Color YELLOW = new Color(0xC7C729);

	protected final Rectangle mBounds = new Rectangle();
	protected final Direction mDirection;
	protected NodeItem mNodeItem;
	protected Color mColor;


	public Connector(Direction aDirection)
	{
		this(aDirection, YELLOW);
	}


	public Connector(Direction aDirection, Color aColor)
	{
		mDirection = aDirection;
		mColor = aColor;
	}


	void bind(NodeItem aNodeItem)
	{
		mNodeItem = aNodeItem;
	}


	public NodeItem getNodeItem()
	{
		return mNodeItem;
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
		Rectangle bounds = mNodeItem.getNodeBox().getBounds();

		return new Point(bounds.x + mBounds.x + mBounds.width / 2, bounds.y + mBounds.y + mBounds.height / 2);
	}


	public Stream<NodeItem> getConnectedItems()
	{
		return mNodeItem.getNodeBox().getEditorPane().getConnectionsTo(this);
	}
}
