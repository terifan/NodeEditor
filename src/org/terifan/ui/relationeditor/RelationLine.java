package org.terifan.ui.relationeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import org.terifan.ui.Utilities;


public class RelationLine
{
	private final static BasicStroke basicStroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	private final static BasicStroke basicStroke1 = new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL);
	private final static BasicStroke basicStroke2 = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	private final static Color COLOR_128 = new Color(128,128,128);
	private final static Color COLOR_192 = new Color(192,192,192);


	public void render(Graphics2D aGraphics, Rectangle aAnchorA, Rectangle aAnchorB, boolean aAnchorALeft, boolean aAnchorBLeft)
	{
		int x0 = aAnchorA.x;
		int y0 = aAnchorA.y + aAnchorA.height / 2;
		int x1 = aAnchorB.x;
		int y1 = aAnchorB.y + aAnchorB.height / 2;
		int d0 = aAnchorALeft ? -16 : 16;
		int d1 = aAnchorBLeft ? -16 : 16;

		Stroke old = aGraphics.getStroke();

		aGraphics.setStroke(basicStroke);
		aGraphics.setColor(COLOR_128);
		aGraphics.drawLine(x0+d0,y0,x1+d1,y1);
		aGraphics.setStroke(basicStroke1);
		aGraphics.drawLine(x0,y0,x0+d0,y0);
		aGraphics.drawLine(x1,y1,x1+d1,y1);

		aGraphics.setStroke(basicStroke2);
		aGraphics.setColor(COLOR_192);
		aGraphics.drawLine(x0,y0,x0+d0,y0);
		aGraphics.drawLine(x0+d0,y0,x1+d1,y1);
		aGraphics.drawLine(x1,y1,x1+d1,y1);

		aGraphics.setStroke(old);
	}

//		Color[] colors = {
//			new Color(160,160,160),
//			new Color(255,255,255),
//			new Color(240,240,240),
//			new Color(240,240,240),
//			new Color(240,240,240),
//			new Color(0,0,0)
//		};
//
//		double t = 3.0;
//		double angle = Math.toDegrees(Math.atan((y1 - y0) / (x1 - x0))) / 360.0;
//
//		double rx1 = t * Math.cos(2 * Math.PI * (angle - 0.25));
//		double ry1 = t * Math.sin(2 * Math.PI * (angle - 0.25));
//		double rx2 = t * Math.cos(2 * Math.PI * (angle + 0.25));
//		double ry2 = t * Math.sin(2 * Math.PI * (angle + 0.25));
//
//		int[] polyX = new int[14];
//		int[] polyY = new int[14];
//
//		for (int i = 0, j = 0; i < 7; i++)
//		{
//			double dx = (rx2 - rx1) * i / 6.0;
//			double dy = (ry2 - ry1) * i / 6.0;
//
//			polyX[j] = (int)(x0 + rx1 + dx);
//			polyY[j] = (int)(y0 + ry1 + dy);
//			j++;
//			polyX[j] = (int)(x1 + rx1 + dx);
//			polyY[j] = (int)(y1 + ry1 + dy);
//			j++;
//		}
//
//		aGraphics.setColor(colors[2]);
//		aGraphics.fillPolygon(new Polygon(new int[]{polyX[2], polyX[3], polyX[11], polyX[10]}, new int[]{polyY[2], polyY[3], polyY[11], polyY[10]}, 4));
//
//		aGraphics.setColor(colors[1]);
//		aGraphics.drawLine(polyX[2], polyY[2], polyX[3], polyY[3]);
//
//		aGraphics.setColor(colors[0]);
//		aGraphics.drawLine(polyX[0], polyY[0], polyX[1], polyY[1]);
//
//		aGraphics.setColor(colors[5]);
//		aGraphics.drawLine(polyX[12], polyY[12], polyX[13], polyY[13]);
//
//		for (int i = 0; i < colors.length; i++)
//		{
//			aGraphics.setColor(colors[i]);
//			aGraphics.drawLine(x0 - 16, y0 - 2 + i, polyX[2 * i], y0 - 2 + i);
//			aGraphics.drawLine(polyX[2 * i + 1], y1 - 2 + i, x1 + 16, y1 - 2 + i);
//		}
}
