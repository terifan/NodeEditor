package org.terifan.ui.relationeditor;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;


class RelationBoxMouseListener extends MouseAdapter
{
	private final static int[] CURSORS = {
		Cursor.DEFAULT_CURSOR, Cursor.W_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.E_RESIZE_CURSOR,
		Cursor.DEFAULT_CURSOR, Cursor.NE_RESIZE_CURSOR, Cursor.DEFAULT_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR,
		Cursor.DEFAULT_CURSOR, Cursor.DEFAULT_CURSOR, Cursor.SE_RESIZE_CURSOR};

	private RelationBox mRelationBox;
	private Point mClickPoint;
	private int mCursor;
	private boolean mDragged;


	public RelationBoxMouseListener(RelationBox aRelationBox)
	{
		mRelationBox = aRelationBox;
	}


	@Override
	public void mouseClicked(MouseEvent aEvent)
	{
		Point point = aEvent.getPoint();

		Component componentAt = mRelationBox;

		if (point.x > componentAt.getWidth() - 4 - 16 && point.x < componentAt.getWidth() - 4 && point.y >= 4 && point.y < 4 + 16)
		{
			RelationBox box = (RelationBox)componentAt;
			box.setMinimized(!box.isMinimized());
			componentAt.getParent().repaint();
		}
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		mClickPoint = aEvent.getPoint();

		((RelationEditor)mRelationBox.getParent()).setSelectedComponent(mRelationBox);
	}


	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
		mDragged = false;
		updateCursor(getCursor(aEvent));
	}

int w;
	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		if (!mDragged)
		{
			w = mRelationBox.getWidth() - aEvent.getX();
		}
		mDragged = true;

		if (mCursor == Cursor.E_RESIZE_CURSOR)
		{
			mRelationBox.setSize(w + aEvent.getX(), mRelationBox.getHeight());

			mRelationBox.invalidate();
			mRelationBox.validate();
			mRelationBox.getParent().repaint();
		}
		else
		{
			Point point = SwingUtilities.convertPoint(mRelationBox, new Point(), mRelationBox.getParent());

			mRelationBox.setLocation(point.x + aEvent.getX() - mClickPoint.x, point.y + aEvent.getY() - mClickPoint.y);

			mRelationBox.getParent().repaint();
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
		if (!mDragged)
		{
			if (mCursor != aCursor)
			{
				mCursor = aCursor;
				mRelationBox.setCursor(Cursor.getPredefinedCursor(mCursor));
			}
		}
	}


	private int getCursor(MouseEvent aEvent)
	{
		int x = aEvent.getX();
		int y = aEvent.getY();
		int S = 4;

		return CURSORS[(x < S ? 1 : 0) + (y < S ? 2 : 0) + (x >= mRelationBox.getWidth() - S ? 4 : 0) + (y >= mRelationBox.getHeight() - S ? 8 : 0)];
	}
}
