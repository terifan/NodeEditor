package org.terifan.nodeeditor;

import java.awt.Dimension;


public abstract class AbstractPropertyItem<T extends AbstractPropertyItem> extends PropertyItem
{
	private final static long serialVersionUID = 1L;


	protected AbstractPropertyItem()
	{
		prepare();
	}


	public AbstractPropertyItem(String aText)
	{
		this(aText, 0, 0);

		mPreferredSize.setSize(mTextBox.measure().getSize());
	}


	public AbstractPropertyItem(String aText, int aWidth, int aHeight)
	{
		super(aText);

		mPreferredSize.setSize(aWidth, aHeight);
		mUserSetSize = true;

		prepare();
	}


	protected void prepare()
	{
		mTextBox.setFont(Styles.BOX_ITEM_FONT).setForeground(Styles.BOX_FOREGROUND_COLOR);
	}


	@Override
	protected T setText(String aText)
	{
		return (T)super.setText(aText);
	}


	@Override
	public T setIdentity(String aIdentity)
	{
		return (T)super.setIdentity(aIdentity);
	}


	@Override
	public T add(Connector aConnector)
	{
		return (T)super.add(aConnector);
	}


	@Override
	public T putProperty(String aName, String aValue)
	{
		return (T)super.putProperty(aName, aValue);
	}


	@Override
	public Dimension measure()
	{
		if (!mUserSetSize && mTextBox.isLayoutRequired())
		{
			mPreferredSize.setSize(mTextBox.measure().getSize());
		}

		return super.measure();
	}
}
