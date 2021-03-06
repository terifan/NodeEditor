package org.terifan.nodeeditor;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import org.terifan.bundle.Bundle;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class SliderNodeItem extends AbstractNodeItem<SliderNodeItem>
{
	private final static long serialVersionUID = 1L;

	private final static float[] RANGES = new float[]
	{
		0f, 1f
	};

	private double mMin;
	private double mMax;
	private double mValue;
	private double mStartValue;
	private boolean mArmed;
	private double mStep;


	protected SliderNodeItem()
	{
		super();

		mPreferredSize.height = 20;
	}


	public SliderNodeItem(String aText, double aValue, double aStepSize)
	{
		this(aText, 0.0, 1.0, aValue);

		mStep = aStepSize;
	}


	public SliderNodeItem(String aText, double aMin, double aMax, double aValue)
	{
		super(aText);

		mMin = aMin;
		mMax = aMax;
		mValue = aValue;
		mPreferredSize.height = 20;
	}


	public double getValue()
	{
		return mValue;
	}


	public SliderNodeItem setValue(double aValue)
	{
		mValue = aValue;
		return this;
	}


	@Override
	protected void paintComponent(NodeEditor aEditor, Graphics2D aGraphics, boolean aHover)
	{
		if (isConnected(Direction.IN))
		{
			mTextBox.setMargins(0, 0, 0, 0);

			mTextBox.setSuffix("").setBounds(mBounds).setAnchor(Anchor.WEST).setMargins(0, 0, 0, 0).setForeground(Styles.SLIDER_COLORS[0][2][1]).setMaxLineCount(1).setFont(Styles.BOX_ITEM_FONT).render(aGraphics);
		}
		else
		{
			int x = mBounds.x;
			int y = mBounds.y;
			int h = mBounds.height;
			int w = mBounds.width;
			int i = aHover ? 1 : mArmed ? 2 : 0;

			Paint oldPaint = aGraphics.getPaint();

			aGraphics.setColor(Styles.SLIDER_BORDER_COLOR);
			aGraphics.fillRoundRect(x, y, w, h, 18, 18);

			aGraphics.setPaint(new LinearGradientPaint(0, y, 0, y + h, RANGES, Styles.SLIDER_COLORS[i][0]));
			aGraphics.fillRoundRect(x + 1, y + 1, w - 2, h - 2, 18, 18);

			if (mStep == 0)
			{
				Shape oldClip = aGraphics.getClip();

				aGraphics.setClip(x, y, 7 + (int)((w - 7) * mValue), h);
				aGraphics.setPaint(new LinearGradientPaint(0, y, 0, y + h, RANGES, Styles.SLIDER_COLORS[i][1]));
				aGraphics.fillRoundRect(x + 1, y + 1, w - 2, h - 2, 18, 18);
				aGraphics.setClip(oldClip);
			}
			else
			{
				int pw = 4;
				int ph = 3;
				int s = 6;
				int[] py = new int[]
				{
					y + h / 2, y + h / 2 - ph, y + h / 2 + ph
				};

				aGraphics.setColor(Styles.SLIDER_ARROW_COLOR);
				aGraphics.fillPolygon(new int[]
				{
					x + s, x + s + pw, x + s + pw
				}, py, 3);
				aGraphics.fillPolygon(new int[]
				{
					x + w - s, x + w - s - pw, x + w - s - pw
				}, py, 3);
			}

			aGraphics.setPaint(oldPaint);

			Rectangle m = new TextBox(String.format("%3.3f", mValue)).setBounds(mBounds).setAnchor(Anchor.EAST).setMargins(0, 0, 0, 15).setForeground(Styles.SLIDER_COLORS[i][2][0]).setMaxLineCount(1).setFont(Styles.SLIDER_FONT).render(aGraphics).measure();

			mTextBox.setSuffix(":").setBounds(mBounds).setAnchor(Anchor.WEST).setMargins(0, 15, 0, m.width).setForeground(Styles.SLIDER_COLORS[i][2][1]).setMaxLineCount(1).setFont(Styles.SLIDER_FONT).render(aGraphics);
		}
	}


	@Override
	protected boolean mousePressed(NodeEditor aEditor, Point aClickPoint)
	{
		if (!isConnected(Direction.IN))
		{
			mArmed = true;
			mStartValue = mValue;
			aEditor.repaint();
			return true;
		}

		return false;
	}


	@Override
	protected void mouseReleased(NodeEditor aEditor, Point aClickPoint)
	{
		if (!isConnected(Direction.IN))
		{
			mArmed = false;
			if (mStartValue != mValue)
			{
//				if (mOnChangeListener != null)
//				{
//					mOnChangeListener.onChange(this, false);
//				}
			}
			aEditor.repaint();
		}
	}


	@Override
	protected void mouseDragged(NodeEditor aEditor, Point aClickPoint, Point aDragPoint)
	{
		if (!isConnected(Direction.IN))
		{
			if (mStep == 0)
			{
				double delta = (aDragPoint.x - aClickPoint.x) / (double)mBounds.width;
				mValue = Math.max(mMin, Math.min(mMax, mStartValue + delta));
			}
			else
			{
				double delta = (aDragPoint.x - aClickPoint.x) * mStep;
				mValue = mStartValue + delta;
			}

//			if (mOnChangeListener != null)
//			{
//				mOnChangeListener.onChange(this, true);
//			}
			aEditor.repaint();
		}
	}


//	@FunctionalInterface
//	public interface OnChangeListener
//	{
//		void onChange(SliderNodeItem aItem, boolean aValueIsAdjusting);
//	}


//	@Override
//	public void readExternal(Bundle aBundle)
//	{
//		super.readExternal(aBundle);
//
//		mMin = aBundle.getDouble("min", 0.0);
//		mMax = aBundle.getDouble("max", 1.0);
//		mValue = aBundle.getDouble("value", 0.0);
//		mStep = aBundle.getDouble("step", 0.0);
//
//		mPreferredSize.height = 20;
//	}
//
//
//	@Override
//	public void writeExternal(Bundle aBundle)
//	{
//		super.writeExternal(aBundle);
//
//		if (mMin != 0.0)
//		{
//			aBundle.putNumber("min", mMin);
//		}
//		if (mMax != 1.0)
//		{
//			aBundle.putNumber("max", mMax);
//		}
//		if (mValue != 0.0)
//		{
//			aBundle.putNumber("value", mValue);
//		}
//		if (mStep != 0.0)
//		{
//			aBundle.putNumber("step", mStep);
//		}
//	}
}
