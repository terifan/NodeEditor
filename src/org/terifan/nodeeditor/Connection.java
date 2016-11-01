package org.terifan.nodeeditor;


public class Connection
{
	protected RelationItem mOut;
	protected RelationItem mIn;


	public Connection(RelationItem aOut, RelationItem aIn)
	{
		mOut = aOut;
		mIn = aIn;
	}


	public RelationItem getOut()
	{
		return mOut;
	}


	public void setOut(RelationItem aOut)
	{
		mOut = aOut;
	}


	public RelationItem getIn()
	{
		return mIn;
	}


	public void setIn(RelationItem aIn)
	{
		mIn = aIn;
	}
}
