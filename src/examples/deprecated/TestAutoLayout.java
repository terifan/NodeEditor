package examples.deprecated;

import javax.swing.JFrame;
import org.terifan.nodeeditor.util.AutoLayout;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.ValueProperty;


public class TestAutoLayout
{
	public static void main(String... args)
	{
		try
		{
			NodeModel model = new NodeModel();

			model.addNode(new Node("0",
				new ValueProperty("1").addConnector(Direction.OUT),
				new ValueProperty("2").addConnector(Direction.OUT),
				new ValueProperty("3").addConnector(Direction.OUT),
				new ValueProperty("4").addConnector(Direction.OUT)
			).setLocation(-300, 0));

			model.addNode(new Node("1",
				new ValueProperty("1").addConnector(Direction.IN),
				new ValueProperty("5").addConnector(Direction.OUT),
				new ValueProperty("6").addConnector(Direction.OUT)
			).setLocation(0, -150));

			model.addNode(new Node("2",
				new ValueProperty("2").addConnector(Direction.IN)
			).setLocation(0, -50));

			model.addNode(new Node("3",
				new ValueProperty("3").addConnector(Direction.IN),
				new ValueProperty("7").addConnector(Direction.OUT),
				new ValueProperty("8").addConnector(Direction.OUT),
				new ValueProperty("9").addConnector(Direction.OUT)
			).setLocation(0, 50));

			model.addNode(new Node("4",
				new ValueProperty("4").addConnector(Direction.IN),
				new ValueProperty("10").addConnector(Direction.OUT),
				new ValueProperty("11").addConnector(Direction.OUT)
			).setLocation(0, 150));

			model.addNode(new Node("5",
				new ValueProperty("5").addConnector(Direction.IN),
				new ValueProperty("14").addConnector(Direction.OUT)
			).setLocation(300, 0));

			model.addNode(new Node("6",
				new ValueProperty("6").addConnector(Direction.IN),
				new ValueProperty("12").addConnector(Direction.OUT),
				new ValueProperty("13").addConnector(Direction.OUT)
			).setLocation(300, 0));

			model.addNode(new Node("7",
				new ValueProperty("7").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.addNode(new Node("8",
				new ValueProperty("8").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.addNode(new Node("9",
				new ValueProperty("9").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.addNode(new Node("10",
				new ValueProperty("10").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.addNode(new Node("11",
				new ValueProperty("11").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.addNode(new Node("12",
				new ValueProperty("12").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.addNode(new Node("13",
				new ValueProperty("13").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.addNode(new Node("14",
				new ValueProperty("14").addConnector(Direction.IN),
				new ValueProperty("15").addConnector(Direction.OUT),
				new ValueProperty("16").addConnector(Direction.OUT),
				new ValueProperty("17").addConnector(Direction.OUT),
				new ValueProperty("18").addConnector(Direction.OUT),
				new ValueProperty("19").addConnector(Direction.OUT)
			).setLocation(300, 0));

			model.addNode(new Node("15",
				new ValueProperty("15").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.addNode(new Node("19",
				new ValueProperty("19").addConnector(Direction.IN)
			).setLocation(300, 0));

//			model.addConnection("0.1", "1.1");
//			model.addConnection("0.2", "2.2");
//			model.addConnection("0.3", "3.3");
//			model.addConnection("0.4", "4.4");
//			model.addConnection("1.5", "5.5");
//			model.addConnection("1.6", "6.6");
//			model.addConnection("3.7", "7.7");
//			model.addConnection("3.8", "8.8");
//			model.addConnection("3.9", "9.9");
//			model.addConnection("4.10", "10.10");
//			model.addConnection("4.11", "11.11");
//			model.addConnection("6.12", "12.12");
//			model.addConnection("6.13", "13.13");
//			model.addConnection("5.14", "14.14");
//			model.addConnection("14.15", "15.15");
//			model.addConnection("14.19", "19.19");

			new AutoLayout().layout(model, model.getNode(0));

			NodeEditorPane editor = new NodeEditorPane(model);
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
