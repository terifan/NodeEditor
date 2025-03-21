package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.image.BufferedImage;
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

	private String mIcon;
	private String mCommand;


	public ButtonProperty(String aText)
	{
		super(aText);

		setIcon(Styles.DefaultIcons.FOLDER);
		getTextBox().setAnchor(Anchor.CENTER).setMargins(0, 0, 0, 0).setMaxLineCount(1).setFont(Styles.SLIDER_FONT);
		getPreferredSize().height = 22;
	}


	public ButtonProperty setCommand(String aCommand)
	{
		mCommand = aCommand;
		return this;
	}


	public String getIcon()
	{
		return mIcon;
	}


	public ButtonProperty setIcon(String aIcon)
	{
		mIcon = aIcon;
		return this;
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

		if (mIcon != null)
		{
			BufferedImage image = aPane.getIconProvider().apply(mIcon);
			if (image != null)
			{
				int t = h - 4;
				int s = (int)(t * aPane.getScale());
				aGraphics.drawImage(ImageResizer.getScaledImageAspect(image, s, s, true), x + 4, y + 2, t, t, null);
			}
		}

		aGraphics.setPaint(oldPaint);

		getTextBox().setForeground(mArmed ? Styles.BOX_FOREGROUND_ARMED_COLOR : Styles.BOX_FOREGROUND_COLOR).setBounds(getBounds()).render(aGraphics);
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aPane, Point aClickPoint)
	{
		mArmed = true;

		aPane.fireCommand(mCommand, getNode(), this);

		return true;
	}


	@Override
	protected void mouseReleased(NodeEditorPane aPane, Point aClickPoint)
	{
		mArmed = false;
		aPane.repaint();
	}


//	@Override
//	protected void actionPerformed(NodeEditorPane aPane, Point aClickPoint)
//	{
//		aPane.fireButtonClicked(this);
//	}
}
