package org.terifan.nodeeditor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;
import org.terifan.bundle.Array;
import org.terifan.bundle.Bundle;


public class NodeModel implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected ArrayList<Node> mNodes;
	protected ArrayList<Connection> mConnections;


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


	public Node addNode(Node aNode)
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


	public void addConnection(String aFromPath, String aToPath)
	{
		Connector out = getNodeItem(aFromPath).getConnector(Direction.OUT);
		Connector in = getNodeItem(aToPath).getConnector(Direction.IN);

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
		mConnections.add(new Connection(aFromConnector, aToConnector));
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


	public byte[] marshal() throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try (ObjectOutputStream dos = new ObjectOutputStream(baos))
		{
			dos.writeObject(this);
		}

		return baos.toByteArray();
	}


	public static NodeModel unmarshal(byte[] aContent) throws IOException
	{
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(aContent)))
		{
			return (NodeModel)ois.readObject();
		}
		catch (ClassNotFoundException e)
		{
			throw new IOException(e);
		}
	}

	private HashMap<String, Factory> mFactoryMap = new HashMap<>();


	public void addFactory(String aPrototype, Factory aFactory)
	{
		mFactoryMap.put(aPrototype, aFactory);
	}


	public Node attachNode(String aPrototype, String aIdentity)
	{
		Node box = mFactoryMap.get(aPrototype).create(aIdentity);
		box.setPrototype(aPrototype);
		return addNode(box);
	}


//	public Bundle marshalBundle()
//	{
//		Bundle bundle = new Bundle();
//
//		bundle.putArray("nodes", Array.of(mNodes));
//		bundle.putArray("connections", Array.of(mConnections));
//
//		return bundle;
//	}
//
//
//	public void unmarshalBundle(Bundle aBundle)
//	{
//		mNodes = new ArrayList<>();
//		mConnections = new ArrayList<>();
//
//		for (Bundle bundle : aBundle.getBundleArray("nodes"))
//		{
//			Node node = new Node();
//			node.bind(this);
//			node.readExternal(bundle);
//			mNodes.add(node);
//		}
//
//		for (Bundle bundle : aBundle.getBundleArray("connections"))
//		{
//			addConnection(getConnector(bundle.getInt("out")), getConnector(bundle.getInt("in")));
//		}
//	}


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


	@FunctionalInterface
	public interface Factory
	{
		Node create(String aIdentity);
	}
}
