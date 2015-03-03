package org.terifan.ui.resizablepanel;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class ResizablePanelBorder implements Border
{
	private Border mBevelBorder;
	private int mTitleHeight;


	public ResizablePanelBorder()
	{
		mBevelBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		mTitleHeight = 16;
	}


	public Border getBevelBorder()
	{
		return mBevelBorder;
	}


	public void setBevelBorder(Border aBevelBorder)
	{
		mBevelBorder = aBevelBorder;
	}


	public int getTitleHeight()
	{
		return mTitleHeight;
	}


	public void setTitleHeight(int aTitleHeight)
	{
		mTitleHeight = aTitleHeight;
	}


	@Override
	public boolean isBorderOpaque()
	{
		return mBevelBorder.isBorderOpaque();
	}


	@Override
	public Insets getBorderInsets(Component aComponent)
	{
		Insets borderInsets = mBevelBorder.getBorderInsets(aComponent);

		borderInsets.left += 2;
		borderInsets.top += 2 + mTitleHeight;
		borderInsets.right += 2;
		borderInsets.bottom += 2;

		return borderInsets;
	}


	@Override
	public void paintBorder(Component aComponent, Graphics aGraphics, int aX, int aY, int aWidth, int aHeight)
	{
		Border createEtchedBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED,  ((BevelBorder)mBevelBorder).getHighlightOuterColor(aComponent), aComponent.getBackground(), ((BevelBorder)mBevelBorder).getShadowOuterColor(aComponent), aComponent.getBackground());

		Graphics2D g = (Graphics2D)aGraphics;

		Rectangle b = new Rectangle(aX, aY, aWidth - 1, aHeight - 1);

		mBevelBorder.paintBorder(aComponent, aGraphics, aX, aY, aWidth, aHeight);

		g.setColor(aComponent.getBackground());
		b.grow(-2, -2);
		g.draw(b);
		b.grow(-1, -1);
		g.draw(b);

		b.width -= mTitleHeight;
		b.height = mTitleHeight;

		ResizablePanel panel = (ResizablePanel)aComponent;

		new TextBox(panel.getTitle()).setAnchor(Anchor.WEST).setBounds(b).setMargins(0, 4, 0, 4).setForeground(aComponent.getForeground()).setMaxLineCount(1).render(g);

		b.x += b.width + 1;
		b.y++;
		b.width = mTitleHeight - 2;
		b.height = mTitleHeight - 2;

		createEtchedBorder.paintBorder(aComponent, aGraphics, b.x, b.y, b.width, b.height);

		g.setColor(aComponent.getForeground().darker());

		if (panel.isMinimized())
		{
			g.drawLine(b.x+3, b.y+4, b.x+b.width-4, b.y+4);
			g.drawLine(b.x+3, b.y+5, b.x+b.width-4, b.y+5);
			g.drawLine(b.x+3, b.y+10, b.x+b.width-4, b.y+10);
			g.drawLine(b.x+3, b.y+4, b.x+3, b.y+10);
			g.drawLine(b.x+b.width-4, b.y+4, b.x+b.width-4, b.y+10);
		}
		else
		{
			g.drawLine(b.x+3, b.y+8, b.x+b.width-4, b.y+8);
			g.drawLine(b.x+3, b.y+9, b.x+b.width-4, b.y+9);
		}
	}
}
