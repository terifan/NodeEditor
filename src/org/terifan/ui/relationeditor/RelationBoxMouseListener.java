package org.terifan.ui.relationeditor;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;


class RelationBoxMouseListener extends MouseAdapter
{
	private RelationBox mRelationBox;
	private Point mClickPoint;


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
	public void mouseDragged(MouseEvent aEvent)
	{
		Point point = SwingUtilities.convertPoint(mRelationBox, new Point(), mRelationBox.getParent());

		mRelationBox.setLocation(point.x + aEvent.getX() - mClickPoint.x, point.y + aEvent.getY() - mClickPoint.y);
		mRelationBox.getParent().repaint();
	}
}
