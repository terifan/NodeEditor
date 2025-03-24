package org.terifan.boxcomponentpane;

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


	public T getNode(int aIndex)
	{
		return mComponents.get(aIndex);
	}


	public BoxComponentModel<T> addNode(T aComponent)
	{
		mComponents.add(aComponent);
		return this;
	}


	public ArrayList<T> getComponents()
	{
		return mComponents;
	}
}
