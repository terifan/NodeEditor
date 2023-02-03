package org.terifan.boxcomponentpane;

import java.io.Serializable;
import java.util.ArrayList;


public class BoxComponentModel<T extends BoxComponent> implements Serializable
{
	private final static long serialVersionUID = 1L;
	private ArrayList<T> mBoxComponents;


	public BoxComponentModel()
	{
		mBoxComponents = new ArrayList<>();
	}


	public int size()
	{
		return mBoxComponents.size();
	}


	public T get(int aIndex)
	{
		return mBoxComponents.get(aIndex);
	}


	public void add(T aComponent)
	{
		mBoxComponents.add(aComponent);
	}


	public ArrayList<T> getNodes()
	{
		return mBoxComponents;
	}
}
