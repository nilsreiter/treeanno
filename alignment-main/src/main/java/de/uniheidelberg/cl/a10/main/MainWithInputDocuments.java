package de.uniheidelberg.cl.a10.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.kohsuke.args4j.Argument;

import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public abstract class MainWithInputDocuments extends Main {
	@Argument(usage = "A collection of files in data2-XML format", required = true)
	List<File> arguments = new ArrayList<File>();

	List<Document> documents = null;

	public List<Document> getDocuments() throws IOException {
		if (documents == null) {
			documents = new LinkedList<Document>();
			DataReader dr = new DataReader();
			for (File s : this.getArguments()) {
				Document d = dr.read(s);
				documents.add(d);
			}
		}
		return documents;
	}

	public List<File> getArguments() {
		return arguments;
	}

	public void setArguments(final List<File> arguments) {
		this.arguments = arguments;
	}
}
