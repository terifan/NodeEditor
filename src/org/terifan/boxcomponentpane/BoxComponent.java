package org.terifan.boxcomponentpane;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.Serializable;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.BORDE_RADIUS;
import static org.terifan.nodeeditor.Styles.BOX_BACKGROUND_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_BORDER_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_BORDER_SELECTED_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_BORDER_TITLE_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_BORDER_TITLE_SEPARATOR_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_FOREGROUND_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_TITLE_TEXT_SHADOW_COLOR;
import static org.terifan.nodeeditor.Styles.BUTTON_WIDTH;
import static org.terifan.nodeeditor.Styles.TITLE_HEIGHT;
import static org.terifan.nodeeditor.Styles.TITLE_HEIGHT_PADDED;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public abstract class BoxComponent<T extends BoxComponent> implements Serializable
{
	private final static long serialVersionUID = 1L;

	protected final Rectangle mBounds;
	protected Dimension mMinimumSize;
	protected Dimension mMaximumSize;
	protected Dimension mRestoredSize;
	protected boolean mResizableHorizontal;
	protected boolean mResizableVertical;
	protected boolean mMinimized;
	protected String mName;


	public BoxComponent(String aName)
	{
		mBounds = new Rectangle();
		mMinimumSize = new Dimension(0, 0);
		mMaximumSize = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
		mResizableHorizontal = true;
		mResizableVertical = true;

		mName = aName;
	}


	public String getName()
	{
		return mName;
	}


	public T setName(String aName)
	{
		mName = aName;
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
		mMinimumSize = aMinSize;
		return (T)this;
	}


	public Dimension getMaximumSize()
	{
		return mMaximumSize;
	}


	public T setMaxSize(Dimension aMaxSize)
	{
		mMaximumSize = aMaxSize;
		return (T)this;
	}


	public boolean isMinimized()
	{
		return mMinimized;
	}


	public void setMinimized(boolean aMinimized)
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
		aGraphics.drawLine(aX, aY + th - 1, aX + aWidth, aY + th - 1);

		int inset = 6 + 4 + BUTTON_WIDTH;

		new TextBox(mName)
			.setShadow(BOX_TITLE_TEXT_SHADOW_COLOR, 1, 1)
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


	public void paintComponent(BoxComponentPane<T> aEditor, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected)
	{
		paintBorder(aGraphics, 0, 0, aWidth, aHeight, aSelected);

		if (!mMinimized)
		{
		}
	}


//	public abstract T setLocation(int aX, int aY);
//
//
//	public abstract boolean isMinimized();
//
//
//	public abstract void setMinimized(boolean aMinimized);
}
