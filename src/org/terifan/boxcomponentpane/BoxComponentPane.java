package org.terifan.boxcomponentpane;

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
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.terifan.nodeeditor.Connection;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.SELECTION_RECTANGLE_STROKE;
import static org.terifan.util.Assert.assertNotNull;


public class BoxComponentPane<T extends BoxComponent> extends JComponent
{
	private final static long serialVersionUID = 1L;

	protected double mScale;
	protected Point mPaneScroll;
	protected Point mDragStartLocation;
	protected Point mDragEndLocation;
	protected Rectangle mSelectionRectangle;
	protected BoxComponentModel<T> mModel;
	protected ArrayList<T> mSelectedNodes;


	public BoxComponentPane(BoxComponentModel aModel)
	{
		mSelectedNodes = new ArrayList<>();

		mScale = 1;

		mModel = aModel;

		BoxComponentMouseListener<T> mouseListener = new BoxComponentMouseListener<>(this);
		super.addMouseMotionListener(mouseListener);
		super.addMouseListener(mouseListener);
		super.addMouseWheelListener(mouseListener);
	}


	public double getScale()
	{
		return mScale;
	}


	public BoxComponentModel<T> getModel()
	{
		return mModel;
	}


	protected void drawPaneBackground(Graphics2D aGraphics)
	{
		int w = getWidth();
		int h = getHeight();

		aGraphics.setColor(Styles.PANE_BACKGROUND_COLOR);
		aGraphics.fillRect(0, 0, w, h);

		int step = Math.max((int)(24 * mScale), 1);

		aGraphics.setColor(Styles.PANE_GRID_COLOR_1);
		for (int x = mPaneScroll.x % step; x < w; x += step)
		{
			aGraphics.drawLine(x, 0, x, h);
		}
		for (int y = mPaneScroll.y % step; y < h; y += step)
		{
			aGraphics.drawLine(0, y, w, y);
		}

		aGraphics.setColor(Styles.PANE_GRID_COLOR_2);
		for (int x = mPaneScroll.x % (5 * step); x < w; x += 5 * step)
		{
			aGraphics.drawLine(x, 0, x, h);
		}
		for (int y = mPaneScroll.y % (5 * step); y < h; y += 5 * step)
		{
			aGraphics.drawLine(0, y, w, y);
		}

		int w2 = mPaneScroll.x;
		int h2 = mPaneScroll.y;
		aGraphics.setColor(Styles.PANE_GRID_COLOR_3);
		aGraphics.drawLine(0, h2, w, h2);
		aGraphics.drawLine(w2, 0, w2, h);
	}


	@Override
	public Dimension getPreferredSize()
	{
		Rectangle bounds = null;
		for (T box : mModel.getNodes())
		{
			box.layout();
			if (bounds == null)
			{
				bounds = box.getBounds();
			}
			else
			{
				bounds.add(box.getBounds());
			}
		}

		return bounds.getSize();
	}


