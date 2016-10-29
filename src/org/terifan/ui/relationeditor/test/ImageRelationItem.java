package org.terifan.ui.relationeditor.test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.terifan.ui.relationeditor.DefaultRelationItem;
import org.terifan.ui.relationeditor.RelationBox;
import org.terifan.ui.relationeditor.RelationEditorPane;
import org.terifan.ui.relationeditor.Styles;
import org.terifan.ui.relationeditor.StackedRelationItem;


public class ImageRelationItem extends StackedRelationItem
{
	public ImageRelationItem(String aText, int aHeight, Anchors aAnchors)
	{
		super(aText, aHeight, aAnchors);
	}


	public ImageRelationItem(String aText, int aHeight, Anchors aAnchors, double aWeight)
	{
		super(aText, aHeight, aAnchors, aWeight);
	}

	
	private JComponent mComponent = new JPanel()
	{
		@Override
		protected void paintComponent(Graphics aGraphics)
		{
			aGraphics.setColor(Styles.BOX_BACKGROUND_COLOR);
			aGraphics.fillRect(0, 0, getWidth(), getHeight());
			aGraphics.setColor(Color.red);
			aGraphics.fillRect(0, 0, 200, 200);
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
