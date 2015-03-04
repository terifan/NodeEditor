package org.terifan.ui.relationeditor;

import java.awt.Component;
import java.util.UUID;


public abstract interface RelationItem
{
	/**
	 * Return an unique identity of this item. Used in drag and drop operations.
	 */
	UUID getIdentity();

	/**
	 * Return the visual representation of this item (e.g. a JLabel). The same instance must be returned trough out the life time of this object.
	 */
	Component getComponent();
}
