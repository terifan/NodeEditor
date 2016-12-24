package org.terifan.nodeeditor.examples;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
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
	
	
	public SliderNodeItem(String aText, double aMin, double aMax, double aValue, Connector... aConnectors)
	{
		super(aText, aConnectors);

		mMin = aMin;
		mMax = aMax;
		mValue = aValue;
	}


	@Override
	protected void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, Rectangle aBounds, boolean aHover, boolean aArmed)
	{
		int i = aHover ? 1 : aArmed ? 2 : 0;

		Shape oldClip = aGraphics.getClip();
		Paint oldPaint = aGraphics.getPaint();

		aGraphics.setColor(new Color(48, 48, 48));
		aGraphics.fillRoundRect(aBounds.x, aBounds.y, aBounds.width, aBounds.height - 1, 15, 15);

		aGraphics.setPaint(new LinearGradientPaint(0, aBounds.y, 0, aBounds.y + aBounds.height, RANGES, Styles.SLIDER_COLORS[i][0]));
		aGraphics.fillRoundRect(aBounds.x + 1, aBounds.y + 1, aBounds.width - 2, aBounds.height - 2 - 1, 15, 15);

		aGraphics.setClip(aBounds.x, aBounds.y, 7 + (int)((aBounds.width - 7) * mValue), aBounds.height);
		aGraphics.setPaint(new LinearGradientPaint(0, aBounds.y, 0, aBounds.y + aBounds.height, RANGES, Styles.SLIDER_COLORS[i][1]));
		aGraphics.fillRoundRect(aBounds.x + 1, aBounds.y + 1, aBounds.width - 2, aBounds.height - 2 - 1, 15, 15);

		aGraphics.setClip(oldClip);
		aGraphics.setPaint(oldPaint);

		Rectangle m = new TextBox("" + mValue).setBounds(aBounds).setAnchor(Anchor.EAST).setMargins(0, 0, 0, 10).setForeground(Styles.SLIDER_COLORS[i][2][0]).setMaxLineCount(1).render(aGraphics).measure();

		new TextBox(mName).setSuffix(":").setBounds(aBounds).setAnchor(Anchor.WEST).setMargins(0, 10, 0, m.width).setForeground(Styles.SLIDER_COLORS[i][2][1]).setMaxLineCount(1).render(aGraphics);
	}
}
