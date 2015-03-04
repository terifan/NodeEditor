package org.terifan.ui.relationeditor;

import org.terifan.ui.NullLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.UUID;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.terifan.ui.Utilities;


public class RelationEditor extends JPanel
{
	private final static long serialVersionUID = 1L;

	private ArrayList<Connection> mRelationships;
	private JComponent mSelectedComponent;


	public RelationEditor()
	{
		setLayout(new NullLayout());
		setBackground(new Color(68,68,68));

		mRelationships = new ArrayList<>();
	}


	public void addRelationship(Connection aRelationship)
	{
		mRelationships.add(aRelationship);
	}


	public void addRelationship(RelationItem aFrom, RelationItem aTo)
	{
		mRelationships.add(new DefaultConnection(aFrom, aTo));
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		Graphics2D g = (Graphics2D)aGraphics;

		Utilities.enableTextAntialiasing(g);
		Utilities.enableAntialiasing(g);

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		for (Connection relationship : mRelationships)
		{
			relationship.draw(g, this);
		}
	}


	public RelationBox getRelationBox(RelationItem aItem)
	{
		for (int i = 0; i < getComponentCount(); i++)
		{
			RelationBox box = (RelationBox)getComponent(i);

			for (int j = 0; j < box.getRelationItemCount(); j++)
			{
				if (box.getRelationItem(j) == aItem)
				{
					return box;
				}
			}
		}

		return null;
	}


	protected void setSelectedComponent(JComponent aElement)
	{
		mSelectedComponent = aElement;
	}


	public JComponent getSelectedComponent()
	{
		return mSelectedComponent;
	}


	public void arrangeBoxes()
	{
		int x = 10;
		int y = 10;

		for (int i = 0; i < getComponentCount(); i++)
		{
			Component box  = getComponent(i);

			Dimension d = box.getPreferredSize();

			d.width = Math.max(d.width, 80);

			box.setBounds(new Rectangle(x, y, d.width, d.height));

			x += d.width + 50;
			y += 20;
		}
	}


	public RelationItem findRelationItem(UUID aIdentity)
	{
		for (int i = 0; i < getComponentCount(); i++)
		{
			Component component = getComponent(i);

			if (RelationBox.class.isAssignableFrom(component.getClass()))
			{
				RelationBox box = (RelationBox)component;

				for (int j = 0; j < box.getRelationItemCount(); j++)
				{
					RelationItem item = box.getRelationItem(j);

					if (item.getIdentity().equals(aIdentity))
					{
						return item;
					}
				}
			}
		}

		return null;
	}
}
