package org.terifan.nodeeditor;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.terifan.boxcomponentpane.BoxComponent;
import org.terifan.boxcomponentpane.BoxComponentMouseListener;
import static org.terifan.nodeeditor.Styles.MIN_HEIGHT;
import static org.terifan.nodeeditor.Styles.MIN_WIDTH;
import org.terifan.nodeeditor.graphics.Popup;
import org.terifan.nodeeditor.graphics.SplineRenderer;


class NodeEditorMouseListener<T extends Node, U extends NodeEditorPane> extends BoxComponentMouseListener<Node, NodeEditorPane>
{
	private boolean mIgnoreNextMouseRelease;
	private boolean mConnectorSelectionAllowed;
	private boolean mRemoveInConnectionsOnDrop;
	private Property mSelectedProperty;


	public NodeEditorMouseListener(NodeEditorPane aPane)
	{
		super(aPane);

		mRemoveInConnectionsOnDrop = true;
		mConnectorSelectionAllowed = true;
	}


	@Override
	public void mouseMoved(MouseEvent aEvent)
	{
		Popup popup = mViewPort.getPopup();
		Point point = mViewPort.calcMousePoint(aEvent.getPoint());

		if (popup != null)
		{
			popup.mouseMoved(point);
			return;
		}

		NodeModel model = mViewPort.getModel();

		List<T> nodes = (List<T>)model.getComponents();
		for (int i = nodes.size(); --i >= 0;)
		{
			T node = nodes.get(i);
			Rectangle bounds = node.getBounds();
			if (!node.isMinimized() && bounds.contains(point) && mViewPort.findNearestConnector(point, node) == null)
			{
				mHoverNode = node;
				updateCursor(getCursor(point, node));
				return;
			}
		}

		updateCursor(Cursor.DEFAULT_CURSOR);
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		NodeModel model = mViewPort.getModel();
		Popup popup = mViewPort.getPopup();

		if (popup != null)
		{
			mIgnoreNextMouseRelease = true;
			popup.mousePressed(aEvent);
			return;
		}

		mDragPoint = aEvent.getPoint();
		mClickPoint = mViewPort.calcMousePoint(aEvent.getPoint());

		if (SwingUtilities.isMiddleMouseButton(aEvent))
		{
			updateCursor(Cursor.MOVE_CURSOR);
		}
		if (!SwingUtilities.isLeftMouseButton(aEvent))
		{
			return;
		}

		if (mCursor != Cursor.DEFAULT_CURSOR)
		{
			mStartBounds = new Rectangle(mHoverNode.getBounds());

			mViewPort.getModel().getComponents().remove(mHoverNode);
			mViewPort.getModel().getComponents().add(mHoverNode);

			mViewPort.getSelectedBoxes().clear();
			mViewPort.getSelectedBoxes().add(mHoverNode);
			mViewPort.setSelectedConnection(null);
			mViewPort.repaint();

			return;
		}

		mSelectedProperty = null;

		Node node = model.getComponentAt(mClickPoint);

		if (node != null)
		{
			Property tmp = node.mousePressed(mClickPoint);
			if (tmp != null)
			{
				if (tmp.mousePressed(mViewPort, mClickPoint))
				{
					mViewPort.getSelectedBoxes().clear();
					mViewPort.getSelectedBoxes().add(node);
					mSelectedProperty = tmp;
					mViewPort.repaint();
					return;
				}
			}
		}

		Connector dragConnector = mViewPort.findNearestConnector(mClickPoint);
		mViewPort.setDragConnector(dragConnector);

		if (dragConnector != null)
		{
			boolean done = false;
			if (dragConnector.getDirection() == Direction.IN)
			{
				List<Connection> list = model.getConnectionsTo(dragConnector.getProperty());
				if (list.size() == 1)
				{
					Connector out = list.get(0).getOut();

					mViewPort.setDragConnector(out);
					mViewPort.setDragEndLocation(out.getConnectorPoint());
					mViewPort.setDragStartLocation(out.getConnectorPoint());

					model.getConnections().remove(list.get(0));

					done = true;
				}
			}

			if (!done)
			{
				mViewPort.setDragStartLocation(dragConnector.getConnectorPoint());
			}
		}
		else
		{
			updateSelections(aEvent, node);

			if (!mClickedBox && dragConnector == null)
			{
				mViewPort.setSelectionRectangle(new Rectangle(mClickPoint));
			}
		}
	}


	@Override
	protected void updateMinimize(MouseEvent aEvent, Node aNode)
	{
		super.updateMinimize(aEvent, aNode);
		updateSelections(aEvent, aNode);
	}


	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
		Popup popup = mViewPort.getPopup();
		NodeModel model = mViewPort.getModel();

