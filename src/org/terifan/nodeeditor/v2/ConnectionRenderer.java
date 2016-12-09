package org.terifan.nodeeditor.v2;

import org.terifan.graphics.BSpline;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import org.terifan.math.vec2;


public class ConnectionRenderer
{
	public void render(Graphics2D aGraphics, Connection aConnection, boolean aSelected, double aScale)
	{
		BSpline spline = createSpline(aConnection, aConnection.mOut, aConnection.mIn);

		Stroke old = aGraphics.getStroke();

		BasicStroke STROKE_WIDE = new BasicStroke(3.0f * (float)aScale, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
		BasicStroke STROKE_THIN = new BasicStroke(1.4f * (float)aScale, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

		aGraphics.setStroke(STROKE_WIDE);
		aGraphics.setColor(aSelected ? Styles.CONNECTOR_COLOR_DARK_SELECTED : Styles.CONNECTOR_COLOR_DARK);
		drawSpline(aGraphics, spline, aScale);

		aGraphics.setStroke(STROKE_THIN);
		aGraphics.setColor(aSelected ? Styles.CONNECTOR_COLOR_BRIGHT_SELECTED : Styles.CONNECTOR_COLOR_BRIGHT);
		drawSpline(aGraphics, spline, aScale);

		aGraphics.setStroke(old);
	}


	private void drawSpline(Graphics2D aGraphics, BSpline aSpline, double aScale)
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

		aGraphics.draw(path);
	}


	public double distance(Connection aConnection, Connector aFromAnchor, Connector aToAnchor, int aX, int aY)
	{
		BSpline spline = createSpline(aConnection, aFromAnchor, aToAnchor);
		Point2D.Double prev = null;
		vec2 p = vec2.as(aX, aY);
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


	protected BSpline createSpline(Connection aConnection, Connector aFromAnchor, Connector aToAnchor)
	{
		Rectangle from = aFromAnchor.getBounds();
		Rectangle to = aToAnchor.getBounds();

		Rectangle b0 = aConnection.mOut.mRelationItem.mRelationBox.getBounds();
		Rectangle b1 = aConnection.mIn.mRelationItem.mRelationBox.getBounds();

		int x0 = (int)from.getCenterX() + b0.x;
		int y0 = (int)from.getCenterY() + b0.y;
		int x1 = (int)to.getCenterX() + b1.x;
		int y1 = (int)to.getCenterY() + b1.y;
		int d0 = -16;
		int d1 = 16;

		return new BSpline(new double[]{x0, x0+d0, x1+d1, x1}, new double[]{y0, y0, y1, y1});
	}
}
