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


public abstract class Property<T extends Property> implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected final ArrayList<Connector> mConnectors;
	protected final HashMap<String, String> mProperties;
	protected final Dimension mPreferredSize;
	protected final Rectangle mBounds;
	protected final TextBox mTextBox;
	protected boolean mUserSetSize;
	protected String mIdentity;
	protected Node mNode;


	protected Property(String aText)
	{
		mConnectors = new ArrayList<>();
		mProperties = new HashMap<>();
		mPreferredSize = new Dimension();
		mBounds = new Rectangle();

		mTextBox = new TextBox(aText)
			.setFont(Styles.BOX_ITEM_FONT)
			.setForeground(Styles.BOX_FOREGROUND_COLOR);

		setPreferredSize(mTextBox.measure().getSize());
	}


	protected abstract void paintComponent(NodeEditorPane aEditor, Graphics2D aGraphics, boolean aHover);


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


	public T setIdentity(String aIdentity)
	{
		mIdentity = aIdentity;
		return (T)this;
	}


	public String getProperty(String aName)
	{
		return mProperties.get(aName);
	}


	public T putProperty(String aName, String aValue)
	{
		mProperties.put(aName, aValue);
		return (T)this;
	}


	public String getText()
	{
		return mTextBox.getText();
	}


	public T setText(String aText)
	{
		mTextBox.setText(aText);
		return (T)this;
	}


	public T addConnector(Connector aConnector)
	{
		mConnectors.add(aConnector);
		return (T)this;
	}


	public T addConnector(Direction aDirection, Color aColor)
	{
		return addConnector(new Connector(aDirection, aColor));
	}


	public T addConnector(Direction aDirection)
	{
		return addConnector(new Connector(aDirection, Color.YELLOW));
	}


	public List<Connector> getConnectors()
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


	public boolean isConnected(Direction aDirection)
	{
		Connector c = getConnector(aDirection);
		return c != null && c.getConnectedItems().count() > 0;
	}


	protected Dimension measure()
	{
		if (!mUserSetSize && mTextBox.isLayoutRequired())
		{
			mPreferredSize.setSize(mTextBox.measure().getSize());
		}

		return (Dimension)mPreferredSize.clone();
	}


	public void setPreferredSize(Dimension aPreferredSize)
	{
		mUserSetSize = true;
		mPreferredSize.setSize(aPreferredSize);
	}


	public Rectangle getBounds()
	{
		return mBounds;
	}


	/**
	 * Perform the action of this item, for instance after a mouse click.
	 */
	protected void actionPerformed(NodeEditorPane aEditor, Point aClickPoint)
	{
	}


	protected void connectionsChanged(NodeEditorPane aEditor, Point aClickPoint)
	{
	}


	/**
	 * Should return true if the clicked point will perform an action. This method return false.
	 */
	protected boolean mousePressed(NodeEditorPane aEditor, Point aClickPoint)
	{
		return false;
	}


	protected void mouseReleased(NodeEditorPane aEditor, Point aClickPoint)
	{
	}


	protected void mouseDragged(NodeEditorPane aEditor, Point aClickPoint, Point aDragPoint)
	{
	}


	// ugly, remove somehow
	public void fireMouseReleased(NodeEditorPane aEditor, Point aPoint)
	{
		mouseReleased(aEditor, aPoint);
	}
}
