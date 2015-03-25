package de.nilsreiter.pipeline.segmentation.wc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.apache.commons.io.IOUtils;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class WordSet extends HashSet<String> implements SharedResourceObject {

	private static final long serialVersionUID = 1L;

	public void load(DataResource aData) throws ResourceInitializationException {
		InputStream is;
		try {
			is = aData.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while (br.ready()) {
				this.add(br.readLine());
			}

			IOUtils.closeQuietly(is);
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}

	}

}
