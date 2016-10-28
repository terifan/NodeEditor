package org.terifan.ui.resizablepanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;


public class ResizablePanel extends JPanel
{
	private Dimension mRestoredDimension;
	private boolean mMinimized;
	private String mTitle;
	private boolean mResizableVertical;
	private boolean mResizableHorizontal;


	public ResizablePanel(Rectangle aBounds)
	{
		RelationBoxMouseListener relationBoxMouseListener = new RelationBoxMouseListener(this);

		super.addMouseListener(relationBoxMouseListener);
		super.addMouseMotionListener(relationBoxMouseListener);
		super.setBorder(new ResizablePanelBorder());
		super.setBounds(aBounds);
		super.setLayout(new BorderLayout());
		
		mResizableVertical = true;
		mResizableHorizontal = true;
	}


	public boolean isResizableVertical()
	{
		return mResizableVertical;
	}


	public ResizablePanel setResizableVertical(boolean aResizableVertical)
	{
		mResizableVertical = aResizableVertical;
		return this;
	}


	public boolean isResizableHorizontal()
	{
		return mResizableHorizontal;
	}


	public ResizablePanel setResizableHorizontal(boolean aResizableHorizontal)
	{
		this.mResizableHorizontal = aResizableHorizontal;
		return this;
	}


	@Override
	public Dimension getPreferredSize()
	{
		Dimension s = super.getPreferredSize();
		s.width = Math.max(s.width, 80);
		return s;
	}


	@Override
	public Dimension getMinimumSize()
	{
		Dimension s = super.getMinimumSize();
		s.width = Math.max(s.width, 80);
		return s;
	}


	protected Dimension getRestoredDimension()
	{
		return mRestoredDimension;
	}


	protected void setRestoredDimension(Dimension aRestoredDimension)
	{
		mRestoredDimension = aRestoredDimension;
	}


	public String getTitle()
	{
		return mTitle;
	}


	public void setTitle(String aTitle)
	{
		this.mTitle = aTitle;
	}


	public boolean isMinimized()
	{
		return mMinimized;
	}


	public void setMinimized(boolean aMinimized)
	{
		mMinimized = aMinimized;

		for (int i = 0; i < getComponentCount(); i++)
		{
			getComponent(i).setVisible(!mMinimized);
		}

		Dimension preferredSize = getPreferredSize();
		preferredSize.width = Math.max(80, preferredSize.width);

		if (mMinimized)
		{
			Rectangle bounds = getBounds();
			mRestoredDimension = bounds.getSize();
			setSize(preferredSize);
		}
		else
		{
			Rectangle bounds = getBounds();
			if (mRestoredDimension != null)
			{
				bounds.setSize(mRestoredDimension);
			}
			else
			{
				bounds.setSize(preferredSize);
			}
			setBounds(bounds);
		}

		invalidate();

		getParent().repaint();
	}


	protected void fireSelectedEvent()
	{
	}
}
