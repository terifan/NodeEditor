package org.terifan.nodeeditor;

import org.terifan.graphics.BSpline;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import org.terifan.vecmath.Vec2d;
import org.terifan.vecmath.VectorMath;


public class SplineRenderer
{
	private SplineRenderer()
	{
	}


	public static void drawSpline(Graphics2D aGraphics, Point aFrom, Point aTo, double aScale, Color aBackgroundColor, Color aStartColor, Color aEndColor)
	{
		drawSplineImpl(aGraphics, createSpline(aFrom, aTo), aScale, aBackgroundColor, aStartColor, aEndColor);
	}


	public static void drawSpline(Graphics2D aGraphics, Connection aConnection, double aScale, Color aBackgroundColor, Color aStartColor, Color aEndColor)
	{
		drawSplineImpl(aGraphics, createSpline(aConnection), aScale, aBackgroundColor, aStartColor, aEndColor);
	}


	private static void drawSplineImpl(Graphics2D aGraphics, BSpline aSpline, double aScale, Color aBackgroundColor, Color aStartColor, Color aEndColor)
	{
		Stroke old = aGraphics.getStroke();

		float strokeScale = (float)Math.sqrt(aScale);
		BasicStroke STROKE_WIDE = new BasicStroke(Styles.CONNECTOR_STROKE_WIDTH_OUTER * strokeScale, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
		BasicStroke STROKE_THIN = new BasicStroke(Styles.CONNECTOR_STROKE_WIDTH_INNER * strokeScale, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

		Path2D.Double spline = createPath(aSpline, aScale, 0.0, 1.0);
		aGraphics.setStroke(STROKE_WIDE);
		aGraphics.setColor(aBackgroundColor);
		aGraphics.draw(spline);

		aGraphics.setStroke(STROKE_THIN);

		if (aStartColor.equals(aEndColor))
		{
			aGraphics.setColor(aStartColor);
			aGraphics.draw(spline);
		}
		else
		{
			int r0 = aStartColor.getRed();
			int r1 = aEndColor.getRed();
			int g0 = aStartColor.getGreen();
			int g1 = aEndColor.getGreen();
			int b0 = aStartColor.getBlue();
			int b1 = aEndColor.getBlue();

			int segments = Math.max(16, (int)Math.pow(aSpline.getPoint(0).distance(aSpline.getPoint(1)), 0.5));

			for (int i = 0; i < segments; i++)
			{
				spline = createPath(aSpline, aScale, i / (double)segments, (i + 1) / (double)segments);

				double a = i / (double)(segments - 1);
				int r = (int)(a * r1 + (1 - a) * r0);
				int g = (int)(a * g1 + (1 - a) * g0);
				int b = (int)(a * b1 + (1 - a) * b0);

				aGraphics.setColor(new Color(r, g, b));
				aGraphics.draw(spline);
			}
		}

		aGraphics.setStroke(old);
	}


	public static double distance(Connection aConnection, Point aPoint)
	{
		BSpline spline = createSpline(aConnection);
		Point2D.Double prev = null;
		Vec2d p = new Vec2d(aPoint.x, aPoint.y);
		int segments = Math.max(20, (int)spline.getPoint(0).distance(spline.getPoint(1)) / 4);
		double dist = Double.MAX_VALUE;

		for (double i = 0, s = segments; --s >= 0; i += 1.0 / s)
		{
			Point2D.Double next = spline.getPoint(i);
			if (prev != null)
			{
				double d = VectorMath.distanceLineSegment(new Vec2d(prev.x, prev.y), new Vec2d(next.x, next.y), p);
				if (d < dist)
				{
					dist = d;
				}
			}
			prev = next;
		}

		return dist;
	}


	private static Path2D.Double createPath(BSpline aSpline, double aScale, double aStart, double aEnd)
	{
		int segments = Math.max(20, (int)aSpline.getPoint(0).distance(aSpline.getPoint(1)) / 4);

		Path2D.Double path = new Path2D.Double(Path2D.WIND_EVEN_ODD, segments);

		boolean first = true;
		for (int i = (int)(segments * aStart); i < (int)(segments * aEnd) + 1; i++)
		{
			Point2D.Double pt = aSpline.getPoint(i / (double)(segments - 1));
			if (first)
			{
				first = false;
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
		Rectangle from = aConnection.getOut().getBounds();
		Rectangle to = aConnection.getIn().getBounds();

		Rectangle b0 = aConnection.getOut().getNodeItem().getNode().getBounds();
		Rectangle b1 = aConnection.getIn().getNodeItem().getNode().getBounds();

		int x0 = (int)from.getCenterX() + b0.x;
		int y0 = (int)from.getCenterY() + b0.y;
		int x1 = (int)to.getCenterX() + b1.x;
		int y1 = (int)to.getCenterY() + b1.y;
		int d0 = 16;
		int d1 = -16;

		return new BSpline(new double[]
		{
			x0, x0 + d0, x1 + d1, x1
		}, new double[]
		{
			y0, y0, y1, y1
		});
	}


	private static BSpline createSpline(Point aFrom, Point aTo)
	{
		int x0 = aFrom.x;
		int y0 = aFrom.y;
		int x1 = aTo.x;
		int y1 = aTo.y;
		int d0 = 16;
		int d1 = -16;

		return new BSpline(new double[]
		{
			x0, x0 + d0, x1 + d1, x1
		}, new double[]
		{
			y0, y0, y1, y1
		});
	}
}
