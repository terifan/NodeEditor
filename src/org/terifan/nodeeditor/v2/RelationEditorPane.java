package org.terifan.nodeeditor.v2;

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
		Graphics2D g = (Graphics2D)aGraphics;

		drawPaneBackground(g);

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

		AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale(mScale, mScale);

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

				box.paintBorder(ig, 0, 0, bounds.width, bounds.height, selected);

				box.paintComponent(ig, selected);

				ig.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

				box.paintConnectors(ig);

				ig.dispose();

				g.drawImage(image, x, y, null);
			}
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

		int step = (int)(25 * mScale);

		aGraphics.setColor(Styles.PANE_GRID_COLOR_1);
		for (int x = 0; x < w; x+=step)
		{
			aGraphics.drawLine(x, 0, x, h);
		}
		for (int y = 0; y < h; y+=step)
		{
			aGraphics.drawLine(0, y, w, y);
		}

		aGraphics.setColor(Styles.PANE_GRID_COLOR_2);
		for (int x = 0; x < w; x+=5*step)
		{
			aGraphics.drawLine(x, 0, x, h);
		}
		for (int y = 0; y < h; y+=5*step)
		{
			aGraphics.drawLine(0, y, w, y);
		}
	}


	public void setNodeSelected(RelationBox aNode, boolean aState)
	{
		mSelectedNodes.remove(aNode);
		if (aState)
		{
			mSelectedNodes.add(aNode);
		}
	}


	private MouseAdapter mMouseListener = new MouseAdapter()
	{
		private Point mClickPoint;
		private boolean mHitBox;


		@Override
		public void mousePressed(MouseEvent aEvent)
		{
			mClickPoint = new Point((int)(aEvent.getX() / mScale), (int)(aEvent.getY() / mScale));

			mDragConnector = findNearestConnector(mClickPoint);

			if (mDragConnector != null)
			{
				mDragStartLocation = new Point(mDragConnector.mRelationItem.mRelationBox.getBounds().x + (int)mDragConnector.getBounds().getCenterX(), mDragConnector.mRelationItem.mRelationBox.getBounds().y + (int)mDragConnector.getBounds().getCenterY());
				mDragEndLocation = null;
			}
			else
			{
				mDragStartLocation = null;
				mDragEndLocation = null;

				updateSelections(aEvent);
			}
		}


		@Override
		public void mouseReleased(MouseEvent aEvent)
		{
			mClickPoint = new Point((int)(aEvent.getX() / mScale), (int)(aEvent.getY() / mScale));

			if (mDragConnector != null)
			{
				Connector nearestConnector = findNearestConnector(mClickPoint);

				if (nearestConnector != null)
				{
					addConnection(mDragConnector, nearestConnector);
				}

				mDragConnector = null;
				mDragEndLocation = null;
				repaint();
			}
		}


		@Override
		public void mouseDragged(MouseEvent aEvent)
		{
			Point oldPoint = mClickPoint;
			mClickPoint = new Point((int)(aEvent.getX() / mScale), (int)(aEvent.getY() / mScale));

			if (mDragConnector != null)
			{
				mDragEndLocation = mClickPoint;

				Connector shadowConnector = findNearestConnector(mDragEndLocation);
				if (shadowConnector != null)
				{
					mDragEndLocation = new Point(shadowConnector.mRelationItem.mRelationBox.getBounds().x + (int)shadowConnector.getBounds().getCenterX(), shadowConnector.mRelationItem.mRelationBox.getBounds().y + (int)shadowConnector.getBounds().getCenterY());
				}
			}
			else if (mHitBox)
			{
				for (RelationBox box : mSelectedNodes)
				{
					Point pt = box.getBounds().getLocation();
					pt.x += mClickPoint.x - oldPoint.x;
					pt.y += mClickPoint.y - oldPoint.y;
					box.setLocation(pt.x, pt.y);
				}
			}

			repaint();
		}


		@Override
		public void mouseWheelMoved(MouseWheelEvent aEvent)
		{
			mScale = Math.max(0.1, Math.min(100, mScale * (aEvent.getWheelRotation() == -1 ? 0.9 : 1.1)));

			repaint();
		}


		private Connector findNearestConnector(Point aPoint)
		{
			Connector nearest = null;
			double dist = 25; //Double.MAX_VALUE;

			for (RelationBox box : mNodes)
			{
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
					mHitBox = true;
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

			if (newSelection != null)
			{
				mSelectedNodes.add(newSelection);
			}
			if (clickedBox != null)
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
}
