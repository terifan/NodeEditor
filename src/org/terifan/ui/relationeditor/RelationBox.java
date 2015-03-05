package org.terifan.ui.relationeditor;


public interface RelationBox
{
	int getRelationItemCount();

	RelationItem getRelationItem(int aIndex);

	Anchor[] getConnectionAnchors(RelationItem aRelationItem);

	void onSelectionChanged(RelationEditor aRelationEditor, boolean aSelected);
}
