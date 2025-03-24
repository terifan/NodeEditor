package org.terifan.nodeeditor.util;

import java.awt.Color;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.Node;
import static org.terifan.nodeeditor.Styles.DefaultColors.GRAY;
import static org.terifan.nodeeditor.Styles.DefaultColors.PURPLE;
import static org.terifan.nodeeditor.Styles.DefaultColors.YELLOW;
import org.terifan.nodeeditor.widgets.ButtonProperty;
import org.terifan.nodeeditor.widgets.CheckBoxProperty;
import org.terifan.nodeeditor.widgets.ColorChooserProperty;
import org.terifan.nodeeditor.widgets.ComboBoxProperty;
import org.terifan.nodeeditor.widgets.ImageProperty;
import org.terifan.nodeeditor.widgets.SliderProperty;
import org.terifan.nodeeditor.widgets.ValueProperty;


public class SimpleNodesFactory
{
	public static Node createSourceTexture()
	{
		return new Node("Texture",
			new ValueProperty("Color").addConnector(OUT, YELLOW),
			new ValueProperty("Alpha").addConnector(OUT, GRAY),
			new ButtonProperty("Open"),
			new ImageProperty("image", 200, 200).setImagePath("Big_pebbles_pxr128_normal.jpg"),
			new ValueProperty("Vector").addConnector(IN, PURPLE)
		).setSize(200, 0);
	}


	public static Node createSourceColorRGB()
	{
		return new Node("Color",
			new ValueProperty("Color").addConnector(OUT, YELLOW),
			new SliderProperty("Red", 0, 1, 0.5).addConnector(IN, GRAY),
			new SliderProperty("Green", 0, 1, 0.5).addConnector(IN, GRAY),
			new SliderProperty("Blue", 0, 1, 0.5).addConnector(IN, GRAY)
		).setSize(200, 0);
	}


	public static Node createSourceColorRGBA()
	{
		return new Node("Color",
			new ValueProperty("Color").addConnector(OUT, YELLOW),
			new SliderProperty("Red", 0, 1, 0).addConnector(IN, GRAY),
			new SliderProperty("Green", 0, 1, 0.5).addConnector(IN, GRAY),
			new SliderProperty("Blue", 0, 1, 0.5).addConnector(IN, GRAY),
			new SliderProperty("Alpha", 0, 1, 1.0).addConnector(IN, GRAY)
		).setSize(200, 0);
	}


	public static Node createSourceAlpha()
	{
		return new Node("Alpha",
			new SliderProperty("Alpha", 0, 1, 1.0).addConnector(OUT, GRAY).addConnector(IN, GRAY)
		).setSize(200, 0);
	}


	public static Node createIntermediateMath()
	{
		return new Node("Math",
			new ValueProperty("Value").addConnector(OUT, GRAY),
			new ComboBoxProperty("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Absolute", "Modulo", "Greater Than"),
			new CheckBoxProperty("Clamp", false),
			new SliderProperty("Value", 0.5, 0.01).addConnector(IN, GRAY),
			new SliderProperty("Value", 0.5, 0.01).addConnector(IN, GRAY)
		).setSize(200, 0);
	}


	public static Node createIntermediateColorMix()
	{
		return new Node("Mix",
			new ValueProperty("Color").addConnector(OUT, YELLOW),
			new SliderProperty("Fac", 0, 1, 0.5).addConnector(IN, GRAY),
			new ColorChooserProperty("Color", new Color(0, 0, 0)).addConnector(IN, YELLOW),
			new ColorChooserProperty("Color", new Color(255, 255, 255)).addConnector(IN, YELLOW)
		).setSize(200, 0);
	}
}
