package org.terifan.nodeeditor.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import javax.swing.JColorChooser;
import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.FIELD_CORNER;
import org.terifan.vecmath.Vec4d;


public class RGBPaletteProperty extends Property<RGBPaletteProperty>
{
	private final static long serialVersionUID = 1L;

	private static final BasicStroke BASIC_STROKE_05 = new BasicStroke(0.5f);
	private static final BasicStroke BASIC_STROKE_1 = new BasicStroke(1f);
	private static final BasicStroke BASIC_STROKE_2 = new BasicStroke(2f);
	private static final Color HANDLE_BRIGHT = new Color(255, 255, 255);
	private static final Color HANDLE_DARK = new Color(0, 0, 0, 224);
	private static final Color TARGET_BRIGHT = new Color(255, 255, 255);
	private static final Color TARGET_DARK = new Color(0, 0, 0);

	private transient int mArmed;
	private transient int mHSBCircleOffet;
	private transient BufferedImage mHSBCircleImage;
	private transient BufferedImage mHSBCircleLookup;
	private transient BufferedImage mBrightnessBarImage;
	private transient float mHSBCircleImageKey = -1f;

	protected int mSize;
	protected int mBrightnessWidth;
	protected int mButtonHeight;
	protected Vec4d mColor;
	protected float[] mHSB;


	public RGBPaletteProperty(Vec4d aColor)
	{
		setColor(aColor);

		mSize = 100;
		mButtonHeight = 20;
		mBrightnessWidth = 15;
		mPreferredSize.setSize(200, 250);
	}


	public Vec4d getColor()
	{
		return mColor;
	}


	public RGBPaletteProperty setColor(Vec4d aColor)
	{
		mColor = aColor;

		if (mHSB == null)
		{
			mHSB = new float[3];
		}

		mHSB = Color.RGBtoHSB((int)(255 * mColor.x + 0.5), (int)(255 * mColor.y + 0.5), (int)(255 * mColor.z + 0.5), mHSB);
		return this;
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		mPreferredSize.setSize(mSize + 30, mSize + 20);

		Stroke s = aGraphics.getStroke();

		if (mHSB[2] != mHSBCircleImageKey)
		{
			createHSBCircle(mSize, mHSBCircleImageKey = mHSB[2]);
		}
		if (mBrightnessBarImage == null || mBrightnessBarImage.getHeight() != mSize)
		{
			mBrightnessBarImage = createBrightnessBar(mBrightnessWidth, mSize);
		}

		Rectangle bounds = getBounds();

		int x = bounds.x;
		int y = bounds.y;
		int w = bounds.width;

		mHSBCircleOffet = x + ((w - 20) - mSize) / 2;

		aGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		aGraphics.drawImage(mHSBCircleImage, mHSBCircleOffet, y, mSize, mSize, null);
		aGraphics.drawImage(mBrightnessBarImage, x + w - mBrightnessBarImage.getWidth(), y, mBrightnessBarImage.getWidth(), mSize, null);

		aGraphics.setStroke(BASIC_STROKE_2);
		aGraphics.setColor(Styles.BOX_BACKGROUND_COLOR);
		aGraphics.translate(0.5, 0.5);
		aGraphics.drawOval(mHSBCircleOffet, y, mSize, mSize);
		aGraphics.translate(-0.5, -0.5);

		aGraphics.setColor(Color.getHSBColor(mHSB[0], mHSB[1], mHSB[2]));
		aGraphics.fillRoundRect(x + 1, y + mSize + 5, w - 2, mButtonHeight, FIELD_CORNER, FIELD_CORNER);

		paintHandle(aGraphics, x, y, w);
		paintTarget(aGraphics, x, y);

		aGraphics.setStroke(s);
	}


	protected void paintTarget(Graphics2D aGraphics, int aX, int aY)
	{
		int ox = mHSBCircleOffet;
		int oy = aY;

		double dx = mHSB[0];
		double r = mHSB[1] * mSize / 2;
		ox += mSize / 2 - (int)(Math.sin(Math.PI * 2 * dx) * r);
		oy += mSize / 2 + (int)(Math.cos(Math.PI * 2 * dx) * r);

		aGraphics.setColor(TARGET_BRIGHT);
		aGraphics.setStroke(BASIC_STROKE_1);
		aGraphics.drawOval(ox - 6, oy - 6, 13, 13);
		aGraphics.setColor(TARGET_DARK);
		aGraphics.setStroke(BASIC_STROKE_05);
		aGraphics.drawOval(ox - 6, oy - 6, 13, 13);
	}


