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
import org.terifan.nodeeditor.widgets.ButtonPropertyItem;
import org.terifan.nodeeditor.widgets.CheckBoxPropertyItem;
import org.terifan.nodeeditor.widgets.ComboBoxPropertyItem;
import org.terifan.nodeeditor.Direction;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.widgets.ImagePropertyItem;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.ColorChooserNodeItem;
import org.terifan.nodeeditor.widgets.SliderPropertyItem;
import org.terifan.nodeeditor.widgets.TextPropertyItem;


public class TestEditor
{
	public static void main(String... args)
	{
		try
		{
			NodeModel model = new NodeModel();

			NodeEditorPane editor = new NodeEditorPane(model);
			editor.addButtonHandler(item -> {
				((ImagePropertyItem)item.getNode().getProperty("Image")).setImagePath("Big_pebbles_pxr128.jpg");
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
					model.addNode(new Node("SourceImage",
						new ButtonPropertyItem("Open"),
						new ImagePropertyItem("Image", 200, 200),
						new TextPropertyItem("Color").addConnector(Direction.OUT),
						new TextPropertyItem("Alpha").addConnector(Direction.OUT)
					).setLocation(0, 0));

					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("RenderOutput")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addNode(new Node("RenderOutput",
						new TextPropertyItem("Color").addConnector(Direction.IN),
						new TextPropertyItem("Alpha").addConnector(Direction.IN),
						new ImagePropertyItem("undefined", 200, 200)
					).setLocation(0, 0));

					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Math")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addNode(new Node("Math")
						.setLocation(0, 0)
						.setSize(200, 0)
						.addProperty(new TextPropertyItem("Value")
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

					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Mix")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addNode(new Node("Mix")
						.setSize(200, 0)
						.addProperty(new TextPropertyItem("Color")
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

					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Alpha")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addNode(new Node("Alpha")
						.setSize(200, 0)
						.addProperty(new SliderPropertyItem("Alpha", 0, 1, 0.75)
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
					model.addNode(new Node("Color")
						.setSize(200, 0)
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
