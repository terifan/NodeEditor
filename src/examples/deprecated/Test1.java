package examples.deprecated;

import org.terifan.nodeeditor.widgets.SliderProperty;
import org.terifan.nodeeditor.widgets.ImageProperty;
import org.terifan.nodeeditor.widgets.ComboBoxProperty;
import org.terifan.nodeeditor.widgets.CheckBoxProperty;
import org.terifan.nodeeditor.widgets.ColorChooserProperty;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import org.terifan.nodeeditor.Connector;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.NodeModel;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.GRAY;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.PURPLE;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.YELLOW;
import org.terifan.nodeeditor.widgets.ValueProperty;


public class Test1
{
	public static void main(String ... args)
	{
		try
		{
			ValueProperty item0 = new ValueProperty("node0").addConnector(new Connector(Direction.OUT));
			ValueProperty item1 = new ValueProperty("node1").addConnector(new Connector(Direction.OUT));
			ValueProperty item2 = new ValueProperty("node2").addConnector(new Connector(Direction.IN));
			ValueProperty item3 = new ValueProperty("node3").addConnector(new Connector(Direction.OUT));
			ValueProperty item4 = new ValueProperty("node4").addConnector(new Connector(Direction.IN));
			ValueProperty item5 = new ValueProperty("node5").addConnector(new Connector(Direction.IN));
			ValueProperty item6 = new ValueProperty("node6").addConnector(new Connector(Direction.OUT));
			ValueProperty item7 = new ValueProperty("node7").addConnector(new Connector(Direction.IN)).addConnector(new Connector(Direction.OUT));
			ValueProperty item8 = new ValueProperty("node8").addConnector(new Connector(Direction.OUT));
			ValueProperty item9 = new ValueProperty("node9").addConnector(new Connector(Direction.OUT, PURPLE));
			ImageProperty item10 = new ImageProperty("node10", 200, 200).addConnector(new Connector(Direction.OUT, PURPLE)).addConnector(new Connector(Direction.OUT, PURPLE)).addConnector(new Connector(Direction.OUT, PURPLE));
			ColorChooserProperty item11 = new ColorChooserProperty("node11", new Color(255,255,255)).addConnector(new Connector(Direction.IN, YELLOW));
			ValueProperty item12 = new ValueProperty("node12").addConnector(new Connector(Direction.IN, PURPLE));
			ValueProperty item13 = new ValueProperty("node13").addConnector(new Connector(Direction.IN, YELLOW));
			ValueProperty item14 = new ValueProperty("node14").addConnector(new Connector(Direction.IN, GRAY));
			ValueProperty item15 = new ValueProperty("node15").addConnector(new Connector(Direction.IN, GRAY));
			ValueProperty item16 = new ValueProperty("node16").addConnector(new Connector(Direction.IN, GRAY));
			ValueProperty item17 = new ValueProperty("node17").addConnector(new Connector(Direction.IN, GRAY));
			ValueProperty item18 = new ValueProperty("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
//			item18.getTextBox().setMaxWidth(300);
			SliderProperty slider1 = new SliderProperty("Roughness", 0, 1, 0);
			SliderProperty slider2 = new SliderProperty("Roughness", 0, 1, 0.5);
			SliderProperty slider3 = new SliderProperty("Roughness", 0, 1, 0.75);
			SliderProperty slider4 = new SliderProperty("Roughness", 0.0, 0.1);
			CheckBoxProperty checkbox1 = new CheckBoxProperty("Inverted", false);
			CheckBoxProperty checkbox2 = new CheckBoxProperty("Inverted", true);
			ComboBoxProperty comboBoxNodeItem1 = new ComboBoxProperty("A", 0, "GCX", "Beckmann");
			ComboBoxProperty comboBoxNodeItem2 = new ComboBoxProperty("B", 1, "Alpha", "Beta");
			Node node0 = new Node("nodeBox0", item0, item1, comboBoxNodeItem1, comboBoxNodeItem2);
			Node node1 = new Node("nodeBox1", item2, slider1, slider2, slider3, slider4, checkbox1, checkbox2, item3);
			Node node2 = new Node("nodeBox2", item4, item5, item10, item6);
			Node node3 = new Node("nodeBox3", item7);
			Node node4 = new Node("nodeBox4", item13, item8, item9, item18, item15, item16, item17);
			Node node5 = new Node("nodeBox5", item11, item12, item14);
			node2.setMinSize(new Dimension(230,0));

			NodeModel model = new NodeModel();

			model.addComponent(node0);
			model.addComponent(node1);
			model.addComponent(node2);
			model.addComponent(node3);
			model.addComponent(node4);
			model.addComponent(node5);
			model.addConnection(item0, item2);
			model.addConnection(item1, item7);
			model.addConnection(item3, item4);
			model.addConnection(item9, item12);
			model.addConnection(item6, item7);
			model.addConnection(item7, item11);
			model.addConnection(item8, item5);

			NodeEditorPane editor = new NodeEditorPane(model);

			node0.setLocation(100, 50);
			node1.setLocation(300, 0);
			node2.setLocation(500, 150);
			node3.setLocation(800, 170);
			node4.setLocation(100, 250);
			node5.setLocation(1000, 200);
			editor.center();
			editor.setScale(1);

			JFrame frame = new JFrame();
			frame.add(editor);
			frame.setSize((int)(editor.getScale()*1600), (int)(editor.getScale()*1000));
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
