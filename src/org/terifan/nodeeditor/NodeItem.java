package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.stream.Stream;
import org.terifan.ui.TextBox;
import org.terifan.util.Strings;


public abstract class NodeItem implements Externalizable
{
	private static final long serialVersionUID = 1L;

	protected final ArrayList<Connector> mConnectors;
	protected final Dimension mPreferredSize;
	protected final Rectangle mBounds;
	protected final TextBox mTextBox;
	protected Node mNode;
	protected String mIdentity;
	protected boolean mFixedSize;
	protected OnInputChangeListener mOnInputChangeListener;


	public NodeItem(String aText)
	{
		mTextBox = new TextBox(aText);
		mConnectors = new ArrayList<>();
		mBounds = new Rectangle();
		mPreferredSize = new Dimension();
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


	/**
	 * Perform the action of this item, for instance after a mouse click.
	 */
	protected void actionPerformed(NodeEditor aEditor, Point aClickPoint)
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


//	public void fireOnChange()
//	{
//		mNode.fireOutputChange(this);
//	}


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


	protected String getIdentityOrName()
	{
		return Strings.isEmptyOrNull(mIdentity) ? getText() : mIdentity;
	}


	@Override
	public void writeExternal(ObjectOutput aOutput) throws IOException
	{
		aOutput.writeUTF(getClass().getName());
		aOutput.writeUTF(getText());
		aOutput.writeUTF(Strings.nullToEmpty(mIdentity));
		aOutput.writeInt(mBounds.x);
		aOutput.writeInt(mBounds.y);
		aOutput.writeInt(mBounds.width);
		aOutput.writeInt(mBounds.height);
		aOutput.writeInt(mPreferredSize.width);
		aOutput.writeInt(mPreferredSize.height);
		aOutput.writeBoolean(mFixedSize);
		aOutput.writeInt(mConnectors.size());
		for (Connector connector : mConnectors)
		{
			aOutput.writeObject(connector);
		}
	}


	@Override
	public void readExternal(ObjectInput aIn) throws IOException, ClassNotFoundException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
