package org.terifan.nodeeditor.v2;

import org.terifan.graphics.BSpline;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import org.terifan.math.vec2;


public class SplineRenderer
{
	private SplineRenderer()
	{
	}
	
	
	public static void drawSpline(Graphics2D aGraphics, Point aFrom, Point aTo, double aScale, boolean aSelected)
	{
		drawSplineImpl(aGraphics, createSpline(aFrom, aTo), aScale, aSelected);
	}


	public static void drawSpline(Graphics2D aGraphics, Connection aConnection, double aScale, boolean aSelected)
	{
		drawSplineImpl(aGraphics, createSpline(aConnection), aScale, aSelected);
	}


	private static void drawSplineImpl(Graphics2D aGraphics, BSpline aSpline, double aScale, boolean aSelected)
	{
		Path2D.Double spline = createPath(aSpline, aScale);

		Stroke old = aGraphics.getStroke();

		BasicStroke STROKE_WIDE = new BasicStroke(3.0f * (float)aScale, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
		BasicStroke STROKE_THIN = new BasicStroke(1.4f * (float)aScale, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

		aGraphics.setStroke(STROKE_WIDE);
		aGraphics.setColor(aSelected ? Styles.CONNECTOR_COLOR_DARK_SELECTED : Styles.CONNECTOR_COLOR_DARK);
		aGraphics.draw(spline);

		aGraphics.setStroke(STROKE_THIN);
		aGraphics.setColor(aSelected ? Styles.CONNECTOR_COLOR_BRIGHT_SELECTED : Styles.CONNECTOR_COLOR_BRIGHT);
		aGraphics.draw(spline);

		aGraphics.setStroke(old);
	}


	public static double distance(Connection aConnection, Point aPoint)
	{
		BSpline spline = createSpline(aConnection);
		Point2D.Double prev = null;
		vec2 p = vec2.as(aPoint.x, aPoint.y);
		int segments = Math.max(20, (int)spline.getPoint(0).distance(spline.getPoint(1)) / 4);
		double dist = Double.MAX_VALUE;

		for (double i = 0, s = segments; --s >= 0; i+=1.0/s)
		{
			Point2D.Double next = spline.getPoint(i);
			if (prev != null)
			{
				double d = p.distanceLineSegment(new vec2(prev.x, prev.y), new vec2(next.x, next.y));
				if (d < dist)
				{
					dist = d;
				}
			}
			prev = next;
		}

		return dist;
	}


	private static Path2D.Double createPath(BSpline aSpline, double aScale)
	{
		int segments = Math.max(20, (int)aSpline.getPoint(0).distance(aSpline.getPoint(1)) / 4);

		Path2D.Double path = new Path2D.Double(Path2D.WIND_EVEN_ODD, segments);

		for (int i = 0; i < segments; i++)
		{
			Point2D.Double pt = aSpline.getPoint(i / (double)(segments - 1));
			if (i == 0)
			{
				path.moveTo(pt.x, pt.y);
			}
			else
			{
				path.lineTo(pt.x, pt.y);
			}
		}

		AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale(aScale, aScale);
		path.transform(affineTransform);

		return path;
	}


	private static BSpline createSpline(Connection aConnection)
	{
		Rectangle from = aConnection.mOut.getBounds();
		Rectangle to = aConnection.mIn.getBounds();

		Rectangle b0 = aConnection.mOut.mRelationItem.mRelationBox.getBounds();
		Rectangle b1 = aConnection.mIn.mRelationItem.mRelationBox.getBounds();

		int x0 = (int)from.getCenterX() + b0.x;
		int y0 = (int)from.getCenterY() + b0.y;
		int x1 = (int)to.getCenterX() + b1.x;
		int y1 = (int)to.getCenterY() + b1.y;
		int d0 = -16;
		int d1 = 16;

		return new BSpline(new double[]{x0, x0 + d0, x1 + d1, x1}, new double[]{y0, y0, y1, y1});
	}


	private static BSpline createSpline(Point aFrom, Point aTo)
	{
		int x0 = aFrom.x;
		int y0 = aFrom.y;
		int x1 = aTo.x;
		int y1 = aTo.y;
		int d0 = -16;
		int d1 = 16;

		return new BSpline(new double[]{x0, x0 + d0, x1 + d1, x1}, new double[]{y0, y0, y1, y1});
	}
}
