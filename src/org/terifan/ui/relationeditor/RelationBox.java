package org.terifan.ui.relationeditor;


public interface RelationBox
{
	String getTitle();

	int getRelationItemCount();

	RelationItem getRelationItem(int aIndex);

	Anchor[] getConnectionAnchors(RelationItem aRelationItem);
}
