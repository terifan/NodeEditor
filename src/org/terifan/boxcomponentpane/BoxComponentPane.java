package org.terifan.boxcomponentpane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JComponent;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.SELECTION_RECTANGLE_STROKE;


public class BoxComponentPane<T extends BoxComponent, U extends BoxComponentPane> extends JComponent
{
	private final static long serialVersionUID = 1L;

	private double mScale;
	private Point2D.Double mScroll;
	private Point mDragStartLocation;
	private Point mDragEndLocation;
	private Rectangle mSelectionRectangle;
	private BoxComponentModel<T> mModel;
	private ArrayList<T> mSelectedBoxes;


	public BoxComponentPane(BoxComponentModel aModel)
	{
		mSelectedBoxes = new ArrayList<>();
		mScale = 1;
		mModel = aModel;

		setupListeners();
	}


	protected void setupListeners()
	{
		BoxComponentMouseListener<T, U> mouseListener = new BoxComponentMouseListener<T, U>((U)this);
		super.addMouseMotionListener(mouseListener);
		super.addMouseListener(mouseListener);
		super.addMouseWheelListener(mouseListener);
	}


	public double getScale()
	{
		return mScale;
	}


	public U setScale(double aScale)
	{
		mScale = aScale;
		return (U)this;
	}


	public BoxComponentModel<T> getModel()
	{
		return mModel;
	}


	public ArrayList<T> getSelectedBoxes()
	{
		return mSelectedBoxes;
	}


	public U setSelectedBoxes(ArrayList<T> aSelectedBoxes)
	{
		mSelectedBoxes = aSelectedBoxes;
		return (U)this;
	}


	public Point2D.Double getPaneScroll()
	{
		return mScroll;
	}


	public Rectangle getSelectionRectangle()
	{
		return mSelectionRectangle;
	}


	public void setSelectionRectangle(Rectangle aSelectionRectangle)
	{
		mSelectionRectangle = aSelectionRectangle;
	}


	public Point getDragStartLocation()
	{
		return mDragStartLocation;
	}


	public void setDragStartLocation(Point aDragStartLocation)
	{
		mDragStartLocation = aDragStartLocation;
	}


	public Point getDragEndLocation()
	{
		return mDragEndLocation;
	}


	public void setDragEndLocation(Point aDragEndLocation)
	{
		mDragEndLocation = aDragEndLocation;
	}


	/**
	 * Move all nodes to the center of the screen
	 */
	public U center()
	{
		if (mModel.getComponents().isEmpty())
		{
			return (U)this;
		}

		Rectangle bounds = new Rectangle(mModel.getComponents().get(0).getBounds());
		for (BoxComponent box : mModel.getComponents())
		{
			box.layout();
			bounds.add(box.getBounds());
		}

		int dx = -(int)bounds.getCenterX();
		int dy = -(int)bounds.getCenterY();

		for (BoxComponent box : mModel.getComponents())
		{
			box.getBounds().translate(dx, dy);
		}

		mScroll = null; // will be centered when pane is repainted
		return (U)this;
	}


