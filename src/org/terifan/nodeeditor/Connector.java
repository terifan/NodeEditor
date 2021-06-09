package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.terifan.bundle.Array;
import org.terifan.bundle.Bundlable;
import org.terifan.bundle.BundlableInput;
import org.terifan.bundle.BundlableOutput;
import org.terifan.bundle.Bundle;


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
	public void readExternal(BundlableInput aInput)
	{
		Bundle in = aInput.bundle();
		Array bounds = in.getArray("bounds");
		String color = in.getString("color");
		mBounds.setBounds(new Rectangle(bounds.getInt(0), bounds.getInt(1), bounds.getInt(2), bounds.getInt(3)));
		mColor = color.equals("YELLOW") ? YELLOW : color.equals("PURPLE") ? PURPLE : color.equals("GRAY") ? GRAY : new Color(Integer.parseInt(color, 16));
		mDirection = Direction.valueOf(in.getString("direction"));
		mModelRef = in.getInt("ref");
	}


	@Override
	public void writeExternal(BundlableOutput aOutput)
	{
		Bundle out = aOutput.bundle();
		if (!mBounds.isEmpty())
		{
			out.putArray("bounds", Array.of(mBounds.x, mBounds.y, mBounds.width, mBounds.height));
		}
		out.putString("color", mColor.equals(YELLOW) ? "YELLOW" : mColor.equals(GRAY) ? "GRAY" : mColor.equals(PURPLE) ? "PURPLE" : String.format("%08x", mColor.getRGB()));
		out.putString("direction", mDirection.name());
		out.putNumber("ref", mModelRef);
	}
}
