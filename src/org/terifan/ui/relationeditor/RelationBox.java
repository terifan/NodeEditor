package org.terifan.ui.relationeditor;

import java.awt.Component;


public interface RelationBox
{
	int getRelationItemCount();

	RelationItem getRelationItem(int aIndex);

	Anchor[] getConnectionAnchors(RelationItem aRelationItem);

	void onSelectionChanged(RelationEditorPane aRelationEditor, boolean aSelected);

	RelationItem getItemByComponent(Component aComponent);

	void startEditItem(RelationItem aItem);

	void cancelEditItem();

	void finishEditItem();
}
