package org.terifan.nodeeditor;

import java.awt.Color;
import org.terifan.nodeeditor.graphics.Popup;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.terifan.boxcomponentpane.BoxComponent;
import org.terifan.boxcomponentpane.BoxComponentPane;
import org.terifan.nodeeditor.graphics.SplineRenderer;
import org.terifan.nodeeditor.widgets.ButtonProperty;
import org.terifan.nodeeditor.widgets.ImageProperty;
import static org.terifan.util.Assert.assertNotNull;


public class NodeEditorPane extends BoxComponentPane<Node>
{
	private static final long serialVersionUID = 1L;

	private transient final ArrayList<OnClickHandler> mButtonHandlers;
	private transient final ArrayList<ImagePainter> mImagePainters;
	private transient Property mClickedItem;
	private transient Popup mPopup;
	private Connection mSelectedConnection;
	private Connector mDragConnector;
	private boolean mConnectorSelectionAllowed;
	private boolean mRemoveInConnectionsOnDrop;


	public NodeEditorPane(NodeModel aModel)
	{
		super(aModel);

		mButtonHandlers = new ArrayList<>();
		mImagePainters = new ArrayList<>();
		mRemoveInConnectionsOnDrop = true;
	}


	@Override
	protected void setupListeners()
	{
		NodeEditorMouseListener mouseListener = new NodeEditorMouseListener(this);
		super.addMouseMotionListener(mouseListener);
		super.addMouseListener(mouseListener);
		super.addMouseWheelListener(mouseListener);
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


	public Connector getDragConnector()
	{
		return mDragConnector;
	}


	public void setDragConnector(Connector aDragConnector)
	{
		mDragConnector = aDragConnector;
	}


	public NodeEditorPane addImagePainter(ImagePainter aImagePainter)
	{
		mImagePainters.add(aImagePainter);
		return this;
	}


	public void paintImage(ImageProperty aProperty, Graphics aGraphics, Rectangle aBounds)
	{
		for (ImagePainter rl : mImagePainters)
		{
			try
			{
				if (rl.paintImage(this, aProperty, aGraphics, aBounds))
				{
					return;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace(System.out);
			}
		}

		try
		{
			FALLBACK_PAINTER.paintImage(this, aProperty, aGraphics, aBounds);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
	}


	private static ImagePainter FALLBACK_PAINTER = (aEditor, aProperty, aGraphics, aBounds) ->
	{
		if (aProperty.getImagePath() != null)
		{
			File file = new File(aProperty.getImagePath());
			if (file.exists())
			{
				BufferedImage image = ImageIO.read(file);
				if (image != null)
				{
					aGraphics.drawImage(image, aBounds.x, aBounds.y, aBounds.width, aBounds.height, null);
					return true;
				}
			}
		}
		return false;
	};


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


	public Connector findNearestConnector(Point aPoint)
	{
		Connector nearest = null;
		double dist = 25;

		for (Node box : mModel.getComponents())
		{
			if (mDragConnector != null && mDragConnector.getProperty().getNode() == box)
			{
				continue;
			}

			Rectangle b = box.getBounds();
			int x = aPoint.x - b.x;
			int y = aPoint.y - b.y;

			for (Property item : box.getProperties())
			{
				for (Connector c : (ArrayList<Connector>)item.mConnectors)
				{
					double dx = x - c.getBounds().getCenterX();
					double dy = y - c.getBounds().getCenterY();
					double d = Math.sqrt(dx * dx + dy * dy);
					if (d < dist)
					{
						nearest = c;
						dist = d;
					}
				}
			}
		}

		if (nearest != null && dist > 16)
		{
			nearest = null;
		}

		return nearest;
	}


	public Connector findNearestConnector(Point aPoint, Node box)
	{
		Connector nearest = null;
		double dist = 25;
		boolean hitBox = false;

		if (mDragConnector != null && mDragConnector.getProperty().getNode() == box)
		{
			return null;
		}

		int x = aPoint.x - box.getBounds().x;
		int y = aPoint.y - box.getBounds().y;

		for (Property item : box.getProperties())
		{
			for (Connector c : (ArrayList<Connector>)item.getConnectors())
			{
				double dx = x - c.getBounds().getCenterX();
				double dy = y - c.getBounds().getCenterY();
				double d = Math.sqrt(dx * dx + dy * dy);
				if (d < dist)
				{
					hitBox = box.getBounds().contains(aPoint);
					nearest = c;
					dist = d;
				}
			}
		}

		if (hitBox && nearest != null && dist > 8)
		{
			nearest = null;
		}

		return nearest;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		super.paintComponent(aGraphics);
	}


	@Override
	protected void paintBoxComponents(Graphics2D aGraphics)
	{
		NodeModel model = (NodeModel)mModel;

		aGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		for (Connection connection : model.getConnections())
		{
			if (mSelectedConnection == connection)
			{
				SplineRenderer.drawSpline(aGraphics, connection, mScale, Styles.CONNECTOR_COLOR_OUTER_SELECTED, Styles.CONNECTOR_COLOR_INNER_SELECTED, Styles.CONNECTOR_COLOR_INNER_SELECTED);
			}
			else
			{
				assertNotNull(connection.getIn(), "connection.getIn() == null");
				assertNotNull(connection.getOut(), "connection.getOut() == null");
				assertNotNull(connection.getIn().getProperty(), "connection.getIn().getNodeItem() == null");
				assertNotNull(connection.getOut().getProperty(), "connection.getOut().getNodeItem() == null");
				assertNotNull(connection.getIn().getProperty().getNode(), "connection.getIn().getNodeItem().getNode() == null");
				assertNotNull(connection.getOut().getProperty().getNode(), "connection.getOut().getNodeItem().getNode() == null");

				Color start = mSelectedBoxes.contains(connection.getOut().getProperty().getNode()) ? Styles.CONNECTOR_COLOR_INNER_FOCUSED : Styles.CONNECTOR_COLOR_INNER;
				Color end = mSelectedBoxes.contains(connection.getIn().getProperty().getNode()) ? Styles.CONNECTOR_COLOR_INNER_FOCUSED : Styles.CONNECTOR_COLOR_INNER;

				SplineRenderer.drawSpline(aGraphics, connection, mScale, Styles.CONNECTOR_COLOR_OUTER, start, end);
			}
		}

		if (mDragEndLocation != null)
		{
			if (mDragConnector.getDirection() == Direction.OUT)
			{
				SplineRenderer.drawSpline(aGraphics, mDragStartLocation, mDragEndLocation, mScale, Styles.CONNECTOR_COLOR_OUTER, Styles.CONNECTOR_COLOR_INNER_DRAGGED, Styles.CONNECTOR_COLOR_INNER_DRAGGED);
			}
			else
			{
				SplineRenderer.drawSpline(aGraphics, mDragEndLocation, mDragStartLocation, mScale, Styles.CONNECTOR_COLOR_OUTER, Styles.CONNECTOR_COLOR_INNER_DRAGGED, Styles.CONNECTOR_COLOR_INNER_DRAGGED);
			}
		}

		super.paintBoxComponents(aGraphics);

		if (mPopup != null)
		{
			paintBoxComponent(aGraphics, mPopup, false);
		}
	}
}
