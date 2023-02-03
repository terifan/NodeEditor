package org.terifan.nodeeditor;

import java.awt.Graphics;
import java.awt.Rectangle;
import org.terifan.nodeeditor.widgets.ImagePropertyItem;


public interface ImagePainter
{
	/**
	 * @return true if the image was painted
	 */
	boolean paintImage(NodeEditor aEditor, ImagePropertyItem aPropertyItem, Graphics aGraphics, Rectangle aBounds) throws Exception;
}