		if (popup != null)
		{
			if (mIgnoreNextMouseRelease)
			{
				popup.mouseReleased(aEvent);
			}
			mIgnoreNextMouseRelease = false;
			mSelectedProperty = null;
			mViewPort.setSelectionRectangle(null);
			return;
		}

		mClickPoint = mViewPort.calcMousePoint(aEvent.getPoint());

		if (mCursor != Cursor.DEFAULT_CURSOR)
		{
			updateCursor(Cursor.DEFAULT_CURSOR);
			return;
		}

		if (mSelectedProperty != null)
		{
			mSelectedProperty.mouseReleased(mViewPort, mClickPoint);
			mSelectedProperty = null;
			mViewPort.repaint();
			return;
		}

		if (mViewPort.getDragConnector() != null)
		{
			Connector nearestConnector = mViewPort.findNearestConnector(mClickPoint);

			if (nearestConnector != null && mViewPort.getDragConnector().getDirection() != nearestConnector.getDirection())
			{
				if (mRemoveInConnectionsOnDrop)
				{
					if (nearestConnector.getDirection() == Direction.IN)
					{
						model.getConnections().removeAll(model.getConnectionsTo(nearestConnector.getProperty()));
					}
					if (nearestConnector.getDirection() == Direction.OUT)
					{
						model.getConnections().removeAll(model.getConnectionsTo(mViewPort.getDragConnector().getProperty()));
					}
				}

				if (mViewPort.getDragConnector().getDirection() == Direction.IN)
				{
					model.addConnection(nearestConnector, mViewPort.getDragConnector());
				}
				else
				{
					model.addConnection(mViewPort.getDragConnector(), nearestConnector);
				}

				nearestConnector.getProperty().connectionsChanged(mViewPort, mClickPoint);
			}

			mViewPort.setDragConnector(null);
			mViewPort.setDragStartLocation(null);
			mViewPort.setDragEndLocation(null);
		}

		Rectangle selectionRectangle = mViewPort.getSelectionRectangle();
		if (selectionRectangle != null)
		{
			if (!aEvent.isControlDown())
			{
				mViewPort.getSelectedBoxes().clear();
			}

			double scale = mViewPort.getScale();
			selectionRectangle.x /= scale;
			selectionRectangle.y /= scale;
			selectionRectangle.width /= scale;
			selectionRectangle.height /= scale;

			for (Node box : mViewPort.getModel().getComponents())
			{
				if (selectionRectangle.intersects(box.getBounds()))
				{
					if (!mViewPort.getSelectedBoxes().contains(box))
					{
						mViewPort.getSelectedBoxes().add(box);
					}
					else if (aEvent.isControlDown())
					{
						mViewPort.getSelectedBoxes().remove(box);
					}
				}
			}
			mViewPort.setSelectionRectangle(null);
		}

