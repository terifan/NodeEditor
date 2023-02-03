package org.terifan.nodeeditor;

import org.terifan.nodeeditor.graphics.Popup;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import static org.terifan.nodeeditor.Styles.SELECTION_RECTANGLE_STROKE;
import org.terifan.boxcomponentpane.BoxComponentModel;
import org.terifan.boxcomponentpane.BoxComponentPane;
import org.terifan.nodeeditor.graphics.SplineRenderer;
import org.terifan.nodeeditor.widgets.ButtonProperty;
import org.terifan.nodeeditor.widgets.ImageProperty;
import static org.terifan.util.Assert.assertNotNull;


public class NodeEditorPane extends BoxComponentPane
{
	private static final long serialVersionUID = 1L;

	private transient final ArrayList<OnClickHandler> mButtonHandlers;
	private transient final ArrayList<ImagePainter> mImagePainters;
	private transient Popup mPopup;
	private transient Property mClickedItem;

	private NodeModel mModel;
	private Connection mSelectedConnection;
	private Connector mDragConnector;
	private boolean mConnectorSelectionAllowed;
	private boolean mRemoveInConnectionsOnDrop;


	public NodeEditorPane(NodeModel aModel)
	{
		super(new BoxComponentModel());

		mButtonHandlers = new ArrayList<>();
		mImagePainters = new ArrayList<>();
		mRemoveInConnectionsOnDrop = true;
		mModel = aModel;
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


	public double getScale()
	{
		return mScale;
	}


	public NodeEditorPane setScale(double aScale)
	{
		mScale = aScale;
		return this;
	}


	/**
	 * Move all nodes to the center of the screen
	 */
	public NodeEditorPane center()
	{
		if (mModel.getNodes().isEmpty())
		{
			return this;
		}

		Rectangle bounds = new Rectangle(mModel.getNodes().get(0).getBounds());
		for (Node box : mModel.getNodes())
		{
			box.layout();
			bounds.add(box.getBounds());
		}

		int dx = -(int)bounds.getCenterX();
		int dy = -(int)bounds.getCenterY();

		for (Node box : mModel.getNodes())
		{
			box.getBounds().translate(dx, dy);
		}

		mPaneScroll = null; // will be centered when pane is repainted
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


	private Connector findNearestConnector(Point aPoint)
	{
		Connector nearest = null;
		double dist = 25;

		for (Node box : mModel.getNodes())
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


	private Connector findNearestConnector(Point aPoint, Node box)
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
}
