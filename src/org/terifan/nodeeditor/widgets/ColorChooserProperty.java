package org.terifan.nodeeditor.widgets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JColorChooser;
import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.FIELD_CORNER;
import org.terifan.ui.Anchor;
import org.terifan.vecmath.Vec4d;


public class ColorChooserProperty extends Property<ColorChooserProperty>
{
	private static final long serialVersionUID = 1L;
	private final Rectangle mColorButtonBounds;

	private Color mColor;


	public ColorChooserProperty()
	{
		this("", Color.BLACK);
	}


	public ColorChooserProperty(String aText, Color aColor)
	{
		super(aText);

		mColorButtonBounds = new Rectangle();
		mColor = aColor;

		getPreferredSize().height = 20;
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		Rectangle bounds = getBounds();
		Rectangle tb = mTextBox
			.setBounds(bounds)
			.setAnchor(Anchor.WEST)
			.setForeground(Styles.BOX_FOREGROUND_COLOR)
			.render(aGraphics)
			.measure();

		if (!isConnected(Direction.IN))
		{
			int w = bounds.width - Math.max(tb.width, bounds.width / 3) - 10;
			int x = bounds.x + bounds.width - w;

			Rectangle nb = mNode.getBounds();
			mColorButtonBounds.x = nb.x + bounds.x + bounds.width - w;
			mColorButtonBounds.y = nb.y + bounds.y;
			mColorButtonBounds.width = w;
			mColorButtonBounds.height = bounds.height;

			aGraphics.setColor(Styles.SLIDER_BORDER_COLOR);
			aGraphics.fillRoundRect(x, bounds.y, w, bounds.height, FIELD_CORNER, FIELD_CORNER);

			aGraphics.setColor(mColor);
			aGraphics.fillRoundRect(x + 1, bounds.y + 1, w - 2, bounds.height - 2, FIELD_CORNER, FIELD_CORNER);
		}
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aPane, Point aClickPoint)
	{
		if (!isConnected(Direction.IN) && mColorButtonBounds.contains(aClickPoint))
		{
			Color c = JColorChooser.showDialog(aPane, mTextBox.getText(), mColor);
			if (c != null)
			{
				mColor = c;
				aPane.repaint();
				return true;
			}
		}

		return false;
	}


	@Override
	public Object execute(Context aContext)
	{
		Object value = super.execute(aContext);

		if (value != null)
		{
			return value;
		}

		return new Vec4d(mColor.getRed() / 255.0, mColor.getGreen() / 255.0, mColor.getBlue() / 255.0, 0);
	}
}
