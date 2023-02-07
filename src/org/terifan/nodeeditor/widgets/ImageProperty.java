package org.terifan.nodeeditor.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;


public class ImageProperty extends Property<ImageProperty>
{
	private static final long serialVersionUID = 1L;

	protected String mImagePath;


	public ImageProperty(String aText, int aWidth, int aHeight)
	{
		super(aText);

		setPreferredSize(new Dimension(aWidth, aHeight));
	}


	public String getImagePath()
	{
		return mImagePath;
	}


	public ImageProperty setImagePath(String aImagePath)
	{
		mImagePath = aImagePath;
		return this;
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		Rectangle bounds = getBounds();

		int t = 10;
		double sx = bounds.width / (double)t;
		double sy = bounds.height / (double)t;

		aGraphics.setColor(new Color(200, 200, 200));
		aGraphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		aGraphics.setColor(new Color(220, 220, 220));
		for (int y = 0; y < t; y++)
		{
			for (int x = (y & 1); x < t; x += 2)
			{
				aGraphics.fillRect(bounds.x + (int)(x * sx), bounds.y + (int)(y * sy), (int)sx, (int)sy);
			}
		}

		aPane.paintImage(this, aGraphics, bounds);
	}
}
