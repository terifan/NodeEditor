package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditor;
import org.terifan.nodeeditor.Property;
import org.terifan.ui.Anchor;


public class TextPropertyItem extends Property<TextPropertyItem>
{
	private static final long serialVersionUID = 1L;


	public TextPropertyItem(String aText)
	{
		super(aText);
	}


	@Override
	protected void paintComponent(NodeEditor aEditor, Graphics2D aGraphics, boolean aHover)
	{
		mTextBox
			.setBounds(mBounds)
			.setAnchor(getConnectors().isEmpty() || getConnectors().get(0).getDirection() == Direction.IN ? Anchor.WEST : Anchor.EAST)
			.render(aGraphics);
	}
}
