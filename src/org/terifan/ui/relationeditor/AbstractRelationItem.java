package org.terifan.ui.relationeditor;

import java.util.UUID;


public abstract class AbstractRelationItem implements RelationItem
{
	private final UUID mIdentity;


	public AbstractRelationItem()
	{
		mIdentity = UUID.randomUUID();
	}


	@Override
	public final UUID getIdentity()
	{
		return mIdentity;
	}
}
