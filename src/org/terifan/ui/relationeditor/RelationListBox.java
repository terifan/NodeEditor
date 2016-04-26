package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.terifan.ui.ColumnLayout;


public class RelationListBox extends AbstractRelationBox
{
	private JPanel mPanel;


	public RelationListBox(String aTitle)
	{
		super(new Rectangle());

		mPanel = new JPanel();
		mPanel.setLayout(new ColumnLayout(1, 0, 1));

		JScrollPane scrollPane = new JScrollPane(mPanel);
		super.add(scrollPane);

		super.setTitle(aTitle);
//		super.setLayout(new ColumnLayout(1, 0, 1));
		super.setBackground(BACKGROUND_COLOR);
		super.setForeground(Color.WHITE);
		super.setOpaque(true);
	}


	public RelationListBox addItem(RelationItem aRelationItem)
	{
		mRelationItems.add(aRelationItem);

		mPanel.add(aRelationItem.getComponent());

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
			Rectangle d = mPanel.getComponent(index).getBounds();

			return new Anchor[]
			{
				new Anchor(new Rectangle(x0 + d.x           - 1, y0 + d.y, 0, d.height), Anchor.LEFT),
				new Anchor(new Rectangle(x0 + d.x + d.width + 1, y0 + d.y, 0, d.height), Anchor.RIGHT)
			};
		}

		return null;
	}
}
