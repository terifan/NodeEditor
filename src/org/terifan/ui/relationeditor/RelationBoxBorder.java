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
	private final static Color COLOR_64 = new Color(64, 64, 64);
	private final static Color COLOR_80 = new Color(80, 80, 80);
	private final static Color COLOR_96 = new Color(96, 96, 96);
	private final static Color COLOR_68 = new Color(68,68,68);


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

		Rectangle bounds = new Rectangle(aX, aY, aWidth - 1, aHeight - 1);

		drawBorder(g, bounds, 0, COLOR_96, COLOR_32);
		drawBorder(g, bounds, 1, COLOR_64, COLOR_80);

		g.setColor(COLOR_68);
		bounds.grow(-2, -2);
		g.draw(bounds);
		bounds.grow(-1, -1);
		g.draw(bounds);

		bounds.width -= 16;
		bounds.height = 16;

		RelationBox box = (RelationBox)aComponent;
		new TextBox(box.getTitle()).setAnchor(Anchor.WEST).setBounds(bounds).setMargins(0, 4, 0, 4).setForeground(Color.WHITE).render(g);
	}


	public static void drawBorder(Graphics aGraphics, Rectangle b, int i, Color aColor, Color aColor0)
	{
		aGraphics.setColor(aColor);
		aGraphics.drawLine(b.x+i, b.y+i, b.x+b.width-i, b.y+i);
		aGraphics.drawLine(b.x+i, b.y+i, b.x+i, b.y+b.height-i);

		aGraphics.setColor(aColor0);
		aGraphics.drawLine(b.x+b.width-i, b.y+i+1, b.x+b.width-i, b.y+b.height-i);
		aGraphics.drawLine(b.x+i+1, b.y+b.height-i, b.x+b.width-i, b.y+b.height-i);
	}
}
