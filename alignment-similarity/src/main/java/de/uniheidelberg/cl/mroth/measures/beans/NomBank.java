package de.uniheidelberg.cl.mroth.measures.beans;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NomBank {

	Map<String, String> nom2verb;

	public NomBank(final File dir) {

		nom2verb = new HashMap<String, String>();

		File[] frames = new File(dir, "frames").listFiles();

		SAXParser parser;

		try {

			parser = SAXParserFactory.newInstance().newSAXParser();

			for (int i = 0; i < frames.length; i++) {

				if (!frames[i].getName().endsWith(".xml"))
					continue;

				NomBankHandler dh = new NomBankHandler();

				String noun = frames[i].getName().split("\\.")[0];

				parser.parse(frames[i], dh);

				String verb = dh.getRoleSetSource();

				if (verb != null) {

					nom2verb.put(noun, verb);

				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public String getSourceVerb(final String noun) {

		// System.out.println(noun);

		return nom2verb.get(noun);

	}

	private class NomBankHandler extends DefaultHandler {

		String roleSetSource;

		public String getRoleSetSource() {

			return roleSetSource;

		}

		@Override
		public void startElement(final String uri, final String localName,
				final String qName,

				final Attributes attributes) throws SAXException {

			if (qName.equals("roleset") && attributes.getIndex("source") > -1) {

				roleSetSource = attributes.getValue("source");

				if (roleSetSource.contains("-"))
					roleSetSource = roleSetSource.split("-")[1];

			}

		}

	}

}
