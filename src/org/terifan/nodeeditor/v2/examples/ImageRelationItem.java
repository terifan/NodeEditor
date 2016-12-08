package org.terifan.nodeeditor.v2.examples;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.terifan.nodeeditor.v2.Direction;
import org.terifan.nodeeditor.v2.Styles;
import org.terifan.nodeeditor.v2.RelationItem;


public class ImageRelationItem extends RelationItem
{
	public ImageRelationItem(String aText, int aWidth, int aHeight, Direction aDirection)
	{
		super(aText, aWidth, aHeight, aDirection, 0);
	}


	public ImageRelationItem(String aText, int aWidth, int aHeight, Direction aDirection, double aWeight)
	{
		super(aText, aWidth, aHeight, aDirection, aWeight);
	}


	@Override
	protected void paintComponent(Graphics2D aGraphics)
	{
		aGraphics.setColor(Styles.BOX_BACKGROUND_COLOR);
		aGraphics.fill(mBounds);

		aGraphics.setColor(new Color(200,200,200));
		aGraphics.fillRect(mBounds.x, mBounds.y, mSize.width, mSize.height);
		aGraphics.setColor(new Color(220,220,220));
		for (int y = 0; y < 10; y++)
		{
			for (int x = (y&1); x < 10; x+=2)
			{
				aGraphics.fillRect(mBounds.x+x*20, mBounds.y+y*20, 20, 20);
			}
		}
	}
}
