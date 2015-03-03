package org.terifan.ui.relationeditor;

import org.terifan.ui.DragAndDrop;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.UUID;
import javax.swing.SwingUtilities;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import org.terifan.util.log.Log;


public class DefaultRelationItem extends RelationItem
{
	private String mText;
	private TextBox mTextBox;
	private UUID mIdentity;


	public DefaultRelationItem(String aText)
	{
		mIdentity = UUID.randomUUID();
		mText = aText;
		mTextBox = new TextBox(mText).setAnchor(Anchor.WEST).setBackground(new Color(48,48,48)).setForeground(Color.WHITE).setMargins(2, 4, 2, 4);

		new DragAndDrop(this)
		{
			@Override
			public Object drag(Point aDragOrigin)
			{
				return mIdentity;
			}

			@Override
			public boolean canDrop(DropEvent aDropEvent)
			{
				return true;
			}

			@Override
			public void drop(DropEvent aDropEvent)
			{
				RelationEditor editor = (RelationEditor)SwingUtilities.getAncestorOfClass(RelationEditor.class, DefaultRelationItem.this);
				RelationItem relatedItem = editor.findRelationItem((UUID)aDropEvent.getTransferData());
				if (relatedItem != null)
				{
					editor.addRelationship(relatedItem, DefaultRelationItem.this);
					editor.repaint();
				}
			}
		};
	}


	@Override
	public UUID getIdentity()
	{
		return mIdentity;
	}


	@Override
	public Dimension getPreferredSize()
	{
		return mTextBox.measure().getSize();
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		mTextBox.setBounds(0, 0, getWidth(), getHeight()).render(aGraphics);
	}
}
