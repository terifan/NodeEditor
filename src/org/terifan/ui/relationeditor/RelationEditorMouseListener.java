package org.terifan.ui.relationeditor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


class RelationEditorMouseListener extends MouseAdapter
{
	private RelationEditorPane mRelationEditor;


	RelationEditorMouseListener(RelationEditorPane aRelationEditor)
	{
		mRelationEditor = aRelationEditor;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		mRelationEditor.requestFocus();

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

		if (conn != null)
		{
			mRelationEditor.setSelectedComponent(conn);
			mRelationEditor.repaint();
		}
	}
}
