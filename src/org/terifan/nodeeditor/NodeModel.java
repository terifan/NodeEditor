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

		assertNotNull(out, "The 'From' parameters are not referencing a node/property with direction OUT.");
		assertNotNull(in, "The 'To' parameters are not referencing a node/property with direction IN.");

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


	public List<Connection> getConnectionsTo(Property aItem)
	{
		return mConnections.stream().filter(e -> e.getIn().getProperty() == aItem).collect(Collectors.toList());
	}


	public List<Connection> getConnectionsFrom(Property aItem)
	{
		return mConnections.stream().filter(e -> e.getOut().getProperty() == aItem).collect(Collectors.toList());
	}


	public List<Property> getConnectionsTo(Connector aConnector)
	{
		return mConnections.stream().filter(e -> e.getIn() == aConnector).map(e -> e.getIn().getProperty()).collect(Collectors.toList());
	}


	public List<Property> getConnectionsFrom(Connector aConnector)
	{
		return mConnections.stream().filter(e -> e.getOut() == aConnector).map(e -> e.getOut().getProperty()).collect(Collectors.toList());
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
}
