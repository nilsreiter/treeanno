package de.uniheidelberg.cl.a10.cluster.io;

import java.io.OutputStream;
import java.util.List;

import nu.xom.Element;
import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.io.AbstractXMLWriter;

public class PartitionListWriter extends
		AbstractXMLWriter<List<? extends IPartition<? extends HasId>>> {

	PartitionWriter pw = null;

	public PartitionListWriter(final OutputStream os) {
		super(os);
		pw = new PartitionWriter(null);
	}

	@Override
	public Element getElement(
			final List<? extends IPartition<? extends HasId>> object) {
		Element rootElement = new Element("partitionlist");
		for (IPartition<? extends HasId> partition : object) {
			rootElement.appendChild(pw.getElement(partition));
		}
		return rootElement;
	}

}
