package org.terifan.nodeeditor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;


public abstract class NodeItem
{
	protected NodeBox mNodeBox;
	protected Connector[] mConnectors;
	protected final Dimension mPreferredSize;
	protected final Rectangle mBounds;
	protected boolean mContinuousLayout;


	public NodeItem(Connector... aConnectors)
	{
		mConnectors = aConnectors;
		mBounds = new Rectangle();
		mPreferredSize = new Dimension();
	}


	public Dimension getPreferredSize(Graphics2D aGraphics, Rectangle aBounds)
	{
		return mPreferredSize;
	}


	public void setPreferredSize(Dimension aPreferredSize)
	{
		mPreferredSize.setSize(aPreferredSize);
	}


	protected abstract void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, boolean aHover);


	/**
	 * Perform the action of this item, for instance after a mouse click.
	 */
	protected void actionPerformed(NodeEditorPane aEditorPane, Point aClickPoint)
	{
	}


	/**
	 * Should return true if the clicked point will perform an action.
	 */
	protected boolean mousePressed(NodeEditorPane aEditorPane, Point aClickPoint)
	{
		return false;
	}


	protected void mouseReleased(NodeEditorPane aEditorPane, Point aClickPoint)
	{
	}


	protected void mouseDragged(NodeEditorPane aEditorPane, Point aClickPoint, Point aDragPoint)
	{
	}


	public boolean isContinuousLayout()
	{
		return mContinuousLayout;
	}


	public void setContinuousLayout(boolean aContinuousLayout)
	{
		mContinuousLayout = aContinuousLayout;
	}


	protected void inputWasChanged(NodeItem aSource)
	{
	}


	public void fireOnChange()
	{
		mNodeBox.fireOutputChange(this);
	}
}
