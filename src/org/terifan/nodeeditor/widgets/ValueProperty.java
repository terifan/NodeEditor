package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.ui.Anchor;


public class ValueProperty extends Property<ValueProperty>
{
	private static final long serialVersionUID = 1L;

	private Object mValue;


	public ValueProperty(String aLabel)
	{
		super(aLabel);

		mTextBox.setMargins(2, 0, 2, 0);
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		mTextBox
			.setBounds(getBounds())
			.setAnchor(getConnectors().isEmpty() || getConnectors().get(0).getDirection() == Direction.IN ? Anchor.WEST : Anchor.EAST)
			.render(aGraphics);
	}


	public Object getValue()
	{
		return mValue;
	}


	public ValueProperty setValue(Object aValue)
	{
		mValue = aValue;
		return this;
	}


	@Override
	public Object execute(Context aContext)
	{
		Object value = super.execute(aContext);

		if (value != null)
		{
			return value;
		}

		return mValue;
	}
}
