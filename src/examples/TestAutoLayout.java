package examples;

import javax.swing.JFrame;
import org.terifan.nodeeditor.util.AutoLayout;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import org.terifan.nodeeditor.widgets.TextProperty;


public class TestAutoLayout
{
	public static void main(String... args)
	{
		try
		{
			NodeModel model = new NodeModel();

			model.add(new Node("0",
				new TextProperty("1").addConnector(Direction.OUT),
				new TextProperty("2").addConnector(Direction.OUT),
				new TextProperty("3").addConnector(Direction.OUT),
				new TextProperty("4").addConnector(Direction.OUT)
			).setLocation(-300, 0));

			model.add(new Node("1",
				new TextProperty("1").addConnector(Direction.IN),
				new TextProperty("5").addConnector(Direction.OUT),
				new TextProperty("6").addConnector(Direction.OUT)
			).setLocation(0, -150));

			model.add(new Node("2",
				new TextProperty("2").addConnector(Direction.IN)
			).setLocation(0, -50));

			model.add(new Node("3",
				new TextProperty("3").addConnector(Direction.IN),
				new TextProperty("7").addConnector(Direction.OUT),
				new TextProperty("8").addConnector(Direction.OUT),
				new TextProperty("9").addConnector(Direction.OUT)
			).setLocation(0, 50));

			model.add(new Node("4",
				new TextProperty("4").addConnector(Direction.IN),
				new TextProperty("10").addConnector(Direction.OUT),
				new TextProperty("11").addConnector(Direction.OUT)
			).setLocation(0, 150));

			model.add(new Node("5",
				new TextProperty("5").addConnector(Direction.IN),
				new TextProperty("14").addConnector(Direction.OUT)
			).setLocation(300, 0));

			model.add(new Node("6",
				new TextProperty("6").addConnector(Direction.IN),
				new TextProperty("12").addConnector(Direction.OUT),
				new TextProperty("13").addConnector(Direction.OUT)
			).setLocation(300, 0));

			model.add(new Node("7",
				new TextProperty("7").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.add(new Node("8",
				new TextProperty("8").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.add(new Node("9",
				new TextProperty("9").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.add(new Node("10",
				new TextProperty("10").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.add(new Node("11",
				new TextProperty("11").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.add(new Node("12",
				new TextProperty("12").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.add(new Node("13",
				new TextProperty("13").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.add(new Node("14",
				new TextProperty("14").addConnector(Direction.IN),
				new TextProperty("15").addConnector(Direction.OUT),
				new TextProperty("16").addConnector(Direction.OUT),
				new TextProperty("17").addConnector(Direction.OUT),
				new TextProperty("18").addConnector(Direction.OUT),
				new TextProperty("19").addConnector(Direction.OUT)
			).setLocation(300, 0));

			model.add(new Node("15",
				new TextProperty("15").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.add(new Node("19",
				new TextProperty("19").addConnector(Direction.IN)
			).setLocation(300, 0));

			model.addConnection("0.1", "1.1");
			model.addConnection("0.2", "2.2");
			model.addConnection("0.3", "3.3");
			model.addConnection("0.4", "4.4");
			model.addConnection("1.5", "5.5");
			model.addConnection("1.6", "6.6");
			model.addConnection("3.7", "7.7");
			model.addConnection("3.8", "8.8");
			model.addConnection("3.9", "9.9");
			model.addConnection("4.10", "10.10");
			model.addConnection("4.11", "11.11");
			model.addConnection("6.12", "12.12");
			model.addConnection("6.13", "13.13");
			model.addConnection("5.14", "14.14");
			model.addConnection("14.15", "15.15");
			model.addConnection("14.19", "19.19");

			new AutoLayout().layout(model, model.get(0));

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
