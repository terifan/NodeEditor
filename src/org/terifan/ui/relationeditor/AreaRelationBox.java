package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import org.terifan.ui.AbsoluteAreaLayout;
import org.terifan.util.log.Log;


public class AreaRelationBox extends AbstractRelationBox
{
	public AreaRelationBox(String aTitle)
	{
		super(new Rectangle());

		super.setTitle(aTitle);
		super.setBackground(BACKGROUND_COLOR);
		super.setForeground(Color.WHITE);
		super.setOpaque(true);

		mContainer.setLayout(new AbsoluteAreaLayout(1, 1));
	}


	public AreaRelationBox addItem(RelationItem aRelationItem, Rectangle aBounds)
	{
		mRelationItems.add(aRelationItem);

		mContainer.add(aRelationItem.getComponent(), aBounds);

		return this;
	}


	@Override
	protected Anchor[] getConnectionAnchorsImpl(int aRelationItemIndex, RelationItem aRelationItem, Rectangle aBounds, Insets aBorderInsets)
	{
		AbsoluteAreaLayout mAbsoluteAreaLayout = (AbsoluteAreaLayout)mContainer.getLayout();
		Rectangle d = mContainer.getComponent(aRelationItemIndex).getBounds();
		Rectangle e = mAbsoluteAreaLayout.getConstraints(aRelationItem.getComponent());

		int xl = aBounds.x + d.x - 1;
		int xr = aBounds.x + aBounds.width + 1;
		int y = Math.min(aBounds.y + aBounds.height - aBorderInsets.bottom - 5, aBounds.y + d.y + d.height / 2 + aBorderInsets.top);

		if (e.x <= 0 && e.width >= 100)
		{
			return new Anchor[]
			{
				new Anchor(new Rectangle(xl - 5, y - 5, 10, 10), Anchor.LEFT),
				new Anchor(new Rectangle(xr - 5, y - 5, 10, 10), Anchor.RIGHT)
			};
		}
		else if (e.x == 0)
		{
			return new Anchor[]
			{
				new Anchor(new Rectangle(xl - 5, y - 5, 10, 10), Anchor.LEFT)
			};
		}
		else if (e.x + e.width >= 100)
		{
			return new Anchor[]
			{
				new Anchor(new Rectangle(xr - 5, y - 5, 10, 10), Anchor.RIGHT)
			};
		}

		return null;
	}
}
