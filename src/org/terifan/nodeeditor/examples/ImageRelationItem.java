package org.terifan.nodeeditor.examples;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.RelationBox;
import org.terifan.nodeeditor.RelationEditorPane;
import org.terifan.nodeeditor.Styles;
import org.terifan.nodeeditor.StackedRelationItem;


public class ImageRelationItem extends StackedRelationItem
{
	public ImageRelationItem(String aText, int aHeight, Direction aDirection)
	{
		super(aText, aHeight, aDirection);
	}


	public ImageRelationItem(String aText, int aHeight, Direction aDirection, double aWeight)
	{
		super(aText, aHeight, aDirection, aWeight);
	}


	private JComponent mComponent = new JPanel()
	{
		@Override
		protected void paintComponent(Graphics aGraphics)
		{
			aGraphics.setColor(Styles.BOX_BACKGROUND_COLOR);
			aGraphics.fillRect(0, 0, getWidth(), getHeight());

			aGraphics.setColor(new Color(200,200,200));
			aGraphics.fillRect(0, 0, 200, 200);
			aGraphics.setColor(new Color(220,220,220));
			for (int y = 0; y < 10; y++)
			{
				for (int x = (y&1); x < 10; x+=2)
				{
					aGraphics.fillRect(x*20, y*20, 20, 20);
				}
			}
		}


		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(200,200);
		}
	};


	@Override
	public Component getComponent()
	{
		return mComponent;
	}


	@Override
	public Component getEditorComponent()
	{
		return null;
	}


	@Override
	public void onSelectionChanged(RelationEditorPane aRelationEditor, RelationBox aRelationBox, boolean aSelected)
	{
	}


	@Override
	public void updateValue(Component aEditorComponent)
	{
	}
}
