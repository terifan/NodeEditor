package org.terifan.ui.relationeditor;

import org.terifan.ui.resizablepanel.ResizablePanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.terifan.ui.AbsoluteAreaLayout;


public class RelationAreaBox extends ResizablePanel implements RelationBox
{
	private final static Color BACKGROUND_COLOR = new Color(68, 68, 68);

	private ArrayList<RelationItem> mRelationItems;
	private AbsoluteAreaLayout mAbsoluteAreaLayout;


	public RelationAreaBox(String aTitle)
	{
		super(new Rectangle());

		mRelationItems = new ArrayList<>();
		mAbsoluteAreaLayout = new AbsoluteAreaLayout(1, 1);

		setTitle(aTitle);
		setLayout(mAbsoluteAreaLayout);
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


	public void add(RelationItem aRelationItem, Rectangle aBounds)
	{
		mRelationItems.add(aRelationItem);

		super.add(aRelationItem.getComponent(), aBounds);
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		aGraphics.setColor(getBackground());
		aGraphics.fillRect(0, 0, getWidth(), getHeight());

		super.paintComponent(aGraphics);
	}


	@Override
	public Rectangle[] getAnchors(RelationItem aRelationItem)
	{
		Rectangle bounds = getBounds();
		int x0 = bounds.x;
		int y0 = bounds.y;

		if (isMinimized() || aRelationItem == null)
		{
			int titleHeight = getInsets().top;

			return new Rectangle[]
			{
				new Rectangle(x0                - 1, y0, 0, titleHeight),
				new Rectangle(x0 + bounds.width + 1, y0, 0, titleHeight)
			};
		}

		int index = mRelationItems.indexOf(aRelationItem);

		if (index != -1)
		{
			Rectangle d = getComponent(index).getBounds();
			Rectangle e = mAbsoluteAreaLayout.getConstraints(aRelationItem.getComponent());

			if (e.x == 0 && e.width == 100)
			{
				return new Rectangle[]
				{
					new Rectangle(x0 + d.x - 1,           y0 + d.y, 0, d.height),
					new Rectangle(x0 + d.x + d.width + 1, y0 + d.y, 0, d.height)
				};
			}
			else if (e.x == 0)
			{
				return new Rectangle[]
				{
					new Rectangle(x0 + d.x - 1,           y0 + d.y, 0, d.height)
				};
			}
			else if (e.x + e.width == 100)
			{
				return new Rectangle[]
				{
					new Rectangle(x0 + d.x + d.width + 1, y0 + d.y, 0, d.height)
				};
			}
		}

		return null;
	}


	@Override
	protected void fireSelectedEvent()
	{
		((RelationEditor)getParent()).setSelectedComponent(this);
	}
}
