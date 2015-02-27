package org.terifan.ui.relationeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import org.terifan.util.log.Log;


public class DefaultRelationItem extends RelationItem
{
	private String mText;
//	private RelationBox mBox;
	private TextBox mTextBox;


	public DefaultRelationItem(String aText)
	{
		mText = aText;
		mTextBox = new TextBox(mText).setAnchor(Anchor.WEST).setBackground(new Color(48,48,48)).setForeground(Color.WHITE).setMargins(2, 4, 2, 4);
	}


//	@Override
//	public RelationBox getRelationBox()
//	{
//		return mBox;
//	}
//
//
//	@Override
//	public void setRelationBox(RelationBox aBox)
//	{
//		mBox = aBox;
//	}


	@Override
	public Dimension getPreferredSize()
	{
		Dimension size = mTextBox.measure().getSize();

		return size;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		mTextBox.setBounds(0,0,getWidth(),getHeight()).render(aGraphics);
	}
}
