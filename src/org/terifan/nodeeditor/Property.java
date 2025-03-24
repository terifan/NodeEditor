package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.terifan.nodeeditor.Styles.BOX_FOREGROUND_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_FOREGROUND_SHADOW_COLOR;
import org.terifan.ui.TextBox;


public abstract class Property<T extends Property> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final ArrayList<Connector> mConnectors;
	private final Rectangle mBounds;
	private final TextBox mTextBox;

	protected Dimension mPreferredSize;
	protected boolean mUserSetSize;
	protected Node mNode;
	protected String mId;
	protected String mModelId;


	public Property()
	{
		mConnectors = new ArrayList<>();
		mPreferredSize = new Dimension();
		mBounds = new Rectangle();
		mTextBox = new TextBox("")
			.setFont(Styles.BOX_ITEM_FONT)
			.setShadow(BOX_FOREGROUND_SHADOW_COLOR, 1, 1)
			.setForeground(BOX_FOREGROUND_COLOR);
	}


	protected Property(String aText)
	{
		this();

		mTextBox.setText(aText);
		mPreferredSize.setSize(mTextBox.measure().getSize());
	}


	protected abstract void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover);


	public Object execute(Context aContext)
	{
		return null;
	}


	public String getId()
	{
		return mId;
	}


	public T setId(String aId)
	{
		mId = aId;
		return (T)this;
	}


	void bind(Node aNode)
	{
		mNode = aNode;
	}


	public Node getNode()
	{
		return mNode;
	}


	public T bind(String aModelId)
	{
		mModelId = aModelId;
		return (T)this;
	}


	public String getModelId()
	{
		return mModelId;
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


	public TextBox getTextBox()
	{
		return mTextBox;
	}


	public T addConnector(Connector aConnector)
	{
		mConnectors.add(aConnector);
		return (T)this;
	}


	public T addConnector(Direction aDirection)
	{
		return addConnector(aDirection, Color.YELLOW);
	}


	public T addConnector(Direction aDirection, Color aColor)
	{
		return addConnector(new Connector(aDirection, aColor));
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


	public boolean isConnected(Direction aDirection)
	{
		Connector c = getConnector(aDirection);
		return c != null && !c.getConnectedProperties().isEmpty();
	}


	protected Dimension measure()
	{
		if (!mUserSetSize && mTextBox.isLayoutRequired())
		{
			mPreferredSize.setSize(mTextBox.measure().getSize());
		}

		return (Dimension)mPreferredSize.clone();
	}


	public Dimension getPreferredSize()
	{
		return mPreferredSize;
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


	protected void connectionsChanged(NodeEditorPane aPane, Point aClickPoint)
	{
	}


	/**
	 * Should return true if the clicked point will perform an action. This method return false.
	 */
	protected boolean mousePressed(NodeEditorPane aPane, Point aClickPoint)
	{
		return false;
	}


	protected void mouseReleased(NodeEditorPane aPane, Point aClickPoint)
	{
	}


	protected void mouseDragged(NodeEditorPane aPane, Point aClickPoint, Point aDragPoint)
	{
	}


	// ugly, remove somehow
	public void fireMouseReleased(NodeEditorPane aPane, Point aPoint)
	{
		mouseReleased(aPane, aPoint);
	}


	@Override
	public String toString()
	{
		return "Property{" + "mId=" + getId() + ", Node=" + mNode + "}";
	}
}
