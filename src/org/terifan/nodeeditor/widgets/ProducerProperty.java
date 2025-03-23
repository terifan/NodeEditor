package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.ui.Anchor;


public abstract class ProducerProperty extends Property<ProducerProperty>
{
	private static final long serialVersionUID = 1L;


	public ProducerProperty(String aLabel)
	{
		super(aLabel);
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		getTextBox()
			.setBounds(getBounds())
			.setAnchor(getConnectors().isEmpty() || getConnectors().get(0).getDirection() == Direction.IN ? Anchor.WEST : Anchor.EAST)
			.render(aGraphics);
	}


	@Override
	public abstract Object execute();


	@Override
	public String toString()
	{
		return "ValueProperty{" + super.toString() + '}';
	}
}
