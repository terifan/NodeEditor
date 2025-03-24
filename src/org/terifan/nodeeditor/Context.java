package org.terifan.nodeeditor;

import org.terifan.nodeeditor.widgets.ValueProperty;



public class Context
{
	private final NodeEditorPane mEditor;


	public Context(NodeEditorPane aEditor)
	{
		mEditor = aEditor;
	}


	public NodeEditorPane getEditor()
	{
		return mEditor;
	}


	public Object invoke(Property aProperty, String aProducer)
	{
		return mEditor.getBindings().get(aProducer).apply(this, aProperty);
	}
}
