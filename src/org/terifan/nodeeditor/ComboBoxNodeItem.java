package org.terifan.nodeeditor;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import org.terifan.bundle.Bundle;
import org.terifan.nodeeditor.Popup.Option;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class ComboBoxNodeItem extends AbstractNodeItem
{
	private static final long serialVersionUID = 1L;

	private final static float[] RANGES = new float[]{0f,1f};

	protected boolean mArmed;
	protected int mSelectedIndex;
	protected String mHeader;
	protected String[] mOptions;


	protected ComboBoxNodeItem()
	{
	}


	public ComboBoxNodeItem(String aText, int aSelectedIndex, String... aOptions)
	{
		super(aOptions[aSelectedIndex]);

		mHeader = aText;
		mSelectedIndex = aSelectedIndex;
		mOptions = aOptions;
		mPreferredSize.height = 21;
	}


	@Override
	protected void paintComponent(NodeEditor aEditor, Graphics2D aGraphics, boolean aHover)
	{
		Paint oldPaint = aGraphics.getPaint();

		aGraphics.setColor(Styles.SLIDER_BORDER_COLOR);
		aGraphics.fillRoundRect(mBounds.x, mBounds.y, mBounds.width, mBounds.height, 4, 4);

		aGraphics.setPaint(new LinearGradientPaint(0, mBounds.y + 1, 0, mBounds.y + mBounds.height - 2, RANGES, Styles.CHECKBOX_COLORS[mArmed ? 1 : 0]));
		aGraphics.fillRoundRect(mBounds.x + 1, mBounds.y + 1, mBounds.width - 2, mBounds.height - 2, 4, 4);

		int pw = 2;
		int ph = 4;
		int ax = mBounds.x + mBounds.width - 7;
		int ay = mBounds.y + mBounds.height / 2;
		int[] px = new int[]{ax - pw, ax, ax + pw};

		aGraphics.setColor(Styles.COMBOBOX_ARROW_COLOR);
		aGraphics.fillPolygon(px, new int[]{ay-1, ay-ph, ay-1}, 3);
		aGraphics.fillPolygon(px, new int[]{ay+1, ay+ph, ay+1}, 3);

		aGraphics.setPaint(oldPaint);

		mTextBox.setBounds(mBounds).setAnchor(Anchor.WEST).setMargins(0, 8, 0, 15).setForeground(Styles.BOX_FOREGROUND_SELECTED_COLOR).setMaxLineCount(1).setFont(Styles.SLIDER_FONT).render(aGraphics);
	}


	@Override
	protected boolean mousePressed(NodeEditor aEditor, Point aClickPoint)
	{
		mArmed = true;

		ArrayList<Option> options = new ArrayList<>();
		for (String option : mOptions)
		{
			options.add(new Option()
			{
				Rectangle bounds = new Rectangle(0, options.size()*Styles.POPUP_DEFAULT_OPTION_HEIGHT, mBounds.width, Styles.POPUP_DEFAULT_OPTION_HEIGHT);


				@Override
				public Rectangle getBounds()
				{
					return bounds;
				}


				@Override
				public void paintOption(Graphics2D aGraphics, boolean aSelected)
				{
					if (aSelected)
					{
						aGraphics.setColor(Styles.POPUP_SELECTION_BACKGROUND);
						aGraphics.fill(bounds);
					}
					new TextBox().setAnchor(Anchor.WEST).setForeground(Styles.POPUP_FOREGROUND).setMargins(0, 10, 0, 0).setText(option).setBounds(bounds).render(aGraphics);
				}
			});
		}

//		Popup popup = new Popup(this, "", new Rectangle(mBounds.x, mBounds.y, 340, 520), options, e->setSelectedIndex(options.indexOf(e)));

		Popup popup = new Popup(aEditor, this, mHeader, mBounds, options, e->setSelectedIndex(options.indexOf(e)));

		aEditor.setPopup(popup);
		aEditor.repaint();

		return true;
	}


	@Override
	protected void mouseReleased(NodeEditor aEditor, Point aClickPoint)
	{
		mArmed = false;

		aEditor.setPopup(null);
		aEditor.repaint();
	}


	public int getSelectedIndex()
	{
		return mSelectedIndex;
	}


	public ComboBoxNodeItem setSelectedIndex(int aSelectedIndex)
	{
		mSelectedIndex = aSelectedIndex;
		setText(mOptions[mSelectedIndex]);

//		if (mResultReceiver != null)
//		{
//			mResultReceiver.selectionChanged(mSelectedIndex);
//		}

		return this;
	}


//	@FunctionalInterface
//	public interface ResultReceiver
//	{
//		void selectionChanged(int aSelectedIndex);
//	}


	@Override
	public void readExternal(Bundle aBundle) throws IOException
	{
		super.readExternal(aBundle);

		mSelectedIndex = aBundle.getInt("selected");
		mHeader = aBundle.getString("header");
		mOptions = aBundle.getStringArray("options");
	}


	@Override
	public void writeExternal(Bundle aBundle) throws IOException
	{
		super.writeExternal(aBundle);

		aBundle.putString("type", "ComboBox");
		aBundle.putInt("selected", mSelectedIndex);
		aBundle.putString("header", mHeader);
		aBundle.putStringArray("options", mOptions);
	}
}
