package org.terifan.nodeeditor;

import java.awt.Graphics2D;
import java.awt.Rectangle;


public interface Renderable
{
	Rectangle getBounds();

	void layout(Graphics2D aGraphics);

	void paintComponent(NodeEditor aEditor, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected);
}
