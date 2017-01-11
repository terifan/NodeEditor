package org.terifan.nodeeditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class Popup implements Renderable
{
	protected final Rectangle mBounds;
	protected final NodeItem mOwner;


	public Popup(NodeItem aOwner, Rectangle aBounds)
	{
		mBounds = aBounds;
		mOwner = aOwner;

		Rectangle pb = mOwner.getNodeBox().getBounds();
		mBounds.translate(pb.x, pb.y);

		mBounds.y += aOwner.mBounds.height;

		mBounds.height = 35+20+20+20+2;

//		mBounds.y -= 100;
	}


	@Override
	public Rectangle getBounds()
	{
		return mBounds;
	}


	@Override
	public void layout(Graphics2D aGraphics)
	{
	}


	@Override
	public void paintComponent(Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected)
	{
		aGraphics.setColor(new Color(0,0,0,160));
		aGraphics.fillRoundRect(0, 0, aWidth, aHeight, 16, 16);

		aGraphics.setColor(new Color(32,32,32,192));
		aGraphics.fillRoundRect(1, 1, aWidth-2, aHeight-2, 16, 16);

		aGraphics.setColor(new Color(55,55,55));
		aGraphics.drawLine(0, 30, aWidth, 30);

		new TextBox("Operation").setMargins(0, 10, 0, 10).setAnchor(Anchor.WEST).setBounds(0, 0, aWidth, 30).setForeground(new Color(208,208,208)).render(aGraphics);

		TextBox text = new TextBox().setAnchor(Anchor.WEST).setBounds(0, 0, aWidth, 20).setForeground(new Color(255,255,255));
		text.setMargins(35, 10, 0, 10).setText("Absolute").render(aGraphics);
		text.setMargins(35+20, 10, 0, 10).setText("Modulo").render(aGraphics);
		text.setMargins(35+20+20, 10, 0, 10).setText("Greater Than").render(aGraphics);
	}


	protected void mouseMoved(MouseEvent aEvent)
	{
	}


	protected void mousePressed(MouseEvent aEvent)
	{
	}


	protected void mouseReleased(MouseEvent aEvent)
	{
		mOwner.mouseReleased(mOwner.getNodeBox().getEditorPane(), aEvent.getPoint());
	}


	protected void mouseWheelMoved(MouseWheelEvent aEvent)
	{
	}
}
