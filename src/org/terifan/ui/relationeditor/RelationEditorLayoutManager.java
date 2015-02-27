package org.terifan.ui.relationeditor;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.terifan.util.log.Log;


public class RelationEditorLayoutManager implements LayoutManager2
{
	private RelationEditor mRelationEditor;
	private ArrayList<RelationBox> mBoxes;


	public RelationEditorLayoutManager(RelationEditor aRelationEditor)
	{
		mBoxes = new ArrayList<>();

		mRelationEditor = aRelationEditor;
	}


	@Override
	public void addLayoutComponent(Component aComp, Object aConstraints)
	{
		mBoxes.add((RelationBox)aComp);
	}


	@Override
	public void addLayoutComponent(String aName, Component aComp)
	{
		mBoxes.add((RelationBox)aComp);
	}


	@Override
	public void removeLayoutComponent(Component aComp)
	{
		mBoxes.remove(aComp);
	}


	@Override
	public float getLayoutAlignmentX(Container aTarget)
	{
		return 0.0f;
	}


	@Override
	public float getLayoutAlignmentY(Container aTarget)
	{
		return 0.0f;
	}


	@Override
	public Dimension maximumLayoutSize(Container aTarget)
	{
		return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
	}


	@Override
	public Dimension minimumLayoutSize(Container aParent)
	{
		return new Dimension(0,0);
	}


	@Override
	public Dimension preferredLayoutSize(Container aParent)
	{
		layoutContainer(aParent);

		int w = 0;
		int h = 0;

		for (RelationBox box : mBoxes)
		{
			Rectangle b = box.getBounds();
			w = Math.max(b.x + b.width, w);
			h = Math.max(h + b.height, h);
		}

		w += 10;
		h += 10;

		Log.out.println(w+" "+h+" "+mBoxes.size());

		return new Dimension(w, h);
	}


	@Override
	public void invalidateLayout(Container aTarget)
	{
	}


	@Override
	public void layoutContainer(Container aParent)
	{
		int x = 10;
		int y = 10;

		for (RelationBox box : mBoxes)
		{
			Dimension d = box.getPreferredSize();

			d.width = Math.max(d.width, 80);

			box.setBounds(x, y, d.width, d.height);

			x += d.width + 50;
			y += 20;
		}
	}
}
