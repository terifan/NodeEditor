package org.terifan.nodeeditor.examples;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.terifan.nodeeditor.ButtonNodeItem;
import org.terifan.nodeeditor.CheckBoxNodeItem;
import org.terifan.nodeeditor.ComboBoxNodeItem;
import static org.terifan.nodeeditor.Connector.GRAY;
import org.terifan.nodeeditor.Direction;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.ImageNodeItem;
import org.terifan.nodeeditor.NodeEditor;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.SliderNodeItem;
import org.terifan.nodeeditor.TextNodeItem;


public class Test5
{
	public static void main(String... args)
	{
		try
		{
			NodeModel model = new NodeModel();

			NodeEditor editor = new NodeEditor(model);
			editor.center();
			editor.setScale(1);

			JToolBar toolbar = new JToolBar();

			toolbar.add(new AbstractAction("SourceImage")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					ImageNodeItem image = new ImageNodeItem("node10", 200, 200);
					ButtonNodeItem button = new ButtonNodeItem("Open");

					model.addNode(new Node("SourceImage",
						button,
						image,
						new TextNodeItem("Color").addConnector(Direction.OUT),
						new TextNodeItem("Alpha").addConnector(Direction.OUT)
					).setLocation(0, 0));

					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("OutputImage")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					ImageNodeItem image = new ImageNodeItem("node10", 200, 200);

					model.addNode(new Node("Image",
						new TextNodeItem("Color").addConnector(Direction.IN),
						new TextNodeItem("Alpha").addConnector(Direction.IN),
						image
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
						.setSize(200, 0)
						.setIdentity("math")
						.add(new TextNodeItem("Value")
							.setIdentity("result")
							.addConnector(OUT, GRAY))
						.add(new ComboBoxNodeItem("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Absolute", "Modulo", "Greater Than"))
						.add(new CheckBoxNodeItem("Clamp", false))
						.add(new SliderNodeItem("Value", 0.5, 0.01)
							.setIdentity("value1")
							.addConnector(IN, GRAY))
						.add(new SliderNodeItem("Value", 0.5, 0.01)
							.setIdentity("value2")
							.addConnector(IN, GRAY))
					).setLocation(0, 0);

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
