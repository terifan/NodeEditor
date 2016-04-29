package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Rectangle;
import org.terifan.ui.AbsoluteAreaLayout;


public class RelationAreaBox extends AbstractRelationBox
{
	public RelationAreaBox(String aTitle)
	{
		super(new Rectangle());

		super.setTitle(aTitle);
		super.setBackground(BACKGROUND_COLOR);
		super.setForeground(Color.WHITE);
		super.setOpaque(true);

		mContainer.setLayout(new AbsoluteAreaLayout(1, 1));
	}


	public RelationAreaBox addItem(RelationItem aRelationItem, Rectangle aBounds)
	{
		mRelationItems.add(aRelationItem);

		mContainer.add(aRelationItem.getComponent(), aBounds);

		return this;
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
			AbsoluteAreaLayout mAbsoluteAreaLayout = (AbsoluteAreaLayout)mContainer.getLayout();

			Rectangle d = mContainer.getComponent(index).getBounds();
			Rectangle e = mAbsoluteAreaLayout.getConstraints(aRelationItem.getComponent());

			if (e.x == 0 && e.width == 100)
			{
				return new Anchor[]
				{
					new Anchor(new Rectangle(x0 + d.x - 1,           y0 + d.y, 0, d.height), Anchor.LEFT),
					new Anchor(new Rectangle(x0 + d.x + d.width + 1, y0 + d.y, 0, d.height), Anchor.RIGHT)
				};
			}
			else if (e.x == 0)
			{
				return new Anchor[]
				{
					new Anchor(new Rectangle(x0 + d.x - 1,           y0 + d.y, 0, d.height), Anchor.LEFT)
				};
			}
			else if (e.x + e.width == 100)
			{
				return new Anchor[]
				{
					new Anchor(new Rectangle(x0 + d.x + d.width + 1, y0 + d.y, 0, d.height), Anchor.RIGHT)
				};
			}
		}

		return null;
	}
}
