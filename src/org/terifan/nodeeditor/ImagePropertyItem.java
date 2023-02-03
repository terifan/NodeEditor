package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class ImagePropertyItem extends AbstractPropertyItem<ImagePropertyItem>
{
	private static final long serialVersionUID = 1L;

	protected String mImagePath;


	public ImagePropertyItem(String aText, int aWidth, int aHeight)
	{
		super(aText, aWidth, aHeight);

		mUserSetSize = true;
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

		BufferedImage image = null;

		if (mImagePath != null)
		{
			image = aEditor.loadResource(BufferedImage.class, mImagePath, p->ImageIO.read(p));
		}

		if (image == null)
		{
			image = aEditor.getImageResource(this);
		}

		if (image != null)
		{
			aGraphics.drawImage(image, mBounds.x, mBounds.y, mBounds.width, mBounds.height, null);
		}
	}


//	@Override
//	public void readExternal(Bundle aBundle)
//	{
//		super.readExternal(aBundle);
//
//		mImagePath = aBundle.getString("path");
//	}
//
//
//	@Override
//	public void writeExternal(Bundle aBundle)
//	{
//		super.writeExternal(aBundle);
//
//		if (mImagePath != null)
//		{
//			aBundle.putString("path", mImagePath);
//		}
//	}
}
