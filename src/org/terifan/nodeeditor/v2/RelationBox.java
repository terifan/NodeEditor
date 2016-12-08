package org.terifan.nodeeditor.v2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
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
	private String mName;
	private Rectangle mBounds;
	private ArrayList<RelationItem> mItems;
	private boolean mMinimized;
	private double mScale;


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
//		paintBorder(aGraphics, 0, 0, (int)(mBounds.width/mScale), (int)(mBounds.height/mScale), aSelected);
		paintBorder(aGraphics, 0, 0, mBounds.width, mBounds.height, aSelected);

		paintAnchors(aGraphics);

		for (RelationItem item : mItems)
		{
			item.paintComponent(aGraphics);
		}
	}


	protected void layout(double aScale)
	{
		mScale = aScale;
		mBounds.width = 0;
		mBounds.height = 0;

		for (RelationItem item : mItems)
		{
			Dimension size = item.getPreferredSize();

			mBounds.width = Math.max(mBounds.width, size.width);
			mBounds.height += size.height;
		}
		
		mBounds.width += 20;
		mBounds.height += TITLE_HEIGHT + 6;

		int y = TITLE_HEIGHT + 4;

		for (RelationItem item : mItems)
		{
			Rectangle bounds = item.getBounds();
			Dimension size = item.getPreferredSize();

			bounds.setSize(mBounds.width - 20, size.height);
			bounds.setLocation(10, y);

			y += bounds.height;
		}

		for (RelationItem item : mItems)
		{
			Rectangle bounds = item.getBounds();

			for (Connector anchor : item.mAnchors)
			{
				if (anchor.getDirection() == Direction.IN)
				{
					anchor.getBounds().setBounds(0, bounds.y, 10, 10);
				}
				else
				{
					anchor.getBounds().setBounds(mBounds.width - 10, bounds.y, 10, 10);
				}
			}
		}
	}


	protected Rectangle getBounds()
	{
		return new Rectangle(mBounds.x, mBounds.y, (int)(mScale * mBounds.width), (int)(mScale * mBounds.height));
	}
	
	
	protected void paintAnchors(Graphics2D aGraphics)
	{
		for (RelationItem relationItem : mItems)
		{
			for (Connector anchor : relationItem.mAnchors)
			{
				Rectangle r = new Rectangle(anchor.getBounds());
				aGraphics.setColor(anchor.getColor());
				aGraphics.fillOval(r.x, r.y, r.width, r.height);
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawOval(r.x, r.y, r.width, r.height);
			}
		}
	}

	
	protected void paintBorder(Graphics2D aGraphics, int aX, int aY, int aWidth, int aHeight, boolean aSelected)
	{
		aX += 4;
		aWidth -= 8;

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

		int inset = 4 + 4 + BUTTON_WIDTH;

		new TextBox(mName)
			.setShadow(BOX_TITLE_TEXT_SHADOW_COLOR)
			.setAnchor(Anchor.WEST)
			.setBounds(aX + inset, aY + 3, aWidth - inset - 4, th)
			.setForeground(Styles.BOX_FOREGROUND_COLOR)
			.setMaxLineCount(1)
			.render(aGraphics);

		aGraphics.setColor(aSelected ? BOX_BORDER_SELECTED_COLOR : BOX_BORDER_COLOR);
		aGraphics.drawRoundRect(aX, aY, aWidth - 1, aHeight - 1, 18, 18);

		aX += 8;
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
	}
}
