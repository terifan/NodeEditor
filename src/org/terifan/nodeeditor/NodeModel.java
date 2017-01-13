package org.terifan.nodeeditor;

import java.util.ArrayList;
import java.util.stream.Stream;


public class NodeModel
{
	protected ArrayList<Node> mNodes;
	protected ArrayList<Connection> mConnections;


	public NodeModel()
	{
		mNodes = new ArrayList<>();
		mConnections = new ArrayList<>();
	}


	public ArrayList<Node> getNodes()
	{
		return mNodes;
	}



	public ArrayList<Connection> getConnections()
	{
		return mConnections;
	}


	public Node add(Node aNode)
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
		return aNode;
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
					throw new IllegalStateException("More than one Node have the same name, provide an Identity to either of them: " + b.getName());
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


	public NodeItem getItem(String aPath)
	{
		return getNode(aPath).getItem(aPath);
	}


	public void addConnection(String aFromPath, String aToPath)
	{
		Connector out = getItem(aFromPath).getConnectors(Direction.OUT).findFirst().get();
		Connector in = getItem(aToPath).getConnectors(Direction.IN).findFirst().get();
		addConnection(out, in);
	}


	public void addConnection(NodeItem aFromItem, NodeItem aToItem)
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
		addConnection(out, in);
	}


	public void addConnection(Connector aFromConnector, Connector aToConnector)
	{
		if (aToConnector.getDirection() != Direction.IN)
		{
			throw new IllegalArgumentException("Expected in connector, found: " + aToConnector.getDirection());
		}
		if (aFromConnector.getDirection() != Direction.OUT)
		{
			throw new IllegalArgumentException("Expected out connector, found: " + aFromConnector.getDirection());
		}
		mConnections.add(new Connection(aFromConnector, aToConnector));
	}


	public Stream<Connection> getConnectionsTo(NodeItem aItem)
	{
		return mConnections.stream().filter((Connection e) -> e.getOut().getNodeItem() == aItem);
	}


	public Stream<Connection> getConnectionsFrom(NodeItem aItem)
	{
		return mConnections.stream().filter((Connection e) -> e.getIn().getNodeItem() == aItem);
	}


	public Stream<NodeItem> getConnectionsTo(Connector aConnector)
	{
		return mConnections.stream().filter((Connection e) -> e.getOut() == aConnector).map((Connection e) -> e.getOut().getNodeItem());
	}


	public Stream<NodeItem> getConnectionsFrom(Connector aConnector)
	{
		return mConnections.stream().filter((Connection e) -> e.getIn() == aConnector).map((Connection e) -> e.getIn().getNodeItem());
	}
}
