package org.terifan.boxcomponentpane;

import java.awt.Graphics2D;
import java.awt.Rectangle;


public interface Renderable<T extends BoxComponent>
{
	Rectangle getBounds();


	void paintComponent(BoxComponentPane<T> aEditor, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected);
}
