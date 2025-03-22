package org.terifan.nodeeditor.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;


/**
 * An ImageProperty reflects a series of values as an image.
 */
public class ImageProperty extends Property<ImageProperty>
{
	private static final long serialVersionUID = 1L;

	protected transient BufferedImage mRenderedImage;

	@Deprecated
	protected String mImagePath;

	protected int mWidth;
	protected int mHeight;
	protected int mType;
	protected String mSourceRGB;
	protected String mSourceAlpha;


	public ImageProperty(String aText, int aWidth, int aHeight)
	{
		super(aText);

		mWidth = aWidth;
		mHeight = aHeight;
		mType = BufferedImage.TYPE_INT_ARGB;

		setPreferredSize(new Dimension(aWidth, aHeight));
	}


	public ImageProperty setConsumes(String aRGBA)
	{
		mSourceRGB = aRGBA;
		mSourceAlpha = null;
		return this;
	}


	public ImageProperty setConsumes(String aRGB, String aAlpha)
	{
		mSourceRGB = aRGB;
		mSourceAlpha = aAlpha;
		return this;
	}


	public BufferedImage getRenderedImage()
	{
		return mRenderedImage;
	}


	@Deprecated
	public String getImagePath()
	{
		return mImagePath;
	}


	@Deprecated
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

		if (mRenderedImage != null)
		{
			aGraphics.drawImage(mRenderedImage, bounds.x, bounds.y, bounds.width, bounds.height, null);
		}

//		aPane.paintImage(this, aGraphics, bounds);
	}


	@Override
	public void execute(Context aContext)
	{
		mRenderedImage = new BufferedImage(mWidth, mHeight, mType);

		for (int y = 0; y < mHeight; y++)
		{
			for (int x = 0; x < mWidth; x++)
			{
				aContext.params.put("x", x / (double)mWidth);
				aContext.params.put("y", y / (double)mHeight);

				Property property = getNode().getProperty(mSourceRGB);
				property.execute(aContext);

				HashMap<String, Object> map = (HashMap<String,Object>)aContext.result;

				System.out.println(map);

				int r = (int)(255 * (Double)map.getOrDefault("r", 0.0));
				int g = (int)(255 * (Double)map.getOrDefault("g", 0.0));
				int b = (int)(255 * (Double)map.getOrDefault("b", 0.0));

				int alpha = 0xff000000;

//				if (mSourceAlpha != null)
//				{
//					property = getNode().getProperty(mSourceAlpha);
//					property.execute(aContext);
//
//					alpha = ((int)(255 * (Double)aContext.params.getOrDefault("alpha", 1.0))) << 24;
//				}

				mRenderedImage.setRGB(x, y, alpha | (r<<16)+(g<<8)+b);
			}
		}

		aContext.getEditor().repaint();
	}
}
