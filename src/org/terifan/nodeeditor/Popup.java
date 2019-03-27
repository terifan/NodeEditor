package org.terifan.nodeeditor;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Path2D;
import java.util.List;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import org.terifan.util.Strings;


public class Popup implements Renderable
{
	protected final NodeEditor mEditor;
	protected final Rectangle mBounds;
	protected final NodeItem mOwner;
	protected final boolean mAboveField;
	protected final ResultReceiver mResultReceiver;
	protected List<Option> mOptions;
	protected Option mSelectedOption;
	protected String mHeader;


	/**
	 * Constructs a new Pop-up.
	 *
	 * @param aOwner
	 * @param aHeader
	 *   optional text header
	 * @param aBounds
	 *   with and heigh will be ignored if the options list contain any elements
	 * @param aOptions
	 *   list of selectable options, can be empty
	 * @param aResultReceiver
	 */
	public Popup(NodeEditor aEditor, NodeItem aOwner, String aHeader, Rectangle aBounds, List<Option> aOptions, ResultReceiver aResultReceiver)
	{
		mHeader = aHeader;
		mOwner = aOwner;
		mOptions = aOptions;
		mResultReceiver = aResultReceiver;

		mSelectedOption = null;
		mAboveField = false;

		mBounds = new Rectangle(mOwner.getNode().getBounds().x + aBounds.x, mOwner.getNode().getBounds().y + aBounds.y, aBounds.width, aBounds.height);

		if (!mOptions.isEmpty())
		{
			Rectangle b = new Rectangle();
			for (Option option : mOptions)
			{
				b.add(option.getBounds());
			}
			mBounds.width = b.width;
			mBounds.height = headerHeight() + Styles.POPUP_FOOTER_HEIGHT + b.height;
		}

		if (mAboveField)
		{
			mBounds.y -= mBounds.height;
		}
		else
		{
			mBounds.y += aOwner.mBounds.height;
		}
		this.mEditor = aEditor;
	}


	protected int headerHeight()
	{
		return Strings.isEmptyOrNull(mHeader) ? Styles.POPUP_FOOTER_HEIGHT : Styles.POPUP_HEADER_HEIGHT;
	}


	@Override
	public Rectangle getBounds()
	{
		return mBounds;
	}


	@Override
	public void layout()
	{
	}


	@Override
	public void paintComponent(NodeEditor aEditor, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected)
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

		if (!Strings.isEmptyOrNull(mHeader))
		{
			int ly = headerHeight() * 6 / 7;
			TextBox textBox = new TextBox(mHeader).setAnchor(Anchor.WEST).setForeground(Styles.POPUP_HEADER_FOREGROUND);

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
		}

		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		if (!mOptions.isEmpty())
		{
			aGraphics.translate(0, mAboveField ? headerHeight() : Styles.POPUP_FOOTER_HEIGHT);

			for (Option option : mOptions)
			{
				option.paintOption(aGraphics, option == mSelectedOption);
			}

			aGraphics.translate(0, mAboveField ? -headerHeight() : -Styles.POPUP_FOOTER_HEIGHT);
		}
	}


	protected void mouseMoved(Point aPoint)
	{
		if (!mOptions.isEmpty())
		{
			int x = aPoint.x - mBounds.x;
			int y = aPoint.y - mBounds.y - (mAboveField ? headerHeight() : Styles.POPUP_FOOTER_HEIGHT);
			Option s = null;

			if (x >= 0 && x < mBounds.width && y > 0)
			{
				for (Option option : mOptions)
				{
					if (option.getBounds().contains(x, y))
					{
						s = option;
						break;
					}
				}
			}

			if (mSelectedOption != s)
			{
				mSelectedOption = s;
				mEditor.repaint();
			}
		}
	}


	protected void mousePressed(MouseEvent aEvent)
	{
		if (mSelectedOption != null)
		{
			mResultReceiver.popupResult(mSelectedOption);
		}
	}


	protected void mouseReleased(MouseEvent aEvent)
	{
		mOwner.mouseReleased(mEditor, aEvent.getPoint());
	}


	protected void mouseWheelMoved(MouseWheelEvent aEvent)
	{
	}


	@FunctionalInterface
	public interface ResultReceiver
	{
		void popupResult(Option aSelectedOption);
	}


	public interface Option
	{
		/**
		 * Return the bounds of the item within the popup. The popup size will be equal to the combined size of all options.
		 */
		Rectangle getBounds();

		void paintOption(Graphics2D aGraphics, boolean aSelected);
	}
}
