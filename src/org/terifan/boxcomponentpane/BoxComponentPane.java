package org.terifan.boxcomponentpane;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JComponent;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.SELECTION_RECTANGLE_STROKE;


public class BoxComponentPane<T extends BoxComponent> extends JComponent
{
	private final static long serialVersionUID = 1L;

	protected double mScale;
	protected Point mPaneScroll;
	protected Point mDragStartLocation;
	protected Point mDragEndLocation;
	protected Rectangle mSelectionRectangle;
	protected BoxComponentModel<T> mModel;
	protected ArrayList<T> mSelectedBoxes;


	public BoxComponentPane(BoxComponentModel aModel)
	{
		mSelectedBoxes = new ArrayList<>();
		mScale = 1;
		mModel = aModel;

		setupListeners();
	}


	protected void setupListeners()
	{
		BoxComponentMouseListener<T> mouseListener = new BoxComponentMouseListener<>(this);
		super.addMouseMotionListener(mouseListener);
		super.addMouseListener(mouseListener);
		super.addMouseWheelListener(mouseListener);
	}


	public double getScale()
	{
		return mScale;
	}


	public BoxComponentPane<T> setScale(double aScale)
	{
		mScale = aScale;
		return this;
	}


	public BoxComponentModel<T> getModel()
	{
		return mModel;
	}


	public ArrayList<T> getSelectedBoxes()
	{
		return mSelectedBoxes;
	}


	public void setSelectedBoxes(ArrayList<T> aSelectedBoxes)
	{
		this.mSelectedBoxes = aSelectedBoxes;
	}


	public Point getPaneScroll()
	{
		return mPaneScroll;
	}


	public Rectangle getSelectionRectangle()
	{
		return mSelectionRectangle;
	}


	public void setSelectionRectangle(Rectangle aSelectionRectangle)
	{
		this.mSelectionRectangle = aSelectionRectangle;
	}


	public Point getDragStartLocation()
	{
		return mDragStartLocation;
	}


	public void setDragStartLocation(Point aDragStartLocation)
	{
		this.mDragStartLocation = aDragStartLocation;
	}


	public Point getDragEndLocation()
	{
		return mDragEndLocation;
	}


	public void setDragEndLocation(Point aDragEndLocation)
	{
		this.mDragEndLocation = aDragEndLocation;
	}


	/**
	 * Move all nodes to the center of the screen
	 */
	public BoxComponentPane<T> center()
	{
		if (mModel.getNodes().isEmpty())
		{
			return this;
		}

		Rectangle bounds = new Rectangle(mModel.getNodes().get(0).getBounds());
		for (BoxComponent box : mModel.getNodes())
		{
			box.layout();
			bounds.add(box.getBounds());
		}

		int dx = -(int)bounds.getCenterX();
		int dy = -(int)bounds.getCenterY();

		for (BoxComponent box : mModel.getNodes())
		{
			box.getBounds().translate(dx, dy);
		}

		mPaneScroll = null; // will be centered when pane is repainted
		return this;
	}


	protected void paintBackground(Graphics2D aGraphics)
	{
		int w = getWidth();
		int h = getHeight();

		aGraphics.setColor(Styles.PANE_BACKGROUND_COLOR);
		aGraphics.fillRect(0, 0, w, h);

		int step = Math.max((int)(24 * mScale), 1);

		aGraphics.setColor(Styles.PANE_GRID_COLOR_1);
		for (int x = mPaneScroll.x % step; x < w; x += step)
		{
			aGraphics.drawLine(x, 0, x, h);
		}
		for (int y = mPaneScroll.y % step; y < h; y += step)
		{
			aGraphics.drawLine(0, y, w, y);
		}

		aGraphics.setColor(Styles.PANE_GRID_COLOR_2);
		for (int x = mPaneScroll.x % (5 * step); x < w; x += 5 * step)
		{
			aGraphics.drawLine(x, 0, x, h);
		}
		for (int y = mPaneScroll.y % (5 * step); y < h; y += 5 * step)
		{
			aGraphics.drawLine(0, y, w, y);
		}

		int w2 = mPaneScroll.x;
		int h2 = mPaneScroll.y;
		aGraphics.setColor(Styles.PANE_GRID_COLOR_3);
		aGraphics.drawLine(0, h2, w, h2);
		aGraphics.drawLine(w2, 0, w2, h);
	}


	@Override
	public Dimension getPreferredSize()
	{
		Rectangle bounds = null;
		for (T box : mModel.getNodes())
		{
			box.layout();
			if (bounds == null)
			{
				bounds = box.getBounds();
			}
			else
			{
				bounds.add(box.getBounds());
			}
		}

		return bounds.getSize();
	}


	protected void paintBox(Graphics2D aGraphics, Renderable aComponent, boolean aSelected)
	{
		Rectangle bounds = aComponent.getBounds();
		int x = (int)(bounds.x * mScale);
		int y = (int)(bounds.y * mScale);
		int width = (int)(bounds.width * mScale);
		int height = (int)(bounds.height * mScale);

		if (aGraphics.hitClip(x, y, width, height))
		{
			boolean offscreen = false;

			Graphics2D ig;
			AffineTransform affineTransform;
			BufferedImage offscreenBuffer = null;

			if (offscreen)
			{
				offscreenBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				ig = offscreenBuffer.createGraphics();
				affineTransform = new AffineTransform();
				affineTransform.scale(mScale, mScale);
			}
			else
			{
				ig = (Graphics2D)aGraphics.create(x, y, width, height);
				affineTransform = new AffineTransform();
				affineTransform.translate(mPaneScroll.x + x, mPaneScroll.y + y);
				affineTransform.scale(mScale, mScale);
			}

			ig.setTransform(affineTransform);
			ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			ig.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

			aComponent.paintComponent(this, ig, bounds.width, bounds.height, aSelected);

			ig.dispose();

			if (offscreen)
			{
				aGraphics.drawImage(offscreenBuffer, x, y, null);
			}
		}
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		if (mPaneScroll == null)
		{
			mPaneScroll = new Point(getWidth() / 2, getHeight() / 2);
		}

		for (T box : mModel.getNodes())
		{
			box.layout();
		}

		Graphics2D g = (Graphics2D)aGraphics;

		paintBackground(g);

		AffineTransform oldTransform = g.getTransform();

		g.translate(mPaneScroll.x, mPaneScroll.y);

		paintElements(g);

		g.setTransform(oldTransform);
	}


	protected void paintElements(Graphics2D aGraphics)
	{
		aGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		for (T box : mModel.getNodes())
		{
			paintBox(aGraphics, box, mSelectedBoxes.contains(box));
		}

		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

		if (mSelectionRectangle != null)
		{
			aGraphics.setColor(Styles.PANE_SELECTION_RECTANGLE_BACKGROUND);
			aGraphics.fillRect(mSelectionRectangle.x, mSelectionRectangle.y, mSelectionRectangle.width + 1, mSelectionRectangle.height + 1);
			aGraphics.setColor(Styles.PANE_SELECTION_RECTANGLE_LINE);
			aGraphics.setStroke(SELECTION_RECTANGLE_STROKE);
			aGraphics.draw(mSelectionRectangle);
		}
	}
}
