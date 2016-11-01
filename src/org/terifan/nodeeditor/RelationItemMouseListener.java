package org.terifan.nodeeditor;

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

		RelationEditorPane editor = RelationEditorPane.findEditor(comp);

		editor.setSelectedComponent(comp, false);

		editor.fireRelationBoxClicked(RelationEditorPane.findRelationBox(comp));
	}
}
