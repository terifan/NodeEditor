package org.terifan.nodeeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Styles
{
	private final static int a = 230;
	private final static int b = 254; // alpha must be less than 255 to force Java to blend alpha correctly!

	public static float CONNECTOR_STROKE_WIDTH_INNER = 1.75f;
	public static float CONNECTOR_STROKE_WIDTH_OUTER = 4.0f;
	public static Color CONNECTOR_COLOR_OUTER = new Color(30, 30, 30);
	public static Color CONNECTOR_COLOR_INNER_FOCUSED = new Color(255, 255, 255);
	public static Color CONNECTOR_COLOR_INNER = new Color(128, 128, 128);
	public static Color CONNECTOR_COLOR_INNER_SELECTED = new Color(192, 0, 0);
	public static Color CONNECTOR_COLOR_OUTER_SELECTED = new Color(128, 0, 0);
	public static Color CONNECTOR_COLOR_INNER_DRAGGED = new Color(240, 160, 62);

	public static int TITLE_HEIGHT = 21;
	public static int MIN_WIDTH = 60;
	public static int MIN_HEIGHT = 21 + 3 + 2 * 4;
	public static int BORDE_RADIUS = 9;

	public static int COLLAPSE_BUTTON_WIDTH = 16;

	public static Color BOX_FOREGROUND_COLOR = new Color(255, 255, 255, b);
	public static Color BOX_FOREGROUND_SHADOW_COLOR = new Color(0, 0, 0, 32);
	public static Color BOX_FOREGROUND_ARMED_COLOR = new Color(30, 30, 30);
	public static Color BOX_FOREGROUND_SELECTED_COLOR = new Color(255, 255, 255, b);
	public static Color BOX_BACKGROUND_COLOR = new Color(48, 48, 48, a);
	public static Color FIELD_BACKGROUND_COLOR = new Color(84, 84, 84);
	public static Color FIELD_BACKGROUND_SELECTED_COLOR = new Color(71, 114, 179);
	public static Color BOX_BORDER_COLOR = new Color(10, 10, 10, a);
	public static Color BOX_BORDER_SELECTED_COLOR = new Color(255, 255, 255, b);
	public static Color BOX_BORDER_TITLE_COLOR = new Color(67, 67, 67, a);
	public static Color BOX_TITLE_TEXT_SHADOW_COLOR = new Color(50, 50, 50, a);
	public static Color PANE_BACKGROUND_COLOR = new Color(29, 29, 29);
	public static Color PANE_GRID_COLOR_1 = new Color(27, 27, 27);
	public static Color PANE_GRID_COLOR_2 = new Color(26, 26, 26);
	public static Color PANE_GRID_COLOR_3 = new Color(25, 25, 25);
	public static Color PANE_SELECTION_RECTANGLE_LINE = new Color(255, 255, 255);
	public static Color PANE_SELECTION_RECTANGLE_BACKGROUND = new Color(255, 255, 255, 15);

	public static Font BOX_FONT = new Font("Segoe UI", Font.PLAIN, 12);
	public static Font BOX_ITEM_FONT = new Font("Segoe UI", Font.PLAIN, 12);
	public static Font SLIDER_FONT = new Font("Segoe UI", Font.PLAIN, 11);
	public static Color SLIDER_BORDER_COLOR = new Color(48, 48, 48);
	public static Color SLIDER_ARROW_COLOR = new Color(118, 118, 118);
	public static Color COMBOBOX_ARROW_COLOR = new Color(190, 190, 190);

	public static final int FIELD_CORNER = 8;

	public static BufferedImage BOX_SHADOW;
	public static int BOX_SHADOW_STRENGTH = 20;
	public static int BOX_SHADOW_SIZE = 10;

	public static Color CHECKERS_BRIGHT = new Color(220, 220, 220);
	public static Color CHECKERS_DARK = new Color(200, 200, 200);


	static
	{
		try
		{
			BOX_SHADOW = ImageIO.read(Styles.class.getResource("background.png"));
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
	}

	public static Color[][][] SLIDER_COLORS =
	{
		// normal
		{
			new Color[]
			{
				FIELD_BACKGROUND_COLOR, FIELD_BACKGROUND_COLOR
			},
			new Color[]
			{
				FIELD_BACKGROUND_SELECTED_COLOR, FIELD_BACKGROUND_SELECTED_COLOR
			},
			new Color[]
			{
				BOX_FOREGROUND_COLOR, BOX_FOREGROUND_COLOR
			}
		},
		// hover
		{
			new Color[]
			{
				new Color(101, 101, 101), new Color(101, 101, 101)
			},
			new Color[]
			{
				FIELD_BACKGROUND_SELECTED_COLOR, FIELD_BACKGROUND_SELECTED_COLOR
			},
			new Color[]
			{
				BOX_FOREGROUND_COLOR, BOX_FOREGROUND_COLOR
			}
		},
		// armed
		{
			new Color[]
			{
				new Color(34, 34, 34), new Color(34, 34, 34)
			},
			new Color[]
			{
				FIELD_BACKGROUND_SELECTED_COLOR, FIELD_BACKGROUND_SELECTED_COLOR
			},
			new Color[]
			{
				BOX_FOREGROUND_COLOR, BOX_FOREGROUND_COLOR
			}
		}
	};

	public static Color[][] CHECKBOX_COLORS =
	{
		// normal
		new Color[]
		{
			FIELD_BACKGROUND_COLOR, FIELD_BACKGROUND_COLOR
		},
		// selected
		new Color[]
		{
			FIELD_BACKGROUND_SELECTED_COLOR, FIELD_BACKGROUND_SELECTED_COLOR
		}
	};

	public static Color[][] COMBOBOX_COLORS =
	{
		// normal
		new Color[]
		{
			new Color(40, 40, 40), new Color(40, 40, 40)
		},
		// selected
		new Color[]
		{
			FIELD_BACKGROUND_SELECTED_COLOR, FIELD_BACKGROUND_SELECTED_COLOR
		},
		new Color[]
		{
			new Color(61, 61, 61)
		}
	};

	public static Color[][] BUTTON_COLORS =
	{
		new Color[]
		{
			new Color(84, 84, 84), new Color(84, 84, 84)
		},
		new Color[]
		{
			new Color(181, 181, 181), new Color(153, 153, 153)
		},
		new Color[]
		{
			new Color(85, 85, 85), new Color(113, 113, 113)
		}
	};

	public static int POPUP_FOOTER_HEIGHT = 5;
	public static int POPUP_HEADER_HEIGHT = 35;
	public static int POPUP_DEFAULT_OPTION_HEIGHT = 20;
	public static Color POPUP_FOREGROUND = new Color(255, 255, 255);
	public static Color POPUP_HEADER_FOREGROUND = new Color(128, 128, 128);
	public static Color POPUP_SELECTION_BACKGROUND = new Color(71,114,179);
	public static Color POPUP_HEADER_LINE = new Color(55, 55, 55);
	public static Color POPUP_BACKGROUND = new Color(16, 16, 16, 220);


	public static interface DefaultIcons
	{
		public String FOLDER = "folder";
		public String RUN = "run";
	}


	public static interface DefaultConnectorColors
	{
		public Color PURPLE = new Color(0x6363C7);
		public Color GRAY = new Color(0xA1A1A1);
		public Color YELLOW = new Color(0xC7C729);
		public Color GREEN = new Color(0x63C763);
	}


	public static interface DefaultNodeColors
	{
		public Color GRAY = new Color(67, 67, 67, a);
		public Color RED = new Color(0x83314A);
		public Color GREEN = new Color(0x2B652B);
		public Color YELLOW = new Color(0x6E6E1D);
		public Color DARKRED = new Color(0x3C1D26);
		public Color BLUE = new Color(0x246283);
		public Color PURPLE = new Color(0x3C3C83);
		public Color BROWN = new Color(0x79461D);
		public Color DARKCYAN = new Color(0x203C3C);
	}

	public static BasicStroke SELECTION_RECTANGLE_STROKE = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]
	{
		3
	}, 0);


	public static BufferedImage loadIcon(String aName)
	{
		try
		{
			return ImageIO.read(Styles.class.getResource("icons/" + aName + ".png"));
		}
		catch (IOException e)
		{
			e.printStackTrace(System.err);
			return null;
		}
	}
}
