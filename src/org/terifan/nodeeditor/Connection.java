package org.terifan.nodeeditor;


public class Connection
{
	protected Connector mOut;
	protected Connector mIn;


	public Connection(Connector aIn, Connector aOut)
	{
		mIn = aIn;
		mOut = aOut;
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
