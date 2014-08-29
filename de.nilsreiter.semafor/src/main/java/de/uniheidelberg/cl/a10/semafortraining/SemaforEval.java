package de.uniheidelberg.cl.a10.semafortraining;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Class to evaluate the different syntactical occurrences of the two defined
 * error classes (ERROR0 and ERROR1) of Semafor.
 * 
 * @author zeller
 */
public class SemaforEval extends DefaultHandler {

	/**
	 * Contains the flags which have been annotated, e.g. 'imperative' (but
	 * discounting ERROR0 and ERROR1).
	 */
	List<String> globalFlagList;

	/**
	 * Contains all frame elements known in the XML file.
	 */
	List<String> globalFeList;

	/**
	 * Counts the amount of error0.
	 */
	int err0Counter;

	/**
	 * Counts the amount of error1.
	 */
	int err1Counter;

	/**
	 * Counts combination of ERROR0 occurring with different syntax phenomena.
	 */
	HashMap<String, Integer> error0Combinations;

	/**
	 * Counts combination of ERROR1 occurring with different syntax phenomena.
	 */
	HashMap<String, Integer> error1Combinations;

	/**
	 * Counts combination of ERROR1 occurring with different frame elements.
	 */
	HashMap<String, Integer> error0FrameElements;

	/**
	 * Counts combination of ERROR1 occurring with different frame elements.
	 */
	HashMap<String, Integer> error1FrameElements;

	/**
	 * Saves the flags annotated of a specific frame element, also counting 
	 * ERROR0 and ERROR1).
	 */
	List<String> localFlagList;

	/**
	 * Saves the frame element ID.
	 */
	String localFeId;

	/**
	 * Saves the frame element name.
	 */
	String localFeName;

	/**
	 * Saves the frame name. Not yet used. TODO: if necessary, save info about
	 * combination of Frame and ERROR*.
	 */
	String localFrameName;

	/**
	 * Saves the sentence ID. Maybe later on necessary for allocations.
	 */
	String sId;

	/**
	 * Value to check if the XML header has already been overrun.
	 */
	boolean headPassed;

	/**
	 * Can be used for testing output.
	 */
	List<String> testGlobalError0;

	/**
	 * Constructor for the parser: initialization.
	 * 
	 * @param file
	 *            the tiger XML input file
	 */
	public SemaforEval(String file) {

		globalFlagList = new ArrayList<String>();
		globalFeList = new ArrayList<String>();
		err0Counter = 0;
		err1Counter = 0;

		error0Combinations = new HashMap<String, Integer>();
		error1Combinations = new HashMap<String, Integer>();

		error0FrameElements = new HashMap<String, Integer>();
		error1FrameElements = new HashMap<String, Integer>();

		headPassed = false;

		testGlobalError0 = new ArrayList<String>();
	}

	/**
	 * Method to define which procedures are conducted when a specific XML tag
	 * is opened.
	 * 
	 */
	public void startElement(String namespaceURI, String localName, String tag,
			Attributes atts) throws SAXException {

		if (tag.equals("flag")) { /* for XML tag "flag" */

			// if we are in the HEADER and have a FE flag which is not ERROR*:
			if (!headPassed && atts.getValue("for").equals("fe")) {
				if (!((atts.getValue("name").startsWith("ERROR")))) {
					// save flag in in globalflaglist
					globalFlagList.add(atts.getValue("name"));
				}
				// if we are at "flag" in the BODY:
			} else {
				// add flag name into localFlagList for this FE
				localFlagList.add(atts.getValue("name"));

			}

		} else if (tag.equals("element")) {
			// use header information about frame elements
			if (!headPassed) {
				globalFeList.add(atts.getValue("name"));
			}

		} else if (tag.equals("frame")) { /* for XML tag "frame" */
			if (headPassed) { // do only for body "frame"s !
				localFrameName = atts.getValue("name"); // e.g. "Placing"
			}

		} else if (tag.equals("fe")) { /* for XML tag "fe" */
			// renew localFlagList for this FE
			localFlagList = new ArrayList<String>();
			localFeId = atts.getValue("id");
			localFeName = atts.getValue("name"); // e.g. "Agent"

		} else if (tag.equals("s")) { /* for XML tag "s" */
			sId = atts.getValue("id");
		}

	}

