package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.terifan.ui.resizablepanel.ResizablePanel;


public abstract class AbstractRelationBox extends ResizablePanel implements RelationBox
{
	protected final static Color BACKGROUND_COLOR = new Color(68, 68, 68);
	protected final static Color BACKGROUND_SELECTED_COLOR = new Color(200, 200, 200);

	protected ArrayList<RelationItem> mRelationItems;


	public AbstractRelationBox(Rectangle aBounds)
	{
		super(aBounds);

		mRelationItems = new ArrayList<>();
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
		RelationEditor.findEditor(this).setSelectedComponent(this);
	}


	@Override
	public void onSelectionChanged(RelationEditor aRelationEditor, boolean aSelected)
	{
		if (aSelected)
		{
			setBackground(BACKGROUND_SELECTED_COLOR);
		}
		else
		{
			setBackground(BACKGROUND_COLOR);
		}
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		aGraphics.setColor(getBackground());
		aGraphics.fillRect(0, 0, getWidth(), getHeight());

		super.paintComponent(aGraphics);
	}
}
