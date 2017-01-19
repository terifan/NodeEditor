package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import static org.terifan.nodeeditor.Styles.*;
import org.terifan.util.Strings;


public class Node implements Iterable<NodeItem>, Renderable, Serializable
{
	private static final long serialVersionUID = 1L;

	protected NodeModel mModel;
	protected ArrayList<NodeItem> mItems;
	protected String mIdentity;
	protected String mName;
	protected String mPrototype;
	protected Rectangle mBounds;
	protected boolean mMinimized;
	protected int mVerticalSpacing;
	protected Dimension mMinSize;
	protected Dimension mMaxSize;
	protected Dimension mRestoredSize;
	protected boolean mResizableHorizontal;
	protected boolean mResizableVertical;


	public Node()
	{
	}


	public Node(String aName, NodeItem... aItems)
	{
		mName = aName;
		mBounds = new Rectangle();
		mItems = new ArrayList<>();

		mVerticalSpacing = 3;
		mMinSize = new Dimension(100, 0);
		mMaxSize = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
		mResizableHorizontal = true;

		for (NodeItem item : aItems)
		{
			add(item);
		}
	}


	void bind(NodeModel aEditor)
	{
		mModel = aEditor;
	}


	public String getIdentity()
	{
		return mIdentity;
	}


	public Node setIdentity(String aIdentity)
	{
		mIdentity = aIdentity;
		return this;
	}


	public String getName()
	{
		return mName;
	}


	public String getPrototype()
	{
		return mPrototype;
	}


	public Node setPrototype(String aPrototype)
	{
		mPrototype = aPrototype;
		return this;
	}


	public NodeModel getModel()
	{
		return mModel;
	}


	public boolean isResizableHorizontal()
	{
		return mResizableHorizontal;
	}


	public Node setResizableHorizontal(boolean aResizableHorizontal)
	{
		mResizableHorizontal = aResizableHorizontal;
		return this;
	}


	public boolean isResizableVertical()
	{
		return mResizableVertical;
	}


	public Node setResizableVertical(boolean aResizableVertical)
	{
		mResizableVertical = aResizableVertical;
		return this;
	}


	public Dimension getMinSize()
	{
		return mMinSize;
	}


	public Node setMinSize(Dimension aMinSize)
	{
		mMinSize = aMinSize;
		return this;
	}


	public Dimension getMaxSize()
	{
		return mMaxSize;
	}


	public Node setMaxSize(Dimension aMaxSize)
	{
		mMaxSize = aMaxSize;
		return this;
	}


	public boolean isMinimized()
	{
		return mMinimized;
	}


	public Node setSize(int aWidth, int aHeight)
	{
		mBounds.setSize(aWidth, aHeight);
		return this;
	}


	public Node setSize(Dimension aSize)
	{
		mBounds.setSize(aSize);
		return this;
	}


	public Node setMinimized(boolean aMinimized)
	{
		mMinimized = aMinimized;

		if (!mMinimized && mRestoredSize != null)
		{
			mBounds.setSize(mRestoredSize);
		}
		else
		{
			mRestoredSize = mBounds.getSize();
		}
		return this;
	}


	public Node add(NodeItem aItem)
	{
		mItems.add(aItem);

		aItem.bind(this);
		return this;
	}


	public Node setLocation(int aX, int aY)
	{
		mBounds.setLocation(aX, aY);
		return this;
	}


	public Stream<NodeItem> getItems()
	{
		return mItems.stream();
	}


	@Override
	public Iterator<NodeItem> iterator()
	{
		return mItems.iterator();
	}


	@Override
	public void paintComponent(NodeEditor aEditor, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected)
	{
		paintBorder(aGraphics, 0, 0, aWidth, aHeight, aSelected);

		if (!mMinimized)
		{
			for (NodeItem item : mItems)
			{
				item.paintComponent(aEditor, aGraphics, false);
			}
		}

		paintConnectors(aGraphics);
	}


	@Override
	public void layout(Graphics2D aGraphics)
	{
		computeBounds(aGraphics);
		layoutItems(aGraphics);
		layoutConnectors();
	}


	protected void computeBounds(Graphics2D aGraphics)
	{
		if (!mMinimized)
		{
			if (mBounds.width == 0)
			{
				mBounds.width = 0;
				mBounds.height = 0;

				for (NodeItem item : mItems)
				{
					Dimension size = item.getPreferredSize(aGraphics, mBounds);

					mBounds.width = Math.max(mBounds.width, Math.min(mMaxSize.width, size.width) + 5 + 9 + 5 + 9);
					mBounds.height += size.height + mVerticalSpacing;
				}

				mBounds.width = Math.max(mBounds.width, mMinSize.width);
				mBounds.height = Math.max(mBounds.height, mMinSize.height);

				mBounds.height += TITLE_HEIGHT_PADDED;

				mBounds.height += 6 + 2 * 4;
			}
			else
			{
				mBounds.width = Math.max(mBounds.width, mMinSize.width);
				mBounds.height = Math.max(mBounds.height, mMinSize.height);
			}
		}
		else
		{
			mBounds.width = mMinSize.width;
			mBounds.height = TITLE_HEIGHT;

			mBounds.height += 6 + 2 * 4;
		}
	}


