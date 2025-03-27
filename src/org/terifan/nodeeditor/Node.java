package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import static org.terifan.nodeeditor.Styles.*;
import org.terifan.boxcomponentpane.BoxComponent;
import static org.terifan.nodeeditor.Property.colorToJava;


public class Node extends BoxComponent<Node, NodeEditorPane> implements Serializable
{
	private final static long serialVersionUID = 1L;

	protected final ArrayList<Property> mProperties;
	protected int mVerticalSpacing;
	protected NodeModel mModel;


	public Node(String aTitle)
	{
		super(aTitle);

		mVerticalSpacing = 3;
		mProperties = new ArrayList<>();
	}


	public Node(String aTitle, Property... aProperties)
	{
		this(aTitle);

		for (Property item : aProperties)
		{
			addProperty(item);
		}
	}


	void bind(NodeModel aModel)
	{
		mModel = aModel;
	}


	public NodeModel getModel()
	{
		return mModel;
	}


	public Node addProperty(Property aItem)
	{
		mProperties.add(aItem);
		aItem.bind(this);

		return this;
	}


	public int getPropertyCount()
	{
		return mProperties.size();
	}


	public ArrayList<Property> getProperties()
	{
		return mProperties;
	}


	public Property getProperty(int aIndex)
	{
		return mProperties.get(aIndex);
	}


	public <T extends Property> T getProperty(String aPath)
	{
		if (aPath == null)
		{
			throw new IllegalArgumentException("Path is null");
		}

		String id = aPath;
		Property item = null;

		for (Property pi : mProperties)
		{
			Property ab = (Property)pi;

			if (id.equals(ab.getId()))
			{
				if (item != null)
				{
					throw new IllegalStateException("More than one NodeItem have the same name, provide an Identity to either of them: " + ab.getText());
				}
				item = pi;
			}
		}

		return (T)item;
	}


