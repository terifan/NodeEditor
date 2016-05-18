package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import org.terifan.ui.ColumnLayout;
import org.terifan.util.log.Log;


public class RelationListBox extends AbstractRelationBox
{
	public RelationListBox(String aTitle)
	{
		super(new Rectangle());

		super.setTitle(aTitle);
		super.setBackground(BACKGROUND_COLOR);
		super.setForeground(Color.WHITE);
		super.setOpaque(true);

		mContainer.setLayout(new ColumnLayout(1, 0, 1));
	}


	public RelationListBox addItem(RelationItem aRelationItem)
	{
		mRelationItems.add(aRelationItem);

		mContainer.add(aRelationItem.getComponent());

		return this;
	}


	@Override
	public Anchor[] getConnectionAnchors(RelationItem aRelationItem)
	{
		Rectangle bounds = getBounds();
		Insets borderInsets = getBorder().getBorderInsets(this);
		int x0 = bounds.x;
		int y0 = bounds.y + borderInsets.top;

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
			Rectangle d = mContainer.getComponent(index).getBounds();

			int yd = mContainerScrollPane.getVerticalScrollBar().getValue();
			int y = Math.min(bounds.height + 5 - 2 * borderInsets.top - borderInsets.bottom, Math.max(-5, d.y - yd));

			return new Anchor[]
			{
				new Anchor(new Rectangle(x0                - 1, y0 + y, 0, d.height), Anchor.LEFT),
				new Anchor(new Rectangle(x0 + bounds.width + 1, y0 + y, 0, d.height), Anchor.RIGHT)
			};
		}

		return null;
	}
}
