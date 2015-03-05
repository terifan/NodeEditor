package org.terifan.ui.relationeditor;

import java.awt.Color;
import org.terifan.ui.DragAndDrop;
import java.awt.Point;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.terifan.util.log.Log;


public class DefaultRelationItem implements RelationItem
{
	private Color BACKGROUND_COLOR = new Color(48,48,48);
	private final static Color BACKGROUND_SELECTED_COLOR = new Color(128, 0, 0);

	private UUID mIdentity;
	private JComponent mComponent;


	public DefaultRelationItem(String aText)
	{
		mIdentity = UUID.randomUUID();

		mComponent = new JLabel(aText);
		mComponent.setBackground(BACKGROUND_COLOR);
		mComponent.setForeground(Color.WHITE);
		mComponent.setOpaque(true);
		mComponent.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		mComponent.addMouseListener(new RelationItemMouseListener());

		new DragAndDrop(mComponent)
		{
			@Override
			public Object drag(Point aDragOrigin)
			{
				return mIdentity;
			}

			@Override
			public boolean canDrop(DropEvent aDropEvent)
			{
				return true;
			}

			@Override
			public void drop(DropEvent aDropEvent)
			{
				RelationEditor editor = RelationEditor.findEditor(mComponent);
				RelationItem relatedItem = editor.findRelationItem(aDropEvent.getTransferData(UUID.class));
				if (relatedItem != null)
				{
					editor.addConnection(relatedItem, DefaultRelationItem.this);
					editor.repaint();
				}
			}
		};
	}


	@Override
	public JComponent getComponent()
	{
		return mComponent;
	}


	@Override
	public UUID getIdentity()
	{
		return mIdentity;
	}


	@Override
	public void onSelectionChanged(RelationEditor aRelationEditor, RelationBox aRelationBox, boolean aSelected)
	{
		if (aSelected)
		{
			mComponent.setBackground(BACKGROUND_SELECTED_COLOR);
		}
		else
		{
			mComponent.setBackground(BACKGROUND_COLOR);
		}
	}
}
