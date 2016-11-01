package org.terifan.nodeeditor;

import java.awt.Component;
import java.awt.Graphics2D;


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
	
	void drawAnchors(Graphics2D aGraphics);
}
