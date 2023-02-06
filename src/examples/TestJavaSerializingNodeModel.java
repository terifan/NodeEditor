package examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import org.terifan.nodeeditor.widgets.SliderProperty;
import org.terifan.nodeeditor.widgets.ImageProperty;
import javax.swing.JFrame;
import org.terifan.nodeeditor.widgets.ButtonProperty;
import org.terifan.nodeeditor.widgets.CheckBoxProperty;
import org.terifan.nodeeditor.widgets.ColorChooserProperty;
import org.terifan.nodeeditor.widgets.ComboBoxProperty;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import static org.terifan.nodeeditor.Styles.GRAY;
import static org.terifan.nodeeditor.Styles.PURPLE;
import static org.terifan.nodeeditor.Styles.YELLOW;
import org.terifan.nodeeditor.widgets.TextProperty;


public class TestJavaSerializingNodeModel
{
	public static void main(String... args)
	{
		try
		{
			byte[] serializedData = createModel();

			NodeModel model;
			try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedData)))
			{
				model = (NodeModel)ois.readObject();
			}

			NodeEditorPane editor = new NodeEditorPane(model);

			editor.addImagePainter((aEditor, aProperty, aGraphics, aBounds) ->
			{
				if ("Rendered.Output".equals(aProperty.getIdentity()))
				{
					BufferedImage image = new BufferedImage(aBounds.width, aBounds.height, BufferedImage.TYPE_INT_RGB);
					aGraphics.drawImage(image, aBounds.x, aBounds.y, aBounds.width, aBounds.height, null);
					return true;
				}
				return false;
			});

			editor.addImagePainter((aEditor, aProperty, aGraphics, aBounds) ->
			{
				BufferedImage image = ImageIO.read(TestJavaSerializingNodeModel.class.getResource(aProperty.getImagePath()));
				aGraphics.drawImage(image, aBounds.x, aBounds.y, aBounds.width, aBounds.height, null);
				return true;
			});

			editor.center();
			editor.setScale(1);

			JFrame frame = new JFrame();
			frame.add(editor);
			frame.setSize(1600, 1000);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}


	private static byte[] createModel() throws IOException
	{
		NodeModel model = new NodeModel();

		model.add(new Node("Color")
			.addProperty(new TextProperty("Color")
				.addConnector(OUT, YELLOW))
			.addProperty(new SliderProperty("Red", 0, 1, 0)
				.addConnector(IN, GRAY))
			.addProperty(new SliderProperty("Green", 0, 1, 0.5)
				.addConnector(IN, GRAY))
			.addProperty(new SliderProperty("Blue", 0, 1, 0.75)
				.addConnector(IN, GRAY))
			.addProperty(new SliderProperty("Alpha", 0, 1, 0.5)
				.addConnector(IN, GRAY))
		);

		model.add(new Node("Texture")
			.setIdentity("texture1")
			.addProperty(new TextProperty("Color")
				.addConnector(OUT, YELLOW))
			.addProperty(new TextProperty("Alpha")
				.addConnector(OUT, GRAY))
			.addProperty(new ButtonProperty("Open"))
			.addProperty(new ImageProperty("image", 200, 200)
				.setImagePath("Big_pebbles_pxr128.jpg"))
			.addProperty(new TextProperty("Vector")
				.addConnector(IN, PURPLE))
		);

		model.add(new Node("Texture")
			.setIdentity("texture2")
			.addProperty(new TextProperty("Color")
				.addConnector(OUT, YELLOW))
			.addProperty(new TextProperty("Alpha")
				.addConnector(OUT, GRAY))
			.addProperty(new ButtonProperty("Open"))
			.addProperty(new ImageProperty("image", 200, 200)
				.setImagePath("Big_pebbles_pxr128_bmp.jpg")
			)
			.addProperty(new TextProperty("Vector")
				.addConnector(IN, PURPLE))
		);

		model.add(new Node("Texture")
			.setIdentity("texture3")
			.addProperty(new TextProperty("Color")
				.addConnector(OUT, YELLOW))
			.addProperty(new TextProperty("Alpha")
				.addConnector(OUT, GRAY))
			.addProperty(new ButtonProperty("Open"))
			.addProperty(new ImageProperty("image", 200, 200)
				.setImagePath("Big_pebbles_pxr128_normal.jpg"))
			.addProperty(new TextProperty("Vector")
				.addConnector(IN, PURPLE))
		);

		model.add(new Node("Output")
			.addProperty(new ColorChooserProperty("Surface", new Color(0, 0, 0))
				.addConnector(IN, YELLOW))
			.addProperty(new SliderProperty("Alpha", 0, 1, 0.75)
				.addConnector(IN, GRAY))
			.addProperty(new ImageProperty("Image", 200, 200)
				.setIdentity("Rendered.Output")
			)
		);

		model.add(new Node("Alpha")
			.addProperty(new SliderProperty("Alpha", 0, 1, 0.75)
				.addConnector(OUT, GRAY))
		);

		model.add(new Node("TextureCoordinate")
			.addProperty(new TextProperty("UV")
				.addConnector(OUT, PURPLE))
		);

		model.add(new Node("Multiply")
			.setIdentity("math")
			.addProperty(new TextProperty("Value")
				.setIdentity("result")
				.addConnector(OUT, GRAY))
			.addProperty(new ComboBoxProperty("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Absolute", "Modulo", "Greater Than"))
			.addProperty(new CheckBoxProperty("Clamp", false))
			.addProperty(new SliderProperty("Value", 0.5, 0.01)
				.setIdentity("value1")
				.addConnector(IN, GRAY))
			.addProperty(new SliderProperty("Value", 0.5, 0.01)
				.setIdentity("value2")
				.addConnector(IN, GRAY))
		);

		model.add(new Node("Multiply")
			.setIdentity("math2")
			.addProperty(new TextProperty("Value")
				.setIdentity("result")
				.addConnector(OUT, GRAY))
			.addProperty(new ComboBoxProperty("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Absolute", "Modulo", "Greater Than"))
			.addProperty(new CheckBoxProperty("Clamp", false))
			.addProperty(new SliderProperty("Value", 0.5, 0.01)
				.setIdentity("value1")
				.addConnector(IN, GRAY))
			.addProperty(new SliderProperty("Value", 0.5, 0.01)
				.setIdentity("value2")
				.addConnector(IN, GRAY))
		);

		model.add(new Node("Mix")
			.addProperty(new TextProperty("Color")
				.setIdentity("colorOut")
				.addConnector(OUT, YELLOW))
			.addProperty(new SliderProperty("Fac", 0, 1, 0.5)
				.addConnector(IN, GRAY))
			.addProperty(new ColorChooserProperty("Color", new Color(255, 0, 0))
				.setIdentity("colorIn1")
				.addConnector(IN, YELLOW))
			.addProperty(new ColorChooserProperty("Color", new Color(0, 0, 255))
				.setIdentity("colorIn2")
				.addConnector(IN, YELLOW))
		);

		model.add(new Node("Mix2")
			.addProperty(new TextProperty("Color")
				.setIdentity("colorOut")
				.addConnector(OUT, YELLOW))
			.addProperty(new SliderProperty("Fac", 0, 1, 0.5)
				.addConnector(IN, GRAY))
			.addProperty(new ColorChooserProperty("Color", new Color(255, 0, 0))
				.setIdentity("colorIn1")
				.addConnector(IN, YELLOW))
			.addProperty(new ColorChooserProperty("Color", new Color(0, 0, 255))
				.setIdentity("colorIn2")
				.addConnector(IN, YELLOW))
		);

		model.addConnection("texture1.color", "mix.colorIn1");
		model.addConnection("color.color", "mix.colorIn2");
		model.addConnection("mix.colorOut", "output.surface");
		model.addConnection("texture2.color", "mix2.colorIn1");
		model.addConnection("alpha.alpha", "output.alpha");
		model.addConnection("alpha.alpha", "mix.fac");
		model.addConnection("texturecoordinate.uv", "math.value1");
		model.addConnection("texturecoordinate.uv", "texture2.vector");
		model.addConnection("texturecoordinate.uv", "texture3.vector");
		model.addConnection("texture3.alpha", "color.alpha");
		model.addConnection("math.result", "texture1.vector");

		model.getNode("color").setLocation(0, 0);
		model.getNode("mix").setLocation(300, -50);
		model.getNode("Mix2").setLocation(300, -330);
		model.getNode("alpha").setLocation(0, 200);
		model.getNode("output").setLocation(600, 100);
		model.getNode("texture1").setLocation(0, -350);
		model.getNode("texture2").setLocation(-300, -550);
		model.getNode("texture3").setLocation(-300, 0);
		model.getNode("texturecoordinate").setLocation(-600, -150);
		model.getNode("math").setLocation(-300, -200);
		model.getNode("math2").setLocation(300, -200);

//		new AutoLayout().layout(model, model.getNode(0));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream dos = new ObjectOutputStream(baos))
		{
			dos.writeObject(model);
		}

		return baos.toByteArray();
	}
}
