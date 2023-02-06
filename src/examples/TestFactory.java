package examples;

import java.util.HashMap;
import javax.swing.JFrame;
import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import static org.terifan.nodeeditor.Styles.YELLOW;
import org.terifan.nodeeditor.widgets.TextProperty;


public class TestFactory
{
	private static HashMap<String, Factory> mFactoryMap = new HashMap<>();
	private static NodeModel mModel;

	public static void main(String... args)
	{
		try
		{
			mModel = new NodeModel();

			mFactoryMap.put("trip", e->
				new Node(e)
					.setSize(200, 0)
					.addProperty(new TextProperty("CLOSED"))
					.addProperty(new TextProperty("stops")
						.addConnector(OUT, YELLOW))
				);

			mFactoryMap.put("stop", e->
				new Node(e)
					.setSize(200, 0)
					.addProperty(new TextProperty("trip")
						.addConnector(IN, YELLOW))
					.addProperty(new TextProperty("CLOSED"))
					.addProperty(new TextProperty("activities")
						.addConnector(OUT, YELLOW))
				);

			mFactoryMap.put("activity", e->
				new Node(e)
					.setSize(200, 0)
					.addProperty(new TextProperty("stop")
						.addConnector(IN, YELLOW))
					.addProperty(new TextProperty("LOADING"))
					.addProperty(new TextProperty("CLOSED"))
					.addProperty(new TextProperty("events")
						.addConnector(OUT, YELLOW))
				);

			attachNode("trip", "trip 8880130").setLocation(-300, 0);
			attachNode("stop", "stop 12652").setLocation(0, -130);
			attachNode("stop", "stop 12696").setLocation(0, 0);
			attachNode("stop", "stop 12687").setLocation(0, 130);
			attachNode("activity", "activity 33054").setLocation(300, -390);
			attachNode("activity", "activity 33649").setLocation(300, -260);
			attachNode("activity", "activity 33267").setLocation(300, -130);
			attachNode("activity", "activity 33979").setLocation(300, 0);
			attachNode("activity", "activity 33249").setLocation(300, 130);
			attachNode("activity", "activity 33687").setLocation(300, 260);
			attachNode("activity", "activity 33987").setLocation(300, 390);

			NodeEditorPane editor = new NodeEditorPane(mModel);
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


	public static Node attachNode(String aPrototype, String aIdentity)
	{
		Node node = mFactoryMap.get(aPrototype).create(aIdentity);
		mModel.add(node);
		return node;
	}


	@FunctionalInterface
	public interface Factory
	{
		Node create(String aIdentity);
	}
}
