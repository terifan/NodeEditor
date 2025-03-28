package org.terifan.nodeeditor.util;

import java.awt.Color;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.NodeFunction;
import org.terifan.nodeeditor.Styles;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.GRAY;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.PURPLE;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.YELLOW;
import org.terifan.nodeeditor.widgets.ButtonProperty;
import org.terifan.nodeeditor.widgets.CheckBoxProperty;
import org.terifan.nodeeditor.widgets.ColorChooserProperty;
import org.terifan.nodeeditor.widgets.ComboBoxProperty;
import org.terifan.nodeeditor.widgets.ImageProperty;
import org.terifan.nodeeditor.widgets.RGBPaletteProperty;
import org.terifan.nodeeditor.widgets.SliderProperty;
import org.terifan.nodeeditor.widgets.ValueProperty;
import org.terifan.vecmath.Vec4d;


public class SimpleNodesFactory
{
	private final static String PREFIX = SimpleNodesFactory.class.getCanonicalName();
	private final static int SIZE = 150;

	private static NodeFunction mMathProducer = aContext ->
	{
		Object value1 = aContext.value("value1");
		Object value2 = aContext.value("value2");
		String func = aContext.value("function", String.class);
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
						return v1.doubleValue() > v2.doubleValue() ? 1 : 0;
				}
			}
			if (value2 instanceof Vec4d v2)
			{
				double d = v1.doubleValue();
				switch (func)
				{
					case "Add":
						return v2.clone().add(d, d, d, d);
					case "Subtract":
						return v2.clone().subtract(d, d, d, d);
					case "Multiply":
						return v2.clone().scale(d, d, d, d);
					case "Divide":
						return v2.clone().divide(d, d, d, d);
					case "Modulo":
						return v2.clone().mod(d);
					case "Greater Than":
						return new Vec4d(d > v2.x ? 1 : 0, d > v2.y ? 1 : 0, d > v2.z ? 1 : 0, d > v2.w ? 1 : 0);
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
						return v1.clone().add(v2);
					case "Subtract":
						return v1.clone().subtract(v2);
					case "Multiply":
						return v1.clone().scale(v2);
					case "Divide":
						return v1.clone().divide(v2);
					case "Modulo":
						return v1.clone().mod(v2);
					case "Greater Than":
						return new Vec4d(v1.x > v2.x ? 1 : 0, v1.y > v2.y ? 1 : 0, v1.z > v2.z ? 1 : 0, v1.w > v2.w ? 1 : 0);
				}
			}
		}
		throw new IllegalArgumentException("Unsupported: " + value1.getClass() + ", " + value2.getClass() + ", " + func);
	};

	private static NodeFunction mAlphaProducer = aContext ->
	{
		Vec4d color = aContext.value("color");
		color.w = aContext.value("a");
		return color;
	};

	private static NodeFunction mCombineColorProducer = aContext ->
	{
		double r = aContext.value("r");
		double g = aContext.value("g");
		double b = aContext.value("b");
		double a = aContext.value("a");
		return new Vec4d(r, g, b, a);
	};

	private static NodeFunction mRGBProducer = aContext ->
	{
		Color color = aContext.value("color");
		return new Vec4d(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).divide(255);
	};

	private static NodeFunction mColorMixProducer = aContext ->
	{
		Vec4d color1 = aContext.value("color1");
		Vec4d color2 = aContext.value("color2");
		double fac = aContext.value("fac");
		return new Vec4d().interpolate(color1, color2, fac);
	};

	private static NodeFunction mInvertColorProducer = aContext ->
	{
		Object value = aContext.value("color");
		double factor = aContext.value("factor");
		if (value instanceof Vec4d v)
		{
			return new Vec4d(1 - v.x, 1 - v.y, 1 - v.z, v.w).scale(factor);
		}
		if (value instanceof Double v)
		{
			return (1 - v) * factor;
		}
		throw new IllegalArgumentException();
	};

	private static NodeFunction mSeparateColorRedProducer = aContext -> ((Vec4d)aContext.value("color")).x;
	private static NodeFunction mSeparateColorGreenProducer = aContext -> ((Vec4d)aContext.value("color")).y;
	private static NodeFunction mSeparateColorBlueProducer = aContext -> ((Vec4d)aContext.value("color")).z;
	private static NodeFunction mSeparateColorAlphaProducer = aContext -> ((Vec4d)aContext.value("color")).w;
	private static NodeFunction mColorAlphaProducer = aContext -> new Vec4d(0, 0, 0, aContext.value("a"));
	private static NodeFunction mValueProducer = aContext -> aContext.value("value");


	public static void install(NodeEditorPane aRuntime)
	{
		aRuntime.bind(PREFIX + ".ColorAlphaProducer", mColorAlphaProducer);
		aRuntime.bind(PREFIX + ".ColorMixProducer", mColorMixProducer);
		aRuntime.bind(PREFIX + ".MathProducer", mMathProducer);
		aRuntime.bind(PREFIX + ".CombineColorProducer", mCombineColorProducer);
		aRuntime.bind(PREFIX + ".RGBProducer", mRGBProducer);
		aRuntime.bind(PREFIX + ".AlphaProducer", mAlphaProducer);
		aRuntime.bind(PREFIX + ".ValueProducer", mValueProducer);
		aRuntime.bind(PREFIX + ".SeparateColorRedProducer", mSeparateColorRedProducer);
		aRuntime.bind(PREFIX + ".SeparateColorGreenProducer", mSeparateColorGreenProducer);
		aRuntime.bind(PREFIX + ".SeparateColorBlueProducer", mSeparateColorBlueProducer);
		aRuntime.bind(PREFIX + ".SeparateColorAlphaProducer", mSeparateColorAlphaProducer);
		aRuntime.bind(PREFIX + ".InvertColorProducer", mInvertColorProducer);
	}


	public static Node createSourceTexture()
	{
		return (Node)new Node("Texture",
			new ValueProperty("Color").addConnector(OUT, YELLOW),
			new ValueProperty("Alpha").addConnector(OUT, GRAY),
			new ButtonProperty("Open"),
			new ImageProperty("Image", SIZE, SIZE),
			new ValueProperty("Vector").addConnector(IN, PURPLE)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColor()
	{
		return (Node)new Node("Color",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(PREFIX + ".ColorAlphaProducer"),
			new ColorChooserProperty("Color", Color.BLACK).setId("color"),
			new SliderProperty("Alpha").setRange(0, 1, 1, 0.001).setId("a").addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColorRGB()
	{
		return (Node)new Node("RGB",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(PREFIX + ".RGBProducer"),
			new RGBPaletteProperty(Color.BLACK).setId("color")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createCombineColor()
	{
		return (Node)new Node("CombineColor",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(PREFIX + ".CombineColorProducer"),
			new SliderProperty("Red").setRange(0, 1, 0, 0.001).setId("r").addConnector(IN, GRAY),
			new SliderProperty("Green").setRange(0, 1, 0.5, 0.001).setId("g").addConnector(IN, GRAY),
			new SliderProperty("Blue").setRange(0, 1, 0.5, 0.001).setId("b").addConnector(IN, GRAY),
			new SliderProperty("Alpha").setRange(0, 1, 1.0, 0.001).setId("a").addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceAlpha()
	{
		return (Node)new Node("Alpha",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(PREFIX + ".AlphaProducer"),
			new SliderProperty("Alpha").setRange(0, 1, 1, 0.001).setId("a").addConnector(OUT, GRAY).addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createIntermediateMath()
	{
		return (Node)new Node("Math",
			new ValueProperty("Value").addConnector(OUT, GRAY).setProducer(PREFIX + ".MathProducer"),
			new ComboBoxProperty("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Modulo", "Greater Than").setId("function"),
			new CheckBoxProperty("Clamp", false).setId("clamp"),
			new SliderProperty("Value", 1, 0.01).setId("value1").addConnector(IN, GRAY),
			new SliderProperty("Value", 0.5, 0.01).setId("value2").addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}


	public static Node createIntermediateColorMix()
	{
		return (Node)new Node("ColorMix",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(PREFIX + ".ColorMixProducer"),
			new SliderProperty("Fac").setRange(0, 1, 0.5, 0.01).setId("fac").addConnector(IN, GRAY),
			new ColorChooserProperty("Color", new Color(0, 0, 0)).setId("color1").addConnector(IN, YELLOW),
			new ColorChooserProperty("Color", new Color(255, 255, 255)).setId("color2").addConnector(IN, YELLOW)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}


	public static Node createIntermediateSeparateColor()
	{
		return (Node)new Node("SeparateColor",
			new ValueProperty("Red").addConnector(OUT, GRAY).setProducer(PREFIX + ".SeparateColorRedProducer"),
			new ValueProperty("Green").addConnector(OUT, GRAY).setProducer(PREFIX + ".SeparateColorGreenProducer"),
			new ValueProperty("Blue").addConnector(OUT, GRAY).setProducer(PREFIX + ".SeparateColorBlueProducer"),
			new ValueProperty("Alpha").addConnector(OUT, GRAY).setProducer(PREFIX + ".SeparateColorAlphaProducer"),
			new ColorChooserProperty("Color", new Color(0, 0, 0)).setId("color").addConnector(IN, YELLOW)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}


	public static Node createIntermediateInvertColor()
	{
		return (Node)new Node("InvertColor",
			new ValueProperty("Red").addConnector(OUT, GRAY).setProducer(PREFIX + ".InvertColorProducer"),
			new SliderProperty("Fac").setId("factor").setRange(0, 1, 1, 0).addConnector(IN, GRAY),
			new ColorChooserProperty("Color", new Color(0, 0, 0)).setId("color").addConnector(IN, YELLOW)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.YELLOW);
	}


	public static Node createSourceValue()
	{
		return (Node)new Node("Value",
			new ValueProperty("Value").addConnector(OUT, GRAY).setProducer(PREFIX + ".ValueProducer"),
			new SliderProperty("").setId("value")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}
}
