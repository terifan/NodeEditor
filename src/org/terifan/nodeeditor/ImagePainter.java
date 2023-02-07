package org.terifan.nodeeditor;

import java.awt.Graphics;
import java.awt.Rectangle;
import org.terifan.nodeeditor.widgets.ImageProperty;


public interface ImagePainter
{
	/**
	 * @return true if the image was painted
	 */
	boolean paintImage(NodeEditorPane aPane, Node aNode, ImageProperty aPropertyItem, Graphics aGraphics, Rectangle aBounds) throws Exception;
}
