package org.terifan.nodeeditor;


@FunctionalInterface
public interface NodeFunction
{
	public abstract Object apply(Context aContext, Property aProperty);
}
