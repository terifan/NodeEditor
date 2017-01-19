package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.terifan.ui.TextBox;
import org.terifan.util.Strings;


public abstract class NodeItem implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected final ArrayList<Connector> mConnectors;
	protected final Dimension mPreferredSize;
	protected final Rectangle mBounds;
	protected final TextBox mTextBox;
	protected Node mNode;
	protected String mIdentity;
	protected boolean mFixedSize;
	protected HashMap<String,String> mProperties;


	public NodeItem(String aText)
	{
		mTextBox = new TextBox(aText);
		mConnectors = new ArrayList<>();
		mBounds = new Rectangle();
		mPreferredSize = new Dimension();
		mProperties = new HashMap<>();
	}


	void bind(Node aNode)
	{
		mNode = aNode;
	}


	public Node getNode()
	{
		return mNode;
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


	public String getProperty(String aName)
	{
		return mProperties.get(aName);
	}


	public NodeItem putProperty(String aName, String aValue)
	{
		mProperties.put(aName, aValue);
		return this;
	}


	protected TextBox getTextBox()
	{
		return mTextBox;
	}


	protected String getText()
	{
		return mTextBox.getText();
	}


	protected NodeItem setText(String aText)
	{
		mTextBox.setText(aText);
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


	protected abstract void paintComponent(NodeEditor aEditor, Graphics2D aGraphics, boolean aHover);


	protected Rectangle getBounds()
	{
		return mBounds;
	}


	/**
	 * Perform the action of this item, for instance after a mouse click.
	 */
	protected void actionPerformed(NodeEditor aEditor, Point aClickPoint)
	{
	}


	protected void connectionsChanged(NodeEditor aEditor, Point aClickPoint)
	{
	}


	/**
	 * Should return true if the clicked point will perform an action. This method return false.
	 */
	protected boolean mousePressed(NodeEditor aEditor, Point aClickPoint)
	{
		return false;
	}


	protected void mouseReleased(NodeEditor aEditor, Point aClickPoint)
	{
	}


	protected void mouseDragged(NodeEditor aEditor, Point aClickPoint, Point aDragPoint)
	{
	}


	public NodeItem add(Connector aConnector)
	{
		mConnectors.add(aConnector);
		return this;
	}


	public NodeItem addConnector(Direction aDirection, Color aColor)
	{
		return add(new Connector(aDirection, aColor));
	}


	public ArrayList<Connector> getConnectors()
	{
		return mConnectors;
	}


	public List<Connector> getConnectors(Direction aDirection)
	{
		return mConnectors.stream().filter(c -> c.getDirection() == aDirection).collect(Collectors.toList());
	}


	public Connector getConnector(Direction aDirection)
	{
		return mConnectors.stream().filter(c -> c.getDirection() == aDirection).findFirst().orElse(null);
	}


	protected boolean isConnected(Direction aDirection)
	{
		Connector c = getConnector(aDirection);
		return c != null && c.getConnectedItems().count() > 0;
	}


	protected String getIdentityOrName()
	{
		return Strings.isEmptyOrNull(mIdentity) ? getText() : mIdentity;
	}


//	public void fireOnChange()
//	{
//		mNode.fireOutputChange(this);
//	}
//
//
//	public NodeItem setOnInputChange(OnInputChangeListener aOnInputChangeListener)
//	{
//		mOnInputChangeListener = aOnInputChangeListener;
//		return this;
//	}
//
//
//	protected void inputWasChanged(NodeItem aSource)
//	{
//		if (mOnInputChangeListener != null)
//		{
//			mOnInputChangeListener.onInputChange(aSource);
//		}
//	}
//
//
//	@FunctionalInterface
//	public interface OnInputChangeListener
//	{
//		void onInputChange(NodeItem aSource);
//	}
//
//
//	@Override
//	public void writeExternal(ObjectOutput aOutput) throws IOException
//	{
//		aOutput.writeUTF(getClass().getName());
//		aOutput.writeUTF(getText());
//		aOutput.writeUTF(Strings.nullToEmpty(mIdentity));
//		aOutput.writeInt(mBounds.x);
//		aOutput.writeInt(mBounds.y);
//		aOutput.writeInt(mBounds.width);
//		aOutput.writeInt(mBounds.height);
//		aOutput.writeInt(mPreferredSize.width);
//		aOutput.writeInt(mPreferredSize.height);
//		aOutput.writeBoolean(mFixedSize);
//		aOutput.writeInt(mConnectors.size());
//		for (Connector connector : mConnectors)
//		{
//			aOutput.writeObject(connector);
//		}
//	}
//
//
//	@Override
//	public void readExternal(ObjectInput aIn) throws IOException, ClassNotFoundException
//	{
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//	}
}
