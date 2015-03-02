package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import org.terifan.ui.ColumnLayout;


public class RelationListBox extends RelationBox
{
	private final static Color COLOR_68 = new Color(68, 68, 68);


	public RelationListBox(String aTitle)
	{
		mTitle = aTitle;

		RelationBoxMouseListener relationBoxMouseListener = new RelationBoxMouseListener(this);
		addMouseListener(relationBoxMouseListener);
		addMouseMotionListener(relationBoxMouseListener);
		setLayout(new ColumnLayout(1, 0, 1));
		setBorder(new RelationBoxBorder());
		setBackground(COLOR_68);
		setOpaque(true);
	}


	@Override
	public void setMinimized(boolean aMinimized)
	{
		mMinimized = aMinimized;

		for (int i = 0; i < getComponentCount(); i++)
		{
			getComponent(i).setVisible(!mMinimized);
		}

		invalidate();
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		aGraphics.setColor(getBackground());
		aGraphics.fillRect(0, 0, getWidth(), getHeight());

		super.paintComponent(aGraphics);
	}


	@Override
	public Rectangle[] getAnchors(RelationItem aItem)
	{
		int x0 = getBounds().x;
		int y0 = getBounds().y;
		int x1 = x0 + getWidth();

		int titleHeight = getBorder().getBorderInsets(this).top;

		if (mMinimized || aItem == null)
		{
			return new Rectangle[]
			{
				new Rectangle(x0 - 1, y0, 0, titleHeight),
				new Rectangle(x1 + 1, y0, 0, titleHeight)
			};
		}

		y0 += titleHeight;

		for (int i = 0; i < getComponentCount(); i++)
		{
			RelationItem item = (RelationItem)getComponent(i);
			Rectangle d = item.getBounds();
			if (item == aItem)
			{
				return new Rectangle[]
				{
					new Rectangle(x0 - 1, y0, 0, d.height),
					new Rectangle(x1 + 1, y0, 0, d.height)
				};
			}
			y0 += d.height;
		}

		return null;
	}
}
