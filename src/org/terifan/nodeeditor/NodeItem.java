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
import java.util.stream.Stream;
import org.terifan.bundle.Array;
import org.terifan.bundle.Bundlable;
import org.terifan.bundle.Bundle;
import org.terifan.bundle.BundleHelper;
import org.terifan.ui.TextBox;
import org.terifan.util.Strings;


public abstract class NodeItem implements Serializable, Bundlable
{
	private static final long serialVersionUID = 1L;

	protected final ArrayList<Connector> mConnectors;
	protected final Dimension mPreferredSize;
	protected final Rectangle mBounds;
	protected final TextBox mTextBox;
	protected HashMap<String,String> mProperties;
	protected boolean mUserSetSize;
	protected String mIdentity;
	protected Node mNode;


	protected NodeItem()
	{
		mConnectors = new ArrayList<>();
		mProperties = new HashMap<>();
		mPreferredSize = new Dimension();
		mBounds = new Rectangle();
		mTextBox = new TextBox("");
	}


	public NodeItem(String aText)
	{
		this();

		mTextBox.setText(aText);
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


	protected Dimension measure()
	{
		return (Dimension)mPreferredSize.clone();
	}


	protected void setPreferredSize(Dimension aPreferredSize)
	{
		mUserSetSize = true;
		mPreferredSize.setSize(aPreferredSize);
	}


	protected boolean isUserSetSize()
	{
		return mUserSetSize;
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


	public NodeItem addConnector(Direction aDirection)
	{
		return add(new Connector(aDirection, Color.YELLOW));
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


	@Override
	public void readExternal(Bundle aBundle)
	{
		mConnectors.clear();
		mProperties.clear();

		mUserSetSize = aBundle.getBundle("size") != null;
		mPreferredSize.setSize(BundleHelper.getDimension(aBundle.getBundle("size"), new Dimension(100, 0)));
		mBounds.setBounds(BundleHelper.getRectangle(aBundle.getBundle("bounds"), new Rectangle()));
		mIdentity = aBundle.getString("identity");
		aBundle.getBundleArray("properties").forEach(e->mProperties.put(e.getString("key"), e.getString("value")));
		aBundle.getBundleArray("connectors").forEach(e->{Connector c = new Connector(); c.readExternal(e); c.bind(this); mConnectors.add(c);});
		mTextBox.setText(aBundle.getString("text"));

		if (!mUserSetSize)
		{
			mBounds.setSize(measure());
		}
		else
		{
			mBounds.setSize(mPreferredSize);
		}
	}


	@Override
	public void writeExternal(Bundle aBundle)
	{
		aBundle.putString("type", getClass().getSimpleName().replace("NodeItem", ""));

		if (mUserSetSize)
		{
			aBundle.putBundle("size", BundleHelper.toBundle(mPreferredSize));
		}
		if (!mBounds.isEmpty())
		{
			aBundle.putBundle("bounds", BundleHelper.toBundle(mBounds));
		}
		if (mIdentity != null)
		{
			aBundle.putString("identity", mIdentity);
		}
		if (!mProperties.isEmpty())
		{
			aBundle.putArray("properties", BundleHelper.toArray(mProperties));
		}
		if (!mConnectors.isEmpty())
		{
			aBundle.putArray("connectors", Array.of(mConnectors));
		}
		aBundle.putString("text", mTextBox.getText());
	}
}
