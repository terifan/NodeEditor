package org.terifan.nodeeditor.examples;

import javax.swing.JFrame;
import org.terifan.nodeeditor.Connector;
import static org.terifan.nodeeditor.Connector.YELLOW;
import org.terifan.nodeeditor.Direction;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.NodeEditor;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.TextNodeItem;


public class Test2
{
	public static void main(String... args)
	{
		try
		{
			NodeModel model = new NodeModel();

			model.addFactory("trip", e->
				new Node(e)
					.setSize(200, 0)
					.add(new TextNodeItem("CLOSED"))
					.add(new TextNodeItem("stops")
						.addConnector(OUT, YELLOW))
				);

			model.addFactory("stop", e->
				new Node(e)
					.setSize(200, 0)
					.add(new TextNodeItem("trip")
						.addConnector(IN, YELLOW))
					.add(new TextNodeItem("CLOSED"))
					.add(new TextNodeItem("activities")
						.addConnector(OUT, YELLOW))
				);

			model.addFactory("activity", e->
				new Node(e)
					.setSize(200, 0)
					.add(new TextNodeItem("stop")
						.addConnector(IN, YELLOW))
					.add(new TextNodeItem("LOADING"))
					.add(new TextNodeItem("CLOSED"))
					.add(new TextNodeItem("events")
						.addConnector(OUT, YELLOW))
				);

			model.attachNode("trip", "trip 8880130").setLocation(-300, 0);
			model.attachNode("stop", "stop 12652").setLocation(0, -130);
			model.attachNode("stop", "stop 12696").setLocation(0, 0);
			model.attachNode("stop", "stop 12687").setLocation(0, 130);
			model.attachNode("activity", "activity 33054").setLocation(300, -390);
			model.attachNode("activity", "activity 33649").setLocation(300, -260);
			model.attachNode("activity", "activity 33267").setLocation(300, -130);
			model.attachNode("activity", "activity 33979").setLocation(300, 0);
			model.attachNode("activity", "activity 33249").setLocation(300, 130);
			model.attachNode("activity", "activity 33687").setLocation(300, 260);
			model.attachNode("activity", "activity 33987").setLocation(300, 390);

			NodeEditor editor = new NodeEditor(model);
			editor.center();
			editor.setScale(1);

			JFrame frame = new JFrame();
			frame.add(editor);
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
