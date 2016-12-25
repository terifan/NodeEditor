package org.terifan.nodeeditor.examples;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Styles;
import org.terifan.nodeeditor.NodeItem;


public class ImageNodeItem extends NodeItem
{
	public ImageNodeItem(String aText, int aWidth, int aHeight, Connector... aConnectors)
	{
		super(aText, aWidth, aHeight, aConnectors);
	}


	@Override
	protected void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, boolean aHover)
	{
		aGraphics.setColor(Styles.BOX_BACKGROUND_COLOR);
		aGraphics.fill(mBounds);

		aGraphics.setColor(new Color(200, 200, 200));
		aGraphics.fillRect(mBounds.x, mBounds.y, mSize.width, mSize.height);
		aGraphics.setColor(new Color(220, 220, 220));
		for (int y = 0; y < 10; y++)
		{
			for (int x = (y & 1); x < 10; x += 2)
			{
				aGraphics.fillRect(mBounds.x + x * 20, mBounds.y + y * 20, 20, 20);
			}
		}
	}
}
