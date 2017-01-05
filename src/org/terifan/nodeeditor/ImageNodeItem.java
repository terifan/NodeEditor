package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class ImageNodeItem extends TextNodeItem
{
	private BufferedImage mImage;


	public ImageNodeItem(String aText, BufferedImage aImage, int aWidth, int aHeight, Connector... aConnectors)
	{
		super(aText, aWidth, aHeight, aConnectors);

		mImage = aImage;
	}


	public BufferedImage getImage()
	{
		return mImage;
	}


	public ImageNodeItem setImage(BufferedImage aImage)
	{
		mImage = aImage;
		return this;
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

		aGraphics.drawImage(mImage, mBounds.x, mBounds.y, mBounds.width, mBounds.height, null);
	}
}
