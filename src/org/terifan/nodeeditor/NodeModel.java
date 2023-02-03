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


	public ArrayList<Connection> getConnections()
	{
		return mConnections;
	}


	public NodeModel addNode(Node aNode)
	{
		mNodes.add(aNode);

		aNode.bind(this);

		for (PropertyItem item : aNode.mItems)
		{
			for (Connector connector : item.mConnectors)
			{
				connector.bind(item);
			}
		}

		return this;
	}


	public Node getNode(String aPath)
	{
		String boxId = aPath.contains(".") ? aPath.split("\\.")[0] : aPath;

		Node box = null;

		for (Node b : mNodes)
		{
			if (b.getIdentity() != null && b.getIdentity().equals(boxId))
			{
				box = b;
				break;
			}
			else if (b.getName().equalsIgnoreCase(boxId))
			{
				if (box != null)
				{
					throw new IllegalStateException("More than one Node have the same name, provide an Identity to either of them and use identity when connecting items: " + b.getName());
				}
				box = b;
			}
		}

		if (box == null)
		{
			throw new IllegalArgumentException("Failed to find Node, ensure name or identity is set: " + aPath);
		}

		return box;
	}


	public NodeModel addConnection(String aFromPath, String aToPath)
	{
		PropertyItem fromNode = getNodeItem(aFromPath);
		PropertyItem toNode = getNodeItem(aToPath);

		assertNotNull(fromNode, "From path cannot be resolved to a Node: path: %s", aFromPath);
		assertNotNull(toNode, "To path cannot be resolved to a Node: path: %s", aToPath);

		Connector out = fromNode.getConnector(Direction.OUT);
		Connector in = toNode.getConnector(Direction.IN);

		assertNotNull(out, "From path cannot be resolved to a OUT connector: path: %s", aFromPath);
		assertNotNull(out, "To path cannot be resolved to a IN connector: path: %s", aToPath);

		return addConnection(out, in);
	}


	public NodeModel addConnection(PropertyItem aFromItem, PropertyItem aToItem)
	{
		Connector out = null;
		Connector in = null;

		for (Connector connector : aFromItem.mConnectors)
		{
			if (connector.getDirection() == Direction.OUT)
			{
				out = connector;
			}
		}

		for (Connector connector : aToItem.mConnectors)
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
		mConnections.add(new Connection(aFromConnector, aToConnector));

		return this;
	}


	public Stream<Connection> getConnectionsTo(PropertyItem aItem)
	{
		return mConnections.stream().filter(e -> e.getIn().getPropertyItem() == aItem);
	}


	public Stream<Connection> getConnectionsFrom(PropertyItem aItem)
	{
		return mConnections.stream().filter(e -> e.getOut().getPropertyItem() == aItem);
	}


	public Stream<PropertyItem> getConnectionsTo(Connector aConnector)
	{
		return mConnections.stream().filter(e -> e.getIn() == aConnector).map(e -> e.getIn().getPropertyItem());
	}


	public Stream<PropertyItem> getConnectionsFrom(Connector aConnector)
	{
		return mConnections.stream().filter(e -> e.getOut() == aConnector).map(e -> e.getOut().getPropertyItem());
	}


	public PropertyItem getNodeItem(String aPath)
	{
		return getNode(aPath).getProperty(aPath);
	}


	public <T extends PropertyItem> T getNodeItem(Class<T> aReturnType, String aPath)
	{
		return (T)getNodeItem(aPath);
	}


	private Connector getConnector(int aRef)
	{
		for (Node node : mNodes)
		{
			for (PropertyItem item : node.mItems)
			{
				for (Connector connector : item.mConnectors)
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


	public ArrayList<Node> getChildNodes(Node aParent)
	{
		ArrayList<Node> result = new ArrayList<>();

		for (Connection conn : mConnections)
		{
			if (conn.getOut().getPropertyItem().getNode() == aParent)
			{
				result.add(conn.getIn().getPropertyItem().getNode());
			}
		}

		return result;
	}
}
