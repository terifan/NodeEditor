package org.terifan.nodeeditor;

import java.awt.Graphics2D;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class TextNodeItem extends AbstractNodeItem<TextNodeItem>
{
	public TextNodeItem(String aText, Connector... aConnectors)
	{
		super(aText, aConnectors);
	}


	public TextNodeItem(String aText, int aWidth, int aHeight, Connector... aConnectors)
	{
		super(aText, aWidth, aHeight, aConnectors);
	}


	public TextBox getTextBox()
	{
		return mTextBox;
	}


	public String getText()
	{
		return mTextBox.getText();
	}


	public TextNodeItem setText(String aText)
	{
		mTextBox.setText(aText);
		return this;
	}


	@Override
	protected void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, boolean aHover)
	{
		mTextBox
			.setBounds(mBounds)
			.setAnchor(mConnectors.isEmpty() || mConnectors.get(0).mDirection == Direction.IN ? Anchor.WEST : Anchor.EAST)
			.render(aGraphics);
	}
}
