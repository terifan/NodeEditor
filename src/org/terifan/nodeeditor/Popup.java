package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Path2D;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class Popup implements Renderable
{
	protected final Rectangle mBounds;
	protected final NodeItem mOwner;
	protected final boolean mAboveField;
	protected int mIndex;

	protected String[] mOptions =
	{
		"Absolute", "Modulo", "Greater Than"
	};


	public Popup(NodeItem aOwner, Rectangle aBounds)
	{
		mBounds = aBounds;
		mOwner = aOwner;

		mIndex = -1;
		mAboveField = false;

		Rectangle pb = mOwner.getNodeBox().getBounds();
		mBounds.translate(pb.x, pb.y);

		mBounds.height = 35 + 20 * mOptions.length + 2;

		if (mAboveField)
		{
			mBounds.y -= mBounds.height;
		}
		else
		{
			mBounds.y += aOwner.mBounds.height;
		}
	}


	@Override
	public Rectangle getBounds()
	{
		return mBounds;
	}


	@Override
	public void layout(Graphics2D aGraphics)
	{
	}


	@Override
	public void paintComponent(Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected)
	{
		int w = aWidth;
		int h = aHeight;

		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

		Path2D.Double path = new Path2D.Double(Path2D.WIND_EVEN_ODD, 6);

		if (mAboveField)
		{
			path.moveTo(10, 0);
			path.lineTo(w - 10, 0);
			path.quadTo(w, 0, w, 10);
			path.lineTo(w, h);
			path.lineTo(0, h);
			path.lineTo(0, 10);
			path.quadTo(0, 0, 10, 0);
		}
		else
		{
			path.moveTo(0, 0);
			path.lineTo(w, 0);
			path.lineTo(w, h - 10);
			path.quadTo(w, h, w - 10, h);
			path.lineTo(10, h);
			path.quadTo(0, h, 0, h - 10);
			path.lineTo(0, 0);
		}

		aGraphics.setColor(new Color(0, 0, 0, 160));
		aGraphics.fill(path);

		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		aGraphics.setColor(new Color(55, 55, 55));
		aGraphics.drawLine(0, 30, aWidth, 30);

		new TextBox("Operation").setMargins(0, 10, 0, 10).setAnchor(Anchor.WEST).setBounds(0, 0, aWidth, 30).setForeground(new Color(208, 208, 208)).render(aGraphics);

		if (mIndex >= 0)
		{
			aGraphics.setColor(new Color(255, 0, 0, 128));
			aGraphics.fillRect(0, 35 + mIndex * 20, aWidth, 20);
		}

		TextBox text = new TextBox().setAnchor(Anchor.WEST).setForeground(new Color(255, 255, 255));

		for (int i = 0; i < mOptions.length; i++)
		{
			text.setText(mOptions[i]).setBounds(10, 35 + i * 20, aWidth, 20).render(aGraphics);
		}
	}


	protected void mouseMoved(Point aPoint)
	{
		int x = aPoint.x - mBounds.x;
		int y = aPoint.y - mBounds.y - 35;

		int i = y / 20;

		if (x < -50 || x > mBounds.width + 50 || y < 0 || mIndex >= mOptions.length)
		{
			i = -1;
		}

		if (mIndex != i)
		{
			mIndex = i;
			mOwner.getNodeBox().getEditorPane().repaint();
		}
	}


	protected void mousePressed(MouseEvent aEvent)
	{
	}


	protected void mouseReleased(MouseEvent aEvent)
	{
		mOwner.mouseReleased(mOwner.getNodeBox().getEditorPane(), aEvent.getPoint());
	}


	protected void mouseWheelMoved(MouseWheelEvent aEvent)
	{
	}
}
