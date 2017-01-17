package org.terifan.nodeeditor;

import java.awt.BasicStroke;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;


public class NodeEditor extends JComponent
{
	private static final long serialVersionUID = 1L;

	private static final BasicStroke SELECTION_RECTANGLE_STROKE = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);

	private ArrayList<Node> mSelectedNodes;
	private Connection mSelectedConnection;
	private Connector mDragConnector;
	private Point mDragStartLocation;
	private Point mDragEndLocation;
	private Rectangle mSelectionRectangle;
	private Point mPaneScroll;
	private boolean mConnectorSelectionAllowed;
	private double mScale;
	private boolean mRemoveInConnectionsOnDrop;
	private NodeItem mClickedItem;
	private Popup mPopup;

	private NodeModel mModel;


	public NodeEditor(NodeModel aModel)
	{
		mModel = aModel;
		mSelectedNodes = new ArrayList<>();
		mScale = 1;
		mRemoveInConnectionsOnDrop = true;

		super.addMouseMotionListener(mMouseListener);
		super.addMouseListener(mMouseListener);
		super.addMouseWheelListener(mMouseListener);
	}


	public NodeModel getModel()
	{
		return mModel;
	}


	public boolean isRemoveInConnectionsOnDrop()
	{
		return mRemoveInConnectionsOnDrop;
	}


	public NodeEditor setRemoveInConnectionsOnDrop(boolean aRemoveInConnectionsOnDrop)
	{
		mRemoveInConnectionsOnDrop = aRemoveInConnectionsOnDrop;
		return this;
	}


	public boolean isConnectorSelectionAllowed()
	{
		return mConnectorSelectionAllowed;
	}


	public NodeEditor setConnectorSelectionAllowed(boolean aConnectorSelectionAllowed)
	{
		mConnectorSelectionAllowed = aConnectorSelectionAllowed;
		return this;
	}


	public double getScale()
	{
		return mScale;
	}


	public NodeEditor setScale(double aScale)
	{
		mScale = aScale;
		return this;
	}


	/**
	 * Move all nodes to the center of the screen
	 */
	public void center()
	{
		if (mModel.getNodes().isEmpty())
		{
			return;
		}

		Rectangle bounds = new Rectangle(mModel.getNodes().get(0).getBounds());
		for (Node box : mModel.getNodes())
		{
			box.layout(null);
			bounds.add(box.getBounds());
		}

		int dx = -(int)bounds.getCenterX();
		int dy = -(int)bounds.getCenterY();

		for (Node box : mModel.getNodes())
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

		for (Node box : mModel.getNodes())
		{
			box.layout(g);
		}
		if (mPopup != null)
		{
			mPopup.layout(g);
		}

		for (Connection connection : mModel.getConnections())
		{
			if (mSelectedConnection == connection)
			{
				SplineRenderer.drawSpline(g, connection, mScale, Styles.CONNECTOR_COLOR_OUTER_SELECTED, Styles.CONNECTOR_COLOR_INNER_SELECTED, Styles.CONNECTOR_COLOR_INNER_SELECTED);
			}
			else
			{
				Color start = mSelectedNodes.contains(connection.getIn().getNodeItem().getNode()) ? Styles.CONNECTOR_COLOR_INNER_FOCUSED : Styles.CONNECTOR_COLOR_INNER;
				Color end = mSelectedNodes.contains(connection.getOut().getNodeItem().getNode()) ? Styles.CONNECTOR_COLOR_INNER_FOCUSED : Styles.CONNECTOR_COLOR_INNER;

				SplineRenderer.drawSpline(g, connection, mScale, Styles.CONNECTOR_COLOR_OUTER, start, end);
			}
		}

		if (mDragEndLocation != null)
		{
			if (mDragConnector.getDirection() == Direction.IN)
			{
				SplineRenderer.drawSpline(g, mDragStartLocation, mDragEndLocation, mScale, Styles.CONNECTOR_COLOR_OUTER, Styles.CONNECTOR_COLOR_INNER_DRAGGED, Styles.CONNECTOR_COLOR_INNER_DRAGGED);
			}
			else
			{
				SplineRenderer.drawSpline(g, mDragEndLocation, mDragStartLocation, mScale, Styles.CONNECTOR_COLOR_OUTER, Styles.CONNECTOR_COLOR_INNER_DRAGGED, Styles.CONNECTOR_COLOR_INNER_DRAGGED);
			}
		}

		for (Node box : mModel.getNodes())
		{
			paintBox(g, box, mSelectedNodes.contains(box));
		}

		if (mPopup != null)
		{
			paintBox(g, mPopup, false);
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


	private void paintBox(Graphics2D aGraphics, Renderable aRenderable, boolean aSelected)
	{
		Rectangle bounds = aRenderable.getBounds();
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

			aRenderable.paintComponent(this, ig, bounds.width, bounds.height, aSelected);

			ig.dispose();

			if (offscreen)
			{
				aGraphics.drawImage(offscreenBuffer, x, y, null);
			}
		}
	}


	@Override
	public Dimension getPreferredSize()
	{
		Rectangle bounds = new Rectangle(mModel.getNodes().get(0).getBounds());
		for (Node box : mModel.getNodes())
		{
			box.layout(null);
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

		int step = Math.max((int)(24 * mScale), 1);

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
		private int mCursor;
		private Node mHoverBox;
		private Rectangle mStartBounds;
		private boolean mIgnoreNextMouseRelease;

		{
			mCursor = Cursor.DEFAULT_CURSOR;
		}


		@Override
		public void mouseMoved(MouseEvent aEvent)
		{
			Point point = calcMousePoint(aEvent);

			if (mPopup != null)
			{
				mPopup.mouseMoved(point);
				return;
			}

			for (Node box : mModel.getNodes())
			{
				Rectangle b = box.getBounds();
				if (!box.isMinimized() && b.contains(point) && findNearestConnector(point, box) == null)
				{
					mHoverBox = box;
					updateCursor(getCursor(point, box));
					return;
				}
			}

			updateCursor(Cursor.DEFAULT_CURSOR);
		}


		@Override
		public void mousePressed(MouseEvent aEvent)
		{
			boolean left = SwingUtilities.isLeftMouseButton(aEvent);

			if (mPopup != null)
			{
				mIgnoreNextMouseRelease = true;
				mPopup.mousePressed(aEvent);
				return;
			}

			mDragPoint = aEvent.getPoint();
			mClickPoint = calcMousePoint(aEvent);

			if (SwingUtilities.isMiddleMouseButton(aEvent))
			{
				updateCursor(Cursor.MOVE_CURSOR);
			}
			if (!left)
			{
				return;
			}

			if (mCursor != Cursor.DEFAULT_CURSOR)
			{
				mStartBounds = new Rectangle(mHoverBox.getBounds());

				mModel.getNodes().remove(mHoverBox);
				mModel.getNodes().add(mHoverBox);

				mSelectedNodes.clear();
				mSelectedNodes.add(mHoverBox);
				mSelectedConnection = null;
				repaint();

				return;
			}

			mClickedItem = null;

			Node clickedBox = null;

			for (Node box : mModel.getNodes())
			{
				Rectangle b = box.getBounds();

				if (b.contains(mClickPoint))
				{
					clickedBox = box;

					if (new Rectangle(b.x + 11, b.y + 7, 14, 16).contains(mClickPoint))
					{
						box.setMinimized(!box.isMinimized());
						updateSelections(aEvent, clickedBox);
						return;
					}

					NodeItem tmp = box.mousePressed(mClickPoint);
					if (tmp != null)
					{
						if (tmp.mousePressed(NodeEditor.this, mClickPoint))
						{
							mSelectedNodes.clear();
							mSelectedNodes.add(box);
							mClickedItem = tmp;
							repaint();

							tmp.actionPerformed(NodeEditor.this, mClickPoint);

							return;
						}
					}
				}
			}

			mDragConnector = findNearestConnector(mClickPoint);

			if (mDragConnector != null)
			{
				boolean done = false;
				if (mDragConnector.getDirection() == Direction.IN)
				{
					List<Connection> list = mModel.getConnectionsTo(mDragConnector.getNodeItem()).collect(Collectors.toList());
					if (list.size() == 1)
					{
						mDragEndLocation = mDragConnector.getConnectorPoint();
						mDragConnector = list.get(0).getIn();
						mDragStartLocation = mDragConnector.getConnectorPoint();

						mModel.getConnections().remove(list.get(0));

						done = true;
					}
				}

				if (!done)
				{
					mDragStartLocation = mDragConnector.getConnectorPoint();
				}
			}
			else if (left)
			{
				updateSelections(aEvent, clickedBox);

				if (!mHitBox && mDragConnector == null)
				{
					mSelectionRectangle = new Rectangle(mClickPoint);
				}
			}
		}


		@Override
		public void mouseReleased(MouseEvent aEvent)
		{
			if (mPopup != null)
			{
				if (mIgnoreNextMouseRelease)
				{
					mPopup.mouseReleased(aEvent);
				}
				mIgnoreNextMouseRelease = false;
				mClickedItem = null;
				mSelectionRectangle = null;
				return;
			}

			mClickPoint = calcMousePoint(aEvent);

			if (mCursor != Cursor.DEFAULT_CURSOR)
			{
				updateCursor(Cursor.DEFAULT_CURSOR);
				return;
			}

			if (mClickedItem != null)
			{
				mClickedItem.mouseReleased(NodeEditor.this, mClickPoint);
				mClickedItem = null;
				repaint();
				return;
			}
			if (mDragConnector != null)
			{
				Connector nearestConnector = findNearestConnector(mClickPoint);

				if (nearestConnector != null && mDragConnector.getDirection() != nearestConnector.getDirection())
				{
					if (mRemoveInConnectionsOnDrop)
					{
						if (nearestConnector.getDirection() == Direction.IN)
						{
							mModel.getConnections().removeAll(mModel.getConnectionsTo(nearestConnector.getNodeItem()).collect(Collectors.toList()));
						}
						if (nearestConnector.getDirection() == Direction.OUT)
						{
							mModel.getConnections().removeAll(mModel.getConnectionsTo(mDragConnector.getNodeItem()).collect(Collectors.toList()));
						}
					}

					if (mDragConnector.getDirection() == Direction.IN)
					{
						mModel.addConnection(nearestConnector, mDragConnector);
					}
					else
					{
						mModel.addConnection(mDragConnector, nearestConnector);
					}
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

				mSelectionRectangle.x /= mScale;
				mSelectionRectangle.y /= mScale;
				mSelectionRectangle.width /= mScale;
				mSelectionRectangle.height /= mScale;

				for (Node box : mModel.getNodes())
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
			if (mPopup != null)
			{
				return;
			}

			Point newPoint = calcMousePoint(aEvent);

			if (mCursor != Cursor.DEFAULT_CURSOR && SwingUtilities.isLeftMouseButton(aEvent))
			{
				resizeBox(mHoverBox, newPoint);
				return;
			}

			if (mClickedItem != null)
			{
				mClickedItem.mouseDragged(NodeEditor.this, mClickPoint, newPoint);
				return;
			}
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
					if (connector != null && mDragConnector.getDirection() != connector.getDirection())
					{
						mDragEndLocation = connector.getConnectorPoint();
					}
				}
				else if (mHitBox || SwingUtilities.isRightMouseButton(aEvent))
				{
					for (Node box : mSelectedNodes)
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
			if (mPopup != null)
			{
				mPopup.mouseWheelMoved(aEvent);
				return;
			}

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

			for (Node box : mModel.getNodes())
			{
				if (mDragConnector != null && mDragConnector.getNodeItem().getNode() == box)
				{
					continue;
				}

				Rectangle b = box.getBounds();
				int x = aPoint.x - b.x;
				int y = aPoint.y - b.y;

				for (NodeItem item : box)
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

			if (mDragConnector != null && mDragConnector.getNodeItem().getNode() == box)
			{
				return null;
			}

			int x = aPoint.x - box.getBounds().x;
			int y = aPoint.y - box.getBounds().y;

			for (NodeItem item : box)
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

			if (hitBox && nearest != null && dist > 8)
			{
				nearest = null;
			}

			return nearest;
		}


		private void updateSelections(MouseEvent aEvent, Node aClickedBox)
		{
			Node newSelection = null;
			Node clickedBox = null;

			if (aClickedBox != null)
			{
				Rectangle shrunkBounds = new Rectangle(aClickedBox.getBounds());
				shrunkBounds.grow(-5, -4);

				if (shrunkBounds.contains(mClickPoint))
				{
					clickedBox = aClickedBox;

					boolean b = mSelectedNodes.contains(aClickedBox);
					if (aEvent.isControlDown())
					{
						if (b)
						{
							mSelectedNodes.remove(aClickedBox);
						}
						else
						{
							newSelection = aClickedBox;
						}
					}
					else if (!b)
					{
						mSelectedNodes.clear();
						newSelection = aClickedBox;
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
				mModel.getNodes().remove(clickedBox);
				mModel.getNodes().add(clickedBox);
				mSelectedConnection = null;
			}
			else if (mConnectorSelectionAllowed)
			{
				double dist = 50;
				Connection nearest = null;
				for (Connection c : mModel.getConnections())
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


		protected void updateCursor(int aCursor)
		{
			if (mCursor != aCursor)
			{
				mCursor = aCursor;
				SwingUtilities.invokeLater(()->setCursor(Cursor.getPredefinedCursor(aCursor < -1 ? Cursor.DEFAULT_CURSOR : aCursor)));
			}
		}


		private int getCursor(Point aPoint, Node aNode)
		{
			boolean rx = aNode.isResizableHorizontal();
			boolean ry = aNode.isResizableVertical();

			if (!rx && !ry)
			{
				return Cursor.DEFAULT_CURSOR;
			}

			int PX = 5;
			int PY = 4;
			Rectangle bounds = aNode.getBounds();

			if (aPoint.y - PY < bounds.y + 2 * PY)
			{
				if (aPoint.x - PX < bounds.x + 2 * PX)
				{
					return rx ? ry ? Cursor.NW_RESIZE_CURSOR : Cursor.W_RESIZE_CURSOR : Cursor.N_RESIZE_CURSOR;
				}
				if (aPoint.x + PX >= bounds.x + bounds.width - 2 * PX)
				{
					return rx ? ry ? Cursor.NE_RESIZE_CURSOR : Cursor.E_RESIZE_CURSOR : Cursor.N_RESIZE_CURSOR;
				}
				if (aPoint.y - PY < bounds.y + PY && ry)
				{
					return Cursor.N_RESIZE_CURSOR;
				}
			}
			else if (aPoint.y + PY >= bounds.y + bounds.height - 2 * PY)
			{
				if (aPoint.x - PX < bounds.x + 2 * PX)
				{
					return rx ? ry ? Cursor.SW_RESIZE_CURSOR : Cursor.W_RESIZE_CURSOR : Cursor.S_RESIZE_CURSOR;
				}
				if (aPoint.x + PX >= bounds.x + bounds.width - 2 * PX)
				{
					return rx ? ry ? Cursor.SE_RESIZE_CURSOR : Cursor.E_RESIZE_CURSOR : Cursor.S_RESIZE_CURSOR;
				}
				if (aPoint.y + PY >= bounds.y + bounds.height - PY && ry)
				{
					return Cursor.S_RESIZE_CURSOR;
				}
			}
			else if (aPoint.x - PX < bounds.x + PX && rx)
			{
				return Cursor.W_RESIZE_CURSOR;
			}
			else if (aPoint.x + PX > bounds.x + bounds.width - PX && rx)
			{
				return Cursor.E_RESIZE_CURSOR;
			}

			return Cursor.DEFAULT_CURSOR;
		}


		private void resizeBox(Node aNode, Point aPoint)
		{
			Rectangle b = aNode.getBounds();

			switch (mCursor)
			{
				case Cursor.W_RESIZE_CURSOR:
				case Cursor.NW_RESIZE_CURSOR:
				case Cursor.SW_RESIZE_CURSOR:
					int o = b.x;
					b.x = Math.min(mStartBounds.x - mClickPoint.x + aPoint.x, mStartBounds.x + mStartBounds.width - aNode.getMinSize().width);
					b.width += o - b.x;
					break;
			}

			switch (mCursor)
			{
				case Cursor.N_RESIZE_CURSOR:
				case Cursor.NW_RESIZE_CURSOR:
				case Cursor.NE_RESIZE_CURSOR:
					int o = b.y;
					b.y = Math.min(mStartBounds.y - mClickPoint.y + aPoint.y, mStartBounds.y + mStartBounds.height - aNode.getMinSize().height);
					b.height += o - b.y;
					break;
			}

			switch (mCursor)
			{
				case Cursor.SW_RESIZE_CURSOR:
				case Cursor.S_RESIZE_CURSOR:
				case Cursor.SE_RESIZE_CURSOR:
					b.height = mStartBounds.height - mClickPoint.y + aPoint.y;
					break;
			}

			switch (mCursor)
			{
				case Cursor.E_RESIZE_CURSOR:
				case Cursor.SE_RESIZE_CURSOR:
				case Cursor.NE_RESIZE_CURSOR:
					b.width = mStartBounds.width - mClickPoint.x + aPoint.x;
					break;
			}

			b.width = Math.min(aNode.getMaxSize().width, Math.max(aNode.getMinSize().width, b.width));
			b.height = Math.min(aNode.getMaxSize().height, Math.max(aNode.getMinSize().height, b.height));

			aNode.mBounds.setBounds(b);
			repaint();
		}
	};


	private Point calcMousePoint(MouseEvent aEvent)
	{
		return new Point((int)((aEvent.getX() - mPaneScroll.x) / mScale), (int)((aEvent.getY() - mPaneScroll.y) / mScale));
	}




	public Popup getPopup()
	{
		return mPopup;
	}


	public void setPopup(Popup aPopup)
	{
		this.mPopup = aPopup;
	}


	protected HashMap<String,ResourceLoader> mResourceLoaders = new HashMap<>();


	protected BufferedImage getImageResource(NodeItem aItem)
	{
		try
		{
			return (BufferedImage)mResourceLoaders.get((aItem.getNode().getIdentityOrName() + "." + aItem.getIdentityOrName()).toLowerCase()).load(aItem);
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}


	public NodeEditor addResourceLoader(String aPath, ResourceLoader aResourceLoader)
	{
		mResourceLoaders.put(aPath.toLowerCase(), aResourceLoader);
		return this;
	}


	public interface ResourceLoader
	{
		Object load(NodeItem aItem) throws Exception;
	}
}