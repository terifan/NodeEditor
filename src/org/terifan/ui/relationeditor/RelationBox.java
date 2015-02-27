package org.terifan.ui.relationeditor;

import java.awt.Rectangle;
import javax.swing.JComponent;


public abstract class RelationBox extends JComponent
{
	protected boolean mMinimized;
	protected String mTitle;

//	abstract ArrayList<RelationItem> getItems();

	abstract Rectangle[] getAnchors(RelationItem aItem);


	public String getTitle()
	{
		return mTitle;
	}


	public boolean isMinimized()
	{
		return mMinimized;
	}


	public void setMinimized(boolean aMinimized)
	{
		mMinimized = aMinimized;
	}
}
