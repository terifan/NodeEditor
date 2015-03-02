package org.terifan.ui.relationeditor;

import java.awt.Rectangle;
import javax.swing.JComponent;


public abstract class RelationBox extends JComponent
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
