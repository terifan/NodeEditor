package org.terifan.ui.resizablepanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;


public class ResizablePanelBorder1 implements Border
{
	private int mTitleHeight;


	public ResizablePanelBorder1()
	{
		mTitleHeight = 16;
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
		aGraphics.setColor(new Color(0x5A5A5A));
		aGraphics.fillRoundRect(aX, aY, aWidth-1, aHeight-1, 15, 15);
		aGraphics.setColor(new Color(false ? 0xD09142 : 0x252525));
		aGraphics.drawRoundRect(aX, aY, aWidth-1, aHeight-1, 15, 15);
		
//		new TextBox(panel.getTitle()).setAnchor(Anchor.WEST).setBounds(b).setMargins(0, 4, 0, 4).setForeground(aComponent.getForeground()).setMaxLineCount(1).render(g);
	}
}