	private void paintBox(Graphics2D aGraphics, BoxComponent<T> aComponent, boolean aSelected)
	{
		Rectangle bounds = aComponent.getBounds();
		int x = (int)(bounds.x * mScale);
		int y = (int)(bounds.y * mScale);
		int width = (int)(bounds.width * mScale);
		int height = (int)(bounds.height * mScale);

		if (aGraphics.hitClip(x, y, width, height))
		{
			boolean offscreen = false;

			Graphics2D ig;
			AffineTransform affineTransform;
			BufferedImage offscreenBuffer = null;

			if (offscreen)
			{
				offscreenBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				ig = offscreenBuffer.createGraphics();
				affineTransform = new AffineTransform();
				affineTransform.scale(mScale, mScale);
			}
			else
			{
				ig = (Graphics2D)aGraphics.create(x, y, width, height);
				affineTransform = new AffineTransform();
				affineTransform.translate(mPaneScroll.x + x, mPaneScroll.y + y);
				affineTransform.scale(mScale, mScale);
			}

			ig.setTransform(affineTransform);
			ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			ig.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

			aComponent.paintComponent(this, ig, bounds.width, bounds.height, aSelected);

			ig.dispose();

			if (offscreen)
			{
				aGraphics.drawImage(offscreenBuffer, x, y, null);
			}
		}
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		if (mPaneScroll == null)
		{
			mPaneScroll = new Point(getWidth() / 2, getHeight() / 2);
		}

		Graphics2D g = (Graphics2D)aGraphics;
		AffineTransform oldTransform = g.getTransform();

		drawPaneBackground(g);

		g.translate(mPaneScroll.x, mPaneScroll.y);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		for (T box : mModel.getNodes())
		{
			box.layout();
		}

//		if (mPopup != null)
//		{
//			mPopup.layout();
//		}

//		for (Connection connection : mModel.getConnections())
//		{
//			if (mSelectedConnection == connection)
//			{
//				SplineRenderer.drawSpline(g, connection, mScale, Styles.CONNECTOR_COLOR_OUTER_SELECTED, Styles.CONNECTOR_COLOR_INNER_SELECTED, Styles.CONNECTOR_COLOR_INNER_SELECTED);
//			}
//			else
//			{
//				assertNotNull(connection.getIn(), "connection.getIn() == null");
//				assertNotNull(connection.getOut(), "connection.getOut() == null");
//				assertNotNull(connection.getIn().getProperty(), "connection.getIn().getNodeItem() == null");
//				assertNotNull(connection.getOut().getProperty(), "connection.getOut().getNodeItem() == null");
//				assertNotNull(connection.getIn().getProperty().getNode(), "connection.getIn().getNodeItem().getNode() == null");
//				assertNotNull(connection.getOut().getProperty().getNode(), "connection.getOut().getNodeItem().getNode() == null");
//
//				Color start = mSelectedNodes.contains(connection.getOut().getProperty().getNode()) ? Styles.CONNECTOR_COLOR_INNER_FOCUSED : Styles.CONNECTOR_COLOR_INNER;
//				Color end = mSelectedNodes.contains(connection.getIn().getProperty().getNode()) ? Styles.CONNECTOR_COLOR_INNER_FOCUSED : Styles.CONNECTOR_COLOR_INNER;
//
//				SplineRenderer.drawSpline(g, connection, mScale, Styles.CONNECTOR_COLOR_OUTER, start, end);
//			}
//		}

//		if (mDragEndLocation != null)
//		{
//			if (mDragConnector.getDirection() == Direction.OUT)
//			{
//				SplineRenderer.drawSpline(g, mDragStartLocation, mDragEndLocation, mScale, Styles.CONNECTOR_COLOR_OUTER, Styles.CONNECTOR_COLOR_INNER_DRAGGED, Styles.CONNECTOR_COLOR_INNER_DRAGGED);
//			}
//			else
//			{
//				SplineRenderer.drawSpline(g, mDragEndLocation, mDragStartLocation, mScale, Styles.CONNECTOR_COLOR_OUTER, Styles.CONNECTOR_COLOR_INNER_DRAGGED, Styles.CONNECTOR_COLOR_INNER_DRAGGED);
//			}
//		}

		for (T box : mModel.getNodes())
		{
			paintBox(g, box, mSelectedNodes.contains(box));
		}

//		if (mPopup != null)
//		{
//			paintBox(g, mPopup, false);
//		}

		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

		if (mSelectionRectangle != null)
		{
			g.setColor(Styles.PANE_SELECTION_RECTANGLE_BACKGROUND);
			g.fillRect(mSelectionRectangle.x, mSelectionRectangle.y, mSelectionRectangle.width + 1, mSelectionRectangle.height + 1);
			g.setColor(Styles.PANE_SELECTION_RECTANGLE_LINE);
			g.setStroke(SELECTION_RECTANGLE_STROKE);
			g.draw(mSelectionRectangle);
		}

		g.setTransform(oldTransform);
	}
}
