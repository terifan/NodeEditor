package org.terifan.nodeeditor.examples;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Styles;
import org.terifan.nodeeditor.NodeItem;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class SliderNodeItem extends NodeItem
{
	private final static float[] RANGES = new float[]{0f,1f};

	private double mMin;
	private double mMax;
	private double mValue;
	private double mStartValue;
	private boolean mArmed;
	private Double mStepSize;


	public SliderNodeItem(String aText, double aValue, double aStepSize, Connector... aConnectors)
	{
		this(aText, Double.MIN_VALUE, Double.MAX_VALUE, aValue, aConnectors);

		mStepSize = aStepSize;
		mSize.height = 20;
	}


	public SliderNodeItem(String aText, double aMin, double aMax, double aValue, Connector... aConnectors)
	{
		super(aText, aConnectors);

		mMin = aMin;
		mMax = aMax;
		mValue = aValue;
		mSize.height = 20;
	}


	@Override
	protected void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, boolean aHover)
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

		if (mStepSize == null)
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
			int[] py = new int[]{y + h / 2, y + h / 2 - ph, y + h / 2 + ph};

			aGraphics.setColor(Styles.SLIDER_ARROW_COLOR);
			aGraphics.fillPolygon(new int[]{x + s, x + s + pw, x + s + pw}, py, 3);
			aGraphics.fillPolygon(new int[]{x + w - s, x + w - s - pw, x + w - s - pw}, py, 3);
		}

		aGraphics.setPaint(oldPaint);

		Rectangle m = new TextBox(String.format("%3.3f", mValue)).setBounds(mBounds).setAnchor(Anchor.EAST).setMargins(0, 0, 0, 15).setForeground(Styles.SLIDER_COLORS[i][2][0]).setMaxLineCount(1).setFont(Styles.SLIDER_FONT).render(aGraphics).measure();

		new TextBox(mName).setSuffix(":").setBounds(mBounds).setAnchor(Anchor.WEST).setMargins(0, 15, 0, m.width).setForeground(Styles.SLIDER_COLORS[i][2][1]).setMaxLineCount(1).setFont(Styles.SLIDER_FONT).render(aGraphics);
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aEditorPane, Point aClickPoint)
	{
		mArmed = true;
		mStartValue = mValue;
		aEditorPane.repaint();
		return true;
	}


	@Override
	protected void mouseReleased(NodeEditorPane aEditorPane, Point aClickPoint)
	{
		mArmed = false;
		aEditorPane.repaint();
	}


	@Override
	protected void mouseDragged(NodeEditorPane aEditorPane, Point aClickPoint, Point aDragPoint)
	{
		if (mStepSize == null)
		{
			double delta = (aDragPoint.x - aClickPoint.x) / (double)mBounds.width;
			mValue = Math.max(mMin, Math.min(mMax, mStartValue + delta));
		}
		else
		{
			double delta = (aDragPoint.x - aClickPoint.x) * mStepSize;
			mValue = mStartValue + delta;
		}
		aEditorPane.repaint();
	}
}