	@Override
	public Dimension getPreferredSize()
	{
		Rectangle bounds = null;
		for (T box : mModel.getComponents())
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


	protected void paintBackground(Graphics2D aGraphics)
	{
		int w = getWidth();
		int h = getHeight();
		int sx = (int)mScroll.x;
		int sy = (int)mScroll.y;

		float gcr = Styles.PANE_GRID_COLOR_3.getRed() / 255f;
		float gcg = Styles.PANE_GRID_COLOR_3.getGreen() / 255f;
		float gcb = Styles.PANE_GRID_COLOR_3.getBlue() / 255f;

		aGraphics.setColor(Styles.PANE_BACKGROUND_COLOR);
		aGraphics.fillRect(0, 0, w, h);

		for (int i = 0; i < 10; i++)
		{
			double s = mScale * Math.pow(5, i);
			if (s > 15 && s < w)
			{
				aGraphics.setColor(new Color(gcr, gcg, gcb, Math.min((float)(s / 200), 1f)));
				drawGrid(aGraphics, w, h, s);
			}
		}

		aGraphics.setColor(Styles.PANE_GRID_COLOR_1);
		aGraphics.drawLine(0, sy - 1, w, sy - 1);
		aGraphics.drawLine(0, sy + 1, w, sy + 1);
		aGraphics.drawLine(sx - 1, 0, sx - 1, h);
		aGraphics.drawLine(sx + 1, 0, sx + 1, h);
		aGraphics.setColor(Styles.PANE_GRID_COLOR_2);
		aGraphics.drawLine(0, sy, w, sy);
		aGraphics.drawLine(sx, 0, sx, h);
	}


	private void drawGrid(Graphics2D aGraphics, int aW, int aH, double aScale)
	{
		int xi = (int)((mScroll.x - aW / 2) / aScale);
		int yi = (int)((mScroll.y - aH / 2) / aScale);
		int wr = (int)Math.ceil(1 + aW / 2 / aScale);
		int hr = (int)Math.ceil(1 + aH / 2 / aScale);

		for (int i = 0; i < wr; i++)
		{
			int x0 = (int)((-i - xi) * aScale + mScroll.x);
			int x1 = (int)((+i - xi) * aScale + mScroll.x);
			aGraphics.drawLine(x0, 0, x0, aH);
			aGraphics.drawLine(x1, 0, x1, aH);
		}
		for (int i = 0; i < hr; i++)
		{
			int y0 = (int)((-i - yi) * aScale + mScroll.y);
			int y1 = (int)((+i - yi) * aScale + mScroll.y);
			aGraphics.drawLine(0, y0, aW, y0);
			aGraphics.drawLine(0, y1, aW, y1);
		}
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		if (mScroll == null)
		{
			mScroll = new Point.Double(getWidth() / 2.0, getHeight() / 2.0);
		}

		for (T box : mModel.getComponents())
		{
			box.layout();
		}

		Graphics2D g = (Graphics2D)aGraphics;

		paintBackground(g);

		AffineTransform oldTransform = g.getTransform();
		g.translate((int)mScroll.x, (int)mScroll.y);
		paintBoxComponents(g);
		paintSelectionRectangle(g);
		g.setTransform(oldTransform);
	}


	protected void paintBoxComponents(Graphics2D aGraphics)
	{
		aGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		for (T box : mModel.getComponents())
		{
			paintBoxComponent(aGraphics, box, mSelectedBoxes.contains(box));
		}

		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
	}


	protected void paintSelectionRectangle(Graphics2D aGraphics)
	{
		if (mSelectionRectangle != null)
		{
			aGraphics.setColor(Styles.PANE_SELECTION_RECTANGLE_BACKGROUND);
			aGraphics.fillRect(mSelectionRectangle.x, mSelectionRectangle.y, mSelectionRectangle.width + 1, mSelectionRectangle.height + 1);
			aGraphics.setColor(Styles.PANE_SELECTION_RECTANGLE_LINE);
			aGraphics.setStroke(SELECTION_RECTANGLE_STROKE);
			aGraphics.draw(mSelectionRectangle);
		}
	}


	protected void paintBoxComponent(Graphics2D aGraphics, Renderable aComponent, boolean aSelected)
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
				affineTransform.translate((int)(mScroll.x + x), (int)(mScroll.y + y));
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


	public Point calcMousePoint(Point aPoint)
	{
		return new Point(
			(int)((aPoint.x - mScroll.x) / mScale),
			(int)((aPoint.y - mScroll.y) / mScale)
		);

//		aPoint.x = (int)((aPoint.x - mScroll.x) / mScale);
//		aPoint.y = (int)((aPoint.y - mScroll.y) / mScale);
//
//		return aPoint;
	}
}
