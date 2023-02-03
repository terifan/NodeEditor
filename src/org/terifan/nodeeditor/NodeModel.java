package org.terifan.nodeeditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Stream;
import static org.terifan.util.Assert.*;


public class NodeModel implements Serializable
{
	private final static long serialVersionUID = 1L;

	private ArrayList<Node> mNodes;
	private ArrayList<Connection> mConnections;


	public NodeModel()
	{
		mNodes = new ArrayList<>();
		mConnections = new ArrayList<>();
	}


	public Node getNode(int aIndex)
	{
		return mNodes.get(aIndex);
	}


	public ArrayList<Node> getNodes()
	{
		return mNodes;
	}


	public NodeModel addNode(Node aNode)
	{
		mNodes.add(aNode);
		aNode.bind(this);

		for (Property item : aNode.mItems)
		{
			for (Connector connector : (ArrayList<Connector>)item.mConnectors)
			{
				connector.bind(item);
			}
		}

		return this;
	}


	public Node getNode(String aPath)
	{
		String nodeId = aPath.contains(".") ? aPath.split("\\.")[0] : aPath;

		Node node = null;

		for (Node tmp : mNodes)
		{
			if (nodeId.equals(tmp.getIdentity()))
			{
				node = tmp;
				break;
			}
			else if (tmp.getName().equalsIgnoreCase(nodeId))
			{
				if (node != null)
				{
					throw new IllegalStateException("More than one Node have the same name, provide an Identity to either of them and use identity when connecting items: " + tmp.getName());
				}
				node = tmp;
			}
		}

		if (node == null)
		{
			throw new IllegalArgumentException("Failed to find Node, ensure name or identity is set: " + aPath);
		}

		return node;
	}


	public ArrayList<Connection> getConnections()
	{
		return mConnections;
	}


	public NodeModel addConnection(String aFromPath, String aToPath)
	{
		Property fromNode = getProperty(aFromPath);
		Property toNode = getProperty(aToPath);

		assertNotNull(fromNode, "From path cannot be resolved to a Node: path: %s", aFromPath);
		assertNotNull(toNode, "To path cannot be resolved to a Node: path: %s", aToPath);

		Connector out = fromNode.getConnector(Direction.OUT);
		Connector in = toNode.getConnector(Direction.IN);

		assertNotNull(out, "From path cannot be resolved to a OUT connector: path: %s", aFromPath);
		assertNotNull(in, "To path cannot be resolved to a IN connector: path: %s", aToPath);

		return addConnection(out, in);
	}


	public NodeModel addConnection(Property aFromItem, Property aToItem)
	{
		Connector out = null;
		Connector in = null;

		for (Connector connector : (ArrayList<Connector>)aFromItem.mConnectors)
		{
			if (connector.getDirection() == Direction.OUT)
			{
				out = connector;
			}
		}

		for (Connector connector : (ArrayList<Connector>)aToItem.mConnectors)
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
		assertEquals(aFromConnector.getDirection(), Direction.OUT, "Expected OUT connector");
		assertEquals(aToConnector.getDirection(), Direction.IN, "Expected IN connector");

		mConnections.add(new Connection(aFromConnector, aToConnector));

		return this;
	}


	public Stream<Connection> getConnectionsTo(Property aItem)
	{
		return mConnections.stream().filter(e -> e.getIn().getProperty() == aItem);
	}


	public Stream<Connection> getConnectionsFrom(Property aItem)
	{
		return mConnections.stream().filter(e -> e.getOut().getProperty() == aItem);
	}


	public Stream<Property> getConnectionsTo(Connector aConnector)
	{
		return mConnections.stream().filter(e -> e.getIn() == aConnector).map(e -> e.getIn().getProperty());
	}


	public Stream<Property> getConnectionsFrom(Connector aConnector)
	{
		return mConnections.stream().filter(e -> e.getOut() == aConnector).map(e -> e.getOut().getProperty());
	}


	public Property getProperty(String aPath)
	{
		return getNode(aPath).getProperty(aPath);
	}


	public <T extends Property> T getProperty(Class<T> aReturnType, String aPath)
	{
		return (T)getProperty(aPath);
	}


	private Connector getConnector(int aRef)
	{
		for (Node node : mNodes)
		{
			for (Property item : node.mItems)
			{
				for (Connector connector : (ArrayList<Connector>)item.mConnectors)
				{
					if (connector.mModelRef == aRef)
					{
						return connector;
					}
				}
			}
		}

		return null;
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
