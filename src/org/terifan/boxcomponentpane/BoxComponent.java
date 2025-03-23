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
import static org.terifan.nodeeditor.Styles.BOX_FOREGROUND_COLOR;
import static org.terifan.nodeeditor.Styles.TITLE_HEIGHT;
import static org.terifan.nodeeditor.Styles.COLLAPSE_BUTTON_WIDTH;
import static org.terifan.nodeeditor.Styles.TITLE_HEIGHT_PADDED;
import static org.terifan.nodeeditor.Styles.BOX_SHADOW;
import static org.terifan.nodeeditor.Styles.BOX_SHADOW_SIZE;
import static org.terifan.nodeeditor.Styles.BOX_SHADOW_STRENGTH;
import static org.terifan.nodeeditor.Styles.BOX_TITLE_TEXT_SHADOW_COLOR;
import org.terifan.nodeeditor.graphics.Arrow;


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
	protected Color mTitleBackground;
	protected Color mTitleForeground;


	public BoxComponent(String aTitle)
	{
		mTitle = aTitle;
		mResizableVertical = true;
		mResizableHorizontal = true;
		mBounds = new Rectangle();
		mMinimumSize = new Dimension(0, 0);
		mMaximumSize = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
		mInsets = new Insets(TITLE_HEIGHT_PADDED + 6 + 4, 5 + 9, 6 + 4, 5 + 9);
		mTitleBackground = BOX_BORDER_TITLE_COLOR;
		mTitleForeground = BOX_FOREGROUND_COLOR;
	}


	public Color getTitleBackground()
	{
		return mTitleBackground;
	}


	public Color getTitleForeground()
	{
		return mTitleForeground;
	}


	public BoxComponent<T, U> setTitleForeground(Color aColor)
	{
		mTitleForeground = aColor;
		return this;
	}


	public BoxComponent<T, U> setTitleBackground(Color aColor)
	{
		mTitleBackground = aColor;
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
		boolean minimized = isMinimizedState(aHeight);
		int th = minimized ? TITLE_HEIGHT : TITLE_HEIGHT_PADDED;

		if (minimized)
		{
			aGraphics.setColor(mTitleBackground);
			aGraphics.fillRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);
		}
		else
		{
			Shape oldClip = aGraphics.getClip();

			aGraphics.setColor(mTitleBackground);
			aGraphics.clipRect(aX, aY, aWidth, th);
			aGraphics.fillRoundRect(aX, aY, aWidth, th + 3 + 12, BORDE_RADIUS, BORDE_RADIUS);
			aGraphics.setClip(oldClip);

			aGraphics.setColor(BOX_BACKGROUND_COLOR);
			aGraphics.clipRect(aX, aY + th, aWidth, aHeight - th);
			aGraphics.fillRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);

			aGraphics.setClip(oldClip);
		}

		int inset = 6 + 4 + COLLAPSE_BUTTON_WIDTH;

		new TextBox(mTitle)
			.setShadow(BOX_TITLE_TEXT_SHADOW_COLOR, 1, 1)
			.setAnchor(Anchor.WEST)
			.setBounds(aX + inset, aY + 3, aWidth - inset - 4, TITLE_HEIGHT)
			.setForeground(mTitleForeground)
			.setMaxLineCount(1)
			.setFont(Styles.BOX_FONT)
			.render(aGraphics);

		if (aSelected)
		{
			aGraphics.setColor(aSelected ? BOX_BORDER_SELECTED_COLOR : BOX_BORDER_COLOR);
			aGraphics.drawRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);
		}

		Arrow.paintArrow(aGraphics, mMinimized ? 1 : 2, aX + 17, aY + 2 + th / 2, 4, 4, BOX_TITLE_TEXT_SHADOW_COLOR, mTitleForeground);
	}


	private boolean isMinimizedState(int aHeight)
	{
		return mMinimized || aHeight <= 4 + 4 + TITLE_HEIGHT;
	}


	protected void paintShadow(Graphics2D aGraphics, int aX, int aY, int aWidth, int aHeight)
	{
		boolean state = isMinimizedState(aHeight);

		int bw = BOX_SHADOW.getWidth();
		int bh = BOX_SHADOW.getHeight();
		int r = 4;

		int dx0 = aX - BOX_SHADOW_SIZE;
		int dy0 = aY - (state ? BOX_SHADOW_SIZE : 0);
		int dx1 = aX + r;
		int dy1 = aY + r + (state ? 0 : BOX_SHADOW_SIZE);
		int dx2 = aX + aWidth - r;
		int dy2 = aY + aHeight - r;
		int dx3 = aX + aWidth + BOX_SHADOW_SIZE;
		int dy3 = aY + aHeight + BOX_SHADOW_SIZE;

		int sx0 = 0;
		int sy0 = 0;
		int sx1 = BOX_SHADOW_STRENGTH;
		int sy1 = BOX_SHADOW_STRENGTH;
		int sx2 = bw - BOX_SHADOW_STRENGTH;
		int sy2 = bh - BOX_SHADOW_STRENGTH;
		int sx3 = bw;
		int sy3 = bh;

		aGraphics.drawImage(BOX_SHADOW, dx0, dy0, dx1, dy1, sx0, sy0, sx1, sy1, null);
		aGraphics.drawImage(BOX_SHADOW, dx1, dy0, dx2, dy1, sx1, sy0, sx2, sy1, null);
		aGraphics.drawImage(BOX_SHADOW, dx2, dy0, dx3, dy1, sx2, sy0, sx3, sy1, null);
		aGraphics.drawImage(BOX_SHADOW, dx0, dy1, dx1, dy2, sx0, sy1, sx1, sy2, null);
		aGraphics.drawImage(BOX_SHADOW, dx2, dy1, dx3, dy2, sx2, sy1, sx3, sy2, null);
		aGraphics.drawImage(BOX_SHADOW, dx0, dy2, dx1, dy3, sx0, sy2, sx1, sy3, null);
		aGraphics.drawImage(BOX_SHADOW, dx1, dy2, dx2, dy3, sx1, sy2, sx2, sy3, null);
		aGraphics.drawImage(BOX_SHADOW, dx2, dy2, dx3, dy3, sx2, sy2, sx3, sy3, null);
	}


	@Override
	public void paintComponent(U aPane, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected)
	{
		int x = 5;
		int y = 5;
		int w = aWidth - 10;
		int h = aHeight - 10;

		paintShadow(aGraphics, x, y, w, h);
		paintBorder(aGraphics, x, y, w, h, aSelected);
	}
}
