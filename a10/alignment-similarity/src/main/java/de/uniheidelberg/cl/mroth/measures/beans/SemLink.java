package de.uniheidelberg.cl.mroth.measures.beans;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SemLink {

	Map<String, List<String>> pb2verbnet;

	Map<String, Map<String, List<String>>> pbargs2verbnet;

	public SemLink(final String dir) {

		pb2verbnet = new HashMap<String, List<String>>();

		pbargs2verbnet = new HashMap<String, Map<String, List<String>>>();

		SAXParser parser;

		try {

			parser = SAXParserFactory.newInstance().newSAXParser();

			SemLinkHandler dh = new SemLinkHandler();

			parser.parse(new File(dir + "//type_map.xml"), dh);

			pb2verbnet = dh.getMapping();

			pbargs2verbnet = dh.getAMapping();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public String[] getVerbNetClasses(final String p) {

		if (!pb2verbnet.containsKey(p))

			return new String[0];

		List<String> classes = pb2verbnet.get(p);

		String[] retval = new String[classes.size()];

		retval = classes.toArray(retval);

		return retval;

	}

	public String[] getVerbNetArg(final String p, final String a) {

		if (!pbargs2verbnet.containsKey(p))

			return new String[0];

		if (!pbargs2verbnet.get(p).containsKey(a))

			return new String[0];

		List<String> args = pbargs2verbnet.get(p).get(a);

		String[] retval = new String[args.size()];

		retval = args.toArray(retval);

		return retval;

	}

	private class SemLinkHandler extends DefaultHandler {

		Map<String, List<String>> mapping;

		Map<String, Map<String, List<String>>> amapping;

		Map<String, String> role2group;

		String curr_p;

		public Map<String, List<String>> getMapping() {

			return mapping;

		}

		public Map<String, Map<String, List<String>>> getAMapping() {

			return amapping;

		}

		public SemLinkHandler() {

			super();

			mapping = new HashMap<String, List<String>>();

			amapping = new HashMap<String, Map<String, List<String>>>();

			curr_p = "";

			role2group = new HashMap<String, String>();

			// manual grouping by Merlo et al. (or vice versa)

			role2group.put("Agent", "Agent");

			role2group.put("Agent1", "Agent");

			role2group.put("Product", "Product");

			role2group.put("Patient", "Product");

			role2group.put("Patient1", "Product");

			role2group.put("Patient2", "Product");

			role2group.put("Theme", "PredAttr");

			role2group.put("Theme1", "PredAttr");

			role2group.put("Theme2", "PredAttr");

			role2group.put("Predicate", "PredAttr");

			role2group.put("Attribute", "PredAttr");

			role2group.put("Stimulus", "PredAttr");

			role2group.put("Topic", "PredAttr");

			role2group.put("Proposition", "PredAttr");

			role2group.put("Recipient", "Goal");

			role2group.put("Source", "Goal");

			role2group.put("Destination", "Goal");

			role2group.put("Location", "Goal");

			role2group.put("Material", "Goal");

			role2group.put("Beneficiary", "Goal");

			role2group.put("Extent", "Extent");

			role2group.put("Value", "Extent");

			role2group.put("Asset", "Extent");

			role2group.put("Instrument", "InstrCause");

			role2group.put("Cause", "InstrCause");

			role2group.put("Experiencer", "InstrCause");

			role2group.put("Actor", "InstrCause");

			role2group.put("Actor2", "InstrCause");

		}

		@Override
		public void startElement(final String uri, final String localName,
				final String qName, final Attributes attributes)
				throws SAXException {

			if (qName.equals("argmap")) {
				String pb = attributes.getValue("pb-roleset");
				String vn = attributes.getValue("vn-class");
				if (!mapping.containsKey(pb))
					mapping.put(pb, new LinkedList<String>());
				mapping.get(pb).add(vn);
				curr_p = pb;
			}

			if (qName.equals("role")) {
				String pb = attributes.getValue("pb-arg");
				try {
					String vn = role2group.get(attributes.getValue("vn-theta"));
					if (!amapping.containsKey(curr_p))
						amapping.put(curr_p,
								new HashMap<String, List<String>>());
					if (!amapping.get(curr_p).containsKey(pb))
						amapping.get(curr_p).put(pb, new LinkedList<String>());
					amapping.get(curr_p).get(pb).add(vn);
				} catch (Exception e) {
					System.err.println("No group found for role: "
							+ attributes.getValue("vn-theta"));
					System.exit(1);
				}
			}
		}
	}
}
