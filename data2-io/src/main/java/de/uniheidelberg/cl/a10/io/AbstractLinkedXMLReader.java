package de.uniheidelberg.cl.a10.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.data2.io.DataReader;

public abstract class AbstractLinkedXMLReader<T> extends AbstractXMLReader<T> {

	public AbstractLinkedXMLReader(final DataReader dr,
			final File ritualDocumentsDirectory) {
		super();
		this.dr = dr;
		this.ritualDocumentsDirectories.add(ritualDocumentsDirectory);
	}

	DataReader dr = null;

	List<File> ritualDocumentsDirectories = new LinkedList<File>();

	boolean searchSubDirectories = true;

	public AbstractLinkedXMLReader(final File dDirectory) {
		dr = new DataReader();
		ritualDocumentsDirectories.add(dDirectory);
	}

	protected de.uniheidelberg.cl.a10.data2.Document getRitualDocument(
			final String id) throws FileNotFoundException, IOException {
		return dr.read(this.searchFileForRitualDocument(id + ".xml"));
	}

	protected File searchFileForRitualDocument(final String name)
			throws FileNotFoundException {
		if (this.searchSubDirectories) {
			for (File dir : this.ritualDocumentsDirectories) {
				try {
					return this.searchFileForRitualDocument(dir, name);
				} catch (FileNotFoundException e) {
					// silently catching
				}
			}
		} else {
			for (File dir : this.ritualDocumentsDirectories) {
				File f = new File(dir, name);
				if (f.exists())
					return f;
			}
		}
		throw new FileNotFoundException(name);
	}

	protected File searchFileForRitualDocument(final File directory,
			final String name) throws FileNotFoundException {
		if (new File(directory, name).exists())
			return new File(directory, name);
		else
			for (File f : directory.listFiles()) {
				if (f.isDirectory())
					try {
						return this.searchFileForRitualDocument(f, name);
					} catch (FileNotFoundException e) {
						// silently catching
					}
			}
		throw new FileNotFoundException(name);
	}

	public void addDocumentsDirectory(final File directory) {
		this.ritualDocumentsDirectories.add(directory);
	}

}
