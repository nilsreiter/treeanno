package de.uniheidelberg.cl.a10.opennlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import opennlp.tools.postag.TagDictionary;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class A10POSDictionary implements TagDictionary {

	Map<String, String[]> dictionary;

	public A10POSDictionary(final String file) throws IOException,
			DocumentException {
		dictionary = new HashMap<String, String[]>();

		SAXReader saxreader = new SAXReader();
		Document document = saxreader.read(file);

		Element root = document.getRootElement();

		for (Iterator<Element> iter = root.elementIterator(); iter.hasNext();) {
			Element element = iter.next();
			// token
			String[] word = new String[] { element.attributeValue("surface") };
			if (word[0].contains(" ")) {
				word = word[0].split(" ");
			}

			// tags
			ArrayList<String> tags = new ArrayList<String>();
			for (Iterator<Element> innerIter = element.elementIterator(); innerIter
					.hasNext();) {
				Element innerElement = innerIter.next();
				tags.add(((String) innerElement.getData()).trim());
			}
			String[] t = tags.toArray(new String[tags.size()]);
			for (String s : word) {
				this.dictionary.put(s, t);
			}
		}

	}

	@Override
	public String[] getTags(String w) {
		// w looks at first like *word_0word
		try {
			if (w.contains("_")) {
				w = w.split("_")[0].substring(1);
			}
		} catch (Exception e) {
			return null;
		}
		return this.dictionary.get(w);

	}

	/*
	 * public String[] getTags(int i, String[] inputSequence) {
	 * 
	 * if (i < inputSequence.length - 1) { String[] r = this.dictionary.get(new
	 * String[] { inputSequence[i], inputSequence[i + 1] }); if (r != null) {
	 * for (int j = 0; j < r.length; j++) r[j] = r[j].split(" ")[0]; return r; }
	 * } if (i > 0) { String[] r = this.dictionary.get(new String[] {
	 * inputSequence[i - 1], inputSequence[i] }); if (r != null) { for (int j =
	 * 0; j < r.length; j++) r[j] = r[j].split(" ")[1]; return r; } } //
	 * System.err.println("Looking up " + inputSequence[i]); return
	 * this.dictionary.get(new String[] { inputSequence[i] }); }
	 */
}