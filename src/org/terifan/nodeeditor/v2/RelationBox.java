package org.terifan.nodeeditor.v2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import static org.terifan.nodeeditor.ResizablePanelBorder_Blender.BUTTON_WIDTH;
import static org.terifan.nodeeditor.v2.Styles.BOX_BORDER_COLOR;
import static org.terifan.nodeeditor.v2.Styles.BOX_BORDER_SELECTED_COLOR;
import static org.terifan.nodeeditor.v2.Styles.BOX_BORDER_TITLE_COLOR;
import static org.terifan.nodeeditor.v2.Styles.TITLE_HEIGHT;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import static org.terifan.nodeeditor.v2.Styles.BOX_TITLE_TEXT_SHADOW_COLOR;


public class RelationBox
{
	protected String mName;
	protected Rectangle mBounds;
	protected ArrayList<RelationItem> mItems;
	protected boolean mMinimized;


	public RelationBox(String aName)
	{
		mName = aName;
		mItems = new ArrayList<>();
		mBounds = new Rectangle(0, 30);
	}


	public void addItem(RelationItem aItem)
	{
		mItems.add(aItem);

		aItem.mRelationBox = this;
	}


	public void setLocation(int aX, int aY)
	{
		mBounds.setLocation(aX, aY);
	}


	protected void paintComponent(Graphics2D aGraphics, boolean aSelected)
	{
		if (!mMinimized)
		{
			for (RelationItem item : mItems)
			{
				item.paintComponent(aGraphics);
			}
		}
	}


	protected void layout()
	{
		mBounds.width = 0;
		mBounds.height = 0;

		if (!mMinimized)
		{
			for (RelationItem item : mItems)
			{
				Dimension size = item.getPreferredSize();

				mBounds.width = Math.max(mBounds.width, size.width);
				mBounds.height += size.height;
			}
		}
		else
		{
			mBounds.width += 100;
		}

		mBounds.width += 28;
		mBounds.height += TITLE_HEIGHT + 6 + 2 * 4;

		int y = TITLE_HEIGHT + 4 + 4;

		if (!mMinimized)
		{
			for (RelationItem item : mItems)
			{
				Rectangle bounds = item.getBounds();
				Dimension size = item.getPreferredSize();

				bounds.setSize(mBounds.width - 26, size.height);
				bounds.setLocation(14, y);

				y += bounds.height;
			}
		}

		if (!mMinimized)
		{
			for (RelationItem item : mItems)
			{
				Rectangle bounds = item.getBounds();

				for (Connector connector : item.mConnectors)
				{
					int by = bounds.y + Math.min(bounds.height, 20) / 2 - 5;

					if (connector.getDirection() == Direction.IN)
					{
						connector.getBounds().setBounds(1, by, 9, 9);
					}
					else
					{
						connector.getBounds().setBounds(mBounds.width - 10, by, 9, 9);
					}
				}
			}
		}
		else
		{
			int n0 = 0;
			int n1 = 0;

			for (RelationItem item : mItems)
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

			for (RelationItem item : mItems)
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
						connector.getBounds().setBounds(mBounds.width - 10 - 4 + pt.x, pt.y, 9, 9);
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
		double y = 4+9 + (n == 0 ? 0 : Math.min(n * 9, 26) * ((c / (double)n - 0.5)));

		return new Point((int)x, (int)y);
	}


	protected Rectangle getBounds()
	{
		return new Rectangle(mBounds);
	}


	protected void paintConnectors(Graphics2D aGraphics)
	{
		for (RelationItem relationItem : mItems)
		{
			for (Connector connector : relationItem.mConnectors)
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
		aX += 4;
		aY += 4;
		aWidth -= 8;
		aHeight -= 8;

		aGraphics.setColor(BOX_BORDER_TITLE_COLOR);
		aGraphics.fillRoundRect(aX + 1, aY + 1, aWidth - 2, aHeight - 2, 18, 18);

		int th = TITLE_HEIGHT;
		boolean minimized = mMinimized || aHeight <= 4 + 4 + th;

		if (!minimized)
		{
			aGraphics.setColor(Styles.BOX_BACKGROUND_COLOR);
			aGraphics.fillRoundRect(aX + 1, aY + 1 + 3 + th, aWidth - 2, aHeight - 2 - 3 - th, 18, 18);
//			aGraphics.fillRect(aX + 1, aY + 1 + 3 + th, aWidth - 2, Math.min(8, aHeight - (1 + 3 + th + 5)));
		}

		int inset = 6 + 4 + BUTTON_WIDTH;

		new TextBox(mName)
			.setShadow(BOX_TITLE_TEXT_SHADOW_COLOR)
			.setAnchor(Anchor.WEST)
			.setBounds(aX + inset, aY + 3, aWidth - inset - 4, th)
			.setForeground(Styles.BOX_FOREGROUND_COLOR)
			.setMaxLineCount(1)
			.render(aGraphics);

		aGraphics.setColor(aSelected ? BOX_BORDER_SELECTED_COLOR : BOX_BORDER_COLOR);
		aGraphics.drawRoundRect(aX, aY, aWidth - 1, aHeight - 1, 18, 18);

		aX += 10;
		aY += 4 + th / 2;
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
