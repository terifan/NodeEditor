package examples;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Color.GRAY;
import static java.awt.Color.YELLOW;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.terifan.nodeeditor.widgets.ButtonProperty;
import org.terifan.nodeeditor.widgets.CheckBoxProperty;
import org.terifan.nodeeditor.widgets.ComboBoxProperty;
import org.terifan.nodeeditor.Direction;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.widgets.ImageProperty;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.ColorChooserProperty;
import org.terifan.nodeeditor.widgets.SliderProperty;
import org.terifan.nodeeditor.widgets.TextProperty;


public class TestEditor
{
	public static void main(String... args)
	{
		try
		{
			NodeModel model = new NodeModel();

			NodeEditorPane editor = new NodeEditorPane(model);
			editor.addButtonHandler(item -> {
				((ImageProperty)item.getNode().getProperty("Image")).setImagePath("Big_pebbles_pxr128.jpg");
				return true;
			});
			editor.addImagePainter((aEditor, aProperty, aGraphics, aBounds) ->
			{
				if (aProperty.getImagePath() != null)
				{
					BufferedImage image = ImageIO.read(TestJavaSerializingNodeModel.class.getResource(aProperty.getImagePath()));
					aGraphics.drawImage(image, aBounds.x, aBounds.y, aBounds.width, aBounds.height, null);
				}
				return true;
			});
			editor.center();
			editor.setScale(1);

			JToolBar toolbar = new JToolBar();

			toolbar.add(new AbstractAction("SourceImage")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.add(new Node("SourceImage",
						new ButtonProperty("Open"),
						new ImageProperty("Image", 200, 200),
						new TextProperty("Color").addConnector(Direction.OUT),
						new TextProperty("Alpha").addConnector(Direction.OUT)
					).setLocation(0, 0));

					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("RenderOutput")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.add(new Node("RenderOutput",
						new TextProperty("Color").addConnector(Direction.IN),
						new TextProperty("Alpha").addConnector(Direction.IN),
						new ImageProperty("undefined", 200, 200)
					).setLocation(0, 0));

					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Math")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.add(new Node("Math")
						.setLocation(0, 0)
						.setSize(200, 0)
						.addProperty(new TextProperty("Value")
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

					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Mix")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.add(new Node("Mix")
						.setSize(200, 0)
						.addProperty(new TextProperty("Color")
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

					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Alpha")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.add(new Node("Alpha")
						.setSize(200, 0)
						.addProperty(new SliderProperty("Alpha", 0, 1, 0.75)
							.addConnector(OUT, GRAY))
					);

					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Color")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.add(new Node("Color")
						.setSize(200, 0)
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

					editor.repaint();
				}
			});

			JPanel panel = new JPanel(new BorderLayout());
			panel.add(toolbar, BorderLayout.NORTH);
			panel.add(editor, BorderLayout.CENTER);

			JFrame frame = new JFrame();
			frame.add(panel);
			frame.setSize((int)(1600 * editor.getScale()), (int)(1000 * editor.getScale()));
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
