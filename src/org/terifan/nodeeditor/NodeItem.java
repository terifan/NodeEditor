package org.terifan.nodeeditor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class NodeItem
{
	NodeBox mNodeBox;
	Connector[] mConnectors;

	protected String mName;
	protected Dimension mSize;


	public NodeItem(String aName, Connector... aConnectors)
	{
		this(aName, 100, 20, aConnectors);
	}


	public NodeItem(String aName, int aWidth, int aHeight, Connector... aConnectors)
	{
		mName = aName;
		mSize = new Dimension(aWidth, aHeight);
		mConnectors = aConnectors;
	}


	public String getName()
	{
		return mName;
	}


	public void setName(String aName)
	{
		mName = aName;
	}


	public Dimension getSize()
	{
		return mSize;
	}


	public void setSize(Dimension aSize)
	{
		mSize = aSize;
	}


	protected void paintComponent(Graphics2D aGraphics, Rectangle aBounds)
	{
		new TextBox(mName).setBounds(aBounds).setAnchor(Anchor.WEST).setForeground(Styles.BOX_FOREGROUND_COLOR).render(aGraphics);
	}
}