	protected void layoutItems(Graphics2D aGraphics)
	{
		if (!mMinimized)
		{
			int y = TITLE_HEIGHT_PADDED + 4 + 4;

			for (NodeItem item : mItems)
			{
				Dimension size = item.getPreferredSize(aGraphics, mBounds);

				item.mBounds.setBounds(5 + 9, y, mBounds.width - (5 + 9 + 5 + 9), size.height);

				y += item.mBounds.height + mVerticalSpacing;
			}

			y += 6;

			if (y >= mBounds.height)
			{
				mMinSize.height = y;
				computeBounds(aGraphics);
				mMinSize.height = mBounds.height;
			}
		}
	}


	protected void layoutConnectors()
	{
		if (!mMinimized)
		{
			for (NodeItem item : mItems)
			{
				int by0 = item.mBounds.y + Math.min(item.mBounds.height, TITLE_HEIGHT_PADDED + 4) / 2 - 5;
				int by1 = by0;

				for (Connector connector : item.mConnectors)
				{
					if (connector.getDirection() == Direction.IN)
					{
						connector.getBounds().setBounds(1, by0, 9, 9);
						by0 += 15;
					}
					else
					{
						connector.getBounds().setBounds(mBounds.width - (1 + 9), by1, 9, 9);
						by1 += 15;
					}
				}
			}
		}
		else
		{
			int n0 = 0;
			int n1 = 0;

			for (NodeItem item : mItems)
			{
				for (Connector connector : item.mConnectors)
				{
					if (connector.getDirection() == Direction.IN)
					{
						n0++;
					}
					else
					{
						n1++;
					}
				}
			}

			int c0 = 0;
			int c1 = 0;

			for (NodeItem item : mItems)
			{
				for (Connector connector : item.mConnectors)
				{
					if (connector.getDirection() == Direction.IN)
					{
						Point pt = calcPoint(c0, n0);
						connector.getBounds().setBounds(1 + 4 - pt.x, pt.y, 9, 9);
						c0++;
					}
					else
					{
						Point pt = calcPoint(c1, n1);
						connector.getBounds().setBounds(mBounds.width - (1 + 9) - 4 + pt.x, pt.y, 9, 9);
						c1++;
					}
				}
			}
		}
	}


	protected Point calcPoint(int c, int n)
	{
		n--;
		double r = n == 0 ? 0 : 2 * Math.PI * (-0.075 * Math.min(3, n) + Math.min(3, n) * 0.15 * c / (double)n);
		double x = 5 * Math.cos(r);
		double y = 4 + 9 + (n == 0 ? 0 : Math.min(n * 9, TITLE_HEIGHT + 4) * ((c / (double)n - 0.5)));

		return new Point((int)x, (int)y);
	}


	@Override
	public Rectangle getBounds()
	{
		return mBounds;
	}


	protected void paintConnectors(Graphics2D aGraphics)
	{
		for (NodeItem item : mItems)
		{
			for (Connector connector : item.mConnectors)
			{
				Rectangle r = connector.getBounds();
				aGraphics.setColor(connector.getColor());
				aGraphics.fillOval(r.x, r.y, r.width, r.height);
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawOval(r.x, r.y, r.width, r.height);
			}
		}
	}


	protected void paintBorder(Graphics2D aGraphics, int aX, int aY, int aWidth, int aHeight, boolean aSelected)
	{
//		aGraphics.setColor(Color.YELLOW);
//		aGraphics.drawRect(aX, aY, aWidth, aHeight);

		aX += 5;
		aY += 4;
		aWidth -= 10;
		aHeight -= 8;

		boolean minimized = mMinimized || aHeight <= 4 + 4 + TITLE_HEIGHT;
		int th = minimized ? TITLE_HEIGHT : TITLE_HEIGHT_PADDED;

//		aGraphics.setColor(new Color(48, 48, 48, 128));
//		aGraphics.fillRoundRect(aX-5, aY+10, aWidth+10, aHeight-5, BORDE_RADIUS, BORDE_RADIUS);

		if (minimized)
		{
			aGraphics.setColor(BOX_BORDER_TITLE_COLOR);
			aGraphics.fillRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);
		}
		else
		{
			Shape oldClip = aGraphics.getClip();

			aGraphics.setColor(BOX_BORDER_TITLE_COLOR);
			aGraphics.clipRect(aX, aY, aWidth, th);
			aGraphics.fillRoundRect(aX, aY, aWidth, th + 3 + 12, BORDE_RADIUS, BORDE_RADIUS);

			aGraphics.setClip(oldClip);

			aGraphics.setColor(BOX_BACKGROUND_COLOR);
			aGraphics.clipRect(aX, aY + th, aWidth, aHeight - th);
			aGraphics.fillRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);

