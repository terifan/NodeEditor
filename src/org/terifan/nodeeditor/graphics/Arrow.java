package org.terifan.nodeeditor.graphics;

import java.awt.Color;
import java.awt.Graphics2D;


public class Arrow
{
	public static void paintArrow(Graphics2D aGraphics, int aDirection, int aX, int aY, int aW, int aH, Color aShadow, Color aColor)
	{
		aX++;
		aY++;
		aGraphics.setColor(aShadow);
		for (int i = 0; i < 2; i++)
		{
			if (aDirection == 0) // up
			{
				aGraphics.drawPolyline(new int[]
				{
					aX - aW, aX, aX + aW
				}, new int[]
				{
					aY + aH / 2, aY - aH / 2, aY + aH / 2
				}, 3);
			}
			else if (aDirection == 1) // right
			{
				aGraphics.drawPolyline(new int[]
				{
					aX - aW / 2, aX + aW / 2, aX - aW / 2
				}, new int[]
				{
					aY - aH, aY, aY + aH
				}, 3);
			}
			else if (aDirection == 2) // down
			{
				aGraphics.drawPolyline(new int[]
				{
					aX - aW, aX, aX + aW
				}, new int[]
				{
					aY - aH / 2, aY + aH / 2, aY - aH / 2
				}, 3);
			}
			else if (aDirection == 3) // left
			{
				aGraphics.drawPolyline(new int[]
				{
					aX + aW / 2, aX - aW / 2, aX + aW / 2
				}, new int[]
				{
					aY - aH, aY, aY + aH
				}, 3);
			}
			aX--;
			aY--;
			aGraphics.setColor(aColor);
		}
	}
}
