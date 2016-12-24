package org.terifan.nodeeditor;

import java.awt.Color;


public class Styles
{
	private final static int a = 220;
	private final static int b = 254; // alpha must be less than 255 to force Java to blend alpha correctly!

//	public final static float CONNECTOR_STROKE_WITH_INNER = 1.4f;
//	public final static float CONNECTOR_STROKE_WIDTH_OUTER = 3.0f;
	public final static float CONNECTOR_STROKE_WIDTH_INNER = 1.75f;
	public final static float CONNECTOR_STROKE_WIDTH_OUTER = 4.0f;
	public final static Color CONNECTOR_COLOR_OUTER = new Color(30, 30, 30);
	public final static Color CONNECTOR_COLOR_INNER_FOCUSED = new Color(255, 255, 255);
	public final static Color CONNECTOR_COLOR_INNER = new Color(128,128,128);
	public final static Color CONNECTOR_COLOR_INNER_SELECTED = new Color(192, 0, 0);
	public final static Color CONNECTOR_COLOR_OUTER_SELECTED = new Color(128, 0, 0);
	public final static Color CONNECTOR_COLOR_INNER_DRAGGED = new Color(240, 160, 62);
	
	public final static int TITLE_HEIGHT = 20;
	public final static int TITLE_HEIGHT_PADDED = TITLE_HEIGHT + 5;
	public final static int BORDE_RADIUS = 18;
	public final static int BUTTON_WIDTH = 16;
	public final static Color BOX_FOREGROUND_COLOR = new Color(0, 0, 0, b);
	public final static Color BOX_FOREGROUND_ARMED_COLOR = new Color(255,255,255);
	public final static Color BOX_FOREGROUND_SELECTED_COLOR = new Color(0, 0, 0, b);
	public final static Color BOX_BACKGROUND_COLOR = new Color(90, 90, 90, a);
	public final static Color BOX_BACKGROUND_SELECTED_COLOR = new Color(90, 90, 90, a);
	public final static Color BOX_BORDER_COLOR = new Color(37, 37, 37, a);
	public final static Color BOX_BORDER_SELECTED_COLOR = new Color(208, 145, 66);
	public final static Color BOX_BORDER_TITLE_COLOR = new Color(77, 77, 77, a);
	public final static Color BOX_TITLE_TEXT_SHADOW_COLOR = new Color(90, 90, 90, a);
	public final static Color PANE_BACKGROUND_COLOR = new Color(57, 57, 57);
	public final static Color PANE_GRID_COLOR_1 = new Color(47, 47, 47);
	public final static Color PANE_GRID_COLOR_2 = new Color(41, 41, 41);
	public final static Color PANE_GRID_COLOR_3 = new Color(30, 30, 30);
	public final static Color PANE_SELECTION_RECTANGLE_LINE = new Color(255, 255, 255);
	public final static Color PANE_SELECTION_RECTANGLE_BACKGROUND = new Color(255, 255, 255, 15);
	public final static int MIN_WIDTH = 80;

	public final static Color[][][] SLIDER_COLORS = 
	{
		// normal
		{
			new Color[]{new Color(159,159,159), new Color(179,179,179)},
			new Color[]{new Color(126, 126, 126), new Color(107,107,107)},
			new Color[]{BOX_FOREGROUND_COLOR, BOX_FOREGROUND_COLOR}
		},
		// hover
		{
			new Color[]{new Color(174,174,174), new Color(194,194,194)},
			new Color[]{new Color(127,127,127), new Color(147,147,147)},
			new Color[]{BOX_FOREGROUND_COLOR, BOX_FOREGROUND_COLOR}
		},
		// armed
		{
			new Color[]{new Color(132,132,132), new Color(152,152,152)},
			new Color[]{new Color(107,107,107), new Color(127,127,127)},
			new Color[]{BOX_FOREGROUND_ARMED_COLOR, BOX_FOREGROUND_ARMED_COLOR}
		}
	};
}
