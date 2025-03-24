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
		Popup popup = mPane.getPopup();
		Point point = mPane.calcMousePoint(aEvent.getPoint());

		if (popup != null)
		{
			popup.mouseMoved(point);
			return;
		}

		NodeModel model = mPane.getModel();

		List<T> nodes = (List<T>)model.getComponents();
		for (int i = nodes.size(); --i >= 0; )
		{
			T node = nodes.get(i);
			Rectangle bounds = node.getBounds();
			if (!node.isMinimized() && bounds.contains(point) && mPane.findNearestConnector(point, node) == null)
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
		NodeModel model = mPane.getModel();
		Popup popup = mPane.getPopup();

		if (popup != null)
		{
			mIgnoreNextMouseRelease = true;
			popup.mousePressed(aEvent);
			return;
		}

		mDragPoint = aEvent.getPoint();
		mClickPoint = mPane.calcMousePoint(aEvent.getPoint());

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

			mPane.getModel().getComponents().remove(mHoverNode);
			mPane.getModel().getComponents().add(mHoverNode);

			mPane.getSelectedBoxes().clear();
			mPane.getSelectedBoxes().add(mHoverNode);
			mPane.setSelectedConnection(null);
			mPane.repaint();

			return;
		}

		mSelectedProperty = null;

		Node clickedBox = null;

		for (Node node : (ArrayList<Node>)model.getComponents())
		{
			Rectangle b = node.getBounds();

			if (b.contains(mClickPoint))
			{
				clickedBox = node;

				if (new Rectangle(b.x + 11, b.y + 7, 20, 20).contains(mClickPoint))
				{
					node.setMinimized(!node.isMinimized());
					updateSelections(aEvent, clickedBox);
					return;
				}

				Property tmp = node.mousePressed(mClickPoint);
				if (tmp != null && tmp.mousePressed(mPane, mClickPoint))
				{
					mPane.getSelectedBoxes().clear();
					mPane.getSelectedBoxes().add(node);
					mSelectedProperty = tmp;
					mPane.repaint();
					break;
				}
			}
		}

		Connector dragConnector = mPane.findNearestConnector(mClickPoint);
		mPane.setDragConnector(dragConnector);

		if (dragConnector != null)
		{
			boolean done = false;
			if (dragConnector.getDirection() == Direction.IN)
			{
				List<Connection> list = model.getConnectionsTo(dragConnector.getProperty());
				if (list.size() == 1)
				{
					Connector out = list.get(0).getOut();

					mPane.setDragConnector(out);
					mPane.setDragEndLocation(out.getConnectorPoint());
					mPane.setDragStartLocation(out.getConnectorPoint());

					model.getConnections().remove(list.get(0));

					done = true;
				}
			}

			if (!done)
			{
				mPane.setDragStartLocation(dragConnector.getConnectorPoint());
			}
		}
		else
		{
			updateSelections(aEvent, clickedBox);

			if (!mClickedBox && dragConnector == null)
			{
				mPane.setSelectionRectangle(new Rectangle(mClickPoint));
			}
		}
	}


	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
		Popup popup = mPane.getPopup();
		NodeModel model = mPane.getModel();

		if (popup != null)
		{
			if (mIgnoreNextMouseRelease)
			{
				popup.mouseReleased(aEvent);
			}
			mIgnoreNextMouseRelease = false;
			mSelectedProperty = null;
			mPane.setSelectionRectangle(null);
			return;
		}

		mClickPoint = mPane.calcMousePoint(aEvent.getPoint());

		if (mCursor != Cursor.DEFAULT_CURSOR)
		{
			updateCursor(Cursor.DEFAULT_CURSOR);
			return;
		}

		if (mSelectedProperty != null)
		{
			mSelectedProperty.mouseReleased(mPane, mClickPoint);
			mSelectedProperty = null;
			mPane.repaint();
			return;
		}

		if (mPane.getDragConnector() != null)
		{
			Connector nearestConnector = mPane.findNearestConnector(mClickPoint);

			if (nearestConnector != null && mPane.getDragConnector().getDirection() != nearestConnector.getDirection())
			{
				if (mRemoveInConnectionsOnDrop)
				{
					if (nearestConnector.getDirection() == Direction.IN)
					{
						model.getConnections().removeAll(model.getConnectionsTo(nearestConnector.getProperty()));
					}
					if (nearestConnector.getDirection() == Direction.OUT)
					{
						model.getConnections().removeAll(model.getConnectionsTo(mPane.getDragConnector().getProperty()));
					}
				}

				if (mPane.getDragConnector().getDirection() == Direction.IN)
				{
					model.addConnection(nearestConnector, mPane.getDragConnector());
				}
				else
				{
					model.addConnection(mPane.getDragConnector(), nearestConnector);
				}

				nearestConnector.getProperty().connectionsChanged(mPane, mClickPoint);
			}

			mPane.setDragConnector(null);
			mPane.setDragStartLocation(null);
			mPane.setDragEndLocation(null);
		}

		Rectangle selectionRectangle = mPane.getSelectionRectangle();
		if (selectionRectangle != null)
		{
			if (!aEvent.isControlDown())
			{
				mPane.getSelectedBoxes().clear();
			}

			double scale = mPane.getScale();
			selectionRectangle.x /= scale;
			selectionRectangle.y /= scale;
			selectionRectangle.width /= scale;
			selectionRectangle.height /= scale;

			for (Node box : mPane.getModel().getComponents())
			{
				if (selectionRectangle.intersects(box.getBounds()))
				{
					if (!mPane.getSelectedBoxes().contains(box))
					{
						mPane.getSelectedBoxes().add(box);
					}
					else if (aEvent.isControlDown())
					{
						mPane.getSelectedBoxes().remove(box);
					}
				}
			}
			mPane.setSelectionRectangle(null);
		}

		mPane.repaint();
	}


	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		Popup popup = mPane.getPopup();

		if (popup != null)
		{
			return;
		}

		Rectangle selectionRectangle = mPane.getSelectionRectangle();
		Point2D.Double paneScroll = mPane.getPaneScroll();

		Point newPoint = mPane.calcMousePoint(aEvent.getPoint());

		if (mCursor != Cursor.DEFAULT_CURSOR && SwingUtilities.isLeftMouseButton(aEvent))
		{
			resizeBox(mHoverNode, newPoint);
			return;
		}

		if (mSelectedProperty != null)
		{
			mSelectedProperty.mouseDragged(mPane, mClickPoint, newPoint);
			return;
		}
		if (selectionRectangle != null)
		{
			int x0 = (int)(Math.min(mClickPoint.x, newPoint.x) * mPane.getScale());
			int y0 = (int)(Math.min(mClickPoint.y, newPoint.y) * mPane.getScale());
			int x1 = (int)(Math.max(mClickPoint.x, newPoint.x) * mPane.getScale());
			int y1 = (int)(Math.max(mClickPoint.y, newPoint.y) * mPane.getScale());

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
			else if (mPane.getDragConnector() != null)
			{
				mPane.setDragEndLocation(mClickPoint);

				Connector connector = mPane.findNearestConnector(mPane.getDragEndLocation());
				if (connector != null && mPane.getDragConnector().getDirection() != connector.getDirection())
				{
					mPane.setDragEndLocation(connector.getConnectorPoint());
				}
			}
			else if (mClickedBox || SwingUtilities.isRightMouseButton(aEvent))
			{
				for (Node box : (ArrayList<Node>)mPane.getSelectedBoxes())
				{
					Point pt = box.getBounds().getLocation();
					pt.x += mClickPoint.x - oldPoint.x;
					pt.y += mClickPoint.y - oldPoint.y;
					box.setLocation(pt.x, pt.y);
				}
			}
		}

		mPane.repaint();
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent aEvent)
	{
		Popup popup = mPane.getPopup();

		if (popup != null)
		{
			popup.mouseWheelMoved(aEvent);
			return;
		}

		Point2D.Double scroll = mPane.getPaneScroll();

		scroll.x -= aEvent.getX();
		scroll.y -= aEvent.getY();

		if (aEvent.getWheelRotation() == 1)
		{
			mPane.setScale(mPane.getScale() * mZoomSpeed);
			scroll.x *= mZoomSpeed;
			scroll.y *= mZoomSpeed;
		}
		else
		{
			mPane.setScale(mPane.getScale() / mZoomSpeed);
			scroll.x /= mZoomSpeed;
			scroll.y /= mZoomSpeed;
		}

		scroll.x += aEvent.getX();
		scroll.y += aEvent.getY();

		mPane.repaint();
	}


	private void updateSelections(MouseEvent aEvent, Node aClickedBox)
	{
		NodeModel model = mPane.getModel();
		Node newSelection = null;
		Node newClickedBox = null;

		if (aClickedBox != null)
		{
			Rectangle shrunkBounds = new Rectangle(aClickedBox.getBounds());
			shrunkBounds.grow(-5, -4);

			if (shrunkBounds.contains(mClickPoint))
			{
				newClickedBox = aClickedBox;

				boolean b = mPane.getSelectedBoxes().contains(aClickedBox);
				if (aEvent.isControlDown())
				{
					if (b)
					{
						mPane.getSelectedBoxes().remove(aClickedBox);
					}
					else
					{
						newSelection = aClickedBox;
					}
				}
				else if (!b)
				{
					mPane.getSelectedBoxes().clear();
					newSelection = aClickedBox;
				}
			}
		}

		mClickedBox = newClickedBox != null;

		if (newSelection != null)
		{
			mPane.getSelectedBoxes().add(newSelection);
		}

		if (mClickedBox)
		{
			mPane.getModel().getComponents().remove(newClickedBox);
			mPane.getModel().getComponents().add(newClickedBox);
			mPane.setSelectedConnection(null);
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
				mPane.setSelectedConnection(mPane.getSelectedConnection());
				mPane.getSelectedBoxes().clear();
			}
		}

		mPane.repaint();
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
		mPane.repaint();
	}
}