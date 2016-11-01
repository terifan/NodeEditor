package org.terifan.nodeeditor;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;


class ResizablePanelMouseListener extends MouseAdapter
{
	private final static int[] CURSORS = {
		Cursor.DEFAULT_CURSOR, Cursor.W_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.E_RESIZE_CURSOR,
		Cursor.DEFAULT_CURSOR, Cursor.NE_RESIZE_CURSOR, Cursor.DEFAULT_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR,
		Cursor.DEFAULT_CURSOR, Cursor.DEFAULT_CURSOR, Cursor.SE_RESIZE_CURSOR};

	private final static int DRAG_BOX = -10;

	private ResizablePanel mPanel;
	private Point mClickPoint;
	private boolean mDragged;
	private int mCursor;

	private Point mStartPos;
	private int mStartWidth;
	private int mStartHeight;


	public ResizablePanelMouseListener(ResizablePanel aPanel)
	{
		mPanel = aPanel;
	}


	@Override
	public void mouseClicked(MouseEvent aEvent)
	{
		mPanel.mouseClicked(aEvent);
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		mDragged = !mPanel.isMinimized();
		mClickPoint = aEvent.getPoint();
		mStartPos = mPanel.getLocation();
		mStartWidth = mPanel.getWidth();
		mStartHeight = mPanel.getHeight();

		Container parent = mPanel.getParent();
		parent.setComponentZOrder(mPanel, 0);
		parent.repaint();

		mPanel.fireSelectedEvent();
	}


	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
		mDragged = false;
		updateCursor(getCursor(aEvent));
	}


	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		if (mCursor == DRAG_BOX)
		{
			Point point = SwingUtilities.convertPoint(mPanel, new Point(), mPanel.getParent());

			mPanel.setLocation(point.x + aEvent.getX() - mClickPoint.x, point.y + aEvent.getY() - mClickPoint.y);

			mPanel.getParent().repaint();
		}
		else if (mDragged)
		{
			Rectangle b = mPanel.getBounds();

			Point point = SwingUtilities.convertPoint(mPanel, aEvent.getPoint(), mPanel.getParent());

			switch (mCursor)
			{
				case Cursor.W_RESIZE_CURSOR:
					b.width -= point.x - b.x;
					b.x = Math.min(point.x, mStartPos.x + mStartWidth - mPanel.getMinWidth());
					break;
				case Cursor.N_RESIZE_CURSOR:
					b.height -= point.y - b.y;
					b.y = Math.min(point.y, mStartPos.y + mStartHeight - mPanel.getMinHeight());
					break;
				case Cursor.NW_RESIZE_CURSOR:
					b.width -= point.x - b.x;
					b.x = point.x;
					b.height -= point.y - b.y;
					b.y = point.y;
					break;
				case Cursor.SW_RESIZE_CURSOR:
					b.width -= point.x - b.x;
					b.x = point.x;
					b.height = mStartHeight - mClickPoint.y + aEvent.getY();
					break;
				case Cursor.NE_RESIZE_CURSOR:
					b.width = mStartWidth - mClickPoint.x + aEvent.getX();
					b.height -= point.y - b.y;
					b.y = point.y;
					break;
				case Cursor.E_RESIZE_CURSOR:
					b.width = mStartWidth - mClickPoint.x + aEvent.getX();
					break;
				case Cursor.S_RESIZE_CURSOR:
					b.height = mStartHeight - mClickPoint.y + aEvent.getY();
					break;
				case Cursor.SE_RESIZE_CURSOR:
					b.width = mStartWidth - mClickPoint.x + aEvent.getX();
					b.height = mStartHeight - mClickPoint.y + aEvent.getY();
					break;
				default:
					break;
			}

			b.width = Math.max(mPanel.getMinWidth(), b.width);
			b.height = Math.max(mPanel.getMinHeight(), b.height);

			mPanel.setBounds(b);
			mPanel.invalidate();
			mPanel.validate();
			mPanel.getParent().repaint();
		}
	}


	@Override
	public void mouseMoved(MouseEvent aEvent)
	{
		updateCursor(getCursor(aEvent));
	}


	@Override
	public void mouseExited(MouseEvent aEvent)
	{
		updateCursor(Cursor.DEFAULT_CURSOR);
	}


	protected void updateCursor(int aCursor)
	{
		if (!mDragged && !mPanel.isMinimized() && mCursor != aCursor)
		{
			SwingUtilities.invokeLater(()->{
				mPanel.setCursor(Cursor.getPredefinedCursor(aCursor < -1 ? Cursor.DEFAULT_CURSOR : aCursor));
				mCursor = aCursor;
			});
		}
	}


	private int getCursor(MouseEvent aEvent)
	{
		int x = aEvent.getX();
		int y = aEvent.getY();
		int SX = 8;
		int SY = 4;

		if (x > SX && x < mPanel.getWidth() - SX && y >= SY && y <= SY + Styles.TITLE_HEIGHT - 6)
		{
			return DRAG_BOX;
		}

		int lx = mPanel.isResizableHorizontal() ? 1 : 0;
		int ly = mPanel.isResizableVertical() ? 1 : 0;
		
		return CURSORS[lx * (x < SX ? 1 : 0) + ly * (y < SY ? 2 : 0) + lx * (x >= mPanel.getWidth() - SX ? 4 : 0) + ly * (y >= mPanel.getHeight() - SY ? 8 : 0)];
	}
}
