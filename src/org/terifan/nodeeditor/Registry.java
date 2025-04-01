package org.terifan.nodeeditor;

import java.util.HashMap;
import java.util.function.Function;


public class Registry
{
	private transient HashMap<String, Object> mBindings;


	public Registry()
	{
		mBindings = new HashMap<>();

		bindDefaultValues();
	}


	protected void bindDefaultValues()
	{
		bind(Styles.DefaultIcons.FOLDER, aId -> Styles.loadIcon("folder"));
		bind(Styles.DefaultIcons.RUN, aId -> Styles.loadIcon("run"));
	}


	public Registry bind(String aId, Function<String, Object> aFunction)
	{
		return bind(aId, (Object)aFunction);
	}


	public Registry bind(String aId, Object aFunction)
	{
		if (mBindings.containsKey(aId))
		{
			throw new IllegalArgumentException("ID already bound: " + aId);
		}
		mBindings.put(aId, aFunction);
		return this;
	}


	public HashMap<String, Object> getBindings()
	{
		return mBindings;
	}


	public <T> T get(String aId, Class<T> aType)
	{
		Object v = mBindings.get(aId);

		if (v == null)
		{
			throw new IllegalArgumentException("No bound value with ID: " + aId + ", expected instanceof: " + aType);
		}
		if (v instanceof Function w)
		{
			v = w.apply(aId);
		}
		if (!aType.isAssignableFrom(v.getClass()))
		{
			throw new IllegalArgumentException("Bound value is not of correct type. ID: " + aId + ", expected instanceof: " + aType + ", found: " + v.getClass());
		}

		return (T)v;
	}


	public <T> T get(String aId, T aDefault, Class<T> aType)
	{
		Object v = mBindings.get(aId);

		if (v instanceof Function w)
		{
			v = w.apply(aId);
		}
		if (v == null || !aType.isAssignableFrom(v.getClass()))
		{
			return aDefault;
		}

		return (T)v;
	}
}
