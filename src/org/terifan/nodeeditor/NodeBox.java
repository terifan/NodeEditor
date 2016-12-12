package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import static org.terifan.nodeeditor.Styles.*;


public class NodeBox
{
	NodeEditorPane mEditorPane;

	protected String mName;
	protected Rectangle mBounds;
	protected boolean mMinimized;
	protected ArrayList<NodeItem> mItems;
	protected HashMap<NodeItem,Rectangle> mItemBounds;


	public NodeBox(String aName, NodeItem... aItems)
	{
		mName = aName;
		mBounds = new Rectangle(0, 30);
		mItems = new ArrayList<>();
		mItemBounds = new HashMap<>();

		for (NodeItem item : aItems)
		{
			addItem(item);
		}
	}


	public boolean isMinimized()
	{
		return mMinimized;
	}


	public void setMinimized(boolean aMinimized)
	{
		mMinimized = aMinimized;
	}


	public void addItem(NodeItem aItem)
	{
		mItems.add(aItem);

		aItem.mNodeBox = this;
	}


	public void setLocation(int aX, int aY)
	{
		mBounds.setLocation(aX, aY);
	}


	protected ArrayList<NodeItem> getItems()
	{
		return mItems;
	}


	protected void paintComponent(Graphics2D aGraphics, boolean aSelected)
	{
		if (!mMinimized)
		{
			for (NodeItem item : mItems)
			{
				item.paintComponent(aGraphics, mItemBounds.get(item));
			}
		}
	}


	protected void layout()
	{
		computeBounds();
		layoutItems();
		layoutConnectors();
	}


	protected void computeBounds()
	{
		if (!mMinimized)
		{
			mBounds.width = 0;
			mBounds.height = 0;

			for (NodeItem item : mItems)
			{
				Dimension size = item.getSize();

				mBounds.width = Math.max(mBounds.width, size.width);
				mBounds.height += size.height;
			}

			mBounds.width += 5 + 9 + 5 + 9;
			mBounds.height += TITLE_HEIGHT_PADDED;
		}
		else
		{
			mBounds.width = 100;
			mBounds.height = TITLE_HEIGHT;
		}

		mBounds.height += 6 + 2 * 4;
	}


	protected void layoutItems()
	{
		if (!mMinimized)
		{
			int y = TITLE_HEIGHT_PADDED + 4 + 4;

			for (NodeItem item : mItems)
			{
				Dimension size = item.getSize();

				Rectangle bounds = new Rectangle(5 + 9, y, mBounds.width - (5 + 9 + 5 + 9), size.height);
				mItemBounds.put(item, bounds);

				y += bounds.height;
			}
		}
	}


	protected void layoutConnectors()
	{
		if (!mMinimized)
		{
			for (NodeItem item : mItems)
			{
				Rectangle bounds = mItemBounds.get(item);

				int by0 = bounds.y + Math.min(bounds.height, TITLE_HEIGHT_PADDED + 4) / 2 - 5;
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
						Point pt = x(c0, n0);
						connector.getBounds().setBounds(1 + 4 - pt.x, pt.y, 9, 9);
						c0++;
					}
					else
					{
						Point pt = x(c1, n1);
						connector.getBounds().setBounds(mBounds.width - (1 + 9) - 4 + pt.x, pt.y, 9, 9);
						c1++;
					}
				}
			}
		}
	}


	protected Point x(int c, int n)
	{
		n--;
		double r = n == 0 ? 0 : 2 * Math.PI * (-0.075 * Math.min(3, n) + Math.min(3, n) * 0.15 * c / (double)n);
		double x = 5 * Math.cos(r);
		double y = 4 + 9 + (n == 0 ? 0 : Math.min(n * 9, TITLE_HEIGHT + 4) * ((c / (double)n - 0.5)));

		return new Point((int)x, (int)y);
	}


	protected Rectangle getBounds()
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
		aX += 5;
		aY += 4;
		aWidth -= 10;
		aHeight -= 8;

		boolean minimized = mMinimized || aHeight <= 4 + 4 + TITLE_HEIGHT;
		int th = minimized ? TITLE_HEIGHT : TITLE_HEIGHT_PADDED;

		if (minimized)
		{
			aGraphics.setColor(BOX_BORDER_TITLE_COLOR);
			aGraphics.fillRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);
		}
		else
		{
			Shape c = aGraphics.getClip();

			aGraphics.setColor(BOX_BORDER_TITLE_COLOR);
			aGraphics.clipRect(aX, aY, aWidth, th);
			aGraphics.fillRoundRect(aX, aY, aWidth, th + 3 + 12, BORDE_RADIUS, BORDE_RADIUS);

			aGraphics.setClip(c);

			aGraphics.setColor(BOX_BACKGROUND_COLOR);
			aGraphics.clipRect(aX, aY + th, aWidth, aHeight - th);
			aGraphics.fillRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);

			aGraphics.setClip(c);
		}

		int inset = 6 + 4 + BUTTON_WIDTH;

		new TextBox(mName)
			.setShadow(BOX_TITLE_TEXT_SHADOW_COLOR)
			.setAnchor(Anchor.WEST)
			.setBounds(aX + inset, aY + 3, aWidth - inset - 4, TITLE_HEIGHT)
			.setForeground(BOX_FOREGROUND_COLOR)
			.setMaxLineCount(1)
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

//		aGraphics.setColor(Color.YELLOW);
//		aGraphics.drawRect(0,0,mBounds.width-1,mBounds.height-1);
	}
}
