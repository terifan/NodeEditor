package org.terifan.ui.resizablepanel;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import org.terifan.ui.relationeditor.RelationBox;
import org.terifan.ui.relationeditor.Styles;
import static org.terifan.ui.relationeditor.Styles.*;


public class ResizablePanelBorder_Blender implements Border, ResizablePanelBorder
{
	public final static int BUTTON_WIDTH = 16;

	private ResizablePanel mPanel;
	
	
	public ResizablePanelBorder_Blender(ResizablePanel aPanel)
	{
		mPanel = aPanel;
	}


	@Override
	public boolean isBorderOpaque()
	{
		return false;
	}


	@Override
	public Insets getBorderInsets(Component aComponent)
	{
		return new Insets(4 + TITLE_HEIGHT, 10, 4, 10);
	}


	@Override
	public void paintBorder(Component aComponent, Graphics aGraphics, int aX, int aY, int aWidth, int aHeight)
	{
		Graphics2D g = (Graphics2D)aGraphics;

		ResizablePanel panel = (ResizablePanel)aComponent;

		aX += 4;
		aWidth -= 8;

		g.setColor(BOX_BORDER_TITLE_COLOR);
		g.fillRoundRect(aX + 1, aY + 1, aWidth - 2, aHeight - 2, 18, 18);

		int th = TITLE_HEIGHT;
		boolean minimized = aHeight <= 4 + 4 + th;

		if (!minimized)
		{
			g.setColor(panel.getBackground());
			g.fillRoundRect(aX + 1, aY + 1 + 3 + th, aWidth - 2, aHeight - 2 - 3 - th, 18, 18);
			g.fillRect(aX + 1, aY + 1 + 3 + th, aWidth - 2, Math.min(8, aHeight - (1 + 3 + th + 5)));
		}
		
		int inset = 4 + 4 + BUTTON_WIDTH;

		new TextBox(panel.getTitle())
			.setShadow(BOX_BORDER_TITLE_SHADOW_COLOR)
			.setAnchor(Anchor.WEST)
			.setBounds(aX + inset, aY + 3, aWidth - inset - 4, th)
			.setForeground(aComponent.getForeground())
			.setMaxLineCount(1)
			.render(g);

		g.setColor(panel.getBackground() == BOX_BACKGROUND_SELECTED_COLOR ? BOX_BORDER_SELECTED_COLOR : BOX_BORDER_COLOR);
		g.drawRoundRect(aX, aY, aWidth - 1, aHeight - 1, 18, 18);

		aX += 8;
		aY += 4 + th / 2;
		int w = 10;
		int h = 5;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (minimized)
		{
			g.fillPolygon(new int[]{aX, aX + w, aX}, new int[]{aY - h, aY, aY + h}, 3);
		}
		else
		{
			g.fillPolygon(new int[]{aX, aX + w, aX + w / 2}, new int[]{aY - h, aY - h, aY + h}, 3);
		}

		drawAnchors(aComponent, g);
	}
	
	
	private void drawAnchors(Component aComponent, Graphics2D aGraphics)
	{
		ResizablePanel panel = (ResizablePanel)aComponent;

		if (panel instanceof RelationBox)
		{
			RelationBox relationBox = (RelationBox)panel;
			Point translate = aComponent.getLocation();

			aGraphics.translate(-translate.x, -translate.y);

			relationBox.drawAnchors(aGraphics);

			aGraphics.translate(translate.x, translate.y);
		}
	}


	@Override
	public void mouseClicked(MouseEvent aEvent)
	{
		Point point = aEvent.getPoint();

		if (point.x >= 4 && point.x < 4 + BUTTON_WIDTH && point.y >= 4 && point.y < 4 + Styles.TITLE_HEIGHT)
		{
			SwingUtilities.invokeLater(()->{
				mPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			});

			mPanel.setMinimized(!mPanel.isMinimized());
		}
	}
}
