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
import org.terifan.nodeeditor.widgets.SliderProperty;
import org.terifan.nodeeditor.widgets.ValueProperty;
import org.terifan.vecmath.Vec4d;


public class SimpleNodesFactory
{
	private final static int SIZE = 150;


	private static NodeFunction mMathProducer = (aContext, self) ->
	{
		Object value1 = self.getNode().getProperty("value1").execute(aContext);
		Object value2 = self.getNode().getProperty("value2").execute(aContext);
		String func = self.getNode().getProperty("function").getText();
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

	private static NodeFunction mColorAlphaProducer = (aContext, self) ->
	{
		Vec4d color = (Vec4d)self.getNode().getProperty("color").execute(aContext);
		color.w = (Double)self.getNode().getProperty("alpha").execute(aContext);
		return color;
	};

	private static NodeFunction mRGBAProducer = (aContext, self) ->
	{
		double r = (Double)self.getNode().getProperty("r").execute(aContext);
		double g = (Double)self.getNode().getProperty("g").execute(aContext);
		double b = (Double)self.getNode().getProperty("b").execute(aContext);
		double a = (Double)self.getNode().getProperty("a").execute(aContext);
		return new Vec4d(r, g, b, a);
	};

	private static NodeFunction mRGBProducer = (aContext, self) ->
	{
		double r = (Double)self.getNode().getProperty("r").execute(aContext);
		double g = (Double)self.getNode().getProperty("g").execute(aContext);
		double b = (Double)self.getNode().getProperty("b").execute(aContext);
		return new Vec4d(r, g, b, 1);
	};

	private static NodeFunction mColorMixProducer = (aContext, self) ->
	{
		Vec4d color1 = (Vec4d)self.getNode().getProperty("color1").execute(aContext);
		Vec4d color2 = (Vec4d)self.getNode().getProperty("color2").execute(aContext);
		double fac = (Double)self.getNode().getProperty("fac").execute(aContext);
		return new Vec4d().interpolate(color1, color2, fac);
	};

	private static NodeFunction mAlphaProducer = (aContext, self) ->
	{
		double a = (Double)self.getNode().getProperty("a").execute(aContext);
		return new Vec4d(0, 0, 0, a);
	};

	private static NodeFunction mValueProducer = (aContext, self) ->
	{
		return (Double)self.getNode().getProperty("value").execute(aContext);
	};


	public static void install(NodeEditorPane aRuntime)
	{
		aRuntime.bind(SimpleNodesFactory.class.getCanonicalName() + ".AlphaProducer", mAlphaProducer);
		aRuntime.bind(SimpleNodesFactory.class.getCanonicalName() + ".ColorMixProducer", mColorMixProducer);
		aRuntime.bind(SimpleNodesFactory.class.getCanonicalName() + ".MathProducer", mMathProducer);
		aRuntime.bind(SimpleNodesFactory.class.getCanonicalName() + ".RGBAProducer", mRGBAProducer);
		aRuntime.bind(SimpleNodesFactory.class.getCanonicalName() + ".RGBProducer", mRGBProducer);
		aRuntime.bind(SimpleNodesFactory.class.getCanonicalName() + ".ColorAlphaProducer", mColorAlphaProducer);
		aRuntime.bind(SimpleNodesFactory.class.getCanonicalName() + ".ValueProducer", mValueProducer);
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
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".ColorAlphaProducer"),
			new ColorChooserProperty("Color", Color.BLACK).setId("color"),
			new SliderProperty("Alpha").setRange(0, 1, 1, 0.001).setId("alpha").addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColorRGB()
	{
		return (Node)new Node("RGB",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".RGBProducer"),
			new SliderProperty("Red").setRange(0, 1, 0.5, 0.001).setId("r").addConnector(IN, GRAY),
			new SliderProperty("Green").setRange(0, 1, 0.5, 0.001).setId("g").addConnector(IN, GRAY),
			new SliderProperty("Blue").setRange(0, 1, 0.5, 0.001).setId("b").addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColorRGBA()
	{
		return (Node)new Node("RGBA",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".RGBAProducer"),
			new SliderProperty("Red").setRange(0, 1, 0, 0.001).setId("r").addConnector(IN, GRAY),
			new SliderProperty("Green").setRange(0, 1, 0.5, 0.001).setId("g").addConnector(IN, GRAY),
			new SliderProperty("Blue").setRange(0, 1, 0.5, 0.001).setId("b").addConnector(IN, GRAY),
			new SliderProperty("Alpha").setRange(0, 1, 1.0, 0.001).setId("a").addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceAlpha()
	{
		return (Node)new Node("Alpha",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".AlphaProducer"),
			new SliderProperty("Alpha").setRange(0, 1, 1, 0.001).setId("a").addConnector(OUT, GRAY).addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createIntermediateMath()
	{
		return (Node)new Node("Math",
			new ValueProperty("Value").addConnector(OUT, GRAY).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".MathProducer"),
			new ComboBoxProperty("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Modulo", "Greater Than").setId("function"),
			new CheckBoxProperty("Clamp", false).setId("clamp"),
			new SliderProperty("Value", 1000, 0.01).setId("value1").addConnector(IN, GRAY),
			new SliderProperty("Value", 0.5, 0.01).setId("value2").addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}


	public static Node createIntermediateColorMix()
	{
		return (Node)new Node("ColorMix",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".ColorMixProducer"),
			new SliderProperty("Fac").setRange(0, 1, 0.5, 0.1).setId("fac").addConnector(IN, GRAY),
			new ColorChooserProperty("Color", new Color(0, 0, 0)).setId("color1").addConnector(IN, YELLOW),
			new ColorChooserProperty("Color", new Color(255, 255, 255)).setId("color2").addConnector(IN, YELLOW)
		).setSize(100, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}


	public static Node createSourceValue()
	{
		return (Node)new Node("Value",
			new ValueProperty("Value").addConnector(OUT, GRAY).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".ValueProducer"),
			new SliderProperty("").setId("value")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}
}
