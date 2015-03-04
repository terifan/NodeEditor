package org.terifan.ui.relationeditor;

import java.awt.Color;
import org.terifan.ui.DragAndDrop;
import java.awt.Point;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


public class DefaultRelationItem implements RelationItem
{
	private String mText;
	private UUID mIdentity;
	private JComponent mComponent;


	public DefaultRelationItem(String aText)
	{
		mIdentity = UUID.randomUUID();
		mText = aText;

		mComponent = new JLabel(mText);
		mComponent.setBackground(new Color(48,48,48));
		mComponent.setForeground(Color.WHITE);
		mComponent.setOpaque(true);
		mComponent.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

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
				RelationEditor editor = (RelationEditor)SwingUtilities.getAncestorOfClass(RelationEditor.class, mComponent);
				RelationItem relatedItem = editor.findRelationItem((UUID)aDropEvent.getTransferData());
				if (relatedItem != null)
				{
					editor.addRelationship(relatedItem, DefaultRelationItem.this);
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
}
