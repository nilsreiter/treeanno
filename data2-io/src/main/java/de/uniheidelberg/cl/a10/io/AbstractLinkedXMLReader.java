package de.uniheidelberg.cl.a10.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public abstract class AbstractLinkedXMLReader<T> extends AbstractXMLReader<T> {

	DataStreamProvider dsProvider;

	DataReader dr = null;

	@Deprecated
	List<File> ritualDocumentsDirectories = new LinkedList<File>();

	@Deprecated
	boolean searchSubDirectories = true;

	@Deprecated
	public AbstractLinkedXMLReader(final DataReader dr,
			final File ritualDocumentsDirectory) {
		super();
		this.dr = dr;
		this.ritualDocumentsDirectories.add(ritualDocumentsDirectory);
	}

	public AbstractLinkedXMLReader(DataStreamProvider dsProvider) {
		super();
		this.dr = new DataReader();
		this.dsProvider = dsProvider;

	}

	public AbstractLinkedXMLReader(final File dDirectory) {
		dr = new DataReader();
		dsProvider = new DirectoryBasedDataStreamProvider(dDirectory, true);
	}

	protected de.uniheidelberg.cl.a10.data2.Document getRitualDocument(
			final String id) throws FileNotFoundException, IOException,
			DocumentNotFoundException {
		return dr.read(this.dsProvider.findStreamFor(id));
	}

}
