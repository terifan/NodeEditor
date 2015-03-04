package org.terifan.ui.relationeditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;


public interface RelationBox
{
	String getTitle();


	int getRelationItemCount();


	RelationItem getRelationItem(int aIndex);


	Dimension getPreferredSize();


	Rectangle getBounds();


	void setBounds(Rectangle aBounds);


	Rectangle[] getAnchors(RelationItem aItem);
}
