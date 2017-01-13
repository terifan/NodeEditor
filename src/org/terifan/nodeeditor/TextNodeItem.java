package org.terifan.nodeeditor;

import java.awt.Graphics2D;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class TextNodeItem extends AbstractNodeItem<TextNodeItem>
{
	private static final long serialVersionUID = 1L;


	public TextNodeItem(String aText)
	{
		super(aText);
	}


	public TextNodeItem(String aText, int aWidth, int aHeight)
	{
		super(aText, aWidth, aHeight);
	}


	@Override
	public TextBox getTextBox()
	{
		return super.getTextBox();
	}


	@Override
	public TextNodeItem setText(String aText)
	{
		super.setText(aText);
		return this;
	}


	@Override
	protected void paintComponent(NodeEditor aEditor, Graphics2D aGraphics, boolean aHover)
	{
		mTextBox
			.setBounds(mBounds)
			.setAnchor(mConnectors.isEmpty() || mConnectors.get(0).mDirection == Direction.IN ? Anchor.WEST : Anchor.EAST)
			.render(aGraphics);
	}
}
