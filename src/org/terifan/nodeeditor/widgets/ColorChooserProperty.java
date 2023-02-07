package org.terifan.nodeeditor.widgets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JColorChooser;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class ColorChooserProperty extends Property<ColorChooserProperty>
{
	private static final long serialVersionUID = 1L;

	private static final int COLOR_BOX_WIDTH = 30;
	private Color mColor;


	public ColorChooserProperty(String aText, Color aColor)
	{
		super(aText);

		mColor = aColor;
		getPreferredSize().height = 20;
	}


	@Override
	protected void paintComponent(NodeEditorPane aEditor, Graphics2D aGraphics, boolean aHover)
	{
		Rectangle bounds = getBounds();
		TextBox textBox = getTextBox();

		if (isConnected(Direction.IN))
		{
			textBox.setMargins(0, 0, 0, 0);
		}
		else
		{
			aGraphics.setColor(new Color(48, 48, 48));
			aGraphics.fillRoundRect(bounds.x, bounds.y, COLOR_BOX_WIDTH, bounds.height, 8, 8);

			aGraphics.setColor(mColor);
			aGraphics.fillRoundRect(bounds.x + 1, bounds.y + 1, COLOR_BOX_WIDTH - 2, bounds.height - 2, 8, 8);

			textBox.setMargins(0, COLOR_BOX_WIDTH + 10, 0, 0);
		}

		textBox
			.setBounds(bounds)
			.setAnchor(Anchor.WEST)
			.setForeground(Styles.BOX_FOREGROUND_COLOR)
			.render(aGraphics);
	}


	@Override
	protected void actionPerformed(NodeEditorPane aEditor, Point aClickPoint)
	{
		if (!isConnected(Direction.IN))
		{
			Color c = JColorChooser.showDialog(aEditor, getTextBox().getText(), mColor);
			if (c != null)
			{
				mColor = c;
				aEditor.repaint();
			}
		}
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aEditor, Point aClickPoint)
	{
		return !isConnected(Direction.IN) && new Rectangle(getNode().getBounds().x + getBounds().x, getNode().getBounds().y + getBounds().y, COLOR_BOX_WIDTH, getBounds().height).contains(aClickPoint);
	}
}
