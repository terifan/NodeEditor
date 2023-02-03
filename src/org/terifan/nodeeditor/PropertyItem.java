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


public abstract class PropertyItem implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected final ArrayList<Connector> mConnectors;
	protected final HashMap<String, String> mProperties;
	protected final Dimension mPreferredSize;
	protected final Rectangle mBounds;
	protected final TextBox mTextBox;
	protected boolean mUserSetSize;
	protected String mIdentity;
	protected Node mOwnerNode;


	protected PropertyItem(String aText)
	{
		mConnectors = new ArrayList<>();
		mProperties = new HashMap<>();
		mPreferredSize = new Dimension();
		mBounds = new Rectangle();
		mTextBox = new TextBox(aText);

		setPreferredSize(mTextBox.measure().getSize());
		prepare();
	}


	protected abstract void paintComponent(NodeEditor aEditor, Graphics2D aGraphics, boolean aHover);


	void bind(Node aNode)
	{
		mOwnerNode = aNode;
	}


	protected void prepare()
	{
		mTextBox.setFont(Styles.BOX_ITEM_FONT).setForeground(Styles.BOX_FOREGROUND_COLOR);
	}


	public Node getOwnerNode()
	{
		return mOwnerNode;
	}


	public String getIdentity()
	{
		return mIdentity;
	}


	public PropertyItem setIdentity(String aIdentity)
	{
		mIdentity = aIdentity;
		return this;
	}


	public String getProperty(String aName)
	{
		return mProperties.get(aName);
	}


	public PropertyItem putProperty(String aName, String aValue)
	{
		mProperties.put(aName, aValue);
		return this;
	}


	protected String getText()
	{
		return mTextBox.getText();
	}


	protected PropertyItem setText(String aText)
	{
		mTextBox.setText(aText);
		return this;
	}


	protected Dimension measure()
	{
		if (!mUserSetSize && mTextBox.isLayoutRequired())
		{
			mPreferredSize.setSize(mTextBox.measure().getSize());
		}

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


	public Rectangle getBounds()
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


	public PropertyItem addConnector(Connector aConnector)
	{
		mConnectors.add(aConnector);
		return this;
	}


	public PropertyItem addConnector(Direction aDirection, Color aColor)
	{
		return addConnector(new Connector(aDirection, aColor));
	}


	public PropertyItem addConnector(Direction aDirection)
	{
		return addConnector(new Connector(aDirection, Color.YELLOW));
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


	// ugly, remove somehow
	public void fireMouseReleased(NodeEditor aEditor, Point aPoint)
	{
		mouseReleased(aEditor, aPoint);
	}
}
