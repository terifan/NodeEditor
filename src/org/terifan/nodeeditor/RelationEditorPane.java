package org.terifan.nodeeditor;

import org.terifan.ui.NullLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.terifan.ui.Utilities;
import org.terifan.util.Tuple;
import org.terifan.util.log.Log;


public class RelationEditorPane extends JPanel implements Iterable<RelationBox>
{
	private final static long serialVersionUID = 1L;

	private ArrayList<Connection> mConnections;
	private ConnectionRenderer mConnectionRenderer;
	private RelationItem mSelectedItem;
	private RelationBox mSelectedBox;
	private Connection mSelectedConnection;
	private RelationEditorPaneBackground mBackground;


	public RelationEditorPane()
	{
		mConnections = new ArrayList<>();
		mConnectionRenderer = new DefaultConnectionRenderer();

		mBackground = new RelationEditorPaneBackground_Blender();

		RelationEditorPaneMouseListener mouseListener = new RelationEditorPaneMouseListener(this);

		super.setLayout(new NullLayout());
		super.setBackground(Styles.PANE_BACKGROUND_COLOR);
		super.addMouseListener(mouseListener);
		super.addMouseMotionListener(mouseListener);
		super.addKeyListener(new RelationEditorPaneKeyListener(this));
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


	public void removeConnection(Connection aConnection)
	{
		mConnections.remove(aConnection);
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		Graphics2D g = (Graphics2D)aGraphics;

		Utilities.enableTextAntialiasing(g);
		Utilities.enableAntialiasing(g);

		mBackground.drawPaneBackground(this, g);

		for (Connection connection : mConnections)
		{
			Anchor[] anchors = findConnectionAnchors(connection);

			if (anchors != null)
			{
				mConnectionRenderer.render(g, connection, anchors[0], anchors[1], mSelectedConnection == connection);
			}
		}
	}


	public RelationBox getRelationBox(RelationItem aItem)
	{
		for (RelationBox box : this)
		{
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


	protected void setSelectedComponent(Object aComponent, boolean aUnselectItem)
	{
		RelationBox relationBox = null;
		RelationItem relationItem = null;
		Connection connection = null;

		if (aComponent instanceof RelationBox)
		{
			relationBox = (RelationBox)aComponent;

			if (!aUnselectItem && relationBox == mSelectedBox)
			{
				relationItem = mSelectedItem;
			}
		}
		else if (aComponent instanceof RelationItem)
		{
			Tuple<RelationBox, RelationItem> findRelation = findRelationBoxImpl(aComponent);
			if (findRelation != null && findRelation.getSecond() == aComponent)
			{
				relationBox = findRelation.getFirst();
				relationItem = (RelationItem)aComponent;
			}
		}
		else if (aComponent instanceof Connection)
		{
			connection = (Connection)aComponent;
		}
		else if (aComponent instanceof Component)
		{
			Tuple<RelationBox, RelationItem> findRelation = findRelationBoxImpl((Component)aComponent);

			if (findRelation != null)
			{
				relationBox = findRelation.getFirst();
				relationItem = findRelation.getSecond();
			}
		}

		for (RelationBox box : this)
		{
			if (box == mSelectedBox || box == relationBox)
			{
				box.onSelectionChanged(this, box == relationBox);
			}

			for (int j = 0; j < box.getRelationItemCount(); j++)
			{
				RelationItem item = box.getRelationItem(j);

				if (item == mSelectedItem || item == relationItem)
				{
					item.onSelectionChanged(this, box, item == relationItem);
				}
			}
		}
		
		mSelectedBox = relationBox;
		mSelectedConnection = connection;
		mSelectedItem = relationItem;

		repaint();
	}


	public RelationBox getSelectedBox()
	{
		return mSelectedBox;
	}


	public RelationItem getSelectedItem()
	{
		return mSelectedItem;
	}


	public Connection getSelectedConnection()
	{
		return mSelectedConnection;
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
		for (RelationBox box : this)
		{
			for (int j = 0; j < box.getRelationItemCount(); j++)
			{
				RelationItem item = box.getRelationItem(j);

				if (item.getIdentity().equals(aIdentity))
				{
					return item;
				}
			}
		}

		return null;
	}


	public static RelationEditorPane findEditor(Component aComponent)
	{
		return (RelationEditorPane)SwingUtilities.getAncestorOfClass(RelationEditorPane.class, aComponent);
	}


	public static RelationBox findRelationBox(Component aComponent)
	{
		return findEditor(aComponent).findRelationBoxImpl(aComponent).getFirst();
	}


	protected Tuple<RelationBox,RelationItem> findRelationBoxImpl(Object aComponentOrItem)
	{
		for (RelationBox box : this)
		{
			for (int j = 0; j < box.getRelationItemCount(); j++)
			{
				RelationItem item = box.getRelationItem(j);

				if (item == aComponentOrItem || item.getComponent() == aComponentOrItem)
				{
					return new Tuple<>(box, item);
				}
			}
		}

		return null;
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


	@Override
	public Iterator<RelationBox> iterator()
	{
		ArrayList<RelationBox> list = new ArrayList<>();

		for (int i = 0; i < getComponentCount(); i++)
		{
			Component component = getComponent(i);

			if (component instanceof RelationBox)
			{
				list.add((RelationBox)component);
			}
		}

		return list.iterator();
	}


	void fireRelationBoxClicked(RelationBox aFindRelationBox)
	{
	}
}
