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


	public <T extends Property> T find(String aId)
	{
		return mEditor.getModel().getProperty(aId);
	}


	public <T> T execute(String aId)
	{
		Property prop = mProperty.getNode().getProperty(aId);
		return (T)prop.execute(new Context(mEditor, prop));
	}


	public <T extends Property> T property(String aId)
	{
		return mProperty.getNode().getProperty(aId);
	}
}
