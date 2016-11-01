package org.terifan.nodeeditor;

import java.awt.Insets;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import org.terifan.ui.StackedLayout;
import org.terifan.util.log.Log;


public class StackedRelationBox extends AbstractRelationBox
{
	private HashMap<RelationItem,Direction> mAnchors;


	public StackedRelationBox(String aTitle)
	{
		super(new Rectangle(), aTitle);

		mAnchors = new HashMap<>();

		mContainer.setLayout(new StackedLayout(0));
	}


	public StackedRelationBox addItem(StackedRelationItem aRelationItem)
	{
		mRelationItems.add(aRelationItem);

		mAnchors.put(aRelationItem, aRelationItem.getDirection());

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

		if (isMinimized())
		{
			y = aBounds.y + aBorderInsets.top / 2 + 2;

//			List<Connection> in = getConnections(true, false);
//			List<Connection> out = getConnections(false, true);
//
//			boolean isIn = false;
//			for (Connection c : in)
//			{
//				if (isConnected(c, Direction.IN))
//				{
//					isIn = true;
//				}
//			}
//
//			double dy = (mRelationItems.indexOf(aRelationItem) - (mRelationItems.size() - 1) / 2.0) * 8;
//			Log.out.println(dy);
//
//			y += dy;
		}

		if (mAnchors.get(aRelationItem) == Direction.IN)
		{
			return new Anchor[]
			{
				new Anchor(new Rectangle(aBounds.x, y - 5, 10, 10), Anchor.LEFT)
			};
		}
		if (mAnchors.get(aRelationItem) == Direction.OUT)
		{
			return new Anchor[]
			{
				new Anchor(new Rectangle(aBounds.x + aBounds.width - 10, y - 5, 10, 10), Anchor.RIGHT)
			};
		}

		return null;
	}
}
