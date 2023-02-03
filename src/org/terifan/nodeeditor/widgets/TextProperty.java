package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.boxcomponentpane.BoxComponentPane;
import org.terifan.ui.Anchor;


public class TextProperty extends Property<TextProperty>
{
	private static final long serialVersionUID = 1L;


	public TextProperty(String aText)
	{
		super(aText);
	}


	@Override
	protected void paintComponent(BoxComponentPane aEditor, Graphics2D aGraphics, boolean aHover)
	{
		mTextBox
			.setBounds(mBounds)
			.setAnchor(getConnectors().isEmpty() || getConnectors().get(0).getDirection() == Direction.IN ? Anchor.WEST : Anchor.EAST)
			.render(aGraphics);
	}
}
