package org.terifan.nodeeditor;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;


public class Connection implements Serializable
{
	private final static long serialVersionUID = 1L;

	protected Connector mOut;
	protected Connector mIn;


	public Connection()
	{
	}


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