			aGraphics.setClip(oldClip);
		}

		aGraphics.setColor(BOX_BORDER_TITLE_SEPARATOR_COLOR);
		aGraphics.drawLine(aX, aY+th-1, aX+aWidth, aY+th-1);

		int inset = 6 + 4 + BUTTON_WIDTH;

		new TextBox(mName)
			.setShadow(BOX_TITLE_TEXT_SHADOW_COLOR)
			.setAnchor(Anchor.WEST)
			.setBounds(aX + inset, aY + 3, aWidth - inset - 4, TITLE_HEIGHT)
			.setForeground(BOX_FOREGROUND_COLOR)
			.setMaxLineCount(1)
			.setFont(Styles.BOX_FONT)
			.render(aGraphics);

		aGraphics.setColor(aSelected ? BOX_BORDER_SELECTED_COLOR : BOX_BORDER_COLOR);
		aGraphics.drawRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);

		aX += 10;
		aY += 3 + th / 2;
		int w = 10;
		int h = 5;

		if (mMinimized)
		{
			aGraphics.fillPolygon(new int[]{aX, aX + w, aX}, new int[]{aY - h, aY, aY + h}, 3);
		}
		else
		{
			aGraphics.fillPolygon(new int[]{aX, aX + w, aX + w / 2}, new int[]{aY - h, aY - h, aY + h}, 3);
		}
	}


	/**
	 * Return item pressed
	 */
	protected NodeItem mousePressed(Point aPoint)
	{
		for (NodeItem item : mItems)
		{
			if (item.mBounds.contains(aPoint.x - mBounds.x, aPoint.y - mBounds.y))
			{
				return item;
			}
		}

		return null;
	}


	/**
	 * Child NodeItems call this when their values are changed
	 */
//	public void fireOutputChange(NodeItem aNodeItem)
//	{
//		if (mOnInputChangeListener != null)
//		{
//			mOnInputChangeListener.onInputChange(aNodeItem, true);
//		}
//
//		for (NodeItem item : mItems)
//		{
//			mModel.getConnectionsFrom(item).forEach(c->{
//				c.getOut().getNodeItem().inputWasChanged(aNodeItem);
//				c.getOut().getNodeItem().getNode().fireInputChange(aNodeItem);
//			});
//		}
//	}


	/**
	 * Other Nodes call this when an item of theirs have changed value.
	 */
//	public void fireInputChange(NodeItem aNodeItem)
//	{
//		if (mOnInputChangeListener != null)
//		{
//			mOnInputChangeListener.onInputChange(aNodeItem, false);
//		}
//	}


	public NodeItem getItem(String aPath)
	{
		String id = aPath.contains(".") ? aPath.split("\\.")[1] : aPath;
		NodeItem item = null;

		for (NodeItem b : mItems)
		{
			if (b.getIdentity() != null && b.getIdentity().equals(id))
			{
				item = b;
				break;
			}
			else if (b instanceof AbstractNodeItem)
			{
				AbstractNodeItem ab = (AbstractNodeItem)b;

				if (ab.getText().equalsIgnoreCase(id))
				{
					if (item != null)
					{
						throw new IllegalStateException("More than one NodeItem have the same name, provide an Identity to either of them: " + ab.getText());
					}
					item = b;
				}
			}
		}

		if (item == null)
		{
			throw new IllegalArgumentException("Failed to find NodeItem, ensure text or identity is set: " + aPath);
		}

		return item;
	}


	protected String getIdentityOrName()
	{
		return Strings.isEmptyOrNull(mIdentity) ? mName : mIdentity;
	}


//	@FunctionalInterface
//	public interface OnInputChangeListener
//	{
//		void onInputChange(NodeItem aSource, boolean aSelf);
//	}


//	@Override
//	public void writeExternal(ObjectOutput aOutput) throws IOException
//	{
//		aOutput.writeInt(mBounds.x);
//		aOutput.writeInt(mBounds.y);
//		aOutput.writeInt(mBounds.width);
//		aOutput.writeInt(mBounds.height);
//		aOutput.writeUTF(Strings.nullToEmpty(mPrototype));
//		aOutput.writeUTF(Strings.nullToEmpty(mIdentity));
//		aOutput.writeUTF(mName);
//		aOutput.writeBoolean(mMinimized);
//		aOutput.writeBoolean(mResizableHorizontal);
//		aOutput.writeBoolean(mResizableVertical);
//		for (NodeItem item : mItems)
//		{
//			aOutput.writeObject(item);
//		}
//	}
//
//
//	@Override
//	public void readExternal(ObjectInput aIn) throws IOException, ClassNotFoundException
//	{
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//	}
}