package org.terifan.ui.relationeditor;

import org.terifan.graphics.BSpline;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import org.terifan.math.vec2;
import org.terifan.ui.Utilities;
import org.terifan.util.log.Log;


public class DefaultConnectionRenderer implements ConnectionRenderer
{
	private final static BasicStroke STROKE_WIDE = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	private final static BasicStroke STROKE_THIN = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	private final static Color COLOR_DARK = new Color(0, 0, 0, 128);
	private final static Color COLOR_BRIGHT = new Color(255, 255, 255);
	private static final Color COLOR_BRIGHT_SELECTED = new Color(192, 0, 0);
	private static final Color COLOR_DARK_SELECTED = new Color(128, 0, 0);


	@Override
	public void render(Graphics aGraphics, Connection aConnection, Anchor aFromAnchor, Anchor aToAnchor, boolean aSelected)
	{
		Graphics2D g = (Graphics2D)aGraphics;
		
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Rectangle from = aFromAnchor.getBounds();
		Rectangle to = aToAnchor.getBounds();

		int x0 = (int)from.getCenterX();
		int y0 = (int)from.getCenterY();
		int x1 = (int)to.getCenterX();
		int y1 = (int)to.getCenterY();
		int d0 = aFromAnchor.getOritentation() == Anchor.LEFT ? -16 : 16;
		int d1 = aToAnchor.getOritentation() == Anchor.LEFT ? -16 : 16;

		BSpline spline = new BSpline(new double[]{x0, x0+d0, x1+d1, x1}, new double[]{y0, y0, y1, y1});

		Stroke old = g.getStroke();

		g.setStroke(STROKE_WIDE);
		g.setColor(COLOR_DARK);
		drawSpline(g, spline);

		g.setStroke(STROKE_THIN);
		g.setColor(COLOR_BRIGHT);
		drawSpline(g, spline);

		g.setStroke(old);
	}


	private void drawSpline(Graphics2D aGraphics, BSpline aSpline)
	{
		Point2D.Double prev = null;

		int segments = Math.max(20, (int)aSpline.getPoint(0).distance(aSpline.getPoint(1)) / 4);

		for (double i = 0, s = segments; --s >= 0; i+=1.0/s)
		{
			Point2D.Double next = aSpline.getPoint(i);
			if (prev != null)
			{
				aGraphics.drawLine((int)Math.round(prev.x), (int)Math.round(prev.y), (int)Math.round(next.x), (int)Math.round(next.y));
			}
			prev = next;
		}
	}


//	private void drawLine(Graphics aGraphics, int x0, int y0, int x1, int y1)
//	{
//		int len = Math.max(Math.abs(x1-x0), Math.abs(y1-y0));
//		double dx = 0;
//		double dy = 0;
//		
//		if (Math.abs(x1-x0) > Math.abs(y1-y0))
//		{
//			if (x0 > x1)
//			{
//				int t = x1;
//				x1 = x0;
//				x0 = t;
//				t = y1;
//				y1 = y0;
//				y0 = t;
//			}
//			dx = 1;
//			dy = (y1 - y0) / (double)len;
//		}
//		else
//		{
//			if (y0 > y1)
//			{
//				int t = x1;
//				x1 = x0;
//				x0 = t;
//				t = y1;
//				y1 = y0;
//				y0 = t;
//			}
//			dx = (x1 - x0) / (double)len;
//			dy = 1;
//		}
//
//		double x = x0;
//		double y = y0;
//
//		for (int i = 0; i < len; i++)
//		{
//			calc(aGraphics, x0, y0, x1, y1, (int)x, (int)y);
//			aGraphics.setColor(Color.WHITE);
//			aGraphics.drawLine((int)x,(int)y,(int)x,(int)y);
//			x += dx;
//			y += dy;
//		}
//	}
//

//	private void calc(Graphics aGraphics, int startX, int startY, int endX, int endY, int x, int y)
//	{
//		// Calculate how far above or below the control point should be
//		int centrePointX = x;
//		int centrePointY = y;
//
//		// Calculate slopes and Y intersects
//		double lineSlope = (endY - startY) / (double)(endX - startX);
//		double perpendicularSlope = lineSlope == 0 ? -1 : -1 / lineSlope;
//		double yIntersect = centrePointY - (centrePointX * perpendicularSlope);
//
////		aGraphics.setColor(Color.WHITE);
////		aGraphics.drawOval(startX, startY, 4,4);
//		aGraphics.setColor(Color.RED);
//		
////		y = (int)((perpendicularSlope * (x+5)) + yIntersect);
//		y = (int)((perpendicularSlope * (x+5)) + yIntersect);
//		aGraphics.drawLine(x, y, x, y);
//	}
	
	@Override
	public double distance(Connection aConnection, Anchor aFromAnchor, Anchor aToAnchor, int aX, int aY)
	{
		Rectangle from = aFromAnchor.getBounds();
		Rectangle to = aToAnchor.getBounds();

		vec2 p = vec2.as(aX, aY);
		vec2 v = vec2.as(from.getCenterX(), from.getCenterY());
		vec2 w = vec2.as(to.getCenterX(), to.getCenterY());

		int d0 = aFromAnchor.getOritentation() == Anchor.LEFT ? -16 : 16;
		int d1 = aToAnchor.getOritentation() == Anchor.LEFT ? -16 : 16;

		return Math.min(p.distanceLineSegment(v, v.add(d0, 0)), Math.min(p.distanceLineSegment(v.add(d0, 0), w.add(d1, 0)), p.distanceLineSegment(w, w.add(d1, 0))));
	}
}
