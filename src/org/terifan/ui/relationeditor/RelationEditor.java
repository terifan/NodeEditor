package org.terifan.ui.relationeditor;

import org.terifan.ui.NullLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.UUID;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.terifan.ui.Utilities;


public class RelationEditor extends JPanel
{
	private final static long serialVersionUID = 1L;

	private ArrayList<Connection> mConnections;
	private Object mSelectedComponent;
	private ConnectionRenderer mConnectionRenderer;


	public RelationEditor()
	{
		mConnections = new ArrayList<>();
		mConnectionRenderer = new DefaultConnectionRenderer();

		setLayout(new NullLayout());
		setBackground(new Color(68,68,68));
		addMouseListener(new RelationEditorMouseListener(this));
	}


	public void addConnection(Connection aRelationship)
	{
		mConnections.add(aRelationship);
	}


	public void addConnection(RelationItem aFrom, RelationItem aTo)
	{
		mConnections.add(new DefaultConnection(aFrom, aTo));
	}


	public ArrayList<Connection> getConnections()
	{
		return mConnections;
	}


	public ConnectionRenderer getConnectionRenderer()
	{
		return mConnectionRenderer;
	}


	public void setConnectionRenderer(ConnectionRenderer aConnectionRenderer)
	{
		mConnectionRenderer = aConnectionRenderer;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		Graphics2D g = (Graphics2D)aGraphics;

		Utilities.enableTextAntialiasing(g);
		Utilities.enableAntialiasing(g);

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		for (Connection connection : mConnections)
		{
			Anchor[] anchors = findConnectionAnchors(connection);

			if (anchors != null)
			{
				mConnectionRenderer.render(g, connection, anchors[0], anchors[1], mSelectedComponent == connection);
			}
		}
	}


	public RelationBox getRelationBox(RelationItem aItem)
	{
		for (int i = 0; i < getComponentCount(); i++)
		{
			RelationBox box = (RelationBox)getComponent(i);

			for (int j = 0; j < box.getRelationItemCount(); j++)
			{
				if (box.getRelationItem(j) == aItem)
				{
					return box;
				}
			}
		}

		return null;
	}

Color old;
	protected void setSelectedComponent(Object aComponent)
	{
		if (mSelectedComponent != null)
		{
			if (mSelectedComponent instanceof Component)
			{
				((Component)mSelectedComponent).setBackground(old);
			}
		}

		mSelectedComponent = aComponent;

		if (mSelectedComponent instanceof Component)
		{
			old = ((Component)mSelectedComponent).getBackground();
			((Component)mSelectedComponent).setBackground(Color.RED);
		}
	}


	public Object getSelectedComponent()
	{
		return mSelectedComponent;
	}


	public void arrangeBoxes()
	{
		int x = 10;
		int y = 10;

		for (int i = 0; i < getComponentCount(); i++)
		{
			Component box  = getComponent(i);

			Dimension d = box.getPreferredSize();

			d.width = Math.max(d.width, 80);

			box.setBounds(new Rectangle(x, y, d.width, d.height));

			x += d.width + 50;
			y += 20;
		}
	}


	public RelationItem findRelationItem(UUID aIdentity)
	{
		for (int i = 0; i < getComponentCount(); i++)
		{
			Component component = getComponent(i);

			if (RelationBox.class.isAssignableFrom(component.getClass()))
			{
				RelationBox box = (RelationBox)component;

				for (int j = 0; j < box.getRelationItemCount(); j++)
				{
					RelationItem item = box.getRelationItem(j);

					if (item.getIdentity().equals(aIdentity))
					{
						return item;
					}
				}
			}
		}

		return null;
	}


	protected static RelationEditor findEditor(Component aComponent)
	{
		return (RelationEditor)SwingUtilities.getAncestorOfClass(RelationEditor.class, aComponent);
	}


	protected Anchor[] findConnectionAnchors(Connection aConnection)
	{
		RelationItem from = aConnection.getFrom();
		RelationItem to = aConnection.getTo();

		RelationBox fromBox = getRelationBox(from);
		RelationBox toBox = getRelationBox(to);

		if (fromBox == null || toBox == null)
		{
			return null;
		}

		Anchor[] anchorsFrom = fromBox.getConnectionAnchors(from);
		Anchor[] anchorsTo = toBox.getConnectionAnchors(to);

		if (anchorsFrom == null || anchorsTo == null)
		{
			return null;
		}

		Anchor bestFrom = null;
		Anchor bestTo = null;
		double dist = Integer.MAX_VALUE;

		for (Anchor fromAnchor : anchorsFrom)
		{
			for (Anchor toAnchor : anchorsTo)
			{
				Rectangle fromRect = fromAnchor.getBounds();
				Rectangle toRect = toAnchor.getBounds();

				double dx = fromRect.getCenterX() - toRect.getCenterX();
				double dy = fromRect.getCenterY() - toRect.getCenterY();
				double dsqr = dx * dx + dy * dy;

				if (dsqr < dist)
				{
					dist = dsqr;
					bestFrom = fromAnchor;
					bestTo = toAnchor;
				}
			}
		}

		if (bestFrom != null)
		{
			return new Anchor[]{bestFrom, bestTo};
		}

		return null;
	}
}
