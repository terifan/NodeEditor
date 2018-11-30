package org.terifan.nodeeditor;

import java.io.IOException;
import java.io.Serializable;
import org.terifan.bundle.Bundlable;
import org.terifan.bundle.Bundle;


public class Connection implements Serializable, Bundlable
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


	@Override
	public void readExternal(Bundle aBundle)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


	@Override
	public void writeExternal(Bundle aBundle)
	{
		aBundle.putNumber("in", mIn.getModelRef());
		aBundle.putNumber("out", mOut.getModelRef());
	}
}
