package org.terifan.nodeeditor;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class Connection implements Externalizable
{
	private static final long serialVersionUID = 1L;

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


	@Override
	public void writeExternal(ObjectOutput aOutput) throws IOException
	{
		aOutput.writeObject(mIn);
		aOutput.writeObject(mOut);
	}


	@Override
	public void readExternal(ObjectInput aIn) throws IOException, ClassNotFoundException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
