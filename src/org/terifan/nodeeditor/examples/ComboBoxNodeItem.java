package org.terifan.nodeeditor.examples;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Styles;
import org.terifan.nodeeditor.NodeItem;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class ComboBoxNodeItem extends NodeItem
{
	private final static float[] RANGES = new float[]{0f,1f};
	private boolean mArmed;


	public ComboBoxNodeItem(String aText, Connector... aConnectors)
	{
		super(aText, aConnectors);
		
		mSize.height = 21;
	}


	@Override
	protected void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, boolean aHover)
	{
		int x = mBounds.x;
		int y = mBounds.y;
		int h = mBounds.height;
		int w = mBounds.width;

		Paint oldPaint = aGraphics.getPaint();

		aGraphics.setColor(Styles.SLIDER_BORDER_COLOR);
		aGraphics.fillRoundRect(x, y, w, h, 4, 4);

		aGraphics.setPaint(new LinearGradientPaint(0, y + 1, 0, y + h - 2, RANGES, Styles.CHECKBOX_COLORS[mArmed ? 1 : 0]));
		aGraphics.fillRoundRect(x + 1, y + 1, w - 2, h - 2, 4, 4);

		int pw = 2;
		int ph = 4;
		int ax = x + w - 7;
		int ay = y + h / 2;
		int[] px = new int[]{ax - pw, ax, ax + pw};

		aGraphics.setColor(Styles.COMBOBOX_ARROW_COLOR);
		aGraphics.fillPolygon(px, new int[]{ay-1, ay-ph, ay-1}, 3);
		aGraphics.fillPolygon(px, new int[]{ay+1, ay+ph, ay+1}, 3);
		
		aGraphics.setPaint(oldPaint);

		new TextBox(mName).setBounds(mBounds).setAnchor(Anchor.WEST).setMargins(0, 8, 0, 15).setForeground(Styles.BOX_FOREGROUND_SELECTED_COLOR).setMaxLineCount(1).setFont(Styles.SLIDER_FONT).render(aGraphics);
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aEditorPane, Point aClickPoint)
	{
		mArmed = true;
		aEditorPane.repaint();
		return true;
	}


	@Override
	protected void mouseReleased(NodeEditorPane aEditorPane, Point aClickPoint)
	{
		mArmed = false;
		aEditorPane.repaint();
	}
}
