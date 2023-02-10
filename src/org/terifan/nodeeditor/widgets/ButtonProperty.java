package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;
import org.terifan.ui.Anchor;
import org.terifan.ui.ImageResizer;


public class ButtonProperty extends Property<ButtonProperty>
{
	private final static long serialVersionUID = 1L;
	private final static float[] RANGES = new float[]{0f,1f};

	private transient boolean mArmed;


	public ButtonProperty(String aText)
	{
		super(aText);

		getTextBox().setAnchor(Anchor.CENTER).setMargins(0, 0, 0, 0).setMaxLineCount(1).setFont(Styles.SLIDER_FONT);
		getPreferredSize().height = 22;
	}


	@Override
	public ButtonProperty setText(String aText)
	{
		super.setText(aText);
		return this;
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		int x = getBounds().x;
		int y = getBounds().y;
		int h = getBounds().height;
		int w = getBounds().width;

		Paint oldPaint = aGraphics.getPaint();

		aGraphics.setColor(Styles.SLIDER_BORDER_COLOR);
		aGraphics.fillRoundRect(x, y, w, h, 4, 4);

		aGraphics.setPaint(new LinearGradientPaint(0, y, 0, y + h, RANGES, Styles.BUTTON_COLORS[mArmed ? 2 : aHover ? 1 : 0]));
		aGraphics.fillRoundRect(x + 1, y + 1, w - 2, h - 2, 4, 4);

		if (Styles.DIRECTORY_ICON != null)
		{
			int t = h - 4;
			int s = (int)(t * aPane.getScale());
			aGraphics.drawImage(ImageResizer.getScaledImageAspect(Styles.DIRECTORY_ICON, s, s, true), x + 4, y + 2, t, t, null);
		}

		aGraphics.setPaint(oldPaint);

		getTextBox().setForeground(mArmed ? Styles.BOX_FOREGROUND_ARMED_COLOR : Styles.BOX_FOREGROUND_COLOR).setBounds(getBounds()).render(aGraphics);
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aPane, Point aClickPoint)
	{
		mArmed = true;
		return true;
	}


	@Override
	protected void mouseReleased(NodeEditorPane aPane, Point aClickPoint)
	{
		mArmed = false;
		aPane.repaint();
	}


	@Override
	protected void actionPerformed(NodeEditorPane aPane, Point aClickPoint)
	{
		aPane.fireButtonClicked(this);
	}


	@FunctionalInterface
	public interface ButtonAction
	{
		void onClick(ButtonProperty aItem);
	}
}
