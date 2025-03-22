package org.terifan.boxcomponentpane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.Serializable;
import javax.imageio.ImageIO;
import org.terifan.nodeeditor.Styles;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import static org.terifan.nodeeditor.Styles.BORDE_RADIUS;
import static org.terifan.nodeeditor.Styles.BOX_BACKGROUND_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_BORDER_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_BORDER_SELECTED_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_BORDER_TITLE_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_FOREGROUND_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_TITLE_TEXT_SHADOW_COLOR;
import static org.terifan.nodeeditor.Styles.TITLE_HEIGHT;
import static org.terifan.nodeeditor.Styles.COLLAPSE_BUTTON_WIDTH;
import static org.terifan.nodeeditor.Styles.TITLE_HEIGHT_PADDED;
import static org.terifan.nodeeditor.Styles.BOX_SHADOW;
import static org.terifan.nodeeditor.Styles.BOX_SHADOW_INSETS;
import static org.terifan.nodeeditor.Styles.BOX_SHADOW_STRENGTH;


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
		boolean minimized = isMinimizedState(aHeight);
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

		aGraphics.setColor(aSelected ? BOX_BORDER_SELECTED_COLOR : BOX_BORDER_COLOR);
		aGraphics.drawRoundRect(aX, aY, aWidth, aHeight, BORDE_RADIUS, BORDE_RADIUS);

		aX += 1 + 17;
		aY += 1 + 2 + th / 2;
		int w = 4;
		int h = 4;

		aGraphics.setColor(BOX_TITLE_TEXT_SHADOW_COLOR);
		for (int i = 0; i < 2; i++)
		{
			if (mMinimized)
			{
				aGraphics.drawLine(aX - w / 2, aY - h, aX + w / 2, aY);
				aGraphics.drawLine(aX - w / 2, aY + h, aX + w / 2, aY);
			}
			else
			{
				aGraphics.drawLine(aX - w, aY - h / 2, aX, aY + h / 2);
				aGraphics.drawLine(aX + w, aY - h / 2, aX, aY + h / 2);
			}
			aX--;
			aY--;
			aGraphics.setColor(mTitleTextColor);
		}
	}


	private boolean isMinimizedState(int aHeight)
	{
		return mMinimized || aHeight <= 4 + 4 + TITLE_HEIGHT;
	}


	protected void paintShadow(Graphics2D aGraphics, int aX, int aY, int aWidth, int aHeight)
	{
		int state = isMinimizedState(aHeight) ? 1 : 0;

		int bw = BOX_SHADOW.getWidth();
		int bh = BOX_SHADOW.getHeight();
		int r = 4;

		int dx0 = aX - BOX_SHADOW_INSETS[state].left;
		int dy0 = aY - BOX_SHADOW_INSETS[state].top;
		int dx1 = aX + r;
		int dy1 = aY + r;
		int dx2 = aX + aWidth - r;
		int dy2 = aY + aHeight - r;
		int dx3 = aX + aWidth + BOX_SHADOW_INSETS[state].right;
		int dy3 = aY + aHeight + BOX_SHADOW_INSETS[state].bottom;

		int sx0 = 0;
		int sy0 = 0;
		int sx1 = BOX_SHADOW_STRENGTH[state].left;
		int sy1 = BOX_SHADOW_STRENGTH[state].top;
		int sx2 = bw - BOX_SHADOW_STRENGTH[state].right;
		int sy2 = bh - BOX_SHADOW_STRENGTH[state].bottom;
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
