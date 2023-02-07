package org.terifan.boxcomponentpane;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import static org.terifan.nodeeditor.Styles.MIN_HEIGHT;
import static org.terifan.nodeeditor.Styles.MIN_WIDTH;


public class BoxComponentMouseListener<T extends BoxComponent, U extends BoxComponentPane> extends MouseAdapter
{
	protected U mPane;
	protected Point mClickPoint;
	protected Point mDragPoint;
	protected boolean mClickedBox;
	protected T mHoverNode;
	protected Rectangle mStartBounds;
	protected double mZoomSpeed;
	protected int mCursor;


	public BoxComponentMouseListener(U aPane)
	{
		mZoomSpeed = 1.1;
		mCursor = Cursor.DEFAULT_CURSOR;
		mPane = aPane;
	}


	@Override
	public void mouseMoved(MouseEvent aEvent)
	{
		Point point = mPane.calcMousePoint(aEvent.getPoint());

		BoxComponentModel model = mPane.getModel();

		List<T> nodes = model.getComponents();
		for (int i = nodes.size(); --i >= 0; )
		{
			T node = nodes.get(i);
			Rectangle bounds = node.getBounds();
			if (!node.isMinimized() && bounds.contains(point))
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
		BoxComponentModel mModel = mPane.getModel();

		boolean left = SwingUtilities.isLeftMouseButton(aEvent);

		mDragPoint = aEvent.getPoint();
		mClickPoint = mPane.calcMousePoint(aEvent.getPoint());

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
			mStartBounds = new Rectangle(mHoverNode.getBounds());

			mPane.getModel().getComponents().remove(mHoverNode);
			mPane.getModel().getComponents().add(mHoverNode);

			mPane.getSelectedBoxes().clear();
			mPane.getSelectedBoxes().add(mHoverNode);
			mPane.repaint();

			return;
		}

		T clickedBox = null;

		for (T box : (ArrayList<T>)mModel.getComponents())
		{
			Rectangle b = box.getBounds();

			if (b.contains(mClickPoint))
			{
				clickedBox = box;

				if (new Rectangle(b.x + 11, b.y + 7, 14, 16).contains(mClickPoint))
				{
					box.setMinimized(!box.isMinimized());
					break;
				}
			}
		}

		if (clickedBox == null)
		{
			mPane.setSelectionRectangle(new Rectangle(mClickPoint));
		}

		updateSelections(aEvent, clickedBox);
	}


	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
		mClickPoint = mPane.calcMousePoint(aEvent.getPoint());

		if (mCursor != Cursor.DEFAULT_CURSOR)
		{
			updateCursor(Cursor.DEFAULT_CURSOR);
			return;
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

			for (BoxComponent box : (List<BoxComponent>)mPane.getModel().getComponents())
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
			mPane.repaint();
		}
	}


	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		Rectangle selectionRectangle = mPane.getSelectionRectangle();

		Point newPoint = mPane.calcMousePoint(aEvent.getPoint());

		if (mCursor != Cursor.DEFAULT_CURSOR && SwingUtilities.isLeftMouseButton(aEvent))
		{
			resizeBox(mHoverNode, newPoint);
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
				Point2D.Double scroll = mPane.getPaneScroll();
				scroll.x += (aEvent.getX() - mDragPoint.x);
				scroll.y += (aEvent.getY() - mDragPoint.y);
				mDragPoint = aEvent.getPoint();
			}
			else if (mClickedBox || SwingUtilities.isRightMouseButton(aEvent))
			{
				for (T box : (ArrayList<T>)mPane.getSelectedBoxes())
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
		Point2D.Double scroll = mPane.getPaneScroll();

		scroll.x -= aEvent.getX();
		scroll.y -= aEvent.getY();

		double d = mZoomSpeed;
		if (aEvent.getWheelRotation() == 1)
		{
			mPane.setScale(mPane.getScale() * d);
			scroll.x *= d;
			scroll.y *= d;
		}
		else
		{
			mPane.setScale(mPane.getScale() / d);
			scroll.x /= d;
			scroll.y /= d;
		}

		scroll.x += aEvent.getX();
		scroll.y += aEvent.getY();

		mPane.repaint();
	}


	private void updateSelections(MouseEvent aEvent, BoxComponent aClickedBox)
	{
		BoxComponent newSelection = null;
		BoxComponent clickedBox = null;

		if (aClickedBox != null)
		{
			Rectangle shrunkBounds = new Rectangle(aClickedBox.getBounds());
			shrunkBounds.grow(-5, -4);

			if (shrunkBounds.contains(mClickPoint))
			{
				clickedBox = aClickedBox;

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

		mClickedBox = clickedBox != null;

		if (newSelection != null)
		{
			mPane.getSelectedBoxes().add(newSelection);
		}

		if (mClickedBox)
		{
			mPane.getModel().getComponents().remove(clickedBox);
			mPane.getModel().getComponents().add(clickedBox);
		}

		mPane.repaint();
	}


	protected void updateCursor(int aCursor)
	{
		if (mCursor != aCursor)
		{
			mCursor = aCursor;
			SwingUtilities.invokeLater(() -> mPane.setCursor(Cursor.getPredefinedCursor(aCursor < -1 ? Cursor.DEFAULT_CURSOR : aCursor)));
		}
	}


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


	private void resizeBox(T aBox, Point aPoint)
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