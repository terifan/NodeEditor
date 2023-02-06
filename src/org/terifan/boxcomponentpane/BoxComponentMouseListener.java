package org.terifan.boxcomponentpane;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import static org.terifan.nodeeditor.Styles.MIN_HEIGHT;
import static org.terifan.nodeeditor.Styles.MIN_WIDTH;


class BoxComponentMouseListener<T extends BoxComponent> extends MouseAdapter
{
	private Point mClickPoint;
	private Point mDragPoint;
	private boolean mHitBox;
	private T mHoverBox;
	private Rectangle mStartBounds;
	private BoxComponentPane mBoxComponentPane;
	private int mCursor;
	private double mZoomSpeed;


	public BoxComponentMouseListener(BoxComponentPane aBoxComponentPane)
	{
		mZoomSpeed = 1.1;
		mCursor = Cursor.DEFAULT_CURSOR;
		mBoxComponentPane = aBoxComponentPane;
	}


	@Override
	public void mouseMoved(MouseEvent aEvent)
	{
		Point point = calcMousePoint(aEvent);

		BoxComponentModel mModel = mBoxComponentPane.getModel();

		ArrayList<T> nodes = mModel.getComponents();
		for (int i = nodes.size(); --i >= 0; )
		{
			T box = nodes.get(i);
			Rectangle b = box.getBounds();
			if (!box.isMinimized() && b.contains(point))
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
		BoxComponentModel mModel = mBoxComponentPane.getModel();

		boolean left = SwingUtilities.isLeftMouseButton(aEvent);

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

			mBoxComponentPane.mModel.getComponents().remove(mHoverBox);
			mBoxComponentPane.mModel.getComponents().add(mHoverBox);

			mBoxComponentPane.mSelectedBoxes.clear();
			mBoxComponentPane.mSelectedBoxes.add(mHoverBox);
			mBoxComponentPane.repaint();

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
					updateSelections(aEvent, clickedBox);
					return;
				}
			}
		}

		updateSelections(aEvent, clickedBox);
	}


	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
		mClickPoint = calcMousePoint(aEvent);

		if (mCursor != Cursor.DEFAULT_CURSOR)
		{
			updateCursor(Cursor.DEFAULT_CURSOR);
			return;
		}

		mBoxComponentPane.repaint();
	}


	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		Rectangle mSelectionRectangle = mBoxComponentPane.mSelectionRectangle;
		Point2D.Double scroll = mBoxComponentPane.mScroll;

		Point newPoint = calcMousePoint(aEvent);

		if (mCursor != Cursor.DEFAULT_CURSOR && SwingUtilities.isLeftMouseButton(aEvent))
		{
			resizeBox(mHoverBox, newPoint);
			return;
		}

		if (mSelectionRectangle != null)
		{
			int x0 = (int)(Math.min(mClickPoint.x, newPoint.x) * mBoxComponentPane.mScale);
			int y0 = (int)(Math.min(mClickPoint.y, newPoint.y) * mBoxComponentPane.mScale);
			int x1 = (int)(Math.max(mClickPoint.x, newPoint.x) * mBoxComponentPane.mScale);
			int y1 = (int)(Math.max(mClickPoint.y, newPoint.y) * mBoxComponentPane.mScale);

			mSelectionRectangle.setBounds(x0, y0, x1 - x0, y1 - y0);
		}
		else
		{
			Point oldPoint = mClickPoint;
			mClickPoint = newPoint;

			if (SwingUtilities.isMiddleMouseButton(aEvent))
			{
				scroll.x += (aEvent.getX() - mDragPoint.x);
				scroll.y += (aEvent.getY() - mDragPoint.y);
				mDragPoint = aEvent.getPoint();
			}
			else if (mHitBox || SwingUtilities.isRightMouseButton(aEvent))
			{
				for (T box : (ArrayList<T>)mBoxComponentPane.mSelectedBoxes)
				{
					Point pt = box.getBounds().getLocation();
					pt.x += mClickPoint.x - oldPoint.x;
					pt.y += mClickPoint.y - oldPoint.y;
					box.setLocation(pt.x, pt.y);
				}
			}
		}

		mBoxComponentPane.repaint();
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent aEvent)
	{
		Point2D.Double scroll = mBoxComponentPane.mScroll;

		scroll.x -= aEvent.getX();
		scroll.y -= aEvent.getY();

		double d = mZoomSpeed;
		if (aEvent.getWheelRotation() == 1)
		{
			mBoxComponentPane.mScale *= d;
			scroll.x *= d;
			scroll.y *= d;
		}
		else
		{
			mBoxComponentPane.mScale /= d;
			scroll.x /= d;
			scroll.y /= d;
		}

		scroll.x += aEvent.getX();
		scroll.y += aEvent.getY();

		mBoxComponentPane.repaint();
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

				boolean b = mBoxComponentPane.mSelectedBoxes.contains(aClickedBox);
				if (aEvent.isControlDown())
				{
					if (b)
					{
						mBoxComponentPane.mSelectedBoxes.remove(aClickedBox);
					}
					else
					{
						newSelection = aClickedBox;
					}
				}
				else if (!b)
				{
					mBoxComponentPane.mSelectedBoxes.clear();
					newSelection = aClickedBox;
				}
			}
		}

		mHitBox = clickedBox != null;

		if (newSelection != null)
		{
			mBoxComponentPane.mSelectedBoxes.add(newSelection);
		}

		if (mHitBox)
		{
			mBoxComponentPane.getModel().getComponents().remove(clickedBox);
			mBoxComponentPane.getModel().getComponents().add(clickedBox);
		}

		mBoxComponentPane.repaint();
	}


	protected void updateCursor(int aCursor)
	{
		if (mCursor != aCursor)
		{
			mCursor = aCursor;
			SwingUtilities.invokeLater(() -> mBoxComponentPane.setCursor(Cursor.getPredefinedCursor(aCursor < -1 ? Cursor.DEFAULT_CURSOR : aCursor)));
		}
	}


	private int getCursor(Point aPoint, T aNode)
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
		mBoxComponentPane.repaint();
	}


	private Point calcMousePoint(MouseEvent aEvent)
	{
		return new Point((int)((aEvent.getX() - mBoxComponentPane.mScroll.x) / mBoxComponentPane.mScale), (int)((aEvent.getY() - mBoxComponentPane.mScroll.y) / mBoxComponentPane.mScale));
	}
}