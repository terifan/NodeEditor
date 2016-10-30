package org.terifan.ui.relationeditor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


class RelationBoxMouseListener extends MouseAdapter
{
	private AbstractRelationBox mRelationBox;


	public RelationBoxMouseListener(AbstractRelationBox aRelationBox)
	{
		mRelationBox = aRelationBox;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		RelationEditorPane.findEditor(mRelationBox).setSelectedComponent(mRelationBox, true);
		
		aEvent.getComponent().requestFocusInWindow();
	}
}
