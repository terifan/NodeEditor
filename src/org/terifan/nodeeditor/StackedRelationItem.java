package org.terifan.nodeeditor;

import java.awt.Component;
import org.terifan.ui.DragAndDrop;
import java.awt.Point;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;


public class StackedRelationItem extends AbstractRelationItem
{
	private Direction mDirection;
	private int mHeight;
	private double mWeight;

	protected JComponent mComponent;


	public StackedRelationItem(String aText, int aHeight, Direction aDirection)
	{
		this(aText, aHeight, aDirection, 0);
	}


	public StackedRelationItem(String aText, int aHeight, Direction aDirection, double aWeight)
	{
		mComponent = new JLabel(aText);

		mDirection = aDirection;
		mHeight = aHeight;
		mWeight = aWeight;

		mComponent.setFocusable(true);
		mComponent.setBackground(Styles.BOX_BACKGROUND_COLOR);
		mComponent.setForeground(Styles.BOX_FOREGROUND_COLOR);
		mComponent.setOpaque(true);
		mComponent.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		mComponent.addMouseListener(new RelationItemMouseListener());
		mComponent.addKeyListener(new RelationItemKeyListener());

		new DragAndDrop(mComponent)
		{
			@Override
			public Object drag(Point aDragOrigin)
			{
				return getIdentity();
			}

			@Override
			public boolean canDrop(DropEvent aDropEvent)
			{
				return true;
			}

			@Override
			public void drop(DropEvent aDropEvent)
			{
				RelationEditorPane editor = RelationEditorPane.findEditor(mComponent);
				RelationItem relatedItem = editor.findRelationItem(aDropEvent.getTransferData(UUID.class));
				if (relatedItem != null)
				{
					editor.addConnection(relatedItem, StackedRelationItem.this);
					editor.repaint();
				}
			}
		};
	}


	@Override
	public Component getComponent()
	{
		return mComponent;
	}


	@Override
	public Component getEditorComponent()
	{
		return null;
	}


	@Override
	public void updateValue(Component aComponent)
	{
	}


	@Override
	public void onSelectionChanged(RelationEditorPane aRelationEditor, RelationBox aRelationBox, boolean aSelected)
	{
		if (aSelected)
		{
			mComponent.setBackground(Styles.ITEM_BACKGROUND_SELECTED_COLOR);
		}
		else
		{
			mComponent.setBackground(Styles.ITEM_BACKGROUND_COLOR);
		}
	}


	public Direction getDirection()
	{
		return mDirection;
	}


	public void setDirection(Direction aDirection)
	{
		mDirection = aDirection;
	}


	public int getHeight()
	{
		return mHeight;
	}


	public void setHeight(int aHeight)
	{
		mHeight = aHeight;
	}


	public double getWeight()
	{
		return mWeight;
	}


	public void setWeight(double aWeight)
	{
		mWeight = aWeight;
	}
}
