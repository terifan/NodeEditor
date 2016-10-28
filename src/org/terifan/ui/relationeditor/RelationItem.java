package org.terifan.ui.relationeditor;

import java.awt.Component;
import java.util.UUID;


public interface RelationItem
{
	/**
	 * Return an unique identity of this item. Used in drag and drop operations.
	 */
	UUID getIdentity();

	/**
	 * Return the visual representation of this item (e.g. a JLabel). The same instance must be returned trough out the life time of this object.
	 */
	Component getComponent();

	/**
	 * Return the editor representation of this item (e.g. a JTextField).
	 */
	Component getEditorComponent();

	void onSelectionChanged(RelationEditorPane aRelationEditor, RelationBox aRelationBox, boolean aSelected);

	void updateValue(Component aEditorComponent);
}
