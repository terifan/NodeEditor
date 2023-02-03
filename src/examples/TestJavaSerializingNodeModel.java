package examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import org.terifan.nodeeditor.widgets.SliderPropertyItem;
import org.terifan.nodeeditor.widgets.ImagePropertyItem;
import javax.swing.JFrame;
import org.terifan.nodeeditor.widgets.ButtonPropertyItem;
import org.terifan.nodeeditor.widgets.CheckBoxPropertyItem;
import org.terifan.nodeeditor.widgets.ColorChooserNodeItem;
import org.terifan.nodeeditor.widgets.ComboBoxPropertyItem;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import static org.terifan.nodeeditor.Styles.GRAY;
import static org.terifan.nodeeditor.Styles.PURPLE;
import static org.terifan.nodeeditor.Styles.YELLOW;
import org.terifan.nodeeditor.widgets.TextPropertyItem;


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
					BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
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

		model.addNode(new Node("Color")
			.addProperty(new TextPropertyItem("Color")
				.addConnector(OUT, YELLOW))
			.addProperty(new SliderPropertyItem("Red", 0, 1, 0)
				.addConnector(IN, GRAY))
			.addProperty(new SliderPropertyItem("Green", 0, 1, 0.5)
				.addConnector(IN, GRAY))
			.addProperty(new SliderPropertyItem("Blue", 0, 1, 0.75)
				.addConnector(IN, GRAY))
			.addProperty(new SliderPropertyItem("Alpha", 0, 1, 0.5)
				.addConnector(IN, GRAY))
		);

		model.addNode(new Node("Texture")
			.setIdentity("texture1")
			.addProperty(new TextPropertyItem("Color")
				.addConnector(OUT, YELLOW))
			.addProperty(new TextPropertyItem("Alpha")
				.addConnector(OUT, GRAY))
			.addProperty(new ButtonPropertyItem("Open"))
			.addProperty(new ImagePropertyItem("image", 200, 200)
				.setImagePath("Big_pebbles_pxr128.jpg"))
			.addProperty(new TextPropertyItem("Vector")
				.addConnector(IN, PURPLE))
		);

		model.addNode(new Node("Texture")
			.setIdentity("texture2")
			.addProperty(new TextPropertyItem("Color")
				.addConnector(OUT, YELLOW))
			.addProperty(new TextPropertyItem("Alpha")
				.addConnector(OUT, GRAY))
			.addProperty(new ButtonPropertyItem("Open"))
			.addProperty(new ImagePropertyItem("image", 200, 200)
				.setImagePath("Big_pebbles_pxr128_bmp.jpg")
			)
			.addProperty(new TextPropertyItem("Vector")
				.addConnector(IN, PURPLE))
		);

		model.addNode(new Node("Texture")
			.setIdentity("texture3")
			.addProperty(new TextPropertyItem("Color")
				.addConnector(OUT, YELLOW))
			.addProperty(new TextPropertyItem("Alpha")
				.addConnector(OUT, GRAY))
			.addProperty(new ButtonPropertyItem("Open"))
			.addProperty(new ImagePropertyItem("image", 200, 200)
				.setImagePath("Big_pebbles_pxr128_normal.jpg"))
			.addProperty(new TextPropertyItem("Vector")
				.addConnector(IN, PURPLE))
		);

		model.addNode(new Node("Output")
			.addProperty(new ColorChooserNodeItem("Surface", new Color(0, 0, 0))
				.addConnector(IN, YELLOW))
			.addProperty(new SliderPropertyItem("Alpha", 0, 1, 0.75)
				.addConnector(IN, GRAY))
			.addProperty(new ImagePropertyItem("Image", 200, 200)
				.setIdentity("Rendered.Output")
			)
		);

		model.addNode(new Node("Alpha")
			.addProperty(new SliderPropertyItem("Alpha", 0, 1, 0.75)
				.addConnector(OUT, GRAY))
		);

		model.addNode(new Node("TextureCoordinate")
			.addProperty(new TextPropertyItem("UV")
				.addConnector(OUT, PURPLE))
		);

		model.addNode(new Node("Multiply")
			.setIdentity("math")
			.addProperty(new TextPropertyItem("Value")
				.setIdentity("result")
				.addConnector(OUT, GRAY))
			.addProperty(new ComboBoxPropertyItem("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Absolute", "Modulo", "Greater Than"))
			.addProperty(new CheckBoxPropertyItem("Clamp", false))
			.addProperty(new SliderPropertyItem("Value", 0.5, 0.01)
				.setIdentity("value1")
				.addConnector(IN, GRAY))
			.addProperty(new SliderPropertyItem("Value", 0.5, 0.01)
				.setIdentity("value2")
				.addConnector(IN, GRAY))
		);

		model.addNode(new Node("Multiply")
			.setIdentity("math2")
			.addProperty(new TextPropertyItem("Value")
				.setIdentity("result")
				.addConnector(OUT, GRAY))
			.addProperty(new ComboBoxPropertyItem("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Absolute", "Modulo", "Greater Than"))
			.addProperty(new CheckBoxPropertyItem("Clamp", false))
			.addProperty(new SliderPropertyItem("Value", 0.5, 0.01)
				.setIdentity("value1")
				.addConnector(IN, GRAY))
			.addProperty(new SliderPropertyItem("Value", 0.5, 0.01)
				.setIdentity("value2")
				.addConnector(IN, GRAY))
		);

		model.addNode(new Node("Mix")
			.addProperty(new TextPropertyItem("Color")
				.setIdentity("colorOut")
				.addConnector(OUT, YELLOW))
			.addProperty(new SliderPropertyItem("Fac", 0, 1, 0.5)
				.addConnector(IN, GRAY))
			.addProperty(new ColorChooserNodeItem("Color", new Color(255, 0, 0))
				.setIdentity("colorIn1")
				.addConnector(IN, YELLOW))
			.addProperty(new ColorChooserNodeItem("Color", new Color(0, 0, 255))
				.setIdentity("colorIn2")
				.addConnector(IN, YELLOW))
		);

		model.addNode(new Node("Mix2")
			.addProperty(new TextPropertyItem("Color")
				.setIdentity("colorOut")
				.addConnector(OUT, YELLOW))
			.addProperty(new SliderPropertyItem("Fac", 0, 1, 0.5)
				.addConnector(IN, GRAY))
			.addProperty(new ColorChooserNodeItem("Color", new Color(255, 0, 0))
				.setIdentity("colorIn1")
				.addConnector(IN, YELLOW))
			.addProperty(new ColorChooserNodeItem("Color", new Color(0, 0, 255))
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