package org.terifan.ui.relationeditor;

import java.awt.Component;
import java.util.UUID;


public abstract interface RelationItem
{
	UUID getIdentity();

	Component getComponent();
}
