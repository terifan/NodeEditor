package org.terifan.nodeeditor;

import java.io.Serializable;


public class Connection implements Serializable
{
	private final static long serialVersionUID = 1L;

	protected Connector mOut;
	protected Connector mIn;


	public Connection(Connector aOut, Connector aIn)
	{
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
	public String toString()
	{
		return "Connection{" + "mOut=" + mOut + ", mIn=" + mIn + '}';
	}
}
