package org.terifan.nodeeditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.terifan.boxcomponentpane.BoxComponentModel;
import static org.terifan.util.Assert.*;


public class NodeModel extends BoxComponentModel<Node> implements Serializable
{
	private final static long serialVersionUID = 1L;

	private ArrayList<Connection> mConnections;


	public NodeModel()
	{
		mConnections = new ArrayList<>();
	}


	@Override
	public NodeModel addNode(Node aNode)
	{
		super.addNode(aNode);

		aNode.bind(this);

		for (Property item : aNode.mProperties)
		{
			for (Connector connector : (ArrayList<Connector>)item.getConnectors())
			{
				connector.bind(item);
			}
		}

		return this;
	}


	public ArrayList<Connection> getConnections()
	{
		return mConnections;
	}


	public NodeModel addConnection(int aFromNodeIndex, int aFromPropertyIndex, int aToNodeIndex, int aToPropertyIndex)
	{
		Connector out = getConnector(aFromNodeIndex, aFromPropertyIndex, Direction.OUT);
		Connector in = getConnector(aToNodeIndex, aToPropertyIndex, Direction.IN);

		assertNotNull(out, "The 'From' parameters are not referencing a node/property with direction OUT: " + aFromNodeIndex + ", " + aFromPropertyIndex);
		assertNotNull(in, "The 'To' parameters are not referencing a node/property with direction IN: " + aToNodeIndex + ", " + aToPropertyIndex);

		return addConnection(out, in);
	}


	public NodeModel addConnection(Property aFromItem, Property aToItem)
	{
		Connector out = null;
		Connector in = null;

		for (Connector connector : (ArrayList<Connector>)aFromItem.getConnectors())
		{
			if (connector.getDirection() == Direction.OUT)
			{
				out = connector;
			}
		}

		for (Connector connector : (ArrayList<Connector>)aToItem.getConnectors())
		{
			if (connector.getDirection() == Direction.IN)
			{
				in = connector;
			}
		}

		assertNotNull(out, "The 'FromItem' has no connectors.");
		assertNotNull(in, "The 'ToItem' has no connectors.");

		return addConnection(out, in);
	}


	public NodeModel addConnection(Connector aFromConnector, Connector aToConnector)
	{
		assertNotNull(aFromConnector, "Expected OUT connector");
		assertNotNull(aToConnector, "Expected IN connector");
		assertEquals(aFromConnector.getDirection(), Direction.OUT, "Expected OUT connector");
		assertEquals(aToConnector.getDirection(), Direction.IN, "Expected IN connector");

		mConnections.add(new Connection(aFromConnector, aToConnector));

		return this;
	}


	public List<Connection> getConnectionsTo(Property aProperty)
	{
		return mConnections.stream().filter(e -> e.getIn().getProperty() == aProperty).collect(Collectors.toList());
	}


	public List<Property> getConnectionsTo(Connector aConnector)
	{
		return mConnections.stream().filter(e -> e.getIn() == aConnector).map(e -> e.getOut().getProperty()).collect(Collectors.toList());
	}


	public List<Connection> getConnectionsFrom(Property aProperty)
	{
		return mConnections.stream().filter(e -> e.getOut().getProperty() == aProperty).collect(Collectors.toList());
	}


	public List<Property> getConnectionsFrom(Connector aConnector)
	{
		return mConnections.stream().filter(e -> e.getOut() == aConnector).map(e -> e.getIn().getProperty()).collect(Collectors.toList());
	}


	public Connector getConnector(int aNodeIndex, int aConnectorIndex, Direction aDirection)
	{
		return getComponents().get(aNodeIndex).getProperties().get(aConnectorIndex).getConnector(aDirection);
	}


	public ArrayList<Node> getConnectedNodes(Node aNode)
	{
		ArrayList<Node> result = new ArrayList<>();

		for (Connection conn : mConnections)
		{
			if (conn.getOut().getProperty().getNode() == aNode)
			{
				result.add(conn.getIn().getProperty().getNode());
			}
		}

		return result;
	}


	public <T extends Property> T getProperty(String aBindId)
	{
		for (int i = 0; i < size(); i++)
		{
			Node node = getNode(i);
			for (Property p : node.getProperties())
			{
				if (aBindId.equals(p.getModelId()))
				{
					return (T)p;
				}
			}
		}
		return null;
	}


	@Override
	public String toString()
	{
		return "NodeModel{" + "mConnections=" + mConnections + '}';
	}


	public void print()
	{
		System.out.println("NodeModel model = new NodeModel()");
		for (int i = 0; i < size(); i++)
		{
			Node node = getNode(i);

			System.out.println("\t.addNode(new Node(\"" + node.getTitle() + "\")");
			System.out.println("\t\t.setTitleForeground(" + "new Color(0x" + ("%06X".formatted(0xffffffL & node.getTitleForeground().getRGB())) + ")");
			System.out.println("\t\t.setTitleBackground(" + "new Color(0x" + ("%06X".formatted(0xffffffL & node.getTitleBackground().getRGB())) + ")");
			System.out.print("\t\t.setBounds(" + node.getBounds().x + "," + node.getBounds().y + "," + node.getBounds().width + "," + node.getBounds().height + ")");
			for (Property p : node.getProperties())
			{
				System.out.println();
				System.out.print("\t\t.addProperty(new " + p.getClass().getSimpleName() + "(\"" + p.getText() + "\")");
				ArrayList<Connector> connectors = p.getConnectors();
				for (Connector c : connectors)
				{
					System.out.print(".addConnector(" + c.getDirection() + ", new Color(0x" + ("%06X".formatted(0xffffffL & c.getColor().getRGB())) + "))");
				}
				if (p.getProducer() != null)
				{
					System.out.print(".setProducer(\"" + p.getProducer() + "\")");
				}
				if (p.getId() != null)
				{
					System.out.print(".setId(\"" + p.getId() + "\")");
				}
				if (p.getModelId() != null)
				{
					System.out.print(".bind(\"" + p.getModelId() + "\")");
				}
			}
			System.out.println();
			System.out.print("\t)");
			System.out.println();
		}
		for (int i = 0; i < mConnections.size(); i++)
		{
			Connection c = mConnections.get(i);
			int n0 = mComponents.indexOf(c.getOut().getProperty().getNode());
			int c0 = c.getOut().mProperty.getNode().getProperties().indexOf(c.getOut().getProperty());
			int n1 = mComponents.indexOf(c.getIn().getProperty().getNode());
			int c1 = c.getIn().mProperty.getNode().getProperties().indexOf(c.getIn().getProperty());

			if(i>0)System.out.println();
			System.out.print("\t.addConnection("+n0+","+c0+","+n1+","+c1+")");
		}
		System.out.println(";");
	}
}
