package org.terifan.nodeeditor;

import java.awt.Graphics2D;


public class RelationEditorPaneBackground_Blender implements RelationEditorPaneBackground 
{
	@Override
	public void drawPaneBackground(RelationEditorPane aEditorPane, Graphics2D aGraphics)
	{
		int w = aEditorPane.getWidth();
		int h = aEditorPane.getHeight();

		aGraphics.setColor(Styles.PANE_BACKGROUND_COLOR);
		aGraphics.fillRect(0, 0, w, h);

		aGraphics.setColor(Styles.PANE_GRID_COLOR_1);
		for (int x = 0; x < w; x+=25)
		{
			aGraphics.drawLine(x, 0, x, h);
		}
		for (int y = 0; y < h; y+=25)
		{
			aGraphics.drawLine(0, y, w, y);
		}

		aGraphics.setColor(Styles.PANE_GRID_COLOR_2);
		for (int x = 0; x < w; x+=5*25)
		{
			aGraphics.drawLine(x, 0, x, h);
		}
		for (int y = 0; y < h; y+=5*25)
		{
			aGraphics.drawLine(0, y, w, y);
		}
	}
}
