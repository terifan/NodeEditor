package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.graphics.Popup;
import org.terifan.nodeeditor.graphics.Popup.Option;
import org.terifan.nodeeditor.Styles;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class ComboBoxProperty extends Property<ComboBoxProperty>
{
	private static final long serialVersionUID = 1L;

	private final static float[] RANGES = new float[]
	{
		0f, 1f
	};

	protected boolean mArmed;
	protected int mSelectedIndex;
	protected String mHeader;
	protected ArrayList<String> mOptions;


	public ComboBoxProperty(String aText, int aSelectedIndex, String... aOptions)
	{
		super(aOptions[aSelectedIndex]);

		mHeader = aText;
		mSelectedIndex = aSelectedIndex;
		mOptions = new ArrayList<>(Arrays.asList(aOptions));
		getPreferredSize().height = 21;
	}


	@Override
	protected void paintComponent(NodeEditorPane aEditor, Graphics2D aGraphics, boolean aHover)
	{
		Paint oldPaint = aGraphics.getPaint();
		Rectangle bounds = getBounds();

		aGraphics.setColor(Styles.SLIDER_BORDER_COLOR);
		aGraphics.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 4, 4);

		aGraphics.setPaint(new LinearGradientPaint(0, bounds.y + 1, 0, bounds.y + bounds.height - 2, RANGES, Styles.CHECKBOX_COLORS[mArmed ? 1 : 0]));
		aGraphics.fillRoundRect(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2, 4, 4);

		int pw = 2;
		int ph = 4;
		int ax = bounds.x + bounds.width - 7;
		int ay = bounds.y + bounds.height / 2;
		int[] px = new int[]
		{
			ax - pw, ax, ax + pw
		};

		aGraphics.setColor(Styles.COMBOBOX_ARROW_COLOR);
		aGraphics.fillPolygon(px, new int[]
		{
			ay - 1, ay - ph, ay - 1
		}, 3);
		aGraphics.fillPolygon(px, new int[]
		{
			ay + 1, ay + ph, ay + 1
		}, 3);

		aGraphics.setPaint(oldPaint);

		getTextBox().setBounds(bounds).setAnchor(Anchor.WEST).setMargins(0, 8, 0, 15).setForeground(Styles.BOX_FOREGROUND_SELECTED_COLOR).setMaxLineCount(1).setFont(Styles.SLIDER_FONT).render(aGraphics);
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aEditor, Point aClickPoint)
	{
		mArmed = true;

		ArrayList<Option> options = new ArrayList<>();
		for (String option : mOptions)
		{
			options.add(new Option()
			{
				Rectangle bounds = new Rectangle(0, options.size() * Styles.POPUP_DEFAULT_OPTION_HEIGHT, getBounds().width, Styles.POPUP_DEFAULT_OPTION_HEIGHT);


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

		Popup popup = new Popup(aEditor, this, mHeader, getBounds(), options, e -> setSelectedIndex(options.indexOf(e)));

		aEditor.setPopup(popup);
		aEditor.repaint();

		return true;
	}


	@Override
	protected void mouseReleased(NodeEditorPane aEditor, Point aClickPoint)
	{
		mArmed = false;

		aEditor.setPopup(null);
		aEditor.repaint();
	}


	public int getSelectedIndex()
	{
		return mSelectedIndex;
	}


	public ComboBoxProperty setSelectedIndex(int aSelectedIndex)
	{
		mSelectedIndex = aSelectedIndex;
		setText(mOptions.get(mSelectedIndex));

//		if (mResultReceiver != null)
//		{
//			mResultReceiver.selectionChanged(mSelectedIndex);
//		}
		return this;
	}
}
