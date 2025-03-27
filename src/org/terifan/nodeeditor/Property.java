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
import org.terifan.nodeeditor.Styles.DefaultConnectorColors;
import org.terifan.nodeeditor.Styles.DefaultNodeColors;
import org.terifan.ui.TextBox;


public abstract class Property<T extends Property> implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected final ArrayList<Connector> mConnectors;
	protected final Dimension mPreferredSize;
	protected final Rectangle mBounds;
	protected final TextBox mTextBox;
	protected boolean mUserSetSize;
	protected Node mNode;
	protected String mId;
	protected String mModelId;
	protected String mProducer;


	public Property()
	{
		mConnectors = new ArrayList<>();
		mPreferredSize = new Dimension();
		mBounds = new Rectangle();
		mTextBox = new TextBox("")
			.setFont(Styles.BOX_ITEM_FONT)
			.setShadow(Styles.BOX_FOREGROUND_SHADOW_COLOR, 1, 1)
			.setForeground(Styles.BOX_FOREGROUND_COLOR);
	}


	protected Property(String aText)
	{
		this();

		mTextBox.setText(aText);
		mPreferredSize.setSize(mTextBox.measure().getSize());
	}


	protected abstract void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover);


	public String getProducer()
	{
		return mProducer;
	}


	public T setProducer(String aProducer)
	{
		mProducer = aProducer;
		return (T)this;
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


	public T addConnector(Connector aConnector)
	{
		mConnectors.add(aConnector);
		return (T)this;
	}


	public T addConnector(Direction aDirection)
	{
		return addConnector(aDirection, DefaultConnectorColors.YELLOW);
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
		mUserSetSize = aPreferredSize != null;
		if (mUserSetSize)
		{
			mPreferredSize.setSize(aPreferredSize);
		}
		else
		{
			mPreferredSize.setSize(mTextBox.measure().getSize());
		}
	}


	public Rectangle getBounds()
	{
		return mBounds;
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


	/**
	 * Calls incoming connectors until a non-null value is returned. If there are no incoming connectors or all connectors return null then
	 * the Producer is invoked and it's value is returned.
	 *
	 * @return a value from an incoming connector, a produced value or null.
	 */
	public <T> T execute(Context aContext)
	{
		Connector in = getConnector(Direction.IN);

		if (in != null)
		{
			for (Property property : in.getConnectedProperties())
			{
				Object value = property.execute(aContext);
				if (value != null)
				{
					return (T)value;
				}
			}
		}
		if (mProducer != null)
		{
			return (T)aContext.getEditor().getBindings().get(mProducer).invoke(new Context(aContext.getEditor(), this));
		}

		return null;
	}


	protected void printJava()
	{
		ArrayList<Connector> connectors = getConnectors();
		for (Connector c : connectors)
		{
			System.out.print(".addConnector(" + c.getDirection() + ", " + colorToJava(c.getColor()) + ")");
		}
		if (getProducer() != null)
		{
			System.out.print(".setProducer(\"" + getProducer() + "\")");
		}
		if (getId() != null)
		{
			System.out.print(".setId(\"" + getId() + "\")");
		}
		if (getModelId() != null)
		{
			System.out.print(".bind(\"" + getModelId() + "\")");
		}
		System.out.print(")");
	}


	protected static String colorToJava(Color aColor)
	{
		if (aColor.equals(DefaultConnectorColors.GRAY))
		{
			return "DefaultConnectorColors.GRAY";
		}
		if (aColor.equals(DefaultConnectorColors.GREEN))
		{
			return "DefaultConnectorColors.GREEN";
		}
		if (aColor.equals(DefaultConnectorColors.PURPLE))
		{
			return "DefaultConnectorColors.PURPLE";
		}
		if (aColor.equals(DefaultConnectorColors.YELLOW))
		{
			return "DefaultConnectorColors.YELLOW";
		}
		if (aColor.equals(DefaultNodeColors.BLUE))
		{
			return "DefaultNodeColors.BLUE";
		}
		if (aColor.equals(DefaultNodeColors.BROWN))
		{
			return "DefaultNodeColors.BROWN";
		}
		if (aColor.equals(DefaultNodeColors.DARKCYAN))
		{
			return "DefaultNodeColors.DARKCYAN";
		}
		if (aColor.equals(DefaultNodeColors.DARKRED))
		{
			return "DefaultNodeColors.DARKRED";
		}
		if (aColor.equals(DefaultNodeColors.GRAY))
		{
			return "DefaultNodeColors.GRAY";
		}
		if (aColor.equals(DefaultNodeColors.GREEN))
		{
			return "DefaultNodeColors.GREEN";
		}
		if (aColor.equals(DefaultNodeColors.PURPLE))
		{
			return "DefaultNodeColors.PURPLE";
		}
		if (aColor.equals(DefaultNodeColors.RED))
		{
			return "DefaultNodeColors.RED";
		}
		if (aColor.equals(DefaultNodeColors.YELLOW))
		{
			return "DefaultNodeColors.YELLOW";
		}
		if (aColor.equals(Color.WHITE))
		{
			return "Color.WHITE";
		}
		if (aColor.equals(Color.BLACK))
		{
			return "Color.BLACK";
		}
		return "new Color(0x" + ("%06X".formatted(0xffffffL & aColor.getRGB())) + ")";
	}
}
