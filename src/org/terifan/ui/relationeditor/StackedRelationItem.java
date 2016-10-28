package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Component;
import org.terifan.ui.DragAndDrop;
import java.awt.Point;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;


public class StackedRelationItem extends AbstractRelationItem
{
	private final static Color BACKGROUND_COLOR = new Color(48,48,48);
	private final static Color BACKGROUND_SELECTED_COLOR = new Color(128, 0, 0);

	public enum Anchors
	{
		NONE,
		LEFT,
		RIGHT,
		BOTH
	}

	private Anchors mAnchors;
	private int mHeight;
	private double mWeight;

	protected JComponent mComponent;


	public StackedRelationItem(String aText, int aHeight, Anchors aAnchors)
	{
		this(aText, aHeight, aAnchors, 0);
	}
	
	
	public StackedRelationItem(String aText, int aHeight, Anchors aAnchors, double aWeight)
	{
		mComponent = new JLabel(aText);

		mAnchors = aAnchors;
		mHeight = aHeight;
		mWeight = aWeight;

		mComponent.setFocusable(true);
		mComponent.setBackground(BACKGROUND_COLOR);
		mComponent.setForeground(Color.WHITE);
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
			mComponent.setBackground(BACKGROUND_SELECTED_COLOR);
		}
		else
		{
			mComponent.setBackground(BACKGROUND_COLOR);
		}
	}


	public Anchors getAnchors()
	{
		return mAnchors;
	}


	public void setAnchors(Anchors aAnchors)
	{
		this.mAnchors = aAnchors;
	}


	public int getHeight()
	{
		return mHeight;
	}


	public void setHeight(int aHeight)
	{
		this.mHeight = aHeight;
	}


	public double getWeight()
	{
		return mWeight;
	}


	public void setWeight(double aWeight)
	{
		this.mWeight = aWeight;
	}
}
