package org.terifan.nodeeditor.v2;

import java.awt.BasicStroke;
import java.awt.Color;
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
import org.terifan.util.log.Log;


public class RelationEditorPane extends JComponent
{
	private static final long serialVersionUID = 1L;

	private ArrayList<RelationBox> mNodes;
	private ArrayList<Connection> mConnections;
	private ArrayList<RelationBox> mSelectedNodes;
	private Connection mSelectedConnection;
	private double mScale;
	private Point mDragStartLocation;
	private Point mDragEndLocation;
	private Connector mDragConnector;
	private Rectangle mSelectionRectangle;
	private Point mPaneScroll;


	public RelationEditorPane()
	{
		mNodes = new ArrayList<>();
		mConnections = new ArrayList<>();
		mSelectedNodes = new ArrayList<>();

		mScale = 1;

		super.addMouseMotionListener(mMouseListener);
		super.addMouseListener(mMouseListener);
		super.addMouseWheelListener(mMouseListener);
	}


	public void add(RelationBox aBox)
	{
		mNodes.add(aBox);
	}


	public void addConnection(RelationItem aFromItem, RelationItem aToItem)
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


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		if (mPaneScroll == null)
		{
//			mPaneScroll = new Point(getWidth() / 2, getHeight() / 2);
			mPaneScroll = new Point();
		}

		Graphics2D g = (Graphics2D)aGraphics;

		drawPaneBackground(g);

		g.translate(mPaneScroll.x, mPaneScroll.y);
		
		AffineTransform oldTransform = g.getTransform();

		AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale(mScale, mScale);
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		for (RelationBox box : mNodes)
		{
			box.layout();
		}

		ConnectionRenderer connectionRenderer = new ConnectionRenderer();

		for (Connection connection : mConnections)
		{
			connectionRenderer.render(g, connection, mScale, mSelectedConnection == connection);
		}

		if (mDragEndLocation != null)
		{
			if (mDragConnector.getDirection() == Direction.IN)
			{
				connectionRenderer.render(g, mDragStartLocation, mDragEndLocation, mScale, false);
			}
			else
			{
				connectionRenderer.render(g, mDragEndLocation, mDragStartLocation, mScale, false);
			}
		}

		for (RelationBox box : mNodes)
		{
			Rectangle bounds = box.getBounds();
			int x = (int)(bounds.x * mScale);
			int y = (int)(bounds.y * mScale);
			int width = (int)(bounds.width * mScale);
			int height = (int)(bounds.height * mScale);

			if (g.hitClip(x, y, width, height))
			{
				boolean selected = mSelectedNodes.contains(box);

				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

				Graphics2D ig = image.createGraphics();
				ig.setTransform(affineTransform);
				ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				ig.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

				box.paintBorder(ig, 0, 0, bounds.width, bounds.height, selected);

				box.paintComponent(ig, selected);

				box.paintConnectors(ig);

				ig.dispose();

				g.drawImage(image, x, y, null);
			}
		}

		g.setTransform(oldTransform);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

		if (mSelectionRectangle != null)
		{
			g.setColor(Styles.PANE_SELECTION_RECTANGLE_BACKGROUND);
			g.fillRect(mSelectionRectangle.x, mSelectionRectangle.y, mSelectionRectangle.width + 1, mSelectionRectangle.height + 1);
			g.setColor(Styles.PANE_SELECTION_RECTANGLE_LINE);
			g.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0));
			g.draw(mSelectionRectangle);
		}
	}


	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(100,100);
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


//	public void setNodeSelected(RelationBox aNode, boolean aState)
//	{
//		mSelectedNodes.remove(aNode);
//		if (aState)
//		{
//			mSelectedNodes.add(aNode);
//		}
//	}


	private MouseAdapter mMouseListener = new MouseAdapter()
	{
		private Point mClickPoint;
		private boolean mHitBox;
		private Point mDragPoint;


		@Override
		public void mousePressed(MouseEvent aEvent)
		{
			mDragPoint = aEvent.getPoint();
			mClickPoint = calcMousePoint(aEvent);

			for (RelationBox box : mNodes)
			{
				Rectangle b = box.getBounds();
				if (b.contains(mClickPoint) && new Rectangle(b.x + 11, b.y + 7, 14, 16).contains(mClickPoint))
				{
					box.mMinimized ^= true;
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

			mSelectionRectangle = null;
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
					for (RelationBox box : mSelectedNodes)
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

			for (RelationBox box : mNodes)
			{
				if (mDragConnector != null && mDragConnector.mRelationItem.mRelationBox == box)
				{
					continue;
				}
				
				int x = aPoint.x - box.getBounds().x;
				int y = aPoint.y - box.getBounds().y;

				for (RelationItem item : box.mItems)
				{
					for (Connector c : item.mConnectors)
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

			return nearest;
		}


		private void updateSelections(MouseEvent aEvent)
		{
			RelationBox newSelection = null;
			RelationBox clickedBox = null;
			
			for (RelationBox box : mNodes)
			{
				if (box.getBounds().contains(mClickPoint))
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
			else
			{
				double dist = 50;
				Connection nearest = null;
				for (Connection c : mConnections)
				{
					double d = new ConnectionRenderer().distance(c, mClickPoint);
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
