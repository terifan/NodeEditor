package org.terifan.boxcomponentpane;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;


public class BoxComponentModel<T extends BoxComponent> implements Serializable
{
	private final static long serialVersionUID = 1L;
	protected final ArrayList<T> mComponents;


	public BoxComponentModel()
	{
		mComponents = new ArrayList<>();
	}


	public int size()
	{
		return mComponents.size();
	}


	public T getComponent(int aIndex)
	{
		return mComponents.get(aIndex);
	}


	public BoxComponentModel<T> addComponent(T aComponent)
	{
		mComponents.add(aComponent);
		return this;
	}


	public ArrayList<T> getComponents()
	{
		return mComponents;
	}


	public void moveTop(T aComponent)
	{
		mComponents.remove(aComponent);
		mComponents.addFirst(aComponent);
	}


	public T getComponentAt(Point aPoint)
	{
		for (T c : mComponents.reversed())
		{
			Rectangle b = c.getBounds();

			if (b.contains(aPoint))
			{
				return c;
			}
		}

		return null;
	}
}
