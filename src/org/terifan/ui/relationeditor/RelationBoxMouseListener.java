package org.terifan.ui.relationeditor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


class RelationBoxMouseListener extends MouseAdapter
{
	private final RelationBox mRelationBox;


	public RelationBoxMouseListener(RelationBox aRelationBox)
	{
		mRelationBox = aRelationBox;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		aEvent.getComponent().requestFocusInWindow();
	}
}
