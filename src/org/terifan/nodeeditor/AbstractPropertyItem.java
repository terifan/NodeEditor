package org.terifan.nodeeditor;

import java.awt.Color;



public abstract class AbstractPropertyItem<T extends AbstractPropertyItem> extends PropertyItem
{
	private final static long serialVersionUID = 1L;


	protected AbstractPropertyItem(String aText)
	{
		super(aText);
	}


	@Override
	protected T setText(String aText)
	{
		return (T)super.setText(aText);
	}


	@Override
	public T setIdentity(String aIdentity)
	{
		return (T)super.setIdentity(aIdentity);
	}


	@Override
	public T addConnector(Direction aDirection)
	{
		return (T)super.addConnector(aDirection);
	}


	@Override
	public T addConnector(Direction aDirection, Color aColor)
	{
		return (T)super.addConnector(aDirection, aColor);
	}


	@Override
	public T addConnector(Connector aConnector)
	{
		return (T)super.addConnector(aConnector);
	}


	@Override
	public T putProperty(String aName, String aValue)
	{
		return (T)super.putProperty(aName, aValue);
	}
}
