package org.terifan.nodeeditor.widgets;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.FIELD_BACKGROUND_COLOR;
import org.terifan.ui.Anchor;
import static org.terifan.nodeeditor.Styles.FIELD_BACKGROUND_SELECTED_COLOR;
import static org.terifan.nodeeditor.Styles.FIELD_CORNER;


public class CheckBoxProperty extends Property<CheckBoxProperty>
{
	private final static long serialVersionUID = 1L;

	private final static float[] RANGES = new float[]
	{
		0f, 1f
	};

	private boolean mSelected;


	public CheckBoxProperty(String aText, boolean aState)
	{
		super(aText);

		mSelected = aState;
		mTextBox.setMargins(2, 0, 2, 0);
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		int x = getBounds().x;
		int y = getBounds().y;
		int h = getBounds().height;
		int sx = x + 1;
		int sy = y + 1;
		int ss = h - 1;

		Paint oldPaint = aGraphics.getPaint();

		aGraphics.setColor(mSelected ? FIELD_BACKGROUND_SELECTED_COLOR : FIELD_BACKGROUND_COLOR);
		aGraphics.fillRoundRect(x, sy, ss, ss, FIELD_CORNER, FIELD_CORNER);

		sx++;
		sy++;
		ss -= 2;

		aGraphics.setPaint(new LinearGradientPaint(x, sy, x, sy + ss, RANGES, Styles.CHECKBOX_COLORS[mSelected ? 1 : 0]));
		aGraphics.fillRoundRect(x, sy, ss, ss, FIELD_CORNER, FIELD_CORNER);

		if (mSelected)
		{
			Stroke stroke = aGraphics.getStroke();

			aGraphics.setColor(Styles.BOX_FOREGROUND_SELECTED_COLOR);
			aGraphics.setStroke(new BasicStroke(2));

			aGraphics.drawPolyline(new int[]
			{
				sx + ss * 7 / 38, sx + ss * 14 / 38, sx + ss * 28 / 38
			}, new int[]
			{
				sy + ss * 22 / 38, sy + ss * 30 / 38, sy + ss * 14 / 38
			}, 3);

			aGraphics.setStroke(stroke);
		}

		aGraphics.setPaint(oldPaint);

		mTextBox
			.setBounds(getBounds())
			.setAnchor(Anchor.WEST)
			.setMargins(3, ss + 5, 0, 0)
			.setForeground(mSelected ? Styles.BOX_FOREGROUND_SELECTED_COLOR : Styles.BOX_FOREGROUND_COLOR)
			.setFont(Styles.SLIDER_FONT)
			.render(aGraphics);
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aPane, Point aClickPoint)
	{
		mSelected = !mSelected;
		aPane.repaint();
		return true;
	}
}
