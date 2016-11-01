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
		mConnectionRenderer = new ConnectionRenderer_Blender();

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
		mConnections.add(new Connection(aFrom, aTo));
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

			d.width = Math.max(d.width, Styles.MIN_WIDTH);

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
		RelationItem out = aConnection.getOut();
		RelationItem in = aConnection.getIn();

		RelationBox outBox = getRelationBox(out);
		RelationBox inBox = getRelationBox(in);

		if (outBox == null || inBox == null)
		{
			return null;
		}

		Anchor[] anchorsOut = outBox.getConnectionAnchors(out);
		Anchor[] anchorsIn = inBox.getConnectionAnchors(in);

		if (anchorsOut == null || anchorsIn == null)
		{
			return null;
		}

		Anchor bestOut = anchorsOut[0];
		Anchor bestIn = anchorsIn[0];

		return new Anchor[]{bestOut, bestIn};
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
