package de.uniheidelberg.cl.a10.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.api.DataStreamProvider;

public class DirectoryBasedDataStreamProvider implements DataStreamProvider {

	List<File> ritualDocumentsDirectories = new LinkedList<File>();

	boolean searchSubDirectories = true;

	public DirectoryBasedDataStreamProvider(
			List<File> ritualDocumentsDirectories, boolean searchSubDirectories) {
		super();
		this.ritualDocumentsDirectories.addAll(ritualDocumentsDirectories);
		this.searchSubDirectories = searchSubDirectories;
	}

	public DirectoryBasedDataStreamProvider(File ritualDocumentsDirectory,
			boolean searchSubDirectories) {
		super();
		this.ritualDocumentsDirectories.add(ritualDocumentsDirectory);
		this.searchSubDirectories = searchSubDirectories;
	}

	@Override
	public InputStream findStreamFor(String objectName) throws IOException {
		return new FileInputStream(this.searchFileForRitualDocument(objectName));
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

	@Override
	public InputStream findStreamFor(String objectName, String type)
			throws IOException {
		return findStreamFor(objectName);
	}

}
