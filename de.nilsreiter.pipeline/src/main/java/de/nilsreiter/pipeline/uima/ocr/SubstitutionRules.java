package de.nilsreiter.pipeline.uima.ocr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class SubstitutionRules extends HashMap<String, String> implements
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
				String[] parts = line.split("[ \t]", 2);
				this.put(parts[0], parts[1]);
			}
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}
	}

}
