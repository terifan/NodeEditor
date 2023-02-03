package org.terifan.nodeeditor.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import org.terifan.nodeeditor.NodeEditor;
import org.terifan.nodeeditor.PropertyItem;


public class ImagePropertyItem extends PropertyItem<ImagePropertyItem>
{
	private static final long serialVersionUID = 1L;

	protected String mImagePath;


	public ImagePropertyItem(String aText, int aWidth, int aHeight)
	{
		super(aText);

		setPreferredSize(new Dimension(aWidth, aHeight));
	}


	public String getImagePath()
	{
		return mImagePath;
	}


	public ImagePropertyItem setImagePath(String aImagePath)
	{
		mImagePath = aImagePath;
		return this;
	}


	@Override
	protected void paintComponent(NodeEditor aEditor, Graphics2D aGraphics, boolean aHover)
	{
		int t = 10;
		double sx = mBounds.width / (double)t;
		double sy = mBounds.height / (double)t;

		aGraphics.setColor(new Color(200, 200, 200));
		aGraphics.fillRect(mBounds.x, mBounds.y, mBounds.width, mBounds.height);

		aGraphics.setColor(new Color(220, 220, 220));
		for (int y = 0; y < t; y++)
		{
			for (int x = (y & 1); x < t; x += 2)
			{
				aGraphics.fillRect(mBounds.x + (int)(x * sx), mBounds.y + (int)(y * sy), (int)sx, (int)sy);
			}
		}

		aEditor.paintImage(this, aGraphics, mBounds);
	}
}
