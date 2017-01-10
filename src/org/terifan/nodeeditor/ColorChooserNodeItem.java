package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JColorChooser;
import org.terifan.ui.Anchor;


public class ColorChooserNodeItem extends AbstractNodeItem<ColorChooserNodeItem>
{
	private static final int COLOR_BOX_WIDTH = 30;
	private Color mColor;


	public ColorChooserNodeItem(String aText, Color aColor)
	{
		super(aText);

		mColor = aColor;
		mPreferredSize.height = 20;
	}


	@Override
	protected void paintComponent(NodeEditorPane aEditorPane, Graphics2D aGraphics, boolean aHover)
	{
		if (countConnections(Direction.IN) > 0)
		{
			mTextBox.setMargins(0, 0, 0, 0);
		}
		else
		{
			aGraphics.setColor(new Color(48, 48, 48));
			aGraphics.fillRoundRect(mBounds.x, mBounds.y, COLOR_BOX_WIDTH, mBounds.height, 8, 8);

			aGraphics.setColor(mColor);
			aGraphics.fillRoundRect(mBounds.x + 1, mBounds.y + 1, COLOR_BOX_WIDTH - 2, mBounds.height - 2, 8, 8);

			mTextBox.setMargins(0, COLOR_BOX_WIDTH + 10, 0, 0);
		}

		mTextBox
			.setBounds(mBounds)
			.setAnchor(Anchor.WEST)
			.setForeground(Styles.BOX_FOREGROUND_COLOR)
			.render(aGraphics);
	}


	@Override
	protected void actionPerformed(NodeEditorPane aEditorPane, Point aClickPoint)
	{
		if (countConnections(Direction.IN) == 0)
		{
			Color c = JColorChooser.showDialog(aEditorPane, mTextBox.getText(), mColor);
			if (c != null)
			{
				mColor = c;
				aEditorPane.repaint();
			}
		}
	}


	@Override
	protected boolean mousePressed(NodeEditorPane aEditorPane, Point aClickPoint)
	{
		if (countConnections(Direction.IN) == 0 && new Rectangle(mNodeBox.getBounds().x + mBounds.x, mNodeBox.getBounds().y + mBounds.y, COLOR_BOX_WIDTH, mBounds.height).contains(aClickPoint))
		{
			return true;
		}

		return false;
	}
}
