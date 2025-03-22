package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.ui.Anchor;


public abstract class ProducerProperty extends Property<ProducerProperty>
{
	private static final long serialVersionUID = 1L;

	private String[] mIds;


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


	public ProducerProperty setProvides(String... aIds)
	{
		mIds = aIds;
		return this;
	}


	public abstract Object produce(Context aContext);


	@Override
	public void execute(Context aContext)
	{
		aContext.result = produce(aContext);

//		Connector in = getConnector(Direction.IN);
//
//		if (in != null)
//		{
//			for (Property p : in.getConnectedProperties())
//			{
//				p.execute(aContext);
//			}
//		}
//		else
//		{
//			HashMap<String,Object> total = new HashMap<>();
//
//			for (String id : mIds)
//			{
//				Property p = getNode().getProperty(id);
//				p.execute(aContext);
//				total.put(id, aContext.result);
//			}
//
//			aContext.result = total;
//		}
	}


	@Override
	public String toString()
	{
		return "ValueProperty{" + super.toString() + '}';
	}
}
