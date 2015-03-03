package org.terifan.ui.relationeditor;

import org.terifan.ui.resizablepanel.ResizablePanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import org.terifan.ui.ColumnLayout;
import org.terifan.ui.resizablepanel.ResizablePanelBorder;


public class RelationListBox extends ResizablePanel implements RelationBox
{
	private final static Color COLOR_68 = new Color(68, 68, 68);
	private final static Color HI_OUTER_COLOR = new Color(96, 96, 96);
	private final static Color HI_INNER_COLOR = new Color(80, 80, 80);
	private final static Color LO_OUTER_COLOR = new Color(32, 32, 32);
	private final static Color LO_INNER_COLOR = new Color(80, 80, 80);
	private final static Color ICON_COLOR = new Color(160, 160, 160);


	public RelationListBox(String aTitle)
	{
		super(new Rectangle());

		setTitle(aTitle);
		setLayout(new ColumnLayout(1, 0, 1));
		setBackground(COLOR_68);
		setForeground(Color.WHITE);
		setOpaque(true);

		((ResizablePanelBorder)getBorder()).setBevelBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, HI_OUTER_COLOR, HI_INNER_COLOR, LO_OUTER_COLOR, LO_INNER_COLOR));
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

		if (isMinimized() || aItem == null)
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


	@Override
	protected void fireSelectedEvent()
	{
		((RelationEditor)getParent()).setSelectedComponent(this);
	}
}
