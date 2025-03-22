package org.terifan.nodeeditor;

import java.util.HashMap;



public class Context
{
	private final NodeEditorPane mEditor;
	private final Node mNode;
	private final Property mProperty;

	public final HashMap<String, Object> params;

	public Object result;


	Context(NodeEditorPane aEditor, Node aNode, Property aProperty)
	{
		mEditor = aEditor;
		mNode = aNode;
		mProperty = aProperty;

		params = new HashMap<>();
	}


	public NodeEditorPane getEditor()
	{
		return mEditor;
	}


	public Node getNode()
	{
		return mNode;
	}


	public Property getProperty()
	{
		return mProperty;
	}


	public NodeModel getModel()
	{
		return mEditor.getModel();
	}
}
