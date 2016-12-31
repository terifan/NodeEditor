package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Graphics2D;


public class ImageNodeItem extends TextNodeItem
{
	public ImageNodeItem(String aText, int aWidth, int aHeight, Connector... aConnectors)
	{
		super(aText, aWidth, aHeight, aConnectors);
	}


	@Override
	protected void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, boolean aHover)
	{
		int s = 20;
		int t = 10;

		aGraphics.setColor(new Color(200, 200, 200));
		aGraphics.fillRect(mBounds.x, mBounds.y, t * s, t * s);

		aGraphics.setColor(new Color(220, 220, 220));
		for (int y = 0; y < t; y++)
		{
			for (int x = (y & 1); x < t; x += 2)
			{
				aGraphics.fillRect(mBounds.x + x * s, mBounds.y + y * s, s, s);
			}
		}
	}
}
