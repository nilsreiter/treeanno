package de.nilsreiter.ocr.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.pdfbox.io.IOUtils;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class WordList extends HashSet<String> implements Set<String>,
SharedResourceObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void load(DataResource dataResource)
			throws ResourceInitializationException {
		InputStream in = null;
		BufferedReader buf = null;
		try {
			in = dataResource.getInputStream();

			InputStreamReader inR = new InputStreamReader(in);
			buf = new BufferedReader(inR);
			String line;
			while ((line = buf.readLine()) != null) {
				this.add(line.trim());
			}
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

}
