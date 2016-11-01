package org.terifan.nodeeditor;

import java.awt.Graphics;


public interface ConnectionRenderer
{
	double distance(Connection aConnection, Anchor aFromAnchor, Anchor aToAnchor, int aX, int aY);

	void render(Graphics aGraphics, Connection aConnection, Anchor aFromAnchor, Anchor aToAnchor, boolean aSelected);
}
