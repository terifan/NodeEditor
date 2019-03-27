package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.terifan.bundle.Bundlable;
import org.terifan.bundle.Bundle;
import org.terifan.bundle.BundleHelper;


public class Connector implements Serializable, Bundlable
{
	private static final long serialVersionUID = 1L;

	private final static AtomicInteger REF_COUNTER = new AtomicInteger();

	public final static Color PURPLE = new Color(0x6363C7);
	public final static Color GRAY = new Color(0xA1A1A1);
	public final static Color YELLOW = new Color(0xC7C729);

	protected final Rectangle mBounds = new Rectangle();
	protected Direction mDirection;
	protected NodeItem mNodeItem;
	protected Color mColor;
	protected int mModelRef;


	public Connector()
	{
		mDirection = null;
		mModelRef = REF_COUNTER.getAndIncrement();
	}


	public Connector(Direction aDirection)
	{
		this(aDirection, YELLOW);
	}


	public Connector(Direction aDirection, Color aColor)
	{
		mDirection = aDirection;
		mColor = aColor;
		mModelRef = REF_COUNTER.getAndIncrement();
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


	protected int getModelRef()
	{
		return mModelRef;
	}


	Point getConnectorPoint()
	{
		Rectangle bounds = mNodeItem.getNode().getBounds();

		return new Point(bounds.x + mBounds.x + mBounds.width / 2, bounds.y + mBounds.y + mBounds.height / 2);
	}


	public Stream<NodeItem> getConnectedItems()
	{
		return mDirection == Direction.IN ? mNodeItem.getNode().getModel().getConnectionsTo(this) : mNodeItem.getNode().getModel().getConnectionsFrom(this);
	}


	@Override
	public void readExternal(Bundle aBundle)
	{
		mBounds.setBounds(BundleHelper.getRectangle(aBundle.getBundle("bounds"), new Rectangle()));
		mColor = aBundle.getString("color").equals("YELLOW") ? YELLOW : aBundle.getString("color").equals("PURPLE") ? PURPLE : aBundle.getString("color").equals("GRAY") ? GRAY : new Color(Integer.parseInt(aBundle.getString("color"), 16));
		mDirection = Direction.valueOf(aBundle.getString("direction"));
		mModelRef = aBundle.getInt("ref");
	}


	@Override
	public void writeExternal(Bundle aBundle)
	{
		if (!mBounds.isEmpty())
		{
			aBundle.putBundle("bounds", BundleHelper.toBundle(mBounds));
		}
		aBundle.putString("color", mColor.equals(YELLOW) ? "YELLOW" : mColor.equals(GRAY) ? "GRAY" : mColor.equals(PURPLE) ? "PURPLE" : String.format("%08x", mColor.getRGB()));
		aBundle.putString("direction", mDirection.name());
		aBundle.putNumber("ref", mModelRef);
	}
}
