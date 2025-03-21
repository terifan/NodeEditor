package org.terifan.boxcomponentpane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.Serializable;
import org.terifan.nodeeditor.Styles;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import static org.terifan.nodeeditor.Styles.BORDE_RADIUS;
import static org.terifan.nodeeditor.Styles.BOX_BACKGROUND_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_BORDER_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_BORDER_SELECTED_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_BORDER_TITLE_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_BORDER_TITLE_SEPARATOR_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_FOREGROUND_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_TITLE_TEXT_SHADOW_COLOR;
import static org.terifan.nodeeditor.Styles.TITLE_HEIGHT;
import static org.terifan.nodeeditor.Styles.COLLAPSE_BUTTON_WIDTH;
import static org.terifan.nodeeditor.Styles.TITLE_HEIGHT_PADDED;


public abstract class BoxComponent<T extends BoxComponent, U extends BoxComponentPane> implements Serializable, Renderable<T, U>
{
	private final static long serialVersionUID = 1L;

	protected final Rectangle mBounds;
	protected final Dimension mMinimumSize;
	protected final Dimension mMaximumSize;
	protected final Insets mInsets;
	protected Dimension mRestoredSize;
	protected boolean mResizableHorizontal;
	protected boolean mResizableVertical;
	protected boolean mMinimized;
	protected String mTitle;
	protected Color mTitleColor;
	protected Color mTitleTextColor;


	public BoxComponent(String aTitle)
	{
		mBounds = new Rectangle();
		mMinimumSize = new Dimension(0, 0);
		mMaximumSize = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
		mResizableHorizontal = true;
		mResizableVertical = true;
		mInsets = new Insets(TITLE_HEIGHT_PADDED + 6 + 4, 5 + 9, 6 + 4, 5 + 9);
		mTitle = aTitle;
		mTitleColor = BOX_BORDER_TITLE_COLOR;
		mTitleTextColor = BOX_FOREGROUND_COLOR;
	}


	public Color getTitleColor()
	{
		return mTitleColor;
	}


	public Color getTitleTextColor()
	{
		return mTitleTextColor;
	}


	public BoxComponent<T, U> setTitleTextColor(Color aTitleTextColor)
	{
		mTitleTextColor = aTitleTextColor;
		return this;
	}


	public BoxComponent<T, U> setTitleColor(Color aTitleColor)
	{
		mTitleColor = aTitleColor;
		return this;
	}


	public String getTitle()
	{
		return mTitle;
	}


	public T setTitle(String aTitle)
	{
		mTitle = aTitle;
		return (T)this;
	}


	public boolean isResizableHorizontal()
	{
		return mResizableHorizontal;
	}


	public T setResizableHorizontal(boolean aResizableHorizontal)
	{
		mResizableHorizontal = aResizableHorizontal;
		return (T)this;
	}


	public boolean isResizableVertical()
	{
		return mResizableVertical;
	}


	public T setResizableVertical(boolean aResizableVertical)
	{
		mResizableVertical = aResizableVertical;
		return (T)this;
	}


	public Dimension getMinimumSize()
	{
		return mMinimumSize;
	}


	public T setMinSize(Dimension aMinSize)
	{
		mMinimumSize.setSize(aMinSize);
		return (T)this;
	}


	public Dimension getMaximumSize()
	{
		return mMaximumSize;
	}


	public T setMaxSize(Dimension aMaxSize)
	{
		mMaximumSize.setSize(aMaxSize);
		return (T)this;
	}


	public boolean isMinimized()
	{
		return mMinimized;
	}


	public T setMinimized(boolean aMinimized)
	{
		mMinimized = aMinimized;

		if (!mMinimized && mRestoredSize != null)
		{
			mBounds.setSize(mRestoredSize);
		}
		else
		{
			mRestoredSize = mBounds.getSize();
			setSize(mBounds.width, TITLE_HEIGHT + 6 + 2 * 4);
		}
		return (T)this;
	}


	public T setSize(int aWidth, int aHeight)
	{
		mBounds.setSize(aWidth, aHeight);
		return (T)this;
	}


	public T setSize(Dimension aSize)
	{
		return setSize(aSize.width, aSize.height);
	}


	public T setLocation(int aX, int aY)
	{
		mBounds.setLocation(aX, aY);
		return (T)this;
	}


	public void layout()
	{
	}


	@Override
	public Rectangle getBounds()
	{
		return mBounds;
	}


	public T setBounds(int aX, int aY, int aWidth, int aHeight)
	{
		mBounds.setBounds(aX, aY, aWidth, aHeight);
		return (T)this;
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
			aGraphics.setColor(mTitleColor);
			aGraphics.fillRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);
		}
		else
		{
			Shape oldClip = aGraphics.getClip();

			aGraphics.setColor(mTitleColor);
			aGraphics.clipRect(aX, aY, aWidth, th);
			aGraphics.fillRoundRect(aX, aY, aWidth, th + 3 + 12, BORDE_RADIUS, BORDE_RADIUS);
			aGraphics.setClip(oldClip);

			aGraphics.setColor(BOX_BACKGROUND_COLOR);
			aGraphics.clipRect(aX, aY + th, aWidth, aHeight - th);
			aGraphics.fillRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);

			aGraphics.setClip(oldClip);

			aGraphics.setColor(BOX_BORDER_TITLE_SEPARATOR_COLOR);
			aGraphics.drawLine(aX, aY + th - 1, aX + aWidth, aY + th - 1);
		}

		int inset = 6 + 4 + COLLAPSE_BUTTON_WIDTH;

		new TextBox(mTitle)
			.setShadow(BOX_TITLE_TEXT_SHADOW_COLOR, 1, 1)
			.setAnchor(Anchor.WEST)
			.setBounds(aX + inset, aY + 3, aWidth - inset - 4, TITLE_HEIGHT)
			.setForeground(mTitleTextColor)
			.setMaxLineCount(1)
			.setFont(Styles.BOX_FONT)
			.render(aGraphics);

		aGraphics.setColor(aSelected ? BOX_BORDER_SELECTED_COLOR : mTitleTextColor);
		aGraphics.drawRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);

		aX += 10;
		aY += 2 + th / 2;
		int w = 10;
		int h = 5;

		if (mMinimized)
		{
			aGraphics.fillPolygon(new int[]
			{
				aX, aX + w, aX
			}, new int[]
			{
				aY - h, aY, aY + h
			}, 3);
		}
		else
		{
			aGraphics.fillPolygon(new int[]
			{
				aX, aX + w, aX + w / 2
			}, new int[]
			{
				aY - h, aY - h, aY + h
			}, 3);
		}
	}


	@Override
	public void paintComponent(U aPane, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected)
	{
		paintBorder(aGraphics, 0, 0, aWidth, aHeight, aSelected);
	}
}
