package org.terifan.ui.relationeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;


public class ResizablePanel extends JPanel
{
	private ResizablePanelBorder mPanelBorder;
	private Dimension mRestoredDimension;
	private boolean mMinimized;
	private String mTitle;
	private boolean mResizableVertical;
	private boolean mResizableHorizontal;
	private int mMinWidth;
	private int mMinHeight;


	public ResizablePanel(Rectangle aBounds, String aTitle)
	{
		ResizablePanelMouseListener mouseListener = new ResizablePanelMouseListener(this);

		mPanelBorder = new ResizablePanelBorder_Blender(this);
//		mPanelBorder = new ResizablePanelBorder_Regular(this);

		super.addMouseListener(mouseListener);
		super.addMouseMotionListener(mouseListener);
		super.setBorder(mPanelBorder);
		super.setBounds(aBounds);
		super.setLayout(new BorderLayout());
		super.setOpaque(false);

		mTitle = aTitle;
		mResizableVertical = true;
		mResizableHorizontal = true;
		mMinWidth = 80;
		mMinHeight = 4 + Styles.TITLE_HEIGHT + 4;
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
		mResizableHorizontal = aResizableHorizontal;
		return this;
	}


	public int getMinWidth()
	{
		return mMinWidth;
	}


	public void setMinWidth(int aMinWidth)
	{
		mMinWidth = aMinWidth;
	}


	public int getMinHeight()
	{
		return mMinHeight;
	}


	public void setMinHeight(int aMinHeight)
	{
		mMinHeight = aMinHeight;
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
		mTitle = aTitle;
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
		preferredSize.width = Math.max(preferredSize.width, mMinWidth);

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


	public void mouseClicked(MouseEvent aEvent)
	{
		mPanelBorder.mouseClicked(aEvent);
	}
}
