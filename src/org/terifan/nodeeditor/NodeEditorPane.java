package org.terifan.nodeeditor;

import java.awt.Color;
import org.terifan.nodeeditor.graphics.Popup;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import org.terifan.boxcomponentpane.BoxComponentPane;
import org.terifan.nodeeditor.graphics.SplineRenderer;
import org.terifan.nodeeditor.widgets.ButtonProperty;


public class NodeEditorPane extends BoxComponentPane<Node, NodeEditorPane>
{
	private static final long serialVersionUID = 1L;

	private transient Function<String, BufferedImage> mIconProvider;

	private transient final ArrayList<OnClickHandler> mButtonHandlers;
	private transient Property mClickedItem;
	private transient Popup mPopup;
	private transient Connection mSelectedConnection;
	private transient Connector mConnectorDragFrom;
	private transient HashMap<String, NodeFunction> mBindings;

	private boolean mConnectorSelectionAllowed;
	private boolean mRemoveInConnectionsOnDrop;


	public NodeEditorPane(NodeModel aModel)
	{
		super(aModel);

		mBindings = new HashMap<>();
		mButtonHandlers = new ArrayList<>();
		mRemoveInConnectionsOnDrop = true;

		setIconProvider(Styles::loadIcon);
	}


	public NodeEditorPane bind(String aId, NodeFunction aFunction)
	{
		if (mBindings.containsKey(aId))
		{
			throw new IllegalArgumentException("ID already bound: " + aId);
		}
		mBindings.put(aId, aFunction);
		return this;
	}


	public HashMap<String, NodeFunction> getBindings()
	{
		return mBindings;
	}


	public void invoke(String aId, Property aProperty)
	{
		mBindings.get(aId).invoke(new Context(this), aProperty);
	}


	public NodeEditorPane setIconProvider(Function<String, BufferedImage> aProvider)
	{
		mIconProvider = aProvider;
		return this;
	}


	public Function<String, BufferedImage> getIconProvider()
	{
		return mIconProvider;
	}


	@Override
	protected void setupListeners()
	{
		NodeEditorMouseListener mouseListener = new NodeEditorMouseListener(this);
		super.addMouseMotionListener(mouseListener);
		super.addMouseListener(mouseListener);
		super.addMouseWheelListener(mouseListener);
	}


	@Override
	public NodeModel getModel()
	{
		return (NodeModel)super.getModel();
	}


	public boolean isRemoveInConnectionsOnDrop()
	{
		return mRemoveInConnectionsOnDrop;
	}


	public NodeEditorPane setRemoveInConnectionsOnDrop(boolean aRemoveInConnectionsOnDrop)
	{
		mRemoveInConnectionsOnDrop = aRemoveInConnectionsOnDrop;
		return this;
	}


	public boolean isConnectorSelectionAllowed()
	{
		return mConnectorSelectionAllowed;
	}


	public NodeEditorPane setConnectorSelectionAllowed(boolean aConnectorSelectionAllowed)
	{
		mConnectorSelectionAllowed = aConnectorSelectionAllowed;
		return this;
	}


	public Popup getPopup()
	{
		return mPopup;
	}


	public NodeEditorPane setPopup(Popup aPopup)
	{
		mPopup = aPopup;
		return this;
	}


	public Property getClickedItem()
	{
		return mClickedItem;
	}


	public void setClickedItem(Property aClickedItem)
	{
		mClickedItem = aClickedItem;
	}


	public Connection getSelectedConnection()
	{
		return mSelectedConnection;
	}


	public void setSelectedConnection(Connection aSelectedConnection)
	{
		mSelectedConnection = aSelectedConnection;
	}


	public Connector getConnectorDragFrom()
	{
		return mConnectorDragFrom;
	}


	public void setConnectorDragFrom(Connector aConnectorDragFrom)
	{
		mConnectorDragFrom = aConnectorDragFrom;
	}


	public NodeEditorPane addButtonHandler(OnClickHandler aHandler)
	{
		mButtonHandlers.add(aHandler);
		return this;
	}


	public void fireButtonClicked(ButtonProperty aButton)
	{
		for (OnClickHandler handler : mButtonHandlers)
		{
			if (handler.onClick(aButton))
			{
				return;
			}
		}
	}


