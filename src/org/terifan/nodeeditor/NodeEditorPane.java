package org.terifan.nodeeditor;

import java.awt.BasicStroke;
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
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;


public class NodeEditorPane extends JComponent
{
	private static final long serialVersionUID = 1L;

	private static final BasicStroke SELECTION_RECTANGLE_STROKE = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);

	private ArrayList<Connection> mConnections;
	private ArrayList<NodeBox> mNodes;
	private ArrayList<NodeBox> mSelectedNodes;
	private Connection mSelectedConnection;
	private Connector mDragConnector;
	private Point mDragStartLocation;
	private Point mDragEndLocation;
	private Rectangle mSelectionRectangle;
	private Point mPaneScroll;
	private boolean mConnectorSelectionAllowed;
	private double mScale;


	public NodeEditorPane()
	{
		mNodes = new ArrayList<>();
		mConnections = new ArrayList<>();
		mSelectedNodes = new ArrayList<>();
		mScale = 1;

		super.addMouseMotionListener(mMouseListener);
		super.addMouseListener(mMouseListener);
		super.addMouseWheelListener(mMouseListener);
	}


	public boolean isConnectorSelectionAllowed()
	{
		return mConnectorSelectionAllowed;
	}


	public void setConnectorSelectionAllowed(boolean aConnectorSelectionAllowed)
	{
		mConnectorSelectionAllowed = aConnectorSelectionAllowed;
	}


	public void add(NodeBox aNode)
	{
		mNodes.add(aNode);

		aNode.mEditorPane = this;

		for (NodeItem item : aNode.mItems)
		{
			for (Connector connector : item.mConnectors)
			{
				connector.mItem = item;
			}
		}
	}


	public void addConnection(NodeItem aFromItem, NodeItem aToItem)
	{
		Connector out = null;
		Connector in = null;

		for (Connector connector : aFromItem.mConnectors)
		{
			if (connector.getDirection() == Direction.OUT)
			{
				out = connector;
			}
		}
		for (Connector connector : aToItem.mConnectors)
		{
			if (connector.getDirection() == Direction.IN)
			{
				in = connector;
			}
		}

		addConnection(out, in);
	}


	public void addConnection(Connector aConnectorOut, Connector aConnectorIn)
	{
		if (aConnectorIn.getDirection() == Direction.OUT && aConnectorOut.getDirection() == Direction.IN)
		{
			Connector tmp = aConnectorIn;
			aConnectorIn = aConnectorOut;
			aConnectorOut = tmp;
		}

		mConnections.add(new Connection(aConnectorOut, aConnectorIn));
	}


	/**
	 * Move all nodes to the center of the screen
	 */
	public void center()
	{
		Rectangle bounds = new Rectangle(mNodes.get(0).getBounds());
		for (NodeBox box : mNodes)
		{
			box.layout();
			bounds.add(box.getBounds());
		}

		int dx = -(int)bounds.getCenterX();
		int dy = -(int)bounds.getCenterY();

		for (NodeBox box : mNodes)
		{
			box.getBounds().translate(dx, dy);
		}

		mPaneScroll = null; // will be centered when pane is repainted
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

		for (NodeBox box : mNodes)
		{
			box.layout();
		}

		for (Connection connection : mConnections)
		{
			SplineRenderer.drawSpline(g, connection, mScale, mSelectedConnection == connection);
		}

		if (mDragEndLocation != null)
		{
			if (mDragConnector.getDirection() == Direction.IN)
			{
				SplineRenderer.drawSpline(g, mDragStartLocation, mDragEndLocation, mScale, false);
			}
			else
			{
				SplineRenderer.drawSpline(g, mDragEndLocation, mDragStartLocation, mScale, false);
			}
		}

		for (NodeBox box : mNodes)
		{
			Rectangle bounds = box.getBounds();
			int x = (int)(bounds.x * mScale);
			int y = (int)(bounds.y * mScale);
			int width = (int)(bounds.width * mScale);
			int height = (int)(bounds.height * mScale);

			if (g.hitClip(x, y, width, height))
			{
				boolean selected = mSelectedNodes.contains(box);

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
					ig = (Graphics2D)g.create(x, y, width, height);
					affineTransform = new AffineTransform();
					affineTransform.translate(mPaneScroll.x + x, mPaneScroll.y + y);
					affineTransform.scale(mScale, mScale);
				}

				ig.setTransform(affineTransform);
				ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				ig.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

				box.paintBorder(ig, 0, 0, bounds.width, bounds.height, selected);
				box.paintComponent(ig, selected);
				box.paintConnectors(ig);

				ig.dispose();

				if (offscreen)
				{
					g.drawImage(offscreenBuffer, x, y, null);
				}
			}
		}

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


	@Override
	public Dimension getPreferredSize()
	{
		Rectangle bounds = new Rectangle(mNodes.get(0).getBounds());
		for (NodeBox box : mNodes)
		{
			box.layout();
			bounds.add(box.getBounds());
		}

		return bounds.getSize();
	}


	protected void drawPaneBackground(Graphics2D aGraphics)
	{
		int w = getWidth();
		int h = getHeight();

		aGraphics.setColor(Styles.PANE_BACKGROUND_COLOR);
		aGraphics.fillRect(0, 0, w, h);

		int step = (int)(24 * mScale);

		aGraphics.setColor(Styles.PANE_GRID_COLOR_1);
		for (int x = mPaneScroll.x % step; x < w; x+=step)
		{
			aGraphics.drawLine(x, 0, x, h);
		}
		for (int y = mPaneScroll.y % step; y < h; y+=step)
		{
			aGraphics.drawLine(0, y, w, y);
		}

		aGraphics.setColor(Styles.PANE_GRID_COLOR_2);
		for (int x = mPaneScroll.x % (5*step); x < w; x+=5*step)
		{
			aGraphics.drawLine(x, 0, x, h);
		}
		for (int y = mPaneScroll.y % (5*step); y < h; y+=5*step)
		{
			aGraphics.drawLine(0, y, w, y);
		}

		int w2 = mPaneScroll.x;
		int h2 = mPaneScroll.y;
		aGraphics.setColor(Styles.PANE_GRID_COLOR_3);
		aGraphics.drawLine(0, h2, w, h2);
		aGraphics.drawLine(w2, 0, w2, h);
	}


	private MouseAdapter mMouseListener = new MouseAdapter()
	{
		private Point mClickPoint;
		private Point mDragPoint;
		private boolean mHitBox;


		@Override
		public void mousePressed(MouseEvent aEvent)
		{
			mDragPoint = aEvent.getPoint();
			mClickPoint = calcMousePoint(aEvent);

			for (NodeBox box : mNodes)
			{
				Rectangle b = box.getBounds();
				if (b.contains(mClickPoint) && new Rectangle(b.x + 11, b.y + 7, 14, 16).contains(mClickPoint))
				{
					box.setMinimized(!box.isMinimized());
					updateSelections(aEvent);
					return;
				}
			}

			mDragConnector = findNearestConnector(mClickPoint);

			if (mDragConnector != null)
			{
				mDragStartLocation = mDragConnector.getConnectorPoint();
			}
			else if (SwingUtilities.isLeftMouseButton(aEvent))
			{
				updateSelections(aEvent);

				if (!mHitBox && mDragConnector == null)
				{
					mSelectionRectangle = new Rectangle(mClickPoint);
				}
			}
		}


		@Override
		public void mouseReleased(MouseEvent aEvent)
		{
			mClickPoint = calcMousePoint(aEvent);

			if (mDragConnector != null)
			{
				Connector nearestConnector = findNearestConnector(mClickPoint);

				if (nearestConnector != null)
				{
					addConnection(mDragConnector, nearestConnector);
				}

				mDragConnector = null;
				mDragStartLocation = null;
				mDragEndLocation = null;
			}
			if (mSelectionRectangle != null)
			{
				if (!aEvent.isControlDown())
				{
					mSelectedNodes.clear();
				}
				for (NodeBox box : mNodes)
				{
					if (mSelectionRectangle.intersects(box.getBounds()))
					{
						if (!mSelectedNodes.contains(box))
						{
							mSelectedNodes.add(box);
						}
						else if (aEvent.isControlDown())
						{
							mSelectedNodes.remove(box);
						}
					}
				}
				mSelectionRectangle = null;
			}

			repaint();
		}


		@Override
		public void mouseDragged(MouseEvent aEvent)
		{
			Point newPoint = calcMousePoint(aEvent);

			if (mSelectionRectangle != null)
			{
				int x0 = (int)(Math.min(mClickPoint.x, newPoint.x) * mScale);
				int y0 = (int)(Math.min(mClickPoint.y, newPoint.y) * mScale);
				int x1 = (int)(Math.max(mClickPoint.x, newPoint.x) * mScale);
				int y1 = (int)(Math.max(mClickPoint.y, newPoint.y) * mScale);

				mSelectionRectangle.setBounds(x0, y0, x1-x0, y1-y0);
			}
			else
			{
				Point oldPoint = mClickPoint;
				mClickPoint = newPoint;

				if (SwingUtilities.isMiddleMouseButton(aEvent))
				{
					mPaneScroll.x += (aEvent.getX() - mDragPoint.x);
					mPaneScroll.y += (aEvent.getY() - mDragPoint.y);
					mDragPoint = aEvent.getPoint();
				}
				else if (mDragConnector != null)
				{
					mDragEndLocation = mClickPoint;

					Connector connector = findNearestConnector(mDragEndLocation);
					if (connector != null)
					{
						mDragEndLocation = connector.getConnectorPoint();
					}
				}
				else if (mHitBox || SwingUtilities.isRightMouseButton(aEvent))
				{
					for (NodeBox box : mSelectedNodes)
					{
						Point pt = box.getBounds().getLocation();
						pt.x += mClickPoint.x - oldPoint.x;
						pt.y += mClickPoint.y - oldPoint.y;
						box.setLocation(pt.x, pt.y);
					}
				}
			}

			repaint();
		}


		@Override
		public void mouseWheelMoved(MouseWheelEvent aEvent)
		{
			mPaneScroll.x -= aEvent.getX();
			mPaneScroll.y -= aEvent.getY();

			double d = 1.1;
			if (aEvent.getWheelRotation() == 1)
			{
				mScale *= d;
				mPaneScroll.x *= d;
				mPaneScroll.y *= d;
			}
			else
			{
				mScale /= d;
				mPaneScroll.x /= d;
				mPaneScroll.y /= d;
			}

			mPaneScroll.x += aEvent.getX();
			mPaneScroll.y += aEvent.getY();

			repaint();
		}


		private Connector findNearestConnector(Point aPoint)
		{
			Connector nearest = null;
			double dist = 25;
			boolean hitBox = false;

			for (NodeBox box : mNodes)
			{
				if (mDragConnector != null && mDragConnector.mItem.mNodeBox == box)
				{
					continue;
				}

				int x = aPoint.x - box.getBounds().x;
				int y = aPoint.y - box.getBounds().y;

				for (NodeItem item : box.getItems())
				{
					for (Connector c : item.mConnectors)
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
			}

			if (hitBox && nearest != null && dist > 8)
			{
				nearest = null;
			}

			return nearest;
		}


		private void updateSelections(MouseEvent aEvent)
		{
			NodeBox newSelection = null;
			NodeBox clickedBox = null;

			for (NodeBox box : mNodes)
			{
				if (box.getBounds().contains(mClickPoint))
				{
					Rectangle shrunkBounds = new Rectangle(box.getBounds());
					shrunkBounds.grow(-5, -4);

					if (shrunkBounds.contains(mClickPoint))
					{
						clickedBox = box;

						boolean b = mSelectedNodes.contains(box);
						if (aEvent.isControlDown())
						{
							if (b)
							{
								mSelectedNodes.remove(box);
							}
							else
							{
								newSelection = box;
							}
						}
						else if (!b)
						{
							mSelectedNodes.clear();
							newSelection = box;
						}
					}
				}
			}

			mHitBox = clickedBox != null;

			if (newSelection != null)
			{
				mSelectedNodes.add(newSelection);
			}

			if (mHitBox)
			{
				mNodes.remove(clickedBox);
				mNodes.add(clickedBox);
				mSelectedConnection = null;
			}
			else if (mConnectorSelectionAllowed)
			{
				double dist = 50;
				Connection nearest = null;
				for (Connection c : mConnections)
				{
					double d = SplineRenderer.distance(c, mClickPoint);
					if (d < dist)
					{
						dist = d;
						nearest = c;
					}
				}
				if (nearest != null)
				{
					mSelectedConnection = nearest;
					mSelectedNodes.clear();
				}
			}

			repaint();
		}
	};


	private Point calcMousePoint(MouseEvent aEvent)
	{
		return new Point((int)((aEvent.getX() - mPaneScroll.x) / mScale), (int)((aEvent.getY() - mPaneScroll.y) / mScale));
	}
}
