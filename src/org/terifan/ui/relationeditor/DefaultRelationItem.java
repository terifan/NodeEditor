package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Component;
import org.terifan.ui.DragAndDrop;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class DefaultRelationItem extends JLabel implements RelationItem
{
	private final static Color BACKGROUND_COLOR = new Color(48,48,48);
	private final static Color BACKGROUND_SELECTED_COLOR = new Color(128, 0, 0);

	private UUID mIdentity;


	public DefaultRelationItem(String aText)
	{
		super(aText);

		mIdentity = UUID.randomUUID();

		super.setFocusable(true);
		super.setBackground(BACKGROUND_COLOR);
		super.setForeground(Color.WHITE);
		super.setOpaque(true);
		super.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		super.addMouseListener(new RelationItemMouseListener());
		super.addKeyListener(new RelationItemKeyListener());

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
				RelationEditorPane editor = RelationEditorPane.findEditor(mComponent);
				RelationItem relatedItem = editor.findRelationItem(aDropEvent.getTransferData(UUID.class));
				if (relatedItem != null)
				{
					editor.addConnection(relatedItem, DefaultRelationItem.this);
					editor.repaint();
				}
			}
		};
	}


	@Override
	public JComponent getComponent()
	{
		return this;
	}


	@Override
	public Component getEditorComponent()
	{
		final JTextField textField = new JTextField(getText());

		textField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent aEvent)
			{
				if (aEvent.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					RelationBox ancestor = (RelationBox)SwingUtilities.getAncestorOfClass(RelationBox.class, textField);
					ancestor.cancelEditItem();
				}
				if (aEvent.getKeyCode() == KeyEvent.VK_ENTER)
				{
					RelationBox ancestor = (RelationBox)SwingUtilities.getAncestorOfClass(RelationBox.class, textField);
					ancestor.finishEditItem();
				}
			}
		});

		textField.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent aEvent)
			{
				textField.requestFocusInWindow();
				textField.setCaretPosition(textField.getText().length());
			}
		});

		return textField;
	}


	@Override
	public void updateValue(Component aEditorComponent)
	{
		setText(((JTextField)aEditorComponent).getText());
	}


	@Override
	public UUID getIdentity()
	{
		return mIdentity;
	}


	@Override
	public void onSelectionChanged(RelationEditorPane aRelationEditor, RelationBox aRelationBox, boolean aSelected)
	{
		if (aSelected)
		{
			setBackground(BACKGROUND_SELECTED_COLOR);
		}
		else
		{
			setBackground(BACKGROUND_COLOR);
		}
	}
}
