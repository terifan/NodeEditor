package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.ui.Anchor;


public class ValueProperty extends Property<ValueProperty>
{
	private static final long serialVersionUID = 1L;

	private String mProvide;
	private Object mValue;
	private String mProducer;


	public ValueProperty(String aLabel)
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


	public Object getValue()
	{
		return mValue;
	}


	public ValueProperty setValue(Object aValue)
	{
		mValue = aValue;
		return this;
	}


	public ValueProperty setProvides(String aProvide)
	{
		mProvide = aProvide;
		return this;
	}


	public ValueProperty setProducer(String aProducer)
	{
		mProducer = aProducer;
		return this;
	}


	@Override
	public Object execute(Context aContext)
	{
		if (mValue == null)
		{
			Connector in = getConnector(Direction.IN);

			if (in != null)
			{
				return in.getConnectedProperties().get(0).execute(aContext);
			}
			else if (mProducer != null)
			{
				return aContext.invoke(this, mProducer);
			}
			else if (mProvide != null)
			{
				return mNode.getProperty(mProvide).execute(aContext);
			}
		}

		return mValue;
	}


	@Override
	public String toString()
	{
		return "ValueProperty{" + super.toString() + '}';
	}
}
