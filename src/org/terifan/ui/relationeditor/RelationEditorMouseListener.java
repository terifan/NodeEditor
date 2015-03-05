package org.terifan.ui.relationeditor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.terifan.util.log.Log;


class RelationEditorMouseListener extends MouseAdapter
{
	private RelationEditor mRelationEditor;


	RelationEditorMouseListener(RelationEditor aRelationEditor)
	{
		mRelationEditor = aRelationEditor;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		double dist = Integer.MAX_VALUE;
		Connection conn = null;

		for (Connection connection : mRelationEditor.getConnections())
		{
			Anchor[] anchors = mRelationEditor.findConnectionAnchors(connection);

			double d = mRelationEditor.getConnectionRenderer().distance(connection, anchors[0], anchors[1], aEvent.getX(), aEvent.getY());

			if (d < dist)
			{
				dist = d;
				conn = connection;
			}
		}

		if (conn != null)
		{
			mRelationEditor.setSelectedComponent(conn);
			mRelationEditor.repaint();
		}
	}
}
