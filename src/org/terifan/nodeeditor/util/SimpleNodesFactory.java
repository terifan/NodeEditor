package org.terifan.nodeeditor.util;

import java.awt.Color;
import java.util.function.Function;
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
import org.terifan.vecmath.Vec4d;


public class SimpleNodesFactory
{
	private static Function<ValueProperty, Object> mMathProducer = prop ->
	{
		Object value1 = prop.getNode().getProperty("value1").execute();
		Object value2 = prop.getNode().getProperty("value2").execute();
		String func = prop.getNode().getProperty("function").getText();
		if (value1 instanceof Number v1)
		{
			if (value2 instanceof Number v2)
			{
				switch (func)
				{
					case "Add":
						return v1.doubleValue() + v2.doubleValue();
					case "Subtract":
						return v1.doubleValue() - v2.doubleValue();
					case "Multiply":
						return v1.doubleValue() * v2.doubleValue();
					case "Divide":
						return v1.doubleValue() / v2.doubleValue();
					case "Modulo":
						return v1.doubleValue() % v2.doubleValue();
					case "Greater Than":
						return v1.doubleValue() > v2.doubleValue();
				}
			}
		}
		if (value1 instanceof Vec4d v1)
		{
			if (value2 instanceof Vec4d v2)
			{
				switch (func)
				{
					case "Add":
						return v1.add(v2);
//						case "Subtract":
//							return v1.subtract(v2);
					case "Multiply":
						return v1.scale(v2);
//						case "Divide":
//							return v1.divide(v2);
//						case "Modulo":
//							return v1.modulo(v2);
					case "Greater Than":
						return v1.dot(1, 1, 1, 1) > v2.dot(1, 1, 1, 1);
				}
			}
		}
		throw new IllegalArgumentException("Unsupported: " + value1.getClass() + ", " + value2.getClass() + ", " + func);
	};

	private static Function<ValueProperty, Object> mRGBAProducer = prop ->
	{
		double r = (Double)prop.getNode().getProperty("r").execute();
		double g = (Double)prop.getNode().getProperty("g").execute();
		double b = (Double)prop.getNode().getProperty("b").execute();
		double a = (Double)prop.getNode().getProperty("a").execute();
		return new Vec4d(r, g, b, a);
	};

	private static Function<ValueProperty, Object> mRGBProducer = prop ->
	{
		double r = (Double)prop.getNode().getProperty("r").execute();
		double g = (Double)prop.getNode().getProperty("g").execute();
		double b = (Double)prop.getNode().getProperty("b").execute();
		return new Vec4d(r, g, b, 1);
	};

	private static Function<ValueProperty, Object> mColorMixProducer = prop ->
	{
		Vec4d color1 = (Vec4d)prop.getNode().getProperty("color1").execute();
		Vec4d color2 = (Vec4d)prop.getNode().getProperty("color2").execute();
		double fac = (Double)prop.getNode().getProperty("fac").execute();
		return new Vec4d().interpolate(color1, color2, fac);
	};

	private static Function<ValueProperty, Object> mAlphaProducer = prop ->
	{
		double a = (Double)prop.getNode().getProperty("a").execute();
		return new Vec4d(0, 0, 0, a);
	};


	public static Node createSourceTexture()
	{
		return new Node("Texture",
			new ValueProperty("Color").addConnector(OUT, YELLOW),
			new ValueProperty("Alpha").addConnector(OUT, GRAY),
			new ButtonProperty("Open"),
			new ImageProperty("Image", 200, 200),
			new ValueProperty("Vector").addConnector(IN, PURPLE)
		).setSize(200, 0);
	}


	public static Node createSourceColorRGB()
	{
		return new Node("Color",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(mRGBProducer),
			new SliderProperty("Red", 0, 1, 0.5).setId("r").addConnector(IN, GRAY),
			new SliderProperty("Green", 0, 1, 0.5).setId("g").addConnector(IN, GRAY),
			new SliderProperty("Blue", 0, 1, 0.5).setId("b").addConnector(IN, GRAY)
		).setSize(200, 0);
	}


	public static Node createSourceColorRGBA()
	{
		return new Node("Color",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(mRGBAProducer),
			new SliderProperty("Red", 0, 1, 0).setId("r").addConnector(IN, GRAY),
			new SliderProperty("Green", 0, 1, 0.5).setId("g").addConnector(IN, GRAY),
			new SliderProperty("Blue", 0, 1, 0.5).setId("b").addConnector(IN, GRAY),
			new SliderProperty("Alpha", 0, 1, 1.0).setId("a").addConnector(IN, GRAY)
		).setSize(200, 0);
	}


	public static Node createSourceAlpha()
	{
		return new Node("Alpha",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(mAlphaProducer),
			new SliderProperty("Alpha", 0, 1, 1.0).setId("a").addConnector(OUT, GRAY).addConnector(IN, GRAY)
		).setSize(200, 0);
	}


	public static Node createIntermediateMath()
	{
		return new Node("Math",
			new ValueProperty("Value").addConnector(OUT, GRAY).setProducer(mMathProducer),
			new ComboBoxProperty("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Absolute", "Modulo", "Greater Than").setId("function"),
			new CheckBoxProperty("Clamp", false).setId("clamp"),
			new SliderProperty("Value", 0.5, 0.01).setId("value1").addConnector(IN, GRAY),
			new SliderProperty("Value", 0.5, 0.01).setId("value2").addConnector(IN, GRAY)
		)
			.setSize(200, 0);
	}


	public static Node createIntermediateColorMix()
	{
		return new Node("Mix",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(mColorMixProducer),
			new SliderProperty("Fac", 0, 1, 0.5).setId("fac").addConnector(IN, GRAY),
			new ColorChooserProperty("Color", new Color(0, 0, 0)).setId("color1").addConnector(IN, YELLOW),
			new ColorChooserProperty("Color", new Color(255, 255, 255)).setId("color2").addConnector(IN, YELLOW)
		).setSize(200, 0);
	}
}
