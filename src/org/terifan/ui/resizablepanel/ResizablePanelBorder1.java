package org.terifan.ui.resizablepanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.Border;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import org.terifan.ui.relationeditor.Styles;


public class ResizablePanelBorder1 implements Border
{
	private int mTitleHeight;


	public ResizablePanelBorder1()
	{
		mTitleHeight = 18;
	}


	@Override
	public boolean isBorderOpaque()
	{
		return false;
	}


	@Override
	public Insets getBorderInsets(Component aComponent)
	{
		Insets borderInsets = new Insets(0,0,0,0);

		borderInsets.left += 4;
		borderInsets.top += 4 + mTitleHeight;
		borderInsets.right += 4;
		borderInsets.bottom += 4;

		return borderInsets;
	}


	@Override
	public void paintBorder(Component aComponent, Graphics aGraphics, int aX, int aY, int aWidth, int aHeight)
	{
		Graphics2D g = (Graphics2D)aGraphics;
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		ResizablePanel panel = (ResizablePanel)aComponent;

		g.setColor(Styles.BOX_BORDER_TITLE_COLOR);
		g.fillRoundRect(aX+1, aY+1, aWidth-2, aHeight-2, 18, 18);

		boolean minimized = aHeight <= 4 + 4 + mTitleHeight;
		
		if (!minimized)
		{
			g.setColor(panel.getBackground());
			g.fillRoundRect(aX+1, aY+1+3+mTitleHeight, aWidth-2, aHeight-2-3-mTitleHeight, 18, 18);
			g.fillRect(aX+1, aY+1+3+mTitleHeight, aWidth-2, aY+1+mTitleHeight);
		}

		Color c = Styles.BOX_BORDER_TITLE_COLOR.brighter();
		c = new Color(c.getRed(),c.getGreen(),c.getBlue(),128);
		new TextBox(panel.getTitle()).setAnchor(Anchor.WEST).setBounds(aX+20-1,aY+4+1,aWidth-2*20,mTitleHeight).setMargins(0, 4, 0, 4).setForeground(c).setMaxLineCount(1).render(g);
		new TextBox(panel.getTitle()).setAnchor(Anchor.WEST).setBounds(aX+20,aY+4,aWidth-2*20,mTitleHeight).setMargins(0, 4, 0, 4).setForeground(aComponent.getForeground()).setMaxLineCount(1).render(g);

		g.setColor(panel.getBackground() == Styles.BOX_BACKGROUND_SELECTED_COLOR ? Styles.BOX_BORDER_SELECTED_COLOR : Styles.BOX_BORDER_COLOR);
		g.drawRoundRect(aX, aY, aWidth-1, aHeight-1, 18, 18);

		int w = 10;
		int h = 5;
		aX += 8;
		aY += 4 + mTitleHeight / 2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (minimized)
		{
			g.fillPolygon(new int[]{aX,aX+w,aX}, new int[]{aY-h,aY,aY+h}, 3);
		}
		else
		{
			g.fillPolygon(new int[]{aX,aX+w,aX+w/2}, new int[]{aY-h,aY-h,aY+h}, 3);
		}
	}
}
