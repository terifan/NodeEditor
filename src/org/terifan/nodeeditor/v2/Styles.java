package org.terifan.nodeeditor.v2;

import java.awt.Color;


public class Styles
{
	private final static int c = 230;
	private final static int a = 150;
	private final static int b = 254; // alpha must be less than 255 to force Java to blend alpha correctly!
	
	public final static int TITLE_HEIGHT = 22;
	public final static Color BOX_FOREGROUND_COLOR = new Color(0, 0, 0, b);
	public final static Color BOX_FOREGROUND_SELECTED_COLOR = new Color(0, 0, 0, b);
	public final static Color BOX_BACKGROUND_COLOR = new Color(90, 90, 90, a);
	public final static Color BOX_BACKGROUND_SELECTED_COLOR = new Color(90, 90, 90, a);
	public final static Color BOX_BORDER_COLOR = new Color(37, 37, 37, a);
	public final static Color BOX_BORDER_SELECTED_COLOR = new Color(208, 145, 66);
	public final static Color BOX_BORDER_TITLE_COLOR = new Color(77, 77, 77, c);
	public final static Color BOX_TITLE_TEXT_SHADOW_COLOR = new Color(90, 90, 90, a);
	public final static Color CONNECTOR_COLOR_DARK = new Color(0, 0, 0);
	public final static Color CONNECTOR_COLOR_BRIGHT = new Color(255, 255, 255);
	public final static Color CONNECTOR_COLOR_BRIGHT_SELECTED = new Color(192, 0, 0);
	public final static Color CONNECTOR_COLOR_DARK_SELECTED = new Color(128, 0, 0);
	public final static Color PANE_BACKGROUND_COLOR = new Color(58, 58, 58);
	public final static Color PANE_GRID_COLOR_1 = new Color(47, 47, 47);
	public final static Color PANE_GRID_COLOR_2 = new Color(41, 41, 41);
	public final static Color PANE_GRID_COLOR_3 = new Color(0, 0, 0);
	public final static Color PANE_SELECTION_RECTANGLE_LINE = new Color(255, 255, 255);
	public final static Color PANE_SELECTION_RECTANGLE_BACKGROUND = new Color(255, 255, 255, 15);
	public final static int MIN_WIDTH = 80;
}
