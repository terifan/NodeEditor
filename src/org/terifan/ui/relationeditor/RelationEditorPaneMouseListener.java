package org.terifan.ui.relationeditor;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;


class RelationEditorPaneMouseListener extends MouseAdapter
{
	private static final Cursor MOVE_CURSOR = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
	private static final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	private RelationEditorPane mRelationEditor;
	private Point mLastLocation;
	private boolean mDragged;


	RelationEditorPaneMouseListener(RelationEditorPane aRelationEditor)
	{
		mRelationEditor = aRelationEditor;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		mRelationEditor.requestFocus();
		
		mLastLocation = aEvent.getPoint();

		if (SwingUtilities.isMiddleMouseButton(aEvent))
		{
			mDragged = true;
			SwingUtilities.invokeLater(()->mRelationEditor.setCursor(MOVE_CURSOR));
			
			return;
		}

		double dist = Integer.MAX_VALUE;
		Connection conn = null;

		for (Connection connection : mRelationEditor.getConnections())
		{
			Anchor[] anchors = mRelationEditor.findConnectionAnchors(connection);

			if (anchors != null)
			{
				double d = mRelationEditor.getConnectionRenderer().distance(connection, anchors[0], anchors[1], aEvent.getX(), aEvent.getY());

				if (d < dist)
				{
					dist = d;
					conn = connection;
				}
			}
		}

		if (dist > 10)
		{
			return;
		}
		
		if (conn != null)
		{
			mRelationEditor.setSelectedComponent(conn);
			mRelationEditor.repaint();
		}
	}


	@Override
	public void mouseReleased(MouseEvent aE)
	{
		if (mDragged)
		{
			SwingUtilities.invokeLater(()->mRelationEditor.setCursor(DEFAULT_CURSOR));
			mDragged = false;
		}
	}


	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		if (mDragged)
		{
			int x = aEvent.getX();
			int y = aEvent.getY();

			for (int i = 0; i < mRelationEditor.getComponentCount(); i++)
			{
				Component comp = mRelationEditor.getComponent(i);
				Point p = comp.getLocation();
				p.x += x - mLastLocation.x;
				p.y += y - mLastLocation.y;
				comp.setLocation(p);
			}

			mRelationEditor.repaint();
			mLastLocation.setLocation(x, y);
		}
	}
}
