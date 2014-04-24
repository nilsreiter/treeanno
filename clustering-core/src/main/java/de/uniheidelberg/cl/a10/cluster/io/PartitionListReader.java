package de.uniheidelberg.cl.a10.cluster.io;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.io.AbstractLinkedXMLReader;

public class PartitionListReader extends
		AbstractLinkedXMLReader<List<IPartition<Document>>> {

	PartitionReader pr = null;

	public PartitionListReader(final File ritualDocumentsDirectory) {
		super(ritualDocumentsDirectory);
		pr = new PartitionReader(ritualDocumentsDirectory);
	}

	@Override
	protected List<IPartition<Document>> read(final Element rootElement)
			throws IOException {
		List<IPartition<Document>> partitionList = new LinkedList<IPartition<Document>>();
		Elements chElem = rootElement.getChildElements("partition");
		for (int i = 0; i < chElem.size(); i++) {
			partitionList.add(pr.read(chElem.get(i)));
		}
		return partitionList;
	}

}
