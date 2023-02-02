package org.terifan.nodeeditor;

import java.io.Serializable;


public class Connection implements Serializable
{
	private final static long serialVersionUID = 1L;

	protected Connector mOut;
	protected Connector mIn;


	public Connection()
	{
	}


	public Connection(Connector aOut, Connector aIn)
	{
		if (aOut.getDirection() != Direction.OUT)
		{
			throw new IllegalArgumentException("Expected out connector, found: " + aOut.getDirection());
		}
		if (aIn.getDirection() != Direction.IN)
		{
			throw new IllegalArgumentException("Expected in connector, found: " + aIn.getDirection());
		}

		mOut = aOut;
		mIn = aIn;
	}


	public Connector getOut()
	{
		return mOut;
	}


	public void setOut(Connector aOut)
	{
		mOut = aOut;
	}


	public Connector getIn()
	{
		return mIn;
	}


	public void setIn(Connector aIn)
	{
		mIn = aIn;
	}
}
