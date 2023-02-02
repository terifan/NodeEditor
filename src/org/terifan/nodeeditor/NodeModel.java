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

		for (NodeItem item : aNode.mItems)
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
		NodeItem fromNode = getNodeItem(aFromPath);
		NodeItem toNode = getNodeItem(aToPath);

		assertNotNull(fromNode, "From path cannot be resolved to a Node: path: %s", aFromPath);
		assertNotNull(toNode, "To path cannot be resolved to a Node: path: %s", aToPath);

		Connector out = fromNode.getConnector(Direction.OUT);
		Connector in = toNode.getConnector(Direction.IN);

		assertNotNull(out, "From path cannot be resolved to a OUT connector: path: %s", aFromPath);
		assertNotNull(out, "To path cannot be resolved to a IN connector: path: %s", aToPath);

		return addConnection(out, in);
	}


	public NodeModel addConnection(NodeItem aFromItem, NodeItem aToItem)
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

		if (out == null)
		{
			throw new IllegalArgumentException("The 'FromItem' has no connectors.");
		}
		if (in == null)
		{
			throw new IllegalArgumentException("The 'ToItem' has no connectors.");
		}

		return addConnection(out, in);
	}


	public NodeModel addConnection(Connector aFromConnector, Connector aToConnector)
	{
		mConnections.add(new Connection(aFromConnector, aToConnector));

		return this;
	}


	public Stream<Connection> getConnectionsTo(NodeItem aItem)
	{
		return mConnections.stream().filter(e -> e.getIn().getNodeItem() == aItem);
	}


	public Stream<Connection> getConnectionsFrom(NodeItem aItem)
	{
		return mConnections.stream().filter(e -> e.getOut().getNodeItem() == aItem);
	}


	public Stream<NodeItem> getConnectionsTo(Connector aConnector)
	{
		return mConnections.stream().filter(e -> e.getIn() == aConnector).map(e -> e.getIn().getNodeItem());
	}


	public Stream<NodeItem> getConnectionsFrom(Connector aConnector)
	{
		return mConnections.stream().filter(e -> e.getOut() == aConnector).map(e -> e.getOut().getNodeItem());
	}


	public NodeItem getNodeItem(String aPath)
	{
		return getNode(aPath).getItem(aPath);
	}


	public <T extends NodeItem> T getNodeItem(Class<T> aReturnType, String aPath)
	{
		return (T)getNodeItem(aPath);
	}


	private Connector getConnector(int aRef)
	{
		for (Node node : mNodes)
		{
			for (NodeItem item : node.mItems)
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
			if (conn.getOut().getNodeItem().getNode() == aParent)
			{
				result.add(conn.getIn().getNodeItem().getNode());
			}
		}

		return result;
	}
}
