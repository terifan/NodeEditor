package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.HashMap;
import org.terifan.ui.StackedLayout;
import static org.terifan.nodeeditor.StackedRelationItem.Anchors;


public class StackedRelationBox extends AbstractRelationBox
{
	private HashMap<RelationItem,Anchors> mAnchors;


	public StackedRelationBox(String aTitle)
	{
		super(new Rectangle(), aTitle);

		mAnchors = new HashMap<>();

		mContainer.setLayout(new StackedLayout(0));
	}


	public StackedRelationBox addItem(StackedRelationItem aRelationItem)
	{
		mRelationItems.add(aRelationItem);

		mAnchors.put(aRelationItem, aRelationItem.getAnchors());

		mContainer.add(aRelationItem.getComponent(), new StackedLayout.LayoutParams(aRelationItem.getHeight(), aRelationItem.getWeight()));

		return this;
	}


	@Override
	protected Anchor[] getConnectionAnchorsImpl(int aRelationItemIndex, RelationItem aRelationItem, Rectangle aBounds, Insets aBorderInsets)
	{
		Rectangle d = mContainer.getComponent(aRelationItemIndex).getBounds();

		int s = getVerticalScrollValue();
		int y = aBorderInsets.top + aBounds.y + d.y + d.height / 2 - s;

		y = Math.max(aBounds.y + aBorderInsets.top / 2, Math.min(y, aBounds.y + aBounds.height - aBorderInsets.bottom - 5));

		switch (mAnchors.get(aRelationItem))
		{
			case LEFT:
				return new Anchor[]
				{
					new Anchor(new Rectangle(aBounds.x, y - 5, 10, 10), Anchor.LEFT)
				};
			case RIGHT:
				return new Anchor[]
				{
					new Anchor(new Rectangle(aBounds.x + aBounds.width - 10, y - 5, 10, 10), Anchor.RIGHT)
				};
			case BOTH:
				return new Anchor[]
				{
					new Anchor(new Rectangle(aBounds.x, y - 5, 10, 10), Anchor.LEFT),
					new Anchor(new Rectangle(aBounds.x + aBounds.width - 10, y - 5, 10, 10), Anchor.RIGHT)
				};
		}

		return null;
	}
}
