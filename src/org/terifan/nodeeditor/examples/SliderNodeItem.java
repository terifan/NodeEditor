package org.terifan.nodeeditor.examples;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Styles;
import org.terifan.nodeeditor.NodeItem;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class SliderNodeItem extends NodeItem
{
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
	protected void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, Rectangle aBounds)
	{
//		aGraphics.setColor(new Color(114, 114, 114));
//		aGraphics.drawRoundRect(aBounds.x, aBounds.y + 1, aBounds.width, aBounds.height - 3, 15, 15);

		aGraphics.setColor(new Color(48, 48, 48));
		aGraphics.drawRoundRect(aBounds.x, aBounds.y, aBounds.width, aBounds.height - 3, 15, 15);

		aGraphics.setColor(new Color(178, 178, 178));
		aGraphics.fillRoundRect(aBounds.x, aBounds.y, aBounds.width, aBounds.height - 3, 15, 15);

		Shape oldClip = aGraphics.getClip();
		Paint oldPaint = aGraphics.getPaint();

		int v = (int)(aBounds.width * mValue);
		aGraphics.setClip(aBounds.x, aBounds.y, v, aBounds.height);
		aGraphics.setPaint(new LinearGradientPaint(0, aBounds.y, 0, aBounds.y+aBounds.height, new float[]{0f,1f}, new Color[]{new Color(126, 126, 126),new Color(107,107,107)}));
		aGraphics.fillRoundRect(aBounds.x, aBounds.y, aBounds.width, aBounds.height - 3, 15, 15);

		aGraphics.setClip(oldClip);
		aGraphics.setPaint(oldPaint);
		
		new TextBox(mName).setBounds(aBounds).setAnchor(Anchor.WEST).setMargins(0, 5, 0, 0).setForeground(Styles.BOX_FOREGROUND_COLOR).render(aGraphics);
	}
}
