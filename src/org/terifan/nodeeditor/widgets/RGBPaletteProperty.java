package org.terifan.nodeeditor.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import static org.terifan.nodeeditor.Styles.FIELD_CORNER;


public class RGBPaletteProperty extends Property<RGBPaletteProperty>
{
	private final static long serialVersionUID = 1L;
	private final static int S = 100;

	private transient boolean mArmed;

	protected Color mColor;
	protected float[] mHSB;


	public RGBPaletteProperty(Color aColor)
	{
		setColor(aColor);

		mPreferredSize.setSize(200, 250);
	}


	public Color getColor()
	{
		return mColor;
	}


	public RGBPaletteProperty setColor(Color aColor)
	{
		mColor = aColor;

		if (mHSB == null)
		{
			mHSB = new float[3];
		}

		mHSB = Color.RGBtoHSB(mColor.getRed(), mColor.getGreen(), mColor.getBlue(), mHSB);
		return this;
	}

	int mChartOffet;
	BufferedImage mChartImage;


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		mPreferredSize.setSize(S + 30, S + 20);

		Stroke s = aGraphics.getStroke();

		mChartImage = createHSBCircle();
		BufferedImage strength = createBrightnessBar();
		Rectangle bounds = getBounds();

		int x = bounds.x;
		int y = bounds.y;
		int w = bounds.width;
		mChartOffet = x + ((w - 20) - S) / 2;

		aGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		aGraphics.drawImage(mChartImage, mChartOffet, y, S, S, null);
		aGraphics.drawImage(strength, x + w - 15, y, 15, S, null);

		aGraphics.setColor(Color.getHSBColor(mHSB[0], mHSB[1], mHSB[2]));
		aGraphics.fillRoundRect(x + 1, y + S + 5, w - 2, 15, FIELD_CORNER, FIELD_CORNER);

		paintHandle(aGraphics, x, y, w);
		paintTarget(aGraphics, x, y);

		aGraphics.setStroke(s);
	}


	protected void paintTarget(Graphics2D aGraphics, int aX, int aY)
	{
		int ox = mChartOffet;
		int oy = aY;

		double dx = mHSB[0];
		double r = mHSB[1] * S / 2;
		ox += S / 2 - (int)(Math.sin(Math.PI * 2 * dx) * r);
		oy += S / 2 + (int)(Math.cos(Math.PI * 2 * dx) * r);

		aGraphics.setColor(new Color(255, 255, 255));
		aGraphics.setStroke(new BasicStroke(1f));
		aGraphics.drawOval(ox - 6, oy - 6, 13, 13);
		aGraphics.setColor(new Color(0, 0, 0));
		aGraphics.setStroke(new BasicStroke(0.5f));
		aGraphics.drawOval(ox - 6, oy - 6, 13, 13);
	}


	private void paintHandle(Graphics2D aGraphics, int aX, int aY, int aW)
	{
		int bo = (int)((1 - mHSB[2]) * (S - 5));
		aGraphics.setStroke(new BasicStroke(1f));
		aGraphics.setColor(new Color(0, 0, 0, 128));
		aGraphics.drawRect(aX + aW - 15 - 1, aY + bo - 1, 15 + 2, 5 + 2);
		aGraphics.setColor(Color.WHITE);
		aGraphics.drawRect(aX + aW - 15, aY + bo, 15, 5);
	}


	protected BufferedImage createBrightnessBar()
	{
		BufferedImage strength = new BufferedImage(15, 4, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = strength.createGraphics();
		g.setColor(Color.WHITE);
		g.drawLine(0, 0, 15, 0);
		g.setColor(new Color(160, 160, 160));
		g.drawLine(0, 1, 15, 1);
		g.setColor(new Color(128, 128, 128));
		g.drawLine(0, 2, 15, 2);
		g.setColor(Color.BLACK);
		g.drawLine(0, 3, 15, 3);
		g.dispose();
		return strength;
	}


	protected BufferedImage createHSBCircle()
	{
		int q = 2;
		int s = q * S;
		BufferedImage chart = new BufferedImage(s, s, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < s; y++)
		{
			for (int x = 0; x < q * 360; x++)
			{
				double dx = x / 360.0 / q;
				double r = y / 2.0;
				int _x = s / 2 - (int)(Math.sin(Math.PI * 2 * dx) * r);
				int _y = s / 2 + (int)(Math.cos(Math.PI * 2 * dx) * r);

				chart.setRGB(_x, _y, Color.getHSBColor(x / 360f / q, y / (float)s, (float)mHSB[2]).getRGB());
			}
		}
		return chart;
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aPane, Point aClickPoint)
	{
		mArmed = true;
		handleMouse(aPane, aClickPoint);
		return true;
	}


	@Override
	protected void mouseReleased(NodeEditorPane aPane, Point aClickPoint)
	{
		mArmed = false;
		aPane.repaint();
	}


	@Override
	protected void mouseDragged(NodeEditorPane aPane, Point aClickPoint, Point aDragPoint)
	{
		handleMouse(aPane, aDragPoint);
	}


	private void handleMouse(NodeEditorPane aPane, Point aPoint)
	{
		try
		{
			Rectangle cb = getBounds();
			Rectangle nb = mNode.getBounds();
			int x = aPoint.x - cb.x - nb.x;
			int y = aPoint.y - cb.y - nb.y;

			if (x > cb.width - 20)
			{
				mHSB[2] = 1f - (float)Math.min(1, Math.max(0, y / (double)S));
				aPane.repaint();
			}
			else
			{
				x -= mChartOffet - cb.x;

				if (x >= 0 && x < S && y >= 0 && y < S)
				{
					int s = mChartImage.getWidth();
					int q = s / S;
					x *= q;
					y *= q;
					int rgb = mChartImage.getRGB(x, y);

					if ((rgb >>> 24) == 255)
					{
						setColor(new Color(rgb));
					}
					else
					{
//						double l = Math.sqrt(Math.pow(x - s / 2, 2) + Math.pow(y - s / 2, 2));
//						System.out.println(l);
//						double dx = (x - s / 2) / ((double)s / 2);
//						double dy = (y - s / 2) / ((double)s / 2);
						double dx = (x - s / 2) / ((double)s / 2);
						double dy = (y - s / 2) / ((double)s / 2);
						System.out.println(dx+" "+dy);
						x = s/2+(int)(s/2 * dx);
						y = s/2+(int)(s/2 * dy);
						rgb = mChartImage.getRGB(x, y);
						if ((rgb >>> 24) == 255)
						{
							setColor(new Color(mChartImage.getRGB(x, y)));
						}
					}
					aPane.repaint();
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("#");
			// ignore
		}
	}


	@Override
	public Object execute(Context aContext)
	{
		return mColor;
	}


	@Override
	protected void printJava()
	{
		System.out.print("\t\t.addProperty(new " + getClass().getSimpleName() + "(" + colorToJava(mColor) + ")");
		super.printJava();
	}
}
