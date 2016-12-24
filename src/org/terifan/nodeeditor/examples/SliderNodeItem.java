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
	
	
	public SliderNodeItem(String aText, double aMin, double aMax, double aValue, Connector... aConnectors)
	{
		super(aText, aConnectors);

		mMin = aMin;
		mMax = aMax;
		mValue = aValue;
	}


	@Override
	protected void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, boolean aHover, boolean aArmed)
	{
		int i = aHover ? 1 : mArmed ? 2 : 0;

		Shape oldClip = aGraphics.getClip();
		Paint oldPaint = aGraphics.getPaint();

		aGraphics.setColor(new Color(48, 48, 48));
		aGraphics.fillRoundRect(mBounds.x, mBounds.y, mBounds.width, mBounds.height - 1, 15, 15);

		aGraphics.setPaint(new LinearGradientPaint(0, mBounds.y, 0, mBounds.y + mBounds.height, RANGES, Styles.SLIDER_COLORS[i][0]));
		aGraphics.fillRoundRect(mBounds.x + 1, mBounds.y + 1, mBounds.width - 2, mBounds.height - 2 - 1, 15, 15);

		aGraphics.setClip(mBounds.x, mBounds.y, 7 + (int)((mBounds.width - 7) * mValue), mBounds.height);
		aGraphics.setPaint(new LinearGradientPaint(0, mBounds.y, 0, mBounds.y + mBounds.height, RANGES, Styles.SLIDER_COLORS[i][1]));
		aGraphics.fillRoundRect(mBounds.x + 1, mBounds.y + 1, mBounds.width - 2, mBounds.height - 2 - 1, 15, 15);

		aGraphics.setClip(oldClip);
		aGraphics.setPaint(oldPaint);

		Rectangle m = new TextBox(String.format("%3.3f", mValue)).setBounds(mBounds).setAnchor(Anchor.EAST).setMargins(0, 0, 0, 10).setForeground(Styles.SLIDER_COLORS[i][2][0]).setMaxLineCount(1).render(aGraphics).measure();

		new TextBox(mName).setSuffix(":").setBounds(mBounds).setAnchor(Anchor.WEST).setMargins(0, 10, 0, m.width).setForeground(Styles.SLIDER_COLORS[i][2][1]).setMaxLineCount(1).render(aGraphics);
	}


	@Override
	protected void mouseClicked(NodeEditorPane aEditorPane, Point aClickPoint)
	{
		mArmed = true;
		mStartValue = mValue;
		aEditorPane.repaint();
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
		mValue = Math.max(mMin, Math.min(mMax, mStartValue + (aDragPoint.x - aClickPoint.x) / (double)mBounds.width));
		aEditorPane.repaint();
	}
}
