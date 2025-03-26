package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import static java.awt.Transparency.OPAQUE;
import java.awt.image.BufferedImage;
import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import static org.terifan.nodeeditor.Styles.CHECKERS_BRIGHT;
import static org.terifan.nodeeditor.Styles.CHECKERS_DARK;
import org.terifan.vecmath.Vec2i;
import org.terifan.vecmath.Vec4d;


/**
 * An ImageProperty reflects a series of values as an image.
 */
public class ImageProperty extends Property<ImageProperty>
{
	private static final long serialVersionUID = 1L;

	protected transient BufferedImage mImage;

	protected int mWidth;
	protected int mHeight;


	public ImageProperty(String aText, int aWidth, int aHeight)
	{
		super(aText);

		mWidth = aWidth;
		mHeight = aHeight;
		mPreferredSize.setSize(aWidth, aHeight);
	}


	public BufferedImage getImage()
	{
		return mImage;
	}


	public ImageProperty setImage(BufferedImage aImage)
	{
		mImage = aImage;
		return this;
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		Rectangle bounds = getBounds();

		if (mImage == null || mImage.getTransparency() != OPAQUE)
		{
			paintCheckers(aGraphics, bounds);
		}

		if (mImage != null)
		{
			aGraphics.drawImage(mImage, bounds.x, bounds.y, bounds.width, bounds.height, null);
		}
	}


	protected void paintCheckers(Graphics2D aGraphics, Rectangle aBounds)
	{
		int t = 10;
		double sx = aBounds.width / (double)t;
		double sy = aBounds.height / (double)t;
		aGraphics.setColor(CHECKERS_DARK);
		aGraphics.fillRect(aBounds.x, aBounds.y, aBounds.width, aBounds.height);
		aGraphics.setColor(CHECKERS_BRIGHT);
		for (int y = 0; y < t; y++)
		{
			for (int x = y & 1; x < t; x += 2)
			{
				aGraphics.fillRect(aBounds.x + (int)(x * sx), aBounds.y + (int)(y * sy), (int)sx, (int)sy);
			}
		}
	}


//	@Override
//	public <T> T execute(Context aContext)
//	{
//		Vec2i cp = aContext.execute("coord");
//		Vec4d argb = aContext.execute("color");
//		ImageProperty ip = aContext.property("image");
//
//		ip.getImage().setRGB(cp.x, cp.y, argb.intValue());
//
//		return null;
//	}
}
