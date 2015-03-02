package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.border.Border;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class RelationBoxBorder implements Border
{
	private final static Color COLOR_32 = new Color(32, 32, 32);
	private final static Color COLOR_48 = new Color(48, 48, 48);
	private final static Color COLOR_68 = new Color(68, 68, 68);
	private final static Color COLOR_80 = new Color(80, 80, 80);
	private final static Color COLOR_96 = new Color(96, 96, 96);
	private final static Color COLOR_160 = new Color(160, 160, 160);


	@Override
	public boolean isBorderOpaque()
	{
		return true;
	}


	@Override
	public Insets getBorderInsets(Component aComponent)
	{
		return new Insets(4 + 16, 4, 4, 4);
	}


	@Override
	public void paintBorder(Component aComponent, Graphics aGraphics, int aX, int aY, int aWidth, int aHeight)
	{
		Graphics2D g = (Graphics2D)aGraphics;

		Rectangle b = new Rectangle(aX, aY, aWidth - 1, aHeight - 1);

		drawBorder(g, b, 0, COLOR_96, COLOR_32);
		drawBorder(g, b, 1, COLOR_80, COLOR_80);

		g.setColor(COLOR_68);
		b.grow(-2, -2);
		g.draw(b);
		b.grow(-1, -1);
		g.draw(b);

		b.width -= 16;
		b.height = 16;

		RelationBox box = (RelationBox)aComponent;

		new TextBox(box.getTitle()).setAnchor(Anchor.WEST).setBounds(b).setMargins(0, 4, 0, 4).setForeground(Color.WHITE).setMaxLineCount(1).render(g);

		b.x += b.width + 1;
		b.y++;
		b.width = 14;
		b.height = 14;

		drawBorder(g, b, 0, COLOR_96, COLOR_48);
		g.setColor(COLOR_160);

		if (box.isMinimized())
		{
			g.drawLine(b.x+3, b.y+4, b.x+b.width-3, b.y+4);
			g.drawLine(b.x+3, b.y+5, b.x+b.width-3, b.y+5);
			g.drawLine(b.x+3, b.y+10, b.x+b.width-3, b.y+10);
			g.drawLine(b.x+3, b.y+4, b.x+3, b.y+10);
			g.drawLine(b.x+b.width-3, b.y+4, b.x+b.width-3, b.y+10);
		}
		else
		{
			g.drawLine(b.x+4, b.y+9, b.x+b.width-4, b.y+9);
			g.drawLine(b.x+4, b.y+10, b.x+b.width-4, b.y+10);
		}
	}


	private void drawBorder(Graphics aGraphics, Rectangle b, int i, Color aColor, Color aColor0)
	{
		aGraphics.setColor(aColor);
		aGraphics.drawLine(b.x+i, b.y+i, b.x+b.width-i, b.y+i);
		aGraphics.drawLine(b.x+i, b.y+i, b.x+i, b.y+b.height-i);

		aGraphics.setColor(aColor0);
		aGraphics.drawLine(b.x+b.width-i, b.y+i+1, b.x+b.width-i, b.y+b.height-i);
		aGraphics.drawLine(b.x+i+1, b.y+b.height-i, b.x+b.width-i, b.y+b.height-i);
	}
}
