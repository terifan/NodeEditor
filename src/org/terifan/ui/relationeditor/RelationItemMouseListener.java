package org.terifan.ui.relationeditor;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class RelationItemMouseListener extends MouseAdapter
{
	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		Component comp = aEvent.getComponent();

		comp.requestFocusInWindow();

		RelationEditor editor = RelationEditor.findEditor(comp);

		editor.setSelectedComponent(comp);

		editor.fireRelationBoxClicked(RelationEditor.findRelationBox(comp));
	}
}
