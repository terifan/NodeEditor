package org.terifan.ui.relationeditor;

import org.terifan.ui.resizablepanel.ResizablePanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.terifan.ui.ColumnLayout;


public class RelationListBox extends ResizablePanel implements RelationBox
{
	private final static Color BACKGROUND_COLOR = new Color(68, 68, 68);

	private ArrayList<RelationItem> mRelationItems;


	public RelationListBox(String aTitle)
	{
		super(new Rectangle());

		mRelationItems = new ArrayList<>();

		setTitle(aTitle);
		setLayout(new ColumnLayout(1, 0, 1));
		setBackground(BACKGROUND_COLOR);
		setForeground(Color.WHITE);
		setOpaque(true);
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


	public void add(RelationItem aRelationItem)
	{
		mRelationItems.add(aRelationItem);

		super.add(aRelationItem.getComponent());
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		aGraphics.setColor(getBackground());
		aGraphics.fillRect(0, 0, getWidth(), getHeight());

		super.paintComponent(aGraphics);
	}


	@Override
	public Anchor[] getConnectionAnchors(RelationItem aRelationItem)
	{
		Rectangle bounds = getBounds();
		int x0 = bounds.x;
		int y0 = bounds.y;

		if (isMinimized() || aRelationItem == null)
		{
			int titleHeight = getInsets().top;

			return new Anchor[]
			{
				new Anchor(new Rectangle(x0                - 1, y0, 0, titleHeight), Anchor.LEFT),
				new Anchor(new Rectangle(x0 + bounds.width + 1, y0, 0, titleHeight), Anchor.RIGHT)
			};
		}

		int index = mRelationItems.indexOf(aRelationItem);

		if (index != -1)
		{
			Rectangle d = getComponent(index).getBounds();

			return new Anchor[]
			{
				new Anchor(new Rectangle(x0 + d.x           - 1, y0 + d.y, 0, d.height), Anchor.LEFT),
				new Anchor(new Rectangle(x0 + d.x + d.width + 1, y0 + d.y, 0, d.height), Anchor.RIGHT)
			};
		}

		return null;
	}


	@Override
	protected void fireSelectedEvent()
	{
		((RelationEditor)getParent()).setSelectedComponent(this);
	}
}
