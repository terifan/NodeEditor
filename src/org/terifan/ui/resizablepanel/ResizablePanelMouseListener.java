package org.terifan.ui.resizablepanel;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import org.terifan.util.log.Log;


class RelationBoxMouseListener extends MouseAdapter
{
	private final static int[] CURSORS = {
		Cursor.DEFAULT_CURSOR, Cursor.W_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.E_RESIZE_CURSOR,
		Cursor.DEFAULT_CURSOR, Cursor.NE_RESIZE_CURSOR, Cursor.DEFAULT_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR,
		Cursor.DEFAULT_CURSOR, Cursor.DEFAULT_CURSOR, Cursor.SE_RESIZE_CURSOR};

	private ResizablePanel mPanel;
	private Point mClickPoint;
	private boolean mDragged;
	private int mCursor;

	private int mStartWidth;
	private int mStartHeight;


	public RelationBoxMouseListener(ResizablePanel aResizablePanel)
	{
		mPanel = aResizablePanel;
	}


	@Override
	public void mouseClicked(MouseEvent aEvent)
	{
		Point point = aEvent.getPoint();

		if (point.x > mPanel.getWidth() - 4 - 16 && point.x < mPanel.getWidth() - 4 && point.y >= 4 && point.y < 4 + 16)
		{
			mPanel.setMinimized(!mPanel.isMinimized());
		}
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		mDragged = !mPanel.isMinimized();
		mClickPoint = aEvent.getPoint();
		mStartWidth = mPanel.getWidth();
		mStartHeight = mPanel.getHeight();

		mPanel.getParent().setComponentZOrder(mPanel, 0);
		mPanel.getParent().repaint();

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
		if (mCursor == -10)
		{
			Point point = SwingUtilities.convertPoint(mPanel, new Point(), mPanel.getParent());

			mPanel.setLocation(point.x + aEvent.getX() - mClickPoint.x, point.y + aEvent.getY() - mClickPoint.y);

			mPanel.getParent().repaint();
		}
		else if (mDragged)
		{
			Rectangle b = mPanel.getBounds();

			Point point = SwingUtilities.convertPoint(mPanel, aEvent.getPoint(), mPanel.getParent());

			if (mCursor == Cursor.W_RESIZE_CURSOR)
			{
				b.width -= point.x - b.x;
				b.x = point.x;
			}
			else if (mCursor == Cursor.N_RESIZE_CURSOR)
			{
				b.height -= point.y - b.y;
				b.y = point.y;
			}
			else if (mCursor == Cursor.NW_RESIZE_CURSOR)
			{
				b.width -= point.x - b.x;
				b.x = point.x;
				b.height -= point.y - b.y;
				b.y = point.y;
			}
			else if (mCursor == Cursor.SW_RESIZE_CURSOR)
			{
				b.width -= point.x - b.x;
				b.x = point.x;
				b.height = mStartHeight - mClickPoint.y + aEvent.getY();
			}
			else if (mCursor == Cursor.NE_RESIZE_CURSOR)
			{
				b.width = mStartWidth - mClickPoint.x + aEvent.getX();
				b.height -= point.y - b.y;
				b.y = point.y;
			}
			else if (mCursor == Cursor.E_RESIZE_CURSOR)
			{
				b.width = mStartWidth - mClickPoint.x + aEvent.getX();
			}
			else if (mCursor == Cursor.S_RESIZE_CURSOR)
			{
				b.height = mStartHeight - mClickPoint.y + aEvent.getY();
			}
			else if (mCursor == Cursor.SE_RESIZE_CURSOR)
			{
				b.width = mStartWidth - mClickPoint.x + aEvent.getX();
				b.height = mStartHeight - mClickPoint.y + aEvent.getY();
			}

			b.width = Math.max(80, b.width);
			b.height = Math.max(4+16+4, b.height);

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
		int S = 4;

		if (x > S && x < mPanel.getWidth() - S && y > S && y <= S + 16)
		{
			return -10;
		}

		return CURSORS[(x < S ? 1 : 0) + (y < S ? 2 : 0) + (x >= mPanel.getWidth() - S ? 4 : 0) + (y >= mPanel.getHeight() - S ? 8 : 0)];
	}
}
