package org.terifan.ui.relationeditor;

import java.awt.Rectangle;


public class Anchor
{
	static int LEFT = 1;
	static int RIGHT = 2;

	private Rectangle mBounds;
	private int mOritentation;


	public Anchor(Rectangle aBounds, int aOritentation)
	{
		mBounds = aBounds;
		mOritentation = aOritentation;
	}


	public Rectangle getBounds()
	{
		return mBounds;
	}


	public int getOritentation()
	{
		return mOritentation;
	}
}
