package org.terifan.ui.relationeditor;


public class DefaultConnection implements Connection
{
	private RelationItem mFrom;
	private RelationItem mTo;


	public DefaultConnection(RelationItem aFrom, RelationItem aTo)
	{
		mFrom = aFrom;
		mTo = aTo;
	}


	@Override
	public RelationItem getFrom()
	{
		return mFrom;
	}


	public void setFrom(RelationItem aFrom)
	{
		mFrom = aFrom;
	}


	@Override
	public RelationItem getTo()
	{
		return mTo;
	}


	public void setTo(RelationItem aTo)
	{
		mTo = aTo;
	}
}
