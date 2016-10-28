package org.terifan.ui.relationeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import org.terifan.math.vec2;


public class DefaultConnectionRenderer implements ConnectionRenderer
{
	private final static BasicStroke basicStroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	private final static BasicStroke basicStroke1 = new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL);
	private final static BasicStroke basicStroke2 = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	private final static Color COLOR_DARK = new Color(128, 128, 128);
	private final static Color COLOR_BRIGHT = new Color(192, 192, 192);
	private static final Color COLOR_BRIGHT_SELECTED = new Color(192, 0, 0);
	private static final Color COLOR_DARK_SELECTED = new Color(128, 0, 0);


	@Override
	public void render(Graphics aGraphics, Connection aConnection, Anchor aFromAnchor, Anchor aToAnchor, boolean aSelected)
	{
		Graphics2D g = (Graphics2D)aGraphics;

		Rectangle from = aFromAnchor.getBounds();
		Rectangle to = aToAnchor.getBounds();

		int x0 = (int)from.getCenterX();
		int y0 = (int)from.getCenterY();
		int x1 = (int)to.getCenterX();
		int y1 = (int)to.getCenterY();
		int d0 = aFromAnchor.getOritentation() == Anchor.LEFT ? -16 : 16;
		int d1 = aToAnchor.getOritentation() == Anchor.LEFT ? -16 : 16;

		Stroke old = g.getStroke();

		g.setStroke(basicStroke);
		g.setColor(aSelected ? COLOR_DARK_SELECTED : COLOR_DARK);
		g.drawLine(x0 + d0, y0, x1 + d1, y1);
		g.setStroke(basicStroke1);
		g.drawLine(x0, y0, x0 + d0, y0);
		g.drawLine(x1, y1, x1 + d1, y1);
		
		g.setStroke(basicStroke2);
		g.setColor(aSelected ? COLOR_BRIGHT_SELECTED : COLOR_BRIGHT);
		g.drawLine(x0, y0, x0 + d0, y0);
		g.drawLine(x0 + d0, y0, x1 + d1, y1);
		g.drawLine(x1, y1, x1 + d1, y1);

		g.setStroke(old);
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
