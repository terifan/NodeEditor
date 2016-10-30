package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;


public abstract class AbstractRelationBox extends ResizablePanel implements RelationBox
{
	protected ArrayList<RelationItem> mRelationItems;
	protected RelationItem mEditedItem;
	protected Component mEditorComponent;
	protected JPanel mContainer;
	protected JScrollPane mContainerScrollPane;


	public AbstractRelationBox(Rectangle aBounds, String aTitle)
	{
		super(aBounds, aTitle);

		mContainer = new JPanel();
		mContainer.setForeground(Styles.BOX_FOREGROUND_COLOR);
		mContainer.setBackground(Styles.BOX_BACKGROUND_COLOR);

		mContainerScrollPane = new JScrollPane(mContainer);
		mContainerScrollPane.setBorder(null);

		mRelationItems = new ArrayList<>();

		mContainerScrollPane.getVerticalScrollBar().addAdjustmentListener(aEvent ->
		{
			RelationEditorPane editor = (RelationEditorPane)SwingUtilities.getAncestorOfClass(RelationEditorPane.class, AbstractRelationBox.this);
			editor.invalidate();
			editor.repaint();
		});

		super.add(mContainerScrollPane);
		super.addMouseListener(new RelationBoxMouseListener(this));
		super.setForeground(Styles.BOX_FOREGROUND_COLOR);
		super.setBackground(Styles.BOX_BACKGROUND_COLOR);
		super.setOpaque(false);
	}


	@Override
	public Component add(Component aComp)
	{
		if (aComp instanceof RelationItem)
		{
			throw new IllegalArgumentException("Use addItem method to add items to a box.");
		}
		return super.add(aComp);
	}


	public JPanel getContainer()
	{
		return mContainer;
	}


	public void removeItem(RelationItem aItem)
	{
		if (mEditedItem == aItem)
		{
			cancelEditItem();
		}

		mRelationItems.remove(aItem);

		super.remove(aItem.getComponent());
		super.validate();

		Container ancestor = SwingUtilities.getAncestorOfClass(RelationEditorPane.class, this);
		if (ancestor != null)
		{
			ancestor.repaint();
		}
	}


	@Override
	public RelationItem getRelationItem(int aIndex)
	{
		return mRelationItems.get(aIndex);
	}


	@Override
	public int getRelationItemCount()
	{
		return mRelationItems.size();
	}


	@Override
	protected void fireSelectedEvent()
	{
		RelationEditorPane.findEditor(this).setSelectedComponent(this);
	}


	@Override
	public void onSelectionChanged(RelationEditorPane aRelationEditor, boolean aSelected)
	{
		if (aSelected)
		{
			setBackground(Styles.BOX_BACKGROUND_SELECTED_COLOR);
			setForeground(Styles.BOX_FOREGROUND_SELECTED_COLOR);
		}
		else
		{
			setBackground(Styles.BOX_BACKGROUND_COLOR);
			setForeground(Styles.BOX_FOREGROUND_COLOR);
		}
		setOpaque(false);
	}


	@Override
	public RelationItem getItemByComponent(Component aComponent)
	{
		for (RelationItem item : mRelationItems)
		{
			if (item.getComponent() == aComponent)
			{
				return item;
			}
		}

		return null;
	}


	@Override
	public void startEditItem(RelationItem aItem)
	{
		if (mEditedItem != null)
		{
			cancelEditItem();
		}

		mEditedItem = aItem;
		mEditorComponent = aItem.getEditorComponent();

		int index = getComponentIndex(aItem.getComponent());

		mContainer.add(mEditorComponent, index);

		mContainer.remove(aItem.getComponent());

		mEditorComponent.setBounds(aItem.getComponent().getBounds());

		aItem.getComponent().setVisible(false);
	}


	@Override
	public void finishEditItem()
	{
		if (mEditedItem != null)
		{
			mEditedItem.updateValue(mEditorComponent);

			cancelEditItem();
		}
	}


	@Override
	public void cancelEditItem()
	{
		if (mEditorComponent != null)
		{
			int index = getComponentIndex(mEditorComponent);

			if (index == -1)
			{
				return;
			}

			mContainer.add(mEditedItem.getComponent(), index);

			mContainer.remove(mEditorComponent);

			mEditedItem.getComponent().setVisible(true);

			mEditorComponent = null;
			mEditedItem = null;
		}
	}


	protected int getComponentIndex(Component aComponent)
	{
		Container c = aComponent.getParent();

		for (int i = 0; i < c.getComponentCount(); i++)
		{
			if (c.getComponent(i) == aComponent)
			{
				return i;
			}
		}

		return -1;
	}


	@Override
	public void drawAnchors(Graphics2D aGraphics)
	{
		for (int i = 0; i < getRelationItemCount(); i++)
		{
			RelationItem relationItem = getRelationItem(i);
			Anchor[] anchors = getConnectionAnchors(relationItem);

			if (anchors != null)
			{
				for (Anchor anchor : anchors)
				{
					Rectangle r = new Rectangle(anchor.getBounds());
					aGraphics.setColor(new Color(0xC7C729));
					aGraphics.fillOval(r.x+1,r.y+1,r.width-2,r.height-2);
					aGraphics.setColor(Color.BLACK);
					aGraphics.drawOval(r.x,r.y,r.width-1,r.height-1);
				}
			}
		}
	}


	@Override
	public Anchor[] getConnectionAnchors(RelationItem aRelationItem)
	{
		Insets borderInsets = getBorder().getBorderInsets(this);
		Rectangle bounds = getBounds();

		if (isMinimized() || aRelationItem == null)
		{
			int titleHeight = getInsets().top;
			int x0 = bounds.x;
			int y0 = bounds.y;

			return new Anchor[]
			{
				new Anchor(new Rectangle(x0                - 1, y0, 0, titleHeight), Anchor.LEFT),
				new Anchor(new Rectangle(x0 + bounds.width + 1, y0, 0, titleHeight), Anchor.RIGHT)
			};
		}

		int index = mRelationItems.indexOf(aRelationItem);

		if (index == -1)
		{
			return null;
		}
		
		return getConnectionAnchorsImpl(index, aRelationItem, bounds, borderInsets);
	}


	protected abstract Anchor[] getConnectionAnchorsImpl(int aRelationItemIndex, RelationItem aRelationItem, Rectangle aBounds, Insets aBorderInsets);
}
