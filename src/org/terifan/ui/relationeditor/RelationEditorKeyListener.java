package org.terifan.ui.relationeditor;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


class RelationEditorKeyListener extends KeyAdapter
{
	private final RelationEditor mEditor;


	public RelationEditorKeyListener(RelationEditor aEditor)
	{
		this.mEditor = aEditor;
	}


	@Override
	public void keyPressed(KeyEvent aEvent)
	{
		if (aEvent.getKeyCode() == KeyEvent.VK_DELETE && mEditor.getSelectedConnection() != null)
		{
			mEditor.removeConnection(mEditor.getSelectedConnection());
			mEditor.repaint();
		}
	}
}
