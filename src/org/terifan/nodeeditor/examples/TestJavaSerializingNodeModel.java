package org.terifan.nodeeditor.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import org.terifan.nodeeditor.SliderPropertyItem;
import org.terifan.nodeeditor.ImagePropertyItem;
import javax.swing.JFrame;
import org.terifan.nodeeditor.ButtonPropertyItem;
import org.terifan.nodeeditor.CheckBoxPropertyItem;
import org.terifan.nodeeditor.ColorChooserNodeItem;
import org.terifan.nodeeditor.ComboBoxPropertyItem;
import static org.terifan.nodeeditor.Connector.GRAY;
import static org.terifan.nodeeditor.Connector.PURPLE;
import static org.terifan.nodeeditor.Connector.YELLOW;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.NodeEditor;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.TextPropertyItem;
import org.terifan.util.cache.Cache;


public class TestJavaSerializingNodeModel
{
	public static void main(String... args)
	{
		try
		{
			NodeModel model = new NodeModel();

			model.addNode(new Node("Color")
				.setSize(200, 0)
				.add(new TextPropertyItem("Color")
					.addConnector(OUT, YELLOW))
				.add(new SliderPropertyItem("Red", 0, 1, 0)
					.addConnector(IN, GRAY))
				.add(new SliderPropertyItem("Green", 0, 1, 0.5)
					.addConnector(IN, GRAY))
				.add(new SliderPropertyItem("Blue", 0, 1, 0.75)
					.addConnector(IN, GRAY))
				.add(new SliderPropertyItem("Alpha", 0, 1, 0.5)
					.addConnector(IN, GRAY))
			);

			model.addNode(new Node("Texture")
				.setIdentity("texture1")
				.add(new TextPropertyItem("Color")
					.addConnector(OUT, YELLOW))
				.add(new TextPropertyItem("Alpha")
					.addConnector(OUT, GRAY))
				.add(new ButtonPropertyItem("Open"))
				.add(new ImagePropertyItem("image", 200, 200)
					.setImagePath("Big_pebbles_pxr128.jpg"))
				.add(new TextPropertyItem("Vector")
					.addConnector(IN, PURPLE))
			);

			model.addNode(new Node("Texture")
				.setIdentity("texture2")
				.add(new TextPropertyItem("Color")
					.addConnector(OUT, YELLOW))
				.add(new TextPropertyItem("Alpha")
					.addConnector(OUT, GRAY))
				.add(new ButtonPropertyItem("Open"))
				.add(new ImagePropertyItem("image", 200, 200))
				.add(new TextPropertyItem("Vector")
					.addConnector(IN, PURPLE))
			);

			model.addNode(new Node("Texture")
				.setIdentity("texture3")
				.add(new TextPropertyItem("Color")
					.addConnector(OUT, YELLOW))
				.add(new TextPropertyItem("Alpha")
					.addConnector(OUT, GRAY))
				.add(new ButtonPropertyItem("Open"))
				.add(new ImagePropertyItem("image", 200, 200)
					.putProperty("image_path", "Big_pebbles_pxr128_normal.jpg"))
				.add(new TextPropertyItem("Vector")
					.addConnector(IN, PURPLE))
			);

			model.addNode(new Node("Output")
				.add(new ColorChooserNodeItem("Surface", new Color(0, 0, 0))
					.addConnector(IN, YELLOW))
				.add(new SliderPropertyItem("Alpha", 0, 1, 0.75)
					.addConnector(IN, GRAY))
			);

			model.addNode(new Node("Alpha")
				.setSize(200, 0)
				.add(new SliderPropertyItem("Alpha", 0, 1, 0.75)
					.addConnector(OUT, GRAY))
			);

			model.addNode(new Node("TextureCoordinate")
				.setSize(200, 0)
				.add(new TextPropertyItem("UV")
					.addConnector(OUT, PURPLE))
			);

			model.addNode(new Node("Multiply")
				.setSize(200, 0)
				.setIdentity("math")
				.add(new TextPropertyItem("Value")
					.setIdentity("result")
					.addConnector(OUT, GRAY))
				.add(new ComboBoxPropertyItem("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Absolute", "Modulo", "Greater Than"))
				.add(new CheckBoxPropertyItem("Clamp", false))
				.add(new SliderPropertyItem("Value", 0.5, 0.01)
					.setIdentity("value1")
					.addConnector(IN, GRAY))
				.add(new SliderPropertyItem("Value", 0.5, 0.01)
					.setIdentity("value2")
					.addConnector(IN, GRAY))
			);

			model.addNode(new Node("Mix")
				.setSize(200, 0)
				.add(new TextPropertyItem("Color")
					.setIdentity("colorOut")
					.addConnector(OUT, YELLOW))
				.add(new SliderPropertyItem("Fac", 0, 1, 0.5)
					.addConnector(IN, GRAY))
				.add(new ColorChooserNodeItem("Color", new Color(255, 0, 0))
					.setIdentity("colorIn1")
					.addConnector(IN, YELLOW))
				.add(new ColorChooserNodeItem("Color", new Color(0, 0, 255))
					.setIdentity("colorIn2")
					.addConnector(IN, YELLOW))
			);

			model.addConnection("texture1.color", "mix.colorIn1");
			model.addConnection("color.color", "mix.colorIn2");
			model.addConnection("mix.colorOut", "output.surface");
			model.addConnection("alpha.alpha", "output.alpha");
			model.addConnection("alpha.alpha", "mix.fac");
			model.addConnection("texturecoordinate.uv", "math.value1");
			model.addConnection("texturecoordinate.uv", "texture2.vector");
			model.addConnection("texturecoordinate.uv", "texture3.vector");
			model.addConnection("texture3.alpha", "color.alpha");
			model.addConnection("math.result", "texture1.vector");

			model.getNode("color").setLocation(0, 0);
			model.getNode("mix").setLocation(300, -50);
			model.getNode("alpha").setLocation(0, 200);
			model.getNode("output").setLocation(600, 100);
			model.getNode("texture1").setLocation(0, -350);
			model.getNode("texture2").setLocation(-300, -550);
			model.getNode("texture3").setLocation(-300, 0);
			model.getNode("texturecoordinate").setLocation(-600, -150);
			model.getNode("math").setLocation(-300, -200);


			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ObjectOutputStream dos = new ObjectOutputStream(baos))
			{
				dos.writeObject(model);
			}
			byte[] serializedData = baos.toByteArray();


			NodeModel deserializedModel;
			try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedData)))
			{
				deserializedModel = (NodeModel)ois.readObject();
			}


			NodeEditor editor = new NodeEditor(deserializedModel);

			editor.setResourceContext(TestJavaSerializingNodeModel.class); // texture1.image is loaded using this resource context

			BufferedImage image = ImageIO.read(TestJavaSerializingNodeModel.class.getResource("Big_pebbles_pxr128_bmp.jpg"));

			Cache<String,BufferedImage> cache = new Cache<>(3);
			editor.addResourceLoader("texture2.image", e->image);
//			editor.addResourceLoader("texture3.image", e->cache.get(e.getProperty("image_path"), p->ImageIO.read(Test3.class.getResource(p))));

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
}