		mViewPort.repaint();
	}


	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		Popup popup = mViewPort.getPopup();

		if (popup != null)
		{
			return;
		}

		Rectangle selectionRectangle = mViewPort.getSelectionRectangle();
		Point2D.Double paneScroll = mViewPort.getPaneScroll();

		Point newPoint = mViewPort.calcMousePoint(aEvent.getPoint());

		if (mCursor != Cursor.DEFAULT_CURSOR && SwingUtilities.isLeftMouseButton(aEvent))
		{
			resizeBox(mHoverNode, newPoint);
			return;
		}

		if (mSelectedProperty != null)
		{
			mSelectedProperty.mouseDragged(mViewPort, mClickPoint, newPoint);
			return;
		}
		if (selectionRectangle != null)
		{
			int x0 = (int)(Math.min(mClickPoint.x, newPoint.x) * mViewPort.getScale());
			int y0 = (int)(Math.min(mClickPoint.y, newPoint.y) * mViewPort.getScale());
			int x1 = (int)(Math.max(mClickPoint.x, newPoint.x) * mViewPort.getScale());
			int y1 = (int)(Math.max(mClickPoint.y, newPoint.y) * mViewPort.getScale());

			selectionRectangle.setBounds(x0, y0, x1 - x0, y1 - y0);
		}
		else
		{
			Point oldPoint = mClickPoint;
			mClickPoint = newPoint;

			if (SwingUtilities.isMiddleMouseButton(aEvent))
			{
				paneScroll.x += (aEvent.getX() - mDragPoint.x);
				paneScroll.y += (aEvent.getY() - mDragPoint.y);
				mDragPoint = aEvent.getPoint();
			}
			else if (mViewPort.getDragConnector() != null)
			{
				mViewPort.setDragEndLocation(mClickPoint);

				Connector connector = mViewPort.findNearestConnector(mViewPort.getDragEndLocation());
				if (connector != null && mViewPort.getDragConnector().getDirection() != connector.getDirection())
				{
					mViewPort.setDragEndLocation(connector.getConnectorPoint());
				}
			}
			else if (mClickedBox || SwingUtilities.isRightMouseButton(aEvent))
			{
				for (Node box : (ArrayList<Node>)mViewPort.getSelectedBoxes())
				{
					Point pt = box.getBounds().getLocation();
					pt.x += mClickPoint.x - oldPoint.x;
					pt.y += mClickPoint.y - oldPoint.y;
					box.setLocation(pt.x, pt.y);
				}
			}
		}

		mViewPort.repaint();
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent aEvent)
	{
		Popup popup = mViewPort.getPopup();

		if (popup != null)
		{
			popup.mouseWheelMoved(aEvent);
			return;
		}

		Point2D.Double scroll = mViewPort.getPaneScroll();

		scroll.x -= aEvent.getX();
		scroll.y -= aEvent.getY();

		if (aEvent.getWheelRotation() == 1)
		{
			mViewPort.setScale(mViewPort.getScale() * mZoomSpeed);
			scroll.x *= mZoomSpeed;
			scroll.y *= mZoomSpeed;
		}
		else
		{
			mViewPort.setScale(mViewPort.getScale() / mZoomSpeed);
			scroll.x /= mZoomSpeed;
			scroll.y /= mZoomSpeed;
		}

		scroll.x += aEvent.getX();
		scroll.y += aEvent.getY();

		mViewPort.repaint();
	}


	private void updateSelections(MouseEvent aEvent, Node aClickedBox)
	{
		NodeModel model = mViewPort.getModel();
		Node newSelection = null;
		Node newClickedBox = null;

		if (aClickedBox != null)
		{
			Rectangle shrunkBounds = new Rectangle(aClickedBox.getBounds());
			shrunkBounds.grow(-5, -4);

			if (shrunkBounds.contains(mClickPoint))
			{
				newClickedBox = aClickedBox;

				boolean b = mViewPort.getSelectedBoxes().contains(aClickedBox);
				if (aEvent.isControlDown())
				{
					if (b)
					{
						mViewPort.getSelectedBoxes().remove(aClickedBox);
					}
					else
					{
						newSelection = aClickedBox;
					}
				}
				else if (!b)
				{
					mViewPort.getSelectedBoxes().clear();
					newSelection = aClickedBox;
				}
			}
		}

		mClickedBox = newClickedBox != null;

		if (newSelection != null)
		{
			mViewPort.getSelectedBoxes().add(newSelection);
		}

		if (mClickedBox)
		{
			mViewPort.getModel().getComponents().remove(newClickedBox);
			mViewPort.getModel().getComponents().add(newClickedBox);
			mViewPort.setSelectedConnection(null);
		}
		else if (mConnectorSelectionAllowed)
		{
			double dist = 50;
			Connection nearest = null;
			for (Connection c : model.getConnections())
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
				mViewPort.setSelectedConnection(mViewPort.getSelectedConnection());
				mViewPort.getSelectedBoxes().clear();
			}
		}

		mViewPort.repaint();
	}


	@Override
	protected int getCursor(Point aPoint, BoxComponent aNode)
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


	private void resizeBox(Node aBox, Point aPoint)
	{
		Rectangle b = aBox.getBounds();

		int minWidth = Math.max(MIN_WIDTH, aBox.getMinimumSize().width);
		int minHeight = Math.max(MIN_HEIGHT, aBox.getMinimumSize().height);

		switch (mCursor)
		{
			case Cursor.W_RESIZE_CURSOR:
			case Cursor.NW_RESIZE_CURSOR:
			case Cursor.SW_RESIZE_CURSOR:
				int o = b.x;
				b.x = Math.min(mStartBounds.x - mClickPoint.x + aPoint.x, mStartBounds.x + mStartBounds.width - minWidth);
				b.width += o - b.x;
				break;
		}

		switch (mCursor)
		{
			case Cursor.N_RESIZE_CURSOR:
			case Cursor.NW_RESIZE_CURSOR:
			case Cursor.NE_RESIZE_CURSOR:
				int o = b.y;
				b.y = Math.min(mStartBounds.y - mClickPoint.y + aPoint.y, mStartBounds.y + mStartBounds.height - minHeight);
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

		b.width = Math.min(aBox.getMaximumSize().width, Math.max(minWidth, b.width));
		b.height = Math.min(aBox.getMaximumSize().height, Math.max(minHeight, b.height));

		aBox.getBounds().setBounds(b);
		mViewPort.repaint();
	}
}
