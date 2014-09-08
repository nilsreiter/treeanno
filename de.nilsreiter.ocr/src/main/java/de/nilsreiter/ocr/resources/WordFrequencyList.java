package de.nilsreiter.ocr.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

import de.uniheidelberg.cl.reiter.util.Counter;

public class WordFrequencyList extends Counter<String> implements
		SharedResourceObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void load(DataResource arg0) throws ResourceInitializationException {
		InputStream in;
		try {
			in = arg0.getInputStream();
			InputStreamReader inR = new InputStreamReader(in);
			BufferedReader buf = new BufferedReader(inR);
			String line;
			while ((line = buf.readLine()) != null) {
				String[] parts = line.split(" \t");
				this.put(parts[1], Integer.valueOf(parts[0]));
			}
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}
	}

}