	private void paintHandle(Graphics2D aGraphics, int aX, int aY, int aW)
	{
		int bo = (int)((1 - mHSB[2]) * (mSize - 5));
		aGraphics.setStroke(BASIC_STROKE_1);
		aGraphics.setColor(HANDLE_DARK);
		aGraphics.drawRect(aX + aW - mBrightnessWidth - 1, aY + bo - 1, mBrightnessWidth + 2, 5 + 2);
		aGraphics.setColor(HANDLE_BRIGHT);
		aGraphics.drawRect(aX + aW - mBrightnessWidth, aY + bo, mBrightnessWidth, 5);
	}


	protected BufferedImage createBrightnessBar(int aWidth, int aHeight)
	{
		BufferedImage strength = new BufferedImage(aWidth, aHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = strength.createGraphics();
		for (int i = 0; i < aHeight; i++)
		{
			int c = 255 - i * 255 / aHeight;
			g.setColor(new Color(c, c, c));
			g.drawLine(0, i, aWidth, i);
		}
		g.dispose();
		return strength;
	}


	protected void createHSBCircle(int aSize, float aBrightness)
	{
		int s = aSize;
		float q = s / 100f;
		BufferedImage circle = new BufferedImage(s, s, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < s; y++)
		{
			for (int x = 0; x < 360 * q; x++)
			{
				float dx = x / 360f / q;
				float dy = y / (float)s;
				double f = Math.PI * 2 * dx;
				double r = y / 2.0;
				int ix = s / 2 - (int)(Math.sin(f) * r);
				int iy = s / 2 + (int)(Math.cos(f) * r);
				int rgb = Color.getHSBColor(dx, dy, aBrightness).getRGB();
				circle.setRGB(ix, iy, rgb);
			}
		}
		BufferedImage lookup = new BufferedImage(s, s, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = lookup.createGraphics();
		for (int i = s / 4; i >= 0; i--)
		{
			g.drawImage(circle, -i, -i, s + i, s + i, 0, 0, s, s, null);
		}
		g.drawImage(circle, 0, 0, null);
		g.dispose();
		mHSBCircleImage = circle;
		mHSBCircleLookup = lookup;
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aPane, Point aClickPoint)
	{
		handleMouse(aPane, aClickPoint, true);
		return true;
	}


	@Override
	protected void mouseDragged(NodeEditorPane aPane, Point aClickPoint, Point aDragPoint)
	{
		handleMouse(aPane, aDragPoint, false);
	}


	private void handleMouse(NodeEditorPane aPane, Point aPoint, boolean aPressed)
	{
		try
		{
			Rectangle cb = getBounds();
			Rectangle nb = mNode.getBounds();
			int x = aPoint.x - cb.x - nb.x;
			int y = aPoint.y - cb.y - nb.y;

			if (y > mSize)
			{
				if (aPressed)
				{
					Vec4d color = openColorChooser(aPane);
					if (color != null)
					{
						setColor(color);
						aPane.repaint();
					}
				}
			}
			else if (mArmed == 2 || mArmed == 0 && x > cb.width - 20)
			{
				mArmed = 2;
				mHSB[2] = 1f - (float)Math.min(1, Math.max(0, y / (double)mSize));
				setColor(new Vec4d().set(Color.getHSBColor(mHSB[0], mHSB[1], mHSB[2]).getRGB()));
				aPane.repaint();
			}
			else if (mArmed != 2)
			{
				mArmed = 1;
				x -= mHSBCircleOffet - cb.x;
				int iw = mHSBCircleImage.getWidth();
				int rgb = mHSBCircleLookup.getRGB(Math.max(0, Math.min(iw - 1, x * iw / mSize)), Math.max(0, Math.min(iw - 1, y * iw / mSize)));
				setColor(new Vec4d().set(rgb));
				aPane.repaint();
			}
		}
		catch (Exception e)
		{
			System.out.println("#");
			// ignore
		}
	}


	@Override
	protected void mouseReleased(NodeEditorPane aPane, Point aClickPoint)
	{
		mArmed = 0;
		aPane.repaint();
	}


	protected Vec4d openColorChooser(NodeEditorPane aPane) throws HeadlessException
	{
		return new Vec4d().set(JColorChooser.showDialog(aPane, "", new Color(mColor.intValue())).getRGB());
	}


	@Override
	public Object execute(Context aContext)
	{
		return mColor.clone();
	}


	@Override
	protected void printJava()
	{
		System.out.print("\t\t.addProperty(new " + getClass().getSimpleName() + "(" + colorToJava(mColor) + ")");
		super.printJava();
	}
}
