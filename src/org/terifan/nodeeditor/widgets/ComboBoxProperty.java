package org.terifan.nodeeditor.widgets;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.graphics.Popup;
import org.terifan.nodeeditor.graphics.Popup.Option;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.BOX_TITLE_TEXT_SHADOW_COLOR;
import static org.terifan.nodeeditor.Styles.COMBOBOX_ARROW_COLOR;
import org.terifan.nodeeditor.graphics.Arrow;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class ComboBoxProperty extends Property<ComboBoxProperty>
{
	private static final long serialVersionUID = 1L;

	private final static float[] RANGES = new float[]
	{
		0f, 1f
	};

	protected String mHeader;
	protected ArrayList<String> mOptions;
	protected int mSelectedIndex;
	protected transient boolean mArmed;


	public ComboBoxProperty(String aText, int aSelectedIndex, String... aOptions)
	{
		super(aOptions[aSelectedIndex]);

		mHeader = aText;
		mSelectedIndex = aSelectedIndex;
		mOptions = new ArrayList<>(Arrays.asList(aOptions));

		mTextBox.setAnchor(Anchor.WEST).setMargins(6, 8, 6, 15).setForeground(Styles.BOX_FOREGROUND_COLOR).setMaxLineCount(1).setFont(Styles.SLIDER_FONT);
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		Paint oldPaint = aGraphics.getPaint();
		Rectangle bounds = getBounds();

		aGraphics.setColor(Styles.SLIDER_BORDER_COLOR);
		aGraphics.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 4, 4);

		aGraphics.setPaint(new LinearGradientPaint(0, bounds.y + 1, 0, bounds.y + bounds.height - 2, RANGES, Styles.COMBOBOX_COLORS[mArmed ? 1 : 0]));
		aGraphics.fillRoundRect(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2, 4, 4);

		aGraphics.setPaint(Styles.COMBOBOX_COLORS[2][0]);
		aGraphics.drawRoundRect(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2, 4, 4);

		int x = bounds.x + bounds.width - 12;
		int y = bounds.y + bounds.height / 2 + 1;
		int w = 4;
		int h = 4;

		Arrow.paintArrow(aGraphics, 2, x, y, w, h, BOX_TITLE_TEXT_SHADOW_COLOR, COMBOBOX_ARROW_COLOR);

		aGraphics.setPaint(oldPaint);

		mTextBox.setBounds(bounds).render(aGraphics);
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aPane, Point aClickPoint)
	{
		mArmed = true;

		ArrayList<Option> options = new ArrayList<>();
		for (String option : mOptions)
		{
			options.add(new Option()
			{
				@Override
				public Rectangle getBounds()
				{
					return new Rectangle(0, options.indexOf(this) * Styles.POPUP_DEFAULT_OPTION_HEIGHT, ComboBoxProperty.this.getBounds().width, Styles.POPUP_DEFAULT_OPTION_HEIGHT);
				}


				@Override
				public void paintOption(Graphics2D aGraphics, boolean aSelected)
				{
					Rectangle bounds = getBounds();
					if (aSelected)
					{
						aGraphics.setColor(Styles.POPUP_SELECTION_BACKGROUND);
						aGraphics.fill(bounds);
					}
					new TextBox().setAnchor(Anchor.WEST).setForeground(Styles.POPUP_FOREGROUND).setMargins(0, 10, 0, 0).setText(option).setBounds(bounds).render(aGraphics);
				}
			});
		}

		Popup popup = new Popup(aPane, this, mHeader, getBounds(), options, e -> setSelectedIndex(options.indexOf(e)));

		aPane.setPopup(popup);
		aPane.repaint();

		return true;
	}


	@Override
	protected void mouseReleased(NodeEditorPane aPane, Point aClickPoint)
	{
		mArmed = false;

		aPane.setPopup(null);
		aPane.repaint();
	}


	@Override
	public Object execute(Context aContext)
	{
		return mOptions.get(mSelectedIndex);
	}


	public int getSelectedIndex()
	{
		return mSelectedIndex;
	}


	public ComboBoxProperty setSelectedIndex(int aSelectedIndex)
	{
		mSelectedIndex = aSelectedIndex;

		setText(mOptions.get(mSelectedIndex));

		return this;
	}
}
