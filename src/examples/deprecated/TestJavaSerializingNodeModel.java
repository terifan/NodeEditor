package examples.deprecated;

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
import org.terifan.nodeeditor.widgets.ValueProperty;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.GRAY;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.PURPLE;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.YELLOW;
import org.terifan.nodeeditor.util.SimpleNodesFactory;


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

//			Debug.hexDump(serializedData);

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
				if (aProperty.getImagePath() != null)
				{
					BufferedImage image = ImageIO.read(TestJavaSerializingNodeModel.class.getResource(aProperty.getImagePath()));
					aGraphics.drawImage(image, aBounds.x, aBounds.y, aBounds.width, aBounds.height, null);
					return true;
				}
				return false;
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

		model.addComponent(SimpleNodesFactory.createSourceColorRGBA());
		model.addComponent(SimpleNodesFactory.createSourceTexture());
		model.addComponent(SimpleNodesFactory.createSourceTexture());
		model.addComponent(SimpleNodesFactory.createSourceTexture());

		model.addComponent(new Node("Output",
			new ColorChooserProperty("Surface", new Color(0, 0, 0)).addConnector(IN, YELLOW),
			new SliderProperty("Alpha", 0, 1, 0.75).addConnector(IN, GRAY), new ImageProperty("Image", 200, 200)
		));

		model.addComponent(SimpleNodesFactory.createSourceAlpha());

		model.addComponent(new Node("TextureCoordinate",
			new ValueProperty("UV").addConnector(OUT, PURPLE)
		));

		model.addComponent(SimpleNodesFactory.createIntermediateMath());
		model.addComponent(SimpleNodesFactory.createIntermediateColorMix());
		model.addComponent(SimpleNodesFactory.createIntermediateColorMix());

		model.addConnection(1, 0, 8, 2);
		model.addConnection(2, 0, 9, 2);
		model.addConnection(9, 0, 4, 0);
		model.addConnection(8, 0, 9, 3);

		model.getComponent(0).setBounds(0, 0, 200, 0);
		model.getComponent(1).setBounds(0, -350, 200, 0);
		model.getComponent(2).setBounds(-300, -550, 200, 0);
		model.getComponent(3).setBounds(-300, 0, 200, 0);
		model.getComponent(4).setBounds(600, 100, 200, 0);
		model.getComponent(5).setBounds(0, 200, 200, 0);
		model.getComponent(6).setBounds(-600, -150, 200, 0);
		model.getComponent(7).setBounds(-300, -200, 200, 0);
		model.getComponent(8).setBounds(300, -50, 200, 0);
		model.getComponent(9).setBounds(300, -330, 200, 0);

//		new AutoLayout().layout(model, model.getNode(0));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream dos = new ObjectOutputStream(baos))
		{
			dos.writeObject(model);
		}

		return baos.toByteArray();
	}
}
