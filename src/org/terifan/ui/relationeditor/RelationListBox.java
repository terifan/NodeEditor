package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import org.terifan.ui.FlowLayout;


public class RelationListBox extends RelationBox
{
	private final static Color COLOR_42 = new Color(42, 42, 42);
	private final static Color COLOR_48 = new Color(48, 48, 48);
	private final static Color COLOR_68 = new Color(68, 68, 68);
	private final static Color COLOR_96 = new Color(96, 96, 96);
	private final static Color COLOR_108 = new Color(108, 108, 108);

//	private ArrayList<RelationItem> mItems;
//	private Rectangle mBounds;


	public RelationListBox(String aTitle)
	{
//		mItems = new ArrayList<>();
//		mBounds = new Rectangle();
		mTitle = aTitle;

		setLayout(new FlowLayout(1, 0, 1));
		setBorder(new RelationBoxBorder());
		setBackground(COLOR_68);
		setOpaque(true);
	}


//	public void addItem(RelationItem aItem)
//	{
//		aItem.setRelationBox(this);
//		mItems.add(aItem);
//	}


//	@Override
//	public ArrayList<RelationItem> getItems()
//	{
//		return mItems;
//	}


//	@Override
//	public Dimension getPreferredSize()
//	{
//		int w = 80;
//		int h = 0;
//
//		if (!mMinimized)
//		{
//			for (int i = 0; i < getComponentCount(); i++)
//			{
//				RelationItem item = (RelationItem)getComponent(i);
//				Dimension d = item.getPreferredSize();
//				w = Math.max(w, d.width);
//				h += d.getHeight();
//			}
//		}
//
//		return new Dimension(w, h);
//	}


//	@Override
//	protected void paintChildren(Graphics aGraphics)
//	{
//		int y = 0;
//		for (int i = 0; i < getComponentCount(); i++)
//		{
//			RelationItem item = (RelationItem)getComponent(i);
//
//			Dimension d = item.getPreferredSize();
//
//			item.setBounds(0, y, getWidth(), d.height);
//			item.paintComponent(aGraphics);
//
//			y += d.getHeight();
//		}
//	}
//
//
	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		aGraphics.setColor(getBackground());
		aGraphics.fillRect(0, 0, getWidth(), getHeight());

		super.paintComponent(aGraphics);

//		Graphics2D g = (Graphics2D)aGraphics;
//
////		Rectangle b = new Rectangle(mBounds);
//
//		g.setColor(COLOR_68);
//		g.fill(getBounds());
//
////		new RelationBoxBorder().render(g, b);
////
////		b.grow(-BORDER_WIDTH, -BORDER_WIDTH);
////
////		RelationBoxBorder.drawBorder(g, getMinimizeButtonBounds(), 0, COLOR_96, COLOR_48);
////		g.setColor(COLOR_108);
////		g.drawLine(b.x+b.width-10, b.y+9, b.x+b.width-3, b.y+9);
////		g.drawLine(b.x+b.width-10, b.y+10, b.x+b.width-3, b.y+10);
////
////		int x = b.x;
////		int y = b.y;
////		int w = b.width;
////
////		new TextBox(mTitle).setAnchor(Anchor.WEST).setBounds(x, y, w-16, TITLE_HEIGHT).setMargins(0, 4, 0, 4).setForeground(Color.WHITE).render(g);
////
////		if (!mMinimized)
////		{
////			y += TITLE_HEIGHT;
////
////			for (RelationItem item : mItems)
////			{
////				Rectangle d = item.getBounds();
////
////				g.setColor(COLOR_42);
////				g.fillRect(x, y, w + 1, d.height);
////
////				Graphics2D gg = (Graphics2D)g.create(x, y, w, d.height);
////				item.render(gg, new Rectangle(0, 0, w, d.height));
////				gg.dispose();
////				y += d.height + 1;
////			}
////		}
	}


	@Override
	public Rectangle[] getAnchors(RelationItem aItem)
	{
		int x0 = getBounds().x;
		int y = getBounds().y;
		int x1 = x0 + getWidth();

		int titleHeight = getBorder().getBorderInsets(this).top;

		if (mMinimized)
		{
			return new Rectangle[]{
				new Rectangle(x0-1, y, 0, titleHeight),
				new Rectangle(x1+1, y, 0, titleHeight)
			};
		}

		y += titleHeight;

		for (int i = 0; i < getComponentCount(); i++)
		{
			RelationItem item = (RelationItem)getComponent(i);
			Rectangle d = item.getBounds();
			if (item == aItem)
			{
				return new Rectangle[]{
					new Rectangle(x0-1, y, 0, d.height),
					new Rectangle(x1+1, y, 0, d.height)
				};
			}
			y += d.height;
		}

		return null;
	}
}
