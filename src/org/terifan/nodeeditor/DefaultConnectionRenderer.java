package org.terifan.nodeeditor;

import org.terifan.graphics.BSpline;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import org.terifan.math.vec2;


public class DefaultConnectionRenderer implements ConnectionRenderer
{
	private final static BasicStroke STROKE_WIDE = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	private final static BasicStroke STROKE_THIN = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);


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
		g.setColor(aSelected ? Styles.CONNECTOR_COLOR_DARK_SELECTED : Styles.CONNECTOR_COLOR_DARK);
		drawSpline(g, spline);

		g.setStroke(STROKE_THIN);
		g.setColor(aSelected ? Styles.CONNECTOR_COLOR_BRIGHT_SELECTED : Styles.CONNECTOR_COLOR_BRIGHT);
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
