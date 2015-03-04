package org.terifan.ui.relationeditor;

import java.awt.Color;
import org.terifan.ui.DragAndDrop;
import java.awt.Point;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


public class DefaultRelationItem implements RelationItem
{
	private UUID mIdentity;
	private JComponent mComponent;


	public DefaultRelationItem(String aText)
	{
		mIdentity = UUID.randomUUID();

		mComponent = new JLabel(aText);
		mComponent.setBackground(new Color(48,48,48));
		mComponent.setForeground(Color.WHITE);
		mComponent.setOpaque(true);
		mComponent.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

		mComponent.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent aEvent)
			{
				RelationEditor.findEditor(mComponent).setSelectedComponent(mComponent);
			}
		});

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
