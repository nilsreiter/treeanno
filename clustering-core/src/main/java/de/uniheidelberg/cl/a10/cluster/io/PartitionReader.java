package de.uniheidelberg.cl.a10.cluster.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import nu.xom.Element;
import nu.xom.Elements;
import de.uniheidelberg.cl.a10.cluster.ICluster;
import de.uniheidelberg.cl.a10.cluster.IFullPartition;
import de.uniheidelberg.cl.a10.cluster.IPartialPartition;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.cluster.impl.Cluster_impl;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.io.AbstractLinkedXMLReader;
import de.uniheidelberg.cl.a10.io.XMLConstants;

public class PartitionReader extends
		AbstractLinkedXMLReader<IPartition<Document>> {

	boolean readFullPartition = false;

	abstract class Partition<D> implements IPartition<D> {
		ArrayList<ICluster<D>> set = new ArrayList<ICluster<D>>();

		public void addCluster(final ICluster<D> cl) {
			set.add(cl);
		}

		@Override
		public ICluster<D> getCluster(final D object) {
			for (ICluster<D> cl : set) {
				if (cl.contains(object))
					return cl;
			}
			return null;
		}

		@Override
		public Collection<ICluster<D>> getClusters() {
			return set;
		}

		@Override
		public Collection<D> getObjects() {
			Set<D> coll = new HashSet<D>();
			for (ICluster<D> cl : set) {
				coll.addAll(cl);
			}
			return coll;
		}

		@Override
		public int size() {
			return set.size();
		}

		@Override
		public boolean together(final D o1, final D o2) {
			return getCluster(o1) == getCluster(o2) && getCluster(o1) != null;
		}
	}

	class PartialPartition<D> extends Partition<D> implements
			IPartialPartition<D> {

	}

	class FullPartition<D> extends PartialPartition<D> implements
			IFullPartition<D> {

	}

	public PartitionReader(final File dDirectory) {
		super(dDirectory);
	}

	@Override
	protected IPartition<Document> read(final Element rootElement) {
		String ptype = rootElement.getAttributeValue("type");
		if (ptype == null)
			ptype = "partial";
		if (this.isReadFullPartition() || ptype.equalsIgnoreCase("full")) {
			FullPartition<Document> p = new FullPartition<Document>();
			Elements chElem = rootElement.getChildElements("cluster");
			for (int i = 0; i < chElem.size(); i++) {
				Element clusterElement = chElem.get(i);
				p.addCluster(this.getCluster(clusterElement));
			}
			return p;
		} else if (ptype.equalsIgnoreCase("partial")) {
			PartialPartition<Document> p = new PartialPartition<Document>();
			Elements chElem = rootElement.getChildElements("cluster");
			for (int i = 0; i < chElem.size(); i++) {
				Element clusterElement = chElem.get(i);
				p.addCluster(this.getCluster(clusterElement));
			}
			return p;
		}
		return null;
	}

	protected ICluster<Document> getCluster(final Element clusterElement) {
		Cluster_impl<Document> cl = new Cluster_impl<Document>();
		cl.setId(clusterElement.getAttributeValue(XMLConstants.ID));
		Elements chElem = clusterElement.getChildElements("member");
		for (int i = 0; i < chElem.size(); i++) {
			Element memberElement = chElem.get(i);
			String id = memberElement.getAttributeValue(XMLConstants.ID);
			try {
				cl.add(this.getRitualDocument(id));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cl;
	}

	public boolean isReadFullPartition() {
		return readFullPartition;
	}

	public void setReadFullPartition(final boolean readFullPartition) {
		this.readFullPartition = readFullPartition;
	}
}
