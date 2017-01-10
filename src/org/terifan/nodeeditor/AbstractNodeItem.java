package org.terifan.nodeeditor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.terifan.ui.TextBox;


public abstract class AbstractNodeItem<T extends AbstractNodeItem> extends NodeItem
{
	protected final TextBox mTextBox;


	public AbstractNodeItem(String aText)
	{
		this(aText, 0, 0);

		mPreferredSize.setSize(mTextBox.measure().getSize());
	}


	public AbstractNodeItem(String aText, int aWidth, int aHeight)
	{
		mTextBox = new TextBox(aText).setFont(Styles.BOX_ITEM_FONT).setForeground(Styles.BOX_FOREGROUND_COLOR);
		mPreferredSize.setSize(aWidth, aHeight);
		mFixedSize = true;
	}


	protected TextBox getTextBox()
	{
		return mTextBox;
	}


	protected String getText()
	{
		return mTextBox.getText();
	}


	protected T setText(String aText)
	{
		mTextBox.setText(aText);
		return (T)this;
	}


	@Override
	public T setIdentity(String aIdentity)
	{
		super.setIdentity(aIdentity);
		return (T)this;
	}


	@Override
	public T add(Connector aConnector)
	{
		super.add(aConnector);
		return (T)this;
	}


	@Override
	public T setOnInputChange(OnInputChangeListener aOnInputChangeListener)
	{
		super.setOnInputChange(aOnInputChangeListener);
		return (T)this;
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
