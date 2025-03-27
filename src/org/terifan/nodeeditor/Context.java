package org.terifan.nodeeditor;


public class Context
{
	private final NodeEditorPane mEditor;
	private final Property mProperty;


	public Context(NodeEditorPane aEditor, Property aProperty)
	{
		mEditor = aEditor;
		mProperty = aProperty;
	}


	public NodeEditorPane getEditor()
	{
		return mEditor;
	}


	public <T extends Property> T getProperty()
	{
		return (T)mProperty;
	}


	public Node getNode()
	{
		return mProperty.getNode();
	}


	public <T extends Property> T getProperty(String aId)
	{
		T property = mProperty.getNode().getProperty(aId);

		if (property == null)
		{
			property = mEditor.getModel().getProperty(aId);
		}

		return property;
	}
	

	public <T> T value(String aId)
	{
		Property prop = mProperty.getNode().getProperty(aId);
		return (T)prop.execute(new Context(mEditor, prop));
	}


	public <T> T value(String aId, Class<T> aType)
	{
		Property prop = mProperty.getNode().getProperty(aId);
		Object value = prop.execute(new Context(mEditor, prop));
		if (value.getClass().isAssignableFrom(aType))
		{
			return (T)value;
		}
		switch (value)
		{
			case Double v ->
			{
				if (aType == Integer.class)
				{
					return (T)(Integer)v.intValue();
				}
			}
			case Integer v ->
			{
				if (aType == Double.class)
				{
					return (T)(Double)v.doubleValue();
				}
			}
			default ->
			{
				return (T)value;
			}
		}
		return (T)value;
	}
}