	public Connector findNearestConnector(Point aPoint, Node aPrioritizeNode, boolean aDropTarget)
	{
		Connector nearest = null;
		double dist = aDropTarget ? 16 : 8;

		for (Node node : (ArrayList<Node>)getModel().getComponents())
		{
			if (mConnectorDragFrom != null && mConnectorDragFrom.getProperty().getNode() == node)
			{
				continue;
			}

			Rectangle b = node.getBounds();
			int x = aPoint.x - b.x;
			int y = aPoint.y - b.y;

			for (Property item : node.getProperties())
			{
				for (Connector c : (ArrayList<Connector>)item.getConnectors())
				{
					double dx = x - c.getBounds().getCenterX();
					double dy = y - c.getBounds().getCenterY();
					double d = Math.sqrt(dx * dx + dy * dy);
					if (d < dist && (aPrioritizeNode == null || node == aPrioritizeNode || nearest == null))
					{
						nearest = c;
						dist = d;
					}
				}
			}
		}

		return nearest;
	}


//	public Connector findNearestNodeConnector(Point aPoint, Node aNode)
//	{
//		Connector nearest = null;
//		double dist = 25;
//		boolean hitBox = false;
//
//		if (mDragConnector != null && mDragConnector.getProperty().getNode() == aNode)
//		{
//			return null;
//		}
//
//		int x = aPoint.x - aNode.getBounds().x;
//		int y = aPoint.y - aNode.getBounds().y;
//
//		for (Property item : aNode.getProperties())
//		{
//			for (Connector c : (ArrayList<Connector>)item.getConnectors())
//			{
//				double dx = x - c.getBounds().getCenterX();
//				double dy = y - c.getBounds().getCenterY();
//				double d = Math.sqrt(dx * dx + dy * dy);
//				if (d < dist)
//				{
//					hitBox = aNode.getBounds().contains(aPoint);
//					nearest = c;
//					dist = d;
//				}
//			}
//		}
//
//		if (hitBox && nearest != null && dist > 8)
//		{
//			nearest = null;
//		}
//
//		return nearest;
//	}


	@Override
	protected void paintBoxComponents(Graphics2D aGraphics)
	{
		NodeModel model = (NodeModel)getModel();

		aGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		for (Connection connection : model.getConnections())
		{
			if (connection != mSelectedConnection)
			{
				ArrayList<Node> selectedBoxes = getSelectedNodes();
				boolean selected = selectedBoxes.contains(connection.getOut().getProperty().getNode()) || selectedBoxes.contains(connection.getIn().getProperty().getNode());

				Color start = selected ? Styles.CONNECTOR_COLOR_INNER_FOCUSED : connection.mOut.getColor();
				Color end = selected ? Styles.CONNECTOR_COLOR_INNER_FOCUSED : connection.mIn.getColor();

				SplineRenderer.drawSpline(aGraphics, connection, getScale(), Styles.CONNECTOR_COLOR_OUTER, start, end);
			}
		}

		super.paintBoxComponents(aGraphics);

		if (getDragEndLocation() != null)
		{
			if (mConnectorDragFrom.getDirection() == Direction.OUT)
			{
				SplineRenderer.drawSpline(aGraphics, getDragStartLocation(), getDragEndLocation(), getScale(), Styles.CONNECTOR_COLOR_OUTER, Styles.CONNECTOR_COLOR_INNER_DRAGGED, Styles.CONNECTOR_COLOR_INNER_DRAGGED);
			}
			else
			{
				SplineRenderer.drawSpline(aGraphics, getDragEndLocation(), getDragStartLocation(), getScale(), Styles.CONNECTOR_COLOR_OUTER, Styles.CONNECTOR_COLOR_INNER_DRAGGED, Styles.CONNECTOR_COLOR_INNER_DRAGGED);
			}
		}

		if (mSelectedConnection != null)
		{
			SplineRenderer.drawSpline(aGraphics, mSelectedConnection, getScale(), Styles.CONNECTOR_COLOR_OUTER_SELECTED, mSelectedConnection.mOut.getColor(), mSelectedConnection.mIn.getColor());
		}

		if (mPopup != null)
		{
			paintBoxComponent(aGraphics, mPopup, false);
		}
	}
}
