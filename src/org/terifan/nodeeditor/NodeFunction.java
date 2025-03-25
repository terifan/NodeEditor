package org.terifan.nodeeditor;


@FunctionalInterface
public interface NodeFunction
{
	public abstract Object invoke(Context aContext, Property aProperty);
}
