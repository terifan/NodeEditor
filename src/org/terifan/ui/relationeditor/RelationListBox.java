package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Rectangle;
import org.terifan.ui.ColumnLayout;


public class RelationListBox extends AbstractRelationBox
{
	public RelationListBox(String aTitle)
	{
		super(new Rectangle());

		setTitle(aTitle);
		setLayout(new ColumnLayout(1, 0, 1));
		setBackground(BACKGROUND_COLOR);
		setForeground(Color.WHITE);
		setOpaque(true);
	}


	public void add(RelationItem aRelationItem)
	{
		mRelationItems.add(aRelationItem);

		super.add(aRelationItem.getComponent());
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
}
