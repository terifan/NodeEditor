package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.HashMap;
import java.util.List;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import static org.terifan.nodeeditor.Styles.FIELD_CORNER;
import org.terifan.nodeeditor.graphics.Arrow;


public class SliderProperty extends Property<SliderProperty>
{
	private final static long serialVersionUID = 1L;

	private final static float[] RANGES = new float[]
	{
		0f, 1f
	};

	private double mMin;
	private double mMax;
	private double mValue;
	private double mStep;
	private transient double mStartValue;
	private transient boolean mArmed;

//	private String[] mIds;


	public SliderProperty(String aText, double aValue, double aStepSize)
	{
		this(aText, 0.0, 1.0, aValue);

		mStep = aStepSize;
	}


	public SliderProperty(String aText, double aMin, double aMax, double aValue)
	{
		super(aText);

		mMin = aMin;
		mMax = aMax;
		mValue = aValue;
		getPreferredSize().height = 20;
	}


	public double getValue()
	{
		return mValue;
	}


	public SliderProperty setValue(double aValue)
	{
		mValue = aValue;
		return this;
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		TextBox textBox = getTextBox();
		Rectangle bounds = getBounds();

		if (isConnected(Direction.IN))
		{
			textBox.setMargins(0, 0, 0, 0).setSuffix("").setBounds(bounds).setAnchor(Anchor.WEST).setMargins(0, 0, 0, 0).setForeground(Styles.SLIDER_COLORS[0][2][1]).setMaxLineCount(1).setFont(Styles.BOX_ITEM_FONT).render(aGraphics);
		}
		else
		{
			int x = bounds.x;
			int y = bounds.y;
			int h = bounds.height;
			int w = bounds.width;
			int i = aHover ? 1 : mArmed ? 2 : 0;

			Paint oldPaint = aGraphics.getPaint();

			aGraphics.setColor(Styles.SLIDER_BORDER_COLOR);
			aGraphics.fillRoundRect(x, y, w, h, FIELD_CORNER, FIELD_CORNER);

			aGraphics.setPaint(new LinearGradientPaint(0, y, 0, y + h, RANGES, Styles.SLIDER_COLORS[i][0]));
			aGraphics.fillRoundRect(x + 1, y + 1, w - 2, h - 2, FIELD_CORNER, FIELD_CORNER);

			if (mStep == 0)
			{
				Shape oldClip = aGraphics.getClip();

				aGraphics.setClip(x, y, 7 + (int)((w - 7) * mValue), h);
				aGraphics.setPaint(new LinearGradientPaint(0, y, 0, y + h, RANGES, Styles.SLIDER_COLORS[i][1]));
				aGraphics.fillRoundRect(x + 1, y + 1, w - 2, h - 2, FIELD_CORNER, FIELD_CORNER);
				aGraphics.setClip(oldClip);
			}
			else
			{
				Arrow.paintArrow(aGraphics, 3, x+8, y+h/2, 3, 3, Styles.BOX_FOREGROUND_SHADOW_COLOR, Styles.BOX_FOREGROUND_COLOR);
				Arrow.paintArrow(aGraphics, 1, x+w-8, y+h/2, 3, 3, Styles.BOX_FOREGROUND_SHADOW_COLOR, Styles.BOX_FOREGROUND_COLOR);
			}

			aGraphics.setPaint(oldPaint);

			Rectangle m = new TextBox(String.format("%3.3f", mValue)).setBounds(bounds).setAnchor(Anchor.EAST).setMargins(0, 0, 0, 15).setForeground(Styles.SLIDER_COLORS[i][2][0]).setMaxLineCount(1).setFont(Styles.SLIDER_FONT).render(aGraphics).measure();

			textBox.setBounds(bounds).setAnchor(Anchor.WEST).setMargins(0, 15, 0, m.width).setForeground(Styles.SLIDER_COLORS[i][2][1]).setMaxLineCount(1).setFont(Styles.SLIDER_FONT).render(aGraphics);
		}
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aPane, Point aClickPoint)
	{
		if (!isConnected(Direction.IN))
		{
			mArmed = true;
			mStartValue = mValue;
			aPane.repaint();
			return true;
		}

		return false;
	}


	@Override
	protected void mouseReleased(NodeEditorPane aPane, Point aClickPoint)
	{
		if (!isConnected(Direction.IN))
		{
			mArmed = false;
//			if (mStartValue != mValue)
//			{
//				if (mOnChangeListener != null)
//				{
//					mOnChangeListener.onChange(this, false);
//				}
//			}
			aPane.repaint();
		}
	}


	@Override
	protected void mouseDragged(NodeEditorPane aPane, Point aClickPoint, Point aDragPoint)
	{
		if (!isConnected(Direction.IN))
		{
			if (mStep == 0)
			{
				double delta = (aDragPoint.x - aClickPoint.x) / (double)getBounds().width;
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
			aPane.repaint();
		}
	}


	@Override
	public Object execute()
	{
		Connector in = getConnector(Direction.IN);

		if (in != null && !in.getConnectedProperties().isEmpty())
		{
			return in.getConnectedProperties().get(0).execute();
		}

		return mValue;
	}
}
