package org.terifan.nodeeditor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.terifan.ui.TextBox;


public abstract class AbstractNodeItem<T extends AbstractNodeItem> extends NodeItem
{
	protected final TextBox mTextBox;


	public AbstractNodeItem(String aText, Connector... aConnectors)
	{
		this(aText, 0, 0, aConnectors);

		mPreferredSize.setSize(mTextBox.measure().getSize());
	}


	public AbstractNodeItem(String aText, int aWidth, int aHeight, Connector... aConnectors)
	{
		super(aConnectors);

		mTextBox = new TextBox(aText).setFont(Styles.BOX_ITEM_FONT).setForeground(Styles.BOX_FOREGROUND_COLOR);
		mPreferredSize.setSize(aWidth, aHeight);
		mFixedSize = true;
	}


	public T setOnInputChange(OnInputChangeListener aOnInputChangeListener)
	{
		mOnInputChangeListener = aOnInputChangeListener;
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


	public T addConnector(Connector aConnector)
	{
		mConnectors.add(aConnector);
		return (T)this;
	}
}
