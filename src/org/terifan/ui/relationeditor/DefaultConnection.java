package org.terifan.ui.relationeditor;

import java.io.Serializable;


public class DefaultConnection implements Connection, Serializable
{
	private final static long serialVersionUID = 1L;
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
		this.mFrom = aFrom;
	}


	@Override
	public RelationItem getTo()
	{
		return mTo;
	}


	public void setTo(RelationItem aTo)
	{
		this.mTo = aTo;
	}
}
