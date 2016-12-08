package org.terifan.nodeeditor.v2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class RelationItem
{
	protected String mName;
	protected RelationBox mRelationBox;
	protected final Rectangle mBounds;
	protected final Dimension mSize;
	protected final Connector[] mAnchors;


	public RelationItem(String aName, Connector... aAnchors)
	{
		this(aName, 100, 20, aAnchors);
	}


	public RelationItem(String aName, int aWidth, int aHeight, Connector... aAnchors)
	{
		mName = aName;
		mSize = new Dimension(aWidth, aHeight);
		mBounds = new Rectangle();
		mAnchors = aAnchors;
		
		for (Connector anchor : mAnchors)
		{
			anchor.mRelationItem = this;
		}
	}


	protected void paintComponent(Graphics2D aGraphics)
	{
//		aGraphics.setColor(Color.YELLOW);
//		aGraphics.draw(mBounds);
		new TextBox(mName).setBounds(mBounds).setAnchor(Anchor.WEST).setForeground(Styles.BOX_FOREGROUND_COLOR).render(aGraphics);
	}


	protected Rectangle getBounds()
	{
		return mBounds;
	}


	protected Dimension getPreferredSize()
	{
		return mSize;
	}
}
