package org.terifan.ui.resizablepanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import org.terifan.ui.relationeditor.Styles;
import static org.terifan.ui.relationeditor.Styles.BOX_BACKGROUND_SELECTED_COLOR;
import static org.terifan.ui.relationeditor.Styles.BOX_BORDER_COLOR;
import static org.terifan.ui.relationeditor.Styles.BOX_BORDER_SELECTED_COLOR;
import static org.terifan.ui.relationeditor.Styles.BOX_BORDER_TITLE_COLOR;
import static org.terifan.ui.relationeditor.Styles.BOX_BORDER_TITLE_SHADOW_COLOR;
import static org.terifan.ui.relationeditor.Styles.TITLE_HEIGHT;


public class ResizablePanelBorder_Regular implements Border, ResizablePanelBorder
{
	public final static int BUTTON_WIDTH = 16;

	private Border mBevelBorder;
	private ResizablePanel mPanel;


	public ResizablePanelBorder_Regular(ResizablePanel aPanel)
	{
		mPanel = aPanel;
		mBevelBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
	}


	public Border getBevelBorder()
	{
		return mBevelBorder;
	}


	public void setBevelBorder(Border aBevelBorder)
	{
		mBevelBorder = aBevelBorder;
	}

	@Override
	public boolean isBorderOpaque()
	{
		return mBevelBorder.isBorderOpaque();
	}


	@Override
	public Insets getBorderInsets(Component aComponent)
	{
		Insets borderInsets = mBevelBorder.getBorderInsets(aComponent);

		borderInsets.left += 2;
		borderInsets.top += 2 + Styles.TITLE_HEIGHT;
		borderInsets.right += 2;
		borderInsets.bottom += 2;

		return borderInsets;
	}


	@Override
	public void paintBorder(Component aComponent, Graphics aGraphics, int aX, int aY, int aWidth, int aHeight)
	{
		Graphics2D g = (Graphics2D)aGraphics;

		Border createEtchedBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED, ((BevelBorder)mBevelBorder).getHighlightOuterColor(aComponent), aComponent.getBackground(), ((BevelBorder)mBevelBorder).getShadowOuterColor(aComponent), aComponent.getBackground());

		ResizablePanel panel = (ResizablePanel)aComponent;

		int th = TITLE_HEIGHT;
		boolean minimized = aHeight <= 4 + 4 + th;
		
		Rectangle b = new Rectangle(aX, aY, aWidth - 1, aHeight - 1);

		g.setColor(BOX_BORDER_TITLE_COLOR);
		g.fill(b);
		
		mBevelBorder.paintBorder(aComponent, aGraphics, aX, aY, aWidth, aHeight);

		g.setColor(aComponent.getBackground());
		b.grow(-2, -2);
		g.draw(b);
		b.grow(-1, -1);
		g.draw(b);

		b.width -= Styles.TITLE_HEIGHT;
		b.height = Styles.TITLE_HEIGHT;

		new TextBox(panel.getTitle())
			.setShadow(BOX_BORDER_TITLE_SHADOW_COLOR)
			.setAnchor(Anchor.WEST)
			.setBounds(b)
			.setMargins(0, 4, 0, 4)
			.setForeground(aComponent.getForeground())
			.setMaxLineCount(1)
			.render(g);

		// button
		b.x += b.width + 1;
		b.y++;
		b.width = Styles.TITLE_HEIGHT - 2;
		b.height = Styles.TITLE_HEIGHT - 2;

		g.setColor(BOX_BACKGROUND_SELECTED_COLOR);
		g.fill(b);
		
		createEtchedBorder.paintBorder(aComponent, aGraphics, b.x, b.y, b.width, b.height);
		
		g.setColor(aComponent.getForeground().darker());

		if (panel.isMinimized())
		{
			g.drawLine(b.x + 3, b.y + 4, b.x + b.width - 4, b.y + 4);
			g.drawLine(b.x + 3, b.y + 5, b.x + b.width - 4, b.y + 5);
			g.drawLine(b.x + 3, b.y + 10, b.x + b.width - 4, b.y + 10);
			g.drawLine(b.x + 3, b.y + 4, b.x + 3, b.y + 10);
			g.drawLine(b.x + b.width - 4, b.y + 4, b.x + b.width - 4, b.y + 10);
		}
		else
		{
			g.drawLine(b.x + 3, b.y + 9, b.x + b.width - 4, b.y + 9);
			g.drawLine(b.x + 3, b.y + 10, b.x + b.width - 4, b.y + 10);
		}
	}


	@Override
	public void mouseClicked(MouseEvent aEvent)
	{
		Point point = aEvent.getPoint();

		if (point.x >= mPanel.getWidth() - 4 - BUTTON_WIDTH && point.x < mPanel.getWidth() - 4 && point.y >= 4 && point.y < 4 + Styles.TITLE_HEIGHT)
		{
			SwingUtilities.invokeLater(()->{
				mPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			});

			mPanel.setMinimized(!mPanel.isMinimized());
		}
	}
}