	/**
	 * Method to define which procedures are conducted when a specific XML tag
	 * is closed.
	 * 
	 */
	public void endElement(String namespaceURI, String localName, String tag) {

		if (tag.equals("head")) { /* for XML tag "head" */
			headPassed = true;

		} else if (tag.equals("flag")) { /* for XML tag "flag" */
			if (!headPassed) {
				// use header information:
				// create counters for each flag in globalFlagList with
				// err0 and err1, e.g. err0-imperative, err1-coord
				for (String globalFlag : globalFlagList) {
					error0Combinations.put(globalFlag, 0);
					error1Combinations.put(globalFlag, 0);
				}
			}

		} else if (tag.equals("fe")) { /* for XML tag "fe" */

			// if the current FE has error flags:
			if (!localFlagList.isEmpty()) {
				/*
				 * NOTE: This commented part shows that there are XML errors in
				 * the tiger xml file: in some cases, an annotated ERROR0 flag
				 * is written down twice in the XML file. That is why the shell
				 * command grep "ERROR0" semafor_m0_m1_errors.xml |wc -l finds
				 * more hits than this program.
				 * 
				 * List<String> modLocalFlagList = new ArrayList<String>();
				 * HashSet hs = new HashSet(); hs.addAll(localFlagList);
				 * modLocalFlagList.addAll(hs); if (modLocalFlagList.size() !=
				 * localFlagList.size()) {
				 * System.out.println("duplicate error0/error1-entries in " +
				 * localFeId + " !"); }
				 */

				// increment pure ERROR0/1 counters
				if (localFlagList.contains("ERROR0")) {
					err0Counter++;
				}
				if (localFlagList.contains("ERROR1")) {
					err1Counter++;
				}

				// count syntax flag-error combination
				// for flag gf in globalflaglist:
				for (String globalFlag : globalFlagList) {
					if (localFlagList.contains("ERROR0")) {
						if (localFlagList.contains(globalFlag)) {
							// increment error0Combinations in HM for gf
							error0Combinations.put(globalFlag,
									(error0Combinations.get(globalFlag) + 1));
						}
					}

					if (localFlagList.contains("ERROR1")) {
						if (localFlagList.contains(globalFlag)) {
							// increment error1Combinations in HM for gf
							error1Combinations.put(globalFlag,
									(error1Combinations.get(globalFlag) + 1));
						}
					}
				}

				// count frame element-error combination
				for (String globalFe : globalFeList) {
					if (localFlagList.contains("ERROR0")) {
						if (localFeName.contains(globalFe))  {
							if (error0FrameElements.containsKey(globalFe)) {
								// increment error0FrameElements in HM
								error0FrameElements.put(globalFe, (error0FrameElements.get(globalFe)+ 1 ));
							} else {
								error0FrameElements.put(globalFe, 1);
							}
						}
					} else if (localFlagList.contains("ERROR1")) {
						if (localFeName.contains(globalFe))  {
							if (error1FrameElements.containsKey(globalFe)) {
								// increment error1FrameElements in HM
								error1FrameElements.put(globalFe, (error1FrameElements.get(globalFe)+ 1 ));
							} else {
								error1FrameElements.put(globalFe, 1);
							}							
							
						}
					}
				}
			}

		} else if (tag.equals("body")) {
			/* for XML tag "body", thus at the very end: */
			printStatistics();

			/*
			 * //testing output:
			 * 
			 * System.out.println("\n\nfinal error0 list: " +
			 * error0Combinations.toString());
			 * System.out.println("final error1 list: " +
			 * error1Combinations.toString());
			 */
		}

	}

	/**
	 * Print method for the statistics.
	 */
	public void printStatistics() {

		/* print ERROR0 statistics */
		System.out.println("***\nAbout ERROR0:\n*** ");
		System.out.println("#times \t| occurs with ...");

		Set<String> error0Keys = error0Combinations.keySet();
		for (String key : error0Keys) {
			System.out.println(error0Combinations.get(key) + "\t| " + key);
		}
		System.out.println("\nRegarding the frames:\n#times \t| occurs with ...");
		Set<String> error0FrameKeys = error0FrameElements.keySet();
		for (String key : error0FrameKeys) {
			System.out.println(error0FrameElements.get(key) + "\t| " + key);
		}
		
		System.out.println("\ntotal amount of ERROR0 (counted once): "
				+ err0Counter);

		/* print ERROR1 statistics */
		System.out.println("\n\n***\nAbout ERROR1:\n*** ");
		System.out.println("#times \t| occurs with ...");

		Set<String> error1Keys = error1Combinations.keySet();
		for (String key : error1Keys) {
			System.out.println(error1Combinations.get(key) + "\t| " + key);
		}
		System.out.println("\nRegarding the frames:\n#times \t| occurs with ...");
		Set<String> error1FrameKeys = error1FrameElements.keySet();
		for (String key : error1FrameKeys) {
			System.out.println(error1FrameElements.get(key) + "\t| " + key);
		}		

		System.out.println("\ntotal amount of ERROR1 (counted once): "
				+ err1Counter);

	}

	/**
	 * Main method to run the evaluation process. Currently to be run with the
	 * file /mnt/proj/rituals/zeller/tmp/semafor_m0_m1_errors.xml.
	 * 
	 * @param args
	 *            the command line arguments
	 * @throws SAXException
	 *             for SAX errors
	 * @throws ParserConfigurationException
	 *             for Parser errors
	 * @throws IOException
	 *             for IO errors
	 */
	public static void main(String[] args) throws SAXException,
			ParserConfigurationException, IOException {
		if (args.length != 1) {
			System.out.println("please give me the path to a tiger xml.");
			System.exit(1);
		}

		SAXParserFactory fac = SAXParserFactory.newInstance();
		SAXParser saxparser = fac.newSAXParser();
		SemaforEval myHandler = new SemaforEval(args[0]);

		saxparser.parse(new File(args[0]), myHandler);

	}
}