package org.terifan.nodeeditor;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class ButtonNodeItem extends AbstractNodeItem<ButtonNodeItem>
{
	private final static float[] RANGES = new float[]{0f,1f};

	private boolean mArmed;
	private ButtonAction mButtonAction;


	public ButtonNodeItem(String aText, ButtonAction aButtonAction)
	{
		super(aText);

		mButtonAction = aButtonAction;
		mPreferredSize.height = 22;
		mTextBox.setAnchor(Anchor.CENTER).setMargins(0, 0, 0, 0).setMaxLineCount(1).setFont(Styles.SLIDER_FONT);
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
	protected void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, boolean aHover)
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

		aGraphics.setPaint(oldPaint);

		mTextBox.setForeground(mArmed ? Styles.BOX_FOREGROUND_ARMED_COLOR : Styles.BOX_FOREGROUND_COLOR).setBounds(mBounds).render(aGraphics);
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aEditorPane, Point aClickPoint)
	{
		mArmed = true;
		return true;
	}


	@Override
	protected void mouseReleased(NodeEditorPane aEditorPane, Point aClickPoint)
	{
		mArmed = false;
		aEditorPane.repaint();
	}


	@Override
	protected void actionPerformed(NodeEditorPane aEditorPane, Point aClickPoint)
	{
		if (mButtonAction != null)
		{
			mButtonAction.onClick(this);
		}
	}


	@FunctionalInterface
	public interface ButtonAction
	{
		void onClick(ButtonNodeItem aItem);
	}
}
