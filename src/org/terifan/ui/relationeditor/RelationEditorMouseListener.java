package org.terifan.ui.relationeditor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import org.terifan.util.log.Log;


class RelationEditorMouseListener extends MouseAdapter implements MouseMotionListener
{
	private RelationEditor mRelationEditor;
	private Point mClickPoint;
	private boolean mClick;


	public RelationEditorMouseListener(RelationEditor aRelationEditor)
	{
		mRelationEditor = aRelationEditor;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		Point point = aEvent.getPoint();
		mClick = true;

		JComponent componentAt = (JComponent)mRelationEditor.getComponentAt(point);

		if (componentAt instanceof RelationBox)
		{
			Rectangle bb = componentAt.getBounds();

			point.translate(-bb.x, -bb.y);
			mRelationEditor.setSelectedComponent(componentAt);
			mClickPoint = point;
		}
	}


	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		if (mRelationEditor.getSelectedComponent() != null)
		{
			mClick = false;
			mRelationEditor.getSelectedComponent().setLocation(aEvent.getX() - mClickPoint.x, aEvent.getY() - mClickPoint.y);
			mRelationEditor.repaint();
		}
	}


	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
//		if (mClick)
//		{
//			Rectangle b = mRelationEditor.getSelectedComponent().getMinimizeButtonBounds();
//
//			if (b.contains(aEvent.getPoint()))
//			{
//				mRelationEditor.getSelectedComponent().setMinimized(!mRelationEditor.getSelectedBox().isMinimized());
//				mRelationEditor.repaint();
//			}
//		}

		mRelationEditor.setSelectedComponent(null);
	}
}
