package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.image.BufferedImage;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.FIELD_CORNER;
import org.terifan.ui.Anchor;
import org.terifan.ui.ImageResizer;


public class ButtonProperty extends Property<ButtonProperty>
{
	private final static long serialVersionUID = 1L;
	private final static float[] RANGES = new float[]{0f,1f};

	private transient boolean mArmed;

	private String mIcon;


	public ButtonProperty(String aText)
	{
		super(aText);

		setIcon(Styles.DefaultIcons.FOLDER);
		getTextBox().setAnchor(Anchor.CENTER).setMargins(4, 0, 4, 0).setMaxLineCount(1).setFont(Styles.SLIDER_FONT);
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
		aGraphics.fillRoundRect(x, y, w, h, FIELD_CORNER, FIELD_CORNER);

		aGraphics.setPaint(new LinearGradientPaint(0, y, 0, y + h, RANGES, Styles.BUTTON_COLORS[mArmed ? 2 : aHover ? 1 : 0]));
		aGraphics.fillRoundRect(x + 1, y + 1, w - 2, h - 2, FIELD_CORNER, FIELD_CORNER);

		if (mIcon != null)
		{
			BufferedImage image = aPane.getIconProvider().apply(mIcon);
			if (image != null)
			{
				int t = h - 6;
				int s = (int)(t * aPane.getScale());
				aGraphics.drawImage(ImageResizer.getScaledImageAspect(image, s, s, true), x + 4, y + 3, t, t, null);
			}
		}

		aGraphics.setPaint(oldPaint);

		getTextBox().setForeground(mArmed ? Styles.BOX_FOREGROUND_ARMED_COLOR : Styles.BOX_FOREGROUND_COLOR).setBounds(getBounds()).render(aGraphics);
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aEditor, Point aClickPoint)
	{
		mArmed = true;

		try
		{
			aEditor.invoke(mModelId, this);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		return true;
	}


	@Override
	protected void mouseReleased(NodeEditorPane aEditor, Point aClickPoint)
	{
		mArmed = false;
		aEditor.repaint();
	}
}
