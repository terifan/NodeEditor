package org.terifan.boxcomponentpane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class EmptyBoxComponent extends BoxComponent
{
	public EmptyBoxComponent(String aName)
	{
		super(aName);
	}


	@Override
	public void paintComponent(BoxComponentPane aEditor, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected)
	{
		super.paintComponent(aEditor, aGraphics, aWidth, aHeight, aSelected);

//		aGraphics.setColor(Color.RED);
//		aGraphics.fillRect(0, 0, aWidth, aHeight);
	}
}
