package org.terifan.ui.relationeditor;

import java.awt.Graphics2D;



public interface Connection
{
	RelationItem getFrom();

	RelationItem getTo();

	void draw(Graphics2D aGraphics, RelationEditor aRelationEditor);
}
