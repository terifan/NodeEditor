package org.terifan.nodeeditor;

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
	protected Rectangle[] mOptionBounds;
	protected String[] mOptions =
	{
		"Absolute", "Modulo", "Greater Than"
	};


	public Popup(NodeItem aOwner, Rectangle aBounds)
	{
		mBounds = aBounds;
		mOwner = aOwner;

		mIndex = -1;
		mAboveField = !false;

		mOptionBounds = new Rectangle[mOptions.length];

		mBounds.translate(mOwner.getNodeBox().getBounds().x, mOwner.getNodeBox().getBounds().y);

		mBounds.height = Styles.POPUP_HEADER_HEIGHT + Styles.POPUP_FOOTER_HEIGHT;
		for (int i = 0; i < mOptions.length; i++)
		{
			mOptionBounds[i] = new Rectangle();

			mBounds.height += optionHeight(i);
		}

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

		aGraphics.setColor(Styles.POPUP_BACKGROUND);
		aGraphics.fill(path);

		int ly = Styles.POPUP_HEADER_HEIGHT * 6 / 7;
		TextBox textBox = new TextBox("Operation").setAnchor(Anchor.WEST).setForeground(Styles.POPUP_HEADER_FOREGROUND);

		aGraphics.setColor(Styles.POPUP_HEADER_LINE);
		if (mAboveField)
		{
			aGraphics.drawLine(0, ly, aWidth, ly);
			textBox.setBounds(10, 0, aWidth-10, ly).render(aGraphics);
		}
		else
		{
			aGraphics.drawLine(0, aHeight - ly, aWidth, aHeight - ly);
			textBox.setBounds(10, aHeight - ly, aWidth-10, ly).render(aGraphics);
		}

		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		int offset = mAboveField ? Styles.POPUP_HEADER_HEIGHT : Styles.POPUP_FOOTER_HEIGHT;

		TextBox text = new TextBox().setAnchor(Anchor.WEST).setForeground(Styles.POPUP_FOREGROUND).setMargins(0,10,0,0);

		for (int i = 0; i < mOptions.length; i++)
		{
			int ih = optionHeight(i);

			mOptionBounds[i].setBounds(0, offset, aWidth, ih);

			if (i == mIndex)
			{
				aGraphics.setColor(Styles.POPUP_SELECTION_BACKGROUND);
				aGraphics.fill(mOptionBounds[i]);
			}

			text.setText(mOptions[i]).setBounds(mOptionBounds[i]).render(aGraphics);

			offset += ih;
		}
	}


	protected void mouseMoved(Point aPoint)
	{
		int x = aPoint.x - mBounds.x;
		int y = aPoint.y - mBounds.y;
		int s = -1;

		if (x < -Styles.POPUP_EXTRA_HORIZONTAL_HOVER || x > mBounds.width + Styles.POPUP_EXTRA_HORIZONTAL_HOVER || y < 0)
		{
			s = -1;
		}
		else
		{
			for (int i = 0; i < mOptionBounds.length; i++)
			{
				if (mOptionBounds[i].contains(x,y))
				{
					s = i;
					break;
				}
			}
		}

		if (mIndex != s)
		{
			mIndex = s;
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


	protected int optionHeight(int aIndex)
	{
		return Styles.POPUP_DEFAULT_OPTION_HEIGHT;
	}
}
