package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class TextNodeItem extends NodeItem
{
	protected final TextBox mTextBox;
	protected boolean mFixedSize;
	protected OnInputChangeListener mOnInputChangeListener;


	public TextNodeItem(String aText, Connector... aConnectors)
	{
		this(aText, 0, 0, aConnectors);

		mPreferredSize.setSize(mTextBox.measure().getSize());
		mFixedSize = false;
	}


	public TextNodeItem(String aText, int aWidth, int aHeight, Connector... aConnectors)
	{
		super(aConnectors);

		mTextBox = new TextBox(aText).setFont(Styles.BOX_ITEM_FONT).setForeground(Styles.BOX_FOREGROUND_COLOR);
		mPreferredSize.setSize(aWidth, aHeight);
		mFixedSize = true;
	}


	public TextNodeItem setOnInputChange(OnInputChangeListener aOnInputChangeListener)
	{
		mOnInputChangeListener = aOnInputChangeListener;
		return this;
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


	public TextBox getTextBox()
	{
		return mTextBox;
	}


	public String getText()
	{
		return mTextBox.getText();
	}


	public void setText(String aText)
	{
		mTextBox.setText(aText);
	}


	@Override
	protected void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, boolean aHover)
	{
		mTextBox
			.setBounds(mBounds)
			.setAnchor(mConnectors.length == 0 || mConnectors[0].mDirection == Direction.IN ? Anchor.WEST : Anchor.EAST)
			.render(aGraphics);
	}


	@FunctionalInterface
	public interface OnInputChangeListener
	{
		void onInputChange(NodeItem aSource);
	}


	@Override
	protected void inputWasChanged(NodeItem aSource)
	{
		if (mOnInputChangeListener != null)
		{
			mOnInputChangeListener.onInputChange(aSource);
		}
	}
}
