package org.terifan.nodeeditor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public abstract class AbstractNodeItem<T extends AbstractNodeItem> extends NodeItem
{
	private static final long serialVersionUID = 1L;


	public AbstractNodeItem(String aText)
	{
		this(aText, 0, 0);

		mPreferredSize.setSize(mTextBox.measure().getSize());
	}


	public AbstractNodeItem(String aText, int aWidth, int aHeight)
	{
		super(aText);

		mTextBox.setFont(Styles.BOX_ITEM_FONT).setForeground(Styles.BOX_FOREGROUND_COLOR);
		mPreferredSize.setSize(aWidth, aHeight);
		mFixedSize = true;
	}


	@Override
	protected T setText(String aText)
	{
		return (T)super.setText(aText);
	}


	@Override
	public T setIdentity(String aIdentity)
	{
		return (T)super.setIdentity(aIdentity);
	}


	@Override
	public T add(Connector aConnector)
	{
		return (T)super.add(aConnector);
	}


	@Override
	public T setOnInputChange(OnInputChangeListener aOnInputChangeListener)
	{
		return (T)super.setOnInputChange(aOnInputChangeListener);
	}


	@Override
	public Dimension getPreferredSize(Graphics2D aGraphics, Rectangle aBounds)
	{
		if (!mFixedSize && mTextBox.isLayoutRequired())
		{
			mPreferredSize.setSize(mTextBox.measure(aGraphics == null ? null : aGraphics.getFontRenderContext()).getSize());
		}

		return super.getPreferredSize(aGraphics, aBounds);
	}
}
