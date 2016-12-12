package org.terifan.nodeeditor.examples;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Styles;
import org.terifan.nodeeditor.NodeItem;


public class ImageRelationItem extends NodeItem
{
	public ImageRelationItem(String aText, int aWidth, int aHeight, Connector... aConnectors)
	{
		super(aText, aWidth, aHeight, aConnectors);
	}


	@Override
	protected void paintComponent(Graphics2D aGraphics, Rectangle aBounds)
	{
		aGraphics.setColor(Styles.BOX_BACKGROUND_COLOR);
		aGraphics.fill(aBounds);

		aGraphics.setColor(new Color(200, 200, 200));
		aGraphics.fillRect(aBounds.x, aBounds.y, mSize.width, mSize.height);
		aGraphics.setColor(new Color(220, 220, 220));
		for (int y = 0; y < 10; y++)
		{
			for (int x = (y & 1); x < 10; x += 2)
			{
				aGraphics.fillRect(aBounds.x + x * 20, aBounds.y + y * 20, 20, 20);
			}
		}
	}
}
