package org.terifan.ui.relationeditor;

import java.awt.Rectangle;
import javax.swing.JPanel;


public abstract class RelationBox extends JPanel
{
	protected boolean mMinimized;
	protected String mTitle;


	abstract Rectangle[] getAnchors(RelationItem aItem);


	public String getTitle()
	{
		return mTitle;
	}


	public boolean isMinimized()
	{
		return mMinimized;
	}


	public abstract void setMinimized(boolean aMinimized);
}
