package org.terifan.nodeeditor;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;


public class AutoLayout
{
	private static int VS = 0;
	private static int HS = 0;
	private static Dimension IP = new Dimension(20, 6);


	public void layout(NodeModel aModel, Node aRoot)
	{
		aRoot.computeBounds();

		Box box = computeSize(aModel, aRoot, aRoot.getChildNodes());
		box.layout(0, 0, 0);
	}


	private Box computeSize(NodeModel aModel, Node aNode, ArrayList<Node> aChildren)
	{
		Box parentBox = new Box(aNode);

		for (Node child : aChildren)
		{
			child.computeBounds();

			ArrayList<Node> grandChildren = child.getChildNodes();
			Box childBox;

			if (!grandChildren.isEmpty())
			{
				childBox = computeSize(aModel, child, grandChildren);

				childBox.mBounds.width = Math.max(child.mBounds.width, childBox.mBounds.width);
				childBox.mBounds.height = Math.max(child.mBounds.height, childBox.mBounds.height);
			}
			else
			{
				childBox = new Box(child);
				childBox.mBounds.width = child.mBounds.width;
				childBox.mBounds.height = child.mBounds.height;
			}

			parentBox.mChildren.add(childBox);
			parentBox.mBounds.width = Math.max(parentBox.mBounds.width, childBox.mBounds.width);
			parentBox.mBounds.height += childBox.mBounds.height;
		}

		return parentBox;
	}


	private static class Box
	{
		private Node mNode;
		public ArrayList<Box> mChildren = new ArrayList<>();
		public Rectangle mBounds = new Rectangle();


		public Box(Node aNode)
		{
			mNode = aNode;
		}


		void layout(int aStartX, int aStartY, int aLevel)
		{
			int childrenHeight = 0;
			for (Box box : mChildren)
			{
				childrenHeight += box.mBounds.height;
			}

			int parentHeight = mNode.mBounds.height;

			int x = aStartX + mBounds.width;
			int y = aStartY + (childrenHeight < parentHeight ? Math.min(parentHeight - childrenHeight, (parentHeight - childrenHeight) / 2 + 8) : 0);

			for (Box box : mChildren)
			{
				box.layout(x, y, aLevel + 1);

				y += box.mBounds.height;
			}

			mNode.mBounds.x = aStartX + 100 * aLevel;
			mNode.mBounds.y = aStartY + (childrenHeight > parentHeight ? Math.max(0, (childrenHeight - parentHeight) / 2 - 8) : 0);
		}
	}
}
