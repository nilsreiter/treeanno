package de.uniheidelberg.cl.a10.cluster.io;

import java.io.OutputStream;

import nu.xom.Attribute;
import nu.xom.Element;
import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.cluster.ICluster;
import de.uniheidelberg.cl.a10.cluster.IFullPartition;
import de.uniheidelberg.cl.a10.cluster.IPartialPartition;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.io.AbstractXMLWriter;
import de.uniheidelberg.cl.a10.io.XMLConstants;

public class PartitionWriter extends
		AbstractXMLWriter<IPartition<? extends HasId>> {

	public PartitionWriter(final OutputStream os) {
		super(os);
	}

	@Override
	public Element getElement(final IPartition<? extends HasId> partition) {
		Element rootElement = new Element("partition");
		if (partition instanceof IFullPartition) {
			rootElement.appendChild(new Attribute(XMLConstants.TYPE,
					XMLConstants.FULL));
		} else if (partition instanceof IPartialPartition) {
			rootElement.appendChild(new Attribute(XMLConstants.TYPE,
					XMLConstants.PARTIAL));
		}

		for (ICluster<? extends HasId> cluster : partition.getClusters()) {
			Element clusterElement = new Element("cluster");
			if (cluster.getId() != null) {
				clusterElement.appendChild(new Attribute(XMLConstants.ID,
						cluster.getId()));
			}
			for (HasId member : cluster) {
				Element memberElement = new Element("member");
				memberElement.appendChild(new Attribute(XMLConstants.ID, member
						.getId()));
				clusterElement.appendChild(memberElement);
			}

			rootElement.appendChild(clusterElement);
		}

		return rootElement;
	}

}
