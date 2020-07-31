package org.terifan.nodeeditor;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import org.terifan.bundle.Bundle;
import org.terifan.ui.Anchor;
import org.terifan.ui.ImageResizer;
import org.terifan.ui.TextBox;


public class ButtonNodeItem extends AbstractNodeItem<ButtonNodeItem>
{
	private final static long serialVersionUID = 1L;
	private final static float[] RANGES = new float[]{0f,1f};

	private boolean mArmed;


	protected ButtonNodeItem()
	{
		mTextBox.setAnchor(Anchor.CENTER).setMargins(0, 0, 0, 0).setMaxLineCount(1).setFont(Styles.SLIDER_FONT);
		mPreferredSize.height = 22;
	}


	public ButtonNodeItem(String aText)
	{
		super(aText);

		mTextBox.setAnchor(Anchor.CENTER).setMargins(0, 0, 0, 0).setMaxLineCount(1).setFont(Styles.SLIDER_FONT);
		mPreferredSize.height = 22;
	}


	@Override
	public TextBox getTextBox()
	{
		return super.getTextBox();
	}


	@Override
	public ButtonNodeItem setText(String aText)
	{
		super.setText(aText);
		return this;
	}


	@Override
	protected void paintComponent(NodeEditor aEditor, Graphics2D aGraphics, boolean aHover)
	{
		int x = mBounds.x;
		int y = mBounds.y;
		int h = mBounds.height;
		int w = mBounds.width;

		Paint oldPaint = aGraphics.getPaint();

		aGraphics.setColor(Styles.SLIDER_BORDER_COLOR);
		aGraphics.fillRoundRect(x, y, w, h, 4, 4);

		aGraphics.setPaint(new LinearGradientPaint(0, y, 0, y + h, RANGES, Styles.BUTTON_COLORS[mArmed ? 2 : aHover ? 1 : 0]));
		aGraphics.fillRoundRect(x + 1, y + 1, w - 2, h - 2, 4, 4);

		if (Styles.DIRECTORY_ICON != null)
		{
			int t = h - 4;
			int s = (int)(t * aEditor.getScale());
			aGraphics.drawImage(ImageResizer.getScaledImageAspect(Styles.DIRECTORY_ICON, s, s, true), x + 4, y + 2, t, t, null);
		}

		aGraphics.setPaint(oldPaint);

		mTextBox.setForeground(mArmed ? Styles.BOX_FOREGROUND_ARMED_COLOR : Styles.BOX_FOREGROUND_COLOR).setBounds(mBounds).render(aGraphics);
	}


	@Override
	protected boolean mousePressed(NodeEditor aEditor, Point aClickPoint)
	{
		mArmed = true;
		return true;
	}


	@Override
	protected void mouseReleased(NodeEditor aEditor, Point aClickPoint)
	{
		mArmed = false;
		aEditor.repaint();
	}


	@Override
	protected void actionPerformed(NodeEditor aEditor, Point aClickPoint)
	{
//		if (mButtonAction != null)
//		{
//			mButtonAction.onClick(this);
//		}
	}


	@FunctionalInterface
	public interface ButtonAction
	{
		void onClick(ButtonNodeItem aItem);
	}


//	@Override
//	public void readExternal(Bundle aBundle)
//	{
//		super.readExternal(aBundle);
//
//		mPreferredSize.height = 22;
//	}
//
//
//	@Override
//	public void writeExternal(Bundle aBundle)
//	{
//		super.writeExternal(aBundle);
//	}
}
