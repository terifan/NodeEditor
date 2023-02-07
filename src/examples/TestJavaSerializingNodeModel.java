package examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import org.terifan.nodeeditor.widgets.SliderProperty;
import org.terifan.nodeeditor.widgets.ImageProperty;
import org.terifan.nodeeditor.widgets.ButtonProperty;
import org.terifan.nodeeditor.widgets.CheckBoxProperty;
import org.terifan.nodeeditor.widgets.ColorChooserProperty;
import org.terifan.nodeeditor.widgets.ComboBoxProperty;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.TextProperty;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import static org.terifan.nodeeditor.Styles.GRAY;
import static org.terifan.nodeeditor.Styles.PURPLE;
import static org.terifan.nodeeditor.Styles.YELLOW;
import org.terifan.util.Debug;


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

			Debug.hexDump(serializedData);

			NodeEditorPane editor = new NodeEditorPane(model);

			editor.addImagePainter((aPane, aNode, aProperty, aGraphics, aBounds) ->
			{
				if (aNode.getTitle().equals("Output"))
				{
					BufferedImage image = new BufferedImage(aBounds.width, aBounds.height, BufferedImage.TYPE_INT_RGB);
					aGraphics.drawImage(image, aBounds.x, aBounds.y, aBounds.width, aBounds.height, null);
					return true;
				}
				return false;
			});

			editor.addImagePainter((aPane, aNode, aProperty, aGraphics, aBounds) ->
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

		model.addNode(new Node("Color",
			new TextProperty("Color").addConnector(OUT, YELLOW),
			new SliderProperty("Red", 0, 1, 0).addConnector(IN, GRAY),
			new SliderProperty("Green", 0, 1, 0.5).addConnector(IN, GRAY),
			new SliderProperty("Blue", 0, 1, 0.75).addConnector(IN, GRAY),
			new SliderProperty("Alpha", 0, 1, 0.5).addConnector(IN, GRAY)
		));

		model.addNode(new Node("Texture",
			new TextProperty("Color").addConnector(OUT, YELLOW),
			new TextProperty("Alpha").addConnector(OUT, GRAY),
			new ButtonProperty("Open"),
			new ImageProperty("image", 200, 200).setImagePath("Big_pebbles_pxr128.jpg"),
			new TextProperty("Vector").addConnector(IN, PURPLE)
		));

		model.addNode(new Node("Texture",
			new TextProperty("Color").addConnector(OUT, YELLOW),
			new TextProperty("Alpha").addConnector(OUT, GRAY),
			new ButtonProperty("Open"),
			new ImageProperty("image", 200, 200).setImagePath("Big_pebbles_pxr128_bmp.jpg"),
			new TextProperty("Vector").addConnector(IN, PURPLE)
		));

		model.addNode(new Node("Texture",
			new TextProperty("Color").addConnector(OUT, YELLOW),
			new TextProperty("Alpha").addConnector(OUT, GRAY),
			new ButtonProperty("Open"),
			new ImageProperty("image", 200, 200).setImagePath("Big_pebbles_pxr128_normal.jpg"),
			new TextProperty("Vector").addConnector(IN, PURPLE)
		));

		model.addNode(new Node("Output",
			new ColorChooserProperty("Surface", new Color(0, 0, 0)).addConnector(IN, YELLOW),
			new SliderProperty("Alpha", 0, 1, 0.75).addConnector(IN, GRAY), new ImageProperty("Image", 200, 200)
		));

		model.addNode(new Node("Alpha",
			new SliderProperty("Alpha", 0, 1, 0.75).addConnector(OUT, GRAY)
		));

		model.addNode(new Node("TextureCoordinate",
			new TextProperty("UV").addConnector(OUT, PURPLE)
		));

		model.addNode(new Node("Multiply",
			new TextProperty("Value").addConnector(OUT, GRAY),
			new ComboBoxProperty("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Absolute", "Modulo", "Greater Than"),
			new CheckBoxProperty("Clamp", false),
			new SliderProperty("Value", 0.5, 0.01).addConnector(IN, GRAY),
			new SliderProperty("Value", 0.5, 0.01).addConnector(IN, GRAY)
		));

		model.addNode(new Node("Mix",
			new TextProperty("Color").addConnector(OUT, YELLOW),
			new SliderProperty("Fac", 0, 1, 0.5).addConnector(IN, GRAY),
			new ColorChooserProperty("Color", new Color(255, 0, 0)).addConnector(IN, YELLOW),
			new ColorChooserProperty("Color", new Color(0, 0, 255)).addConnector(IN, YELLOW)
		));

		model.addNode(new Node("Mix",
			new TextProperty("Color").addConnector(OUT, YELLOW),
			new SliderProperty("Fac", 0, 1, 0.5).addConnector(IN, GRAY),
			new ColorChooserProperty("Color", new Color(255, 0, 0)).addConnector(IN, YELLOW),
			new ColorChooserProperty("Color", new Color(0, 0, 255)).addConnector(IN, YELLOW)
		));

		model.addConnection(1, 0, 8, 2);
		model.addConnection(2, 0, 9, 2);
		model.addConnection(9, 0, 4, 0);
		model.addConnection(8, 0, 9, 3);

		model.getNode(0).setBounds(0, 0, 200, 0);
		model.getNode(1).setBounds(0, -350, 200, 0);
		model.getNode(2).setBounds(-300, -550, 200, 0);
		model.getNode(3).setBounds(-300, 0, 200, 0);
		model.getNode(4).setBounds(600, 100, 200, 0);
		model.getNode(5).setBounds(0, 200, 200, 0);
		model.getNode(6).setBounds(-600, -150, 200, 0);
		model.getNode(7).setBounds(-300, -200, 200, 0);
		model.getNode(8).setBounds(300, -50, 200, 0);
		model.getNode(9).setBounds(300, -330, 200, 0);

//		new AutoLayout().layout(model, model.getNode(0));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream dos = new ObjectOutputStream(baos))
		{
			dos.writeObject(model);
		}

		return baos.toByteArray();
	}
}