	@Override
	public void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected)
	{
		super.paintComponent(aPane, aGraphics, aWidth, aHeight, aSelected);

		if (!mMinimized)
		{
			for (Property item : mProperties)
			{
				item.paintComponent(aPane, aGraphics, false);
			}
		}

		paintConnectors(aGraphics);
	}


	@Override
	public void layout()
	{
		computeBounds();
		layoutNode();
		layoutConnectors();
	}


	public void computeBounds()
	{
		if (mMinimized)
		{
			mBounds.width = Math.max(mRestoredSize.width, mMinimumSize.width);
			mBounds.height = MIN_HEIGHT;
		}
		else
		{
			if (mBounds.width == 0)
			{
				mBounds.width = 0;
				mBounds.height = 0;

				for (Property item : mProperties)
				{
					Dimension size = item.measure();

					mBounds.width = Math.max(mBounds.width, Math.min(mMaximumSize.width, size.width) + 5 + 9 + 5 + 9);
					mBounds.height += size.height + mVerticalSpacing;
				}

				mBounds.width = Math.max(mBounds.width, mMinimumSize.width);
				mBounds.height = Math.max(mBounds.height, mMinimumSize.height);

				mBounds.height += TITLE_HEIGHT;
				mBounds.height += 6 + 2 * 4;
			}
			else
			{
				mBounds.width = Math.max(mBounds.width, mMinimumSize.width);
				mBounds.height = Math.max(mBounds.height, mMinimumSize.height);
			}
		}
	}


	protected void layoutNode()
	{
		if (!mMinimized)
		{
			int y = TITLE_HEIGHT + 4 + 4;

			for (Property item : mProperties)
			{
				Dimension size = item.measure();

				item.getBounds().setBounds(5 + 9, y, mBounds.width - (5 + 9 + 5 + 9), size.height);

				y += item.getBounds().height + mVerticalSpacing;
			}

			y += 6;

			if (y >= mBounds.height)
			{
				mMinimumSize.height = y;

				computeBounds();

				mMinimumSize.height = mBounds.height;
			}
		}
	}


	protected void layoutConnectors()
	{
		if (!mMinimized)
		{
			for (Property item : mProperties)
			{
				int by0 = item.getBounds().y + Math.min(item.getBounds().height, TITLE_HEIGHT + 4) / 2 - 5;
				int by1 = by0;

				for (Connector connector : (ArrayList<Connector>)item.getConnectors())
				{
					if (connector.getDirection() == Direction.IN)
					{
						connector.getBounds().setBounds(1, by0, 9, 9);
						by0 += 15;
					}
					else
					{
						connector.getBounds().setBounds(mBounds.width - (1 + 9), by1, 9, 9);
						by1 += 15;
					}
				}
			}
		}
		else
		{
			int n0 = 0;
			int n1 = 0;

			for (Property item : mProperties)
			{
				for (Connector connector : (ArrayList<Connector>)item.getConnectors())
				{
					if (connector.getDirection() == Direction.IN)
					{
						n0++;
					}
					else
					{
						n1++;
					}
				}
			}

			int c0 = 0;
			int c1 = 0;

			for (Property item : mProperties)
			{
				for (Connector connector : (ArrayList<Connector>)item.getConnectors())
				{
					if (connector.getDirection() == Direction.IN)
					{
						Point pt = calcPoint(c0, n0);
						connector.getBounds().setBounds(1 + 4 - pt.x, pt.y, 9, 9);
						c0++;
					}
					else
					{
						Point pt = calcPoint(c1, n1);
						connector.getBounds().setBounds(mBounds.width - (1 + 9) - 4 + pt.x, pt.y, 9, 9);
						c1++;
					}
				}
			}
		}
	}


	private Point calcPoint(int c, int n)
	{
		n--;
		double r = n == 0 ? 0 : 2 * Math.PI * (-0.075 * Math.min(3, n) + Math.min(3, n) * 0.15 * c / (double)n);
		double x = 5 * Math.cos(r);
		double y = 4 + 9 + (n == 0 ? 0 : Math.min(n * 9, TITLE_HEIGHT + 4) * ((c / (double)n - 0.5)));

		return new Point((int)x, (int)y);
	}


	public void paintConnectors(Graphics2D aGraphics)
	{
		for (Property item : mProperties)
		{
			for (Connector connector : (ArrayList<Connector>)item.getConnectors())
			{
				Rectangle r = connector.getBounds();
				aGraphics.setColor(connector.getColor());
				aGraphics.fillOval(r.x, r.y, r.width, r.height);
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawOval(r.x, r.y, r.width, r.height);
			}
		}
	}


	public Property getPropertyAt(Point aPoint)
	{
		for (Property item : mProperties)
		{
			if (item.getBounds().contains(aPoint.x - mBounds.x, aPoint.y - mBounds.y))
			{
				return item;
			}
		}

		return null;
	}


	public ArrayList<Node> getConnectedNodes()
	{
		return mModel.getConnectedNodes(this);
	}


	@Override
	public String toString()
	{
		return "Node{" + mTitle + '}';
	}


	protected void printJava()
	{
		System.out.println("\t.addComponent(new Node(\"" + getTitle() + "\")");
		if (!getTitleForeground().equals(Styles.BOX_FOREGROUND_COLOR))
		{
			System.out.println("\t\t.setTitleForeground(" + colorToJava(getTitleForeground()) + ")");
		}
		if (!getTitleBackground().equals(Styles.BOX_BACKGROUND_COLOR))
		{
			System.out.println("\t\t.setTitleBackground(" + colorToJava(getTitleBackground()) + ")");
		}
		System.out.print("\t\t.setBounds(" + mBounds.x + ", " + mBounds.y + ", " + mBounds.width + ", " + mBounds.height + ")");
		for (Property p : getProperties())
		{
			System.out.println();
			p.printJava();
		}
		System.out.println();
		System.out.print("\t)");
		System.out.println();
	}
}
