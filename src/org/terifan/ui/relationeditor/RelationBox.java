package org.terifan.ui.relationeditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;


public interface RelationBox
{
	Component getComponent(int aIndex);


	int getComponentCount();


	Dimension getPreferredSize();


	Rectangle getBounds();


	void setBounds(Rectangle aBounds);


	Rectangle[] getAnchors(RelationItem aItem);


	String getTitle();
}
