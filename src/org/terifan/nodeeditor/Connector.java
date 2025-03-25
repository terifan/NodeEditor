package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.YELLOW;


public class Connector implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected final Rectangle mBounds;
	protected Direction mDirection;
	protected Property mProperty;
	protected Color mColor;


	public Connector()
	{
		mBounds = new Rectangle();
	}


	public Connector(Direction aDirection)
	{
		this(aDirection, YELLOW);
	}


	public Connector(Direction aDirection, Color aColor)
	{
		mBounds = new Rectangle();
		mDirection = aDirection;
		mColor = aColor;
	}


	void bind(Property aNodeItem)
	{
		mProperty = aNodeItem;
	}


	public Property getProperty()
	{
		return mProperty;
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
		Rectangle bounds = mProperty.getNode().getBounds();

		return new Point(bounds.x + mBounds.x + mBounds.width / 2, bounds.y + mBounds.y + mBounds.height / 2);
	}


	public List<Property> getConnectedProperties()
	{
		NodeModel model = mProperty.getNode().getModel();

		return mDirection == Direction.IN ? model.getConnectionsTo(this) : model.getConnectionsFrom(this);
	}


	@Override
	public String toString()
	{
		return "Connector{" + "Node:" + getProperty().getNode().getTitle() + ", Property:" + getProperty().getId() + ", " + "mDirection=" + mDirection + '}';
	}
}
