package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
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

	private transient double mStartValue;
	private transient boolean mArmed;

	private TextBox mValueTextBox;
	private double mMin;
	private double mMax;
	private double mValue;
	private double mStep;


	public SliderProperty()
	{
		mTextBox.setMargins(4, 0, 4, 0).setFont(Styles.SLIDER_FONT).setMaxLineCount(1).setAnchor(Anchor.WEST);
		mValueTextBox = new TextBox("").setAnchor(Anchor.EAST).setMargins(0, 0, 0, 15).setMaxLineCount(1).setFont(Styles.SLIDER_FONT);
	}


	public SliderProperty(String aText)
	{
		this(aText, 0, 0.001);
	}


	public SliderProperty(String aText, double aValue, double aStep)
	{
		this();

		mMin = -Float.MAX_VALUE;
		mMax = Float.MAX_VALUE;
		mValue = aValue;
		mStep = aStep;
		setText(aText);
	}


	public SliderProperty setRange(double aMin, double aMax, double aValue, double aStep)
	{
		mMin = aMin;
		mMax = aMax;
		mValue = aValue;
		mStep = aStep;
		return this;
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


	public double getMin()
	{
		return mMin;
	}


	public SliderProperty setMin(double aMin)
	{
		mMin = aMin;
		return this;
	}


	public double getMax()
	{
		return mMax;
	}


	public SliderProperty setMax(double aMax)
	{
		mMax = aMax;
		return this;
	}


	public double getStep()
	{
		return mStep;
	}


	public SliderProperty setStep(double aStep)
	{
		mStep = aStep;
		return this;
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		Rectangle bounds = getBounds();

		if (isConnected(Direction.IN))
		{
			mTextBox.setMargins(4, 0, 4, 0).setSuffix("").setBounds(bounds).setForeground(Styles.SLIDER_COLORS[0][2][1]).setFont(Styles.BOX_ITEM_FONT).render(aGraphics);
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
				Arrow.paintArrow(aGraphics, 3, x + 8, y + h / 2, 3, 3, Styles.BOX_FOREGROUND_SHADOW_COLOR, Styles.BOX_FOREGROUND_COLOR);
				Arrow.paintArrow(aGraphics, 1, x + w - 8, y + h / 2, 3, 3, Styles.BOX_FOREGROUND_SHADOW_COLOR, Styles.BOX_FOREGROUND_COLOR);
			}

			aGraphics.setPaint(oldPaint);

			String value;
			if (mStep >= 1 && mValue == (long)mValue)
			{
				value = String.format("%d", (long)mValue);
			}
			else if (mStep == 0)
			{
				value = String.format("%3.1f", mValue);
			}
			else if (mStep < 0.000001)
			{
				value = String.format("%3.7f", mValue);
			}
			else if (mStep < 0.00001)
			{
				value = String.format("%3.6f", mValue);
			}
			else if (mStep < 0.0001)
			{
				value = String.format("%3.5f", mValue);
			}
			else if (mStep < 0.001)
			{
				value = String.format("%3.4f", mValue);
			}
			else if (mStep < 0.01)
			{
				value = String.format("%3.3f", mValue);
			}
			else if (mStep < 0.1)
			{
				value = String.format("%3.2f", mValue);
			}
			else
			{
				value = String.format("%3.8f", mValue);
			}

			if (mTextBox.getText().isEmpty())
			{
				mValueTextBox.setAnchor(Anchor.CENTER).setMargins(0, 0, 0, 0).setForeground(Styles.SLIDER_COLORS[i][2][0]).setBounds(bounds).setText(value).render(aGraphics).measure();
			}
			else
			{
				Rectangle m = mValueTextBox.setAnchor(Anchor.EAST).setMargins(0, 0, 0, 15).setForeground(Styles.SLIDER_COLORS[i][2][0]).setBounds(bounds).setText(value).render(aGraphics).measure();
				mTextBox.setBounds(bounds).setMargins(4, 15, 4, m.width).setForeground(Styles.SLIDER_COLORS[i][2][1]).render(aGraphics);
			}
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

			mValue = Math.max(Math.min(mValue, mMax), mMin);

			aPane.repaint();
		}
	}


	@Override
	public Object execute(Context aContext)
	{
		Object value = super.execute(aContext);

		if (value != null)
		{
			return value;
		}

		return mValue;
	}
}
