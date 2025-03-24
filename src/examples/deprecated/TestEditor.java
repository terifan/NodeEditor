package examples.deprecated;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.ImageProperty;
import org.terifan.nodeeditor.widgets.ValueProperty;
import static org.terifan.nodeeditor.Direction.OUT;
import static org.terifan.nodeeditor.Styles.DefaultColors.PURPLE;
import org.terifan.nodeeditor.util.SimpleNodesFactory;


public class TestEditor
{
	public static void main(String... args)
	{
		try
		{
			NodeModel model = new NodeModel();

			NodeEditorPane editor = new NodeEditorPane(model);
			editor.addButtonHandler(item -> {
				((ImageProperty)item.getNode().getProperty("Image")).setImagePath(new String[]{"Big_pebbles_pxr128.jpg","Big_pebbles_pxr128_bmp.jpg","Big_pebbles_pxr128_normal.jpg"}[new Random().nextInt(3)]);
				return true;
			});
			editor.addImagePainter((aPane, aNode, aProperty, aGraphics, aBounds) ->
			{
				if (aNode.getTitle().equals("Output") && aProperty.getImagePath() != null)
				{
					BufferedImage image = ImageIO.read(TestJavaSerializingNodeModel.class.getResource(aProperty.getImagePath()));
					aGraphics.drawImage(image, aBounds.x, aBounds.y, aBounds.width, aBounds.height, null);
				}
				return true;
			});
			editor.center();
			editor.setScale(1);

			JToolBar toolbar = new JToolBar();

			toolbar.add(new AbstractAction("TexturCoordinate")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addNode(new Node("TexturCoordinate")
						.setSize(200, 0)
						.addProperty(new ValueProperty("UV")
							.addConnector(OUT, PURPLE))
					);

					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("SourceImage")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addNode(SimpleNodesFactory.createSourceTexture());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("RenderOutput")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addNode(new Node("RenderOutput",
						new ValueProperty("Color").addConnector(Direction.IN),
						new ValueProperty("Alpha").addConnector(Direction.IN),
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
					model.addNode(SimpleNodesFactory.createIntermediateMath());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Mix")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addNode(SimpleNodesFactory.createIntermediateColorMix());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Alpha")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addNode(SimpleNodesFactory.createSourceAlpha());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Color")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					model.addNode(SimpleNodesFactory.createSourceColorRGBA());
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
