package org.terifan.nodeeditor;

import java.awt.Graphics2D;
import java.awt.Rectangle;


public interface Renderable
{
	Rectangle getBounds();


	void layout();


	void paintComponent(NodeEditorPane aEditor, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected);
}
