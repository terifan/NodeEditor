package org.terifan.nodeeditor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.stream.Stream;


public abstract class NodeItem
{
	protected String mIdentity;
	protected NodeBox mNodeBox;
	protected final ArrayList<Connector> mConnectors;
	protected final Dimension mPreferredSize;
	protected final Rectangle mBounds;
	protected OnInputChangeListener mOnInputChangeListener;
	protected boolean mFixedSize;


	public NodeItem()
	{
		mConnectors = new ArrayList<>();
		mBounds = new Rectangle();
		mPreferredSize = new Dimension();
	}


	void bind(NodeBox aNodeBox)
	{
		mNodeBox = aNodeBox;
	}


	public NodeBox getNodeBox()
	{
		return mNodeBox;
	}


	public String getIdentity()
	{
		return mIdentity;
	}


	public NodeItem setIdentity(String aIdentity)
	{
		mIdentity = aIdentity;
		return this;
	}


	protected Dimension getPreferredSize(Graphics2D aGraphics, Rectangle aBounds)
	{
		return mPreferredSize;
	}


	protected void setPreferredSize(Dimension aPreferredSize)
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
	 * Should return true if the clicked point will perform an action. This method return false.
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


	public void fireOnChange()
	{
		mNodeBox.fireOutputChange(this);
	}


	public NodeItem add(Connector aConnector)
	{
		mConnectors.add(aConnector);
		return this;
	}


	public ArrayList<Connector> getConnectors()
	{
		return mConnectors;
	}


	public Stream<Connector> getConnectors(Direction aDirection)
	{
		return mConnectors.stream().filter(e->e.mDirection == aDirection);
	}


	protected long countConnections(Direction aDirection)
	{
		Connector c = getConnectors(aDirection).findFirst().orElse(null);
		return c == null ? 0 : c.getConnectedItems().count();
	}


	public NodeItem setOnInputChange(OnInputChangeListener aOnInputChangeListener)
	{
		mOnInputChangeListener = aOnInputChangeListener;
		return this;
	}


	protected void inputWasChanged(NodeItem aSource)
	{
		if (mOnInputChangeListener != null)
		{
			mOnInputChangeListener.onInputChange(aSource);
		}
	}


	@FunctionalInterface
	public interface OnInputChangeListener
	{
		void onInputChange(NodeItem aSource);
	}
}
