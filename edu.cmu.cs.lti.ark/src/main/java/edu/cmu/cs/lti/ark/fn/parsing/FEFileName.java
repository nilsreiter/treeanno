package edu.cmu.cs.lti.ark.fn.parsing;

import gnu.trove.THashMap;

/**
 * @brief File names listed here are mere examples. i.e. they are not hard coded.
 *        they will be read from the command-line argument accordingly.
 * */
public class FEFileName {
	public static String stopwordFilename = "lrdata/stopwords.txt";
	public static String wordnetFilename = "lrdata/file_properties.xml";
	public static String frameDir = "../framenet_j";
	public static String fedictFilename1 = null;
	public static String fedictFilename2 = null;

	public static String tmpDirname = "../tmp/";

	public static String feFilename = "lrdata/semeval.fulltrain.sentences.frame.elements";
	public static String eventFilename = "lrdata/parser.train.events.bin";
	// public static String tagfilename =
	// "lrdata/kbest.fulltrain.sentences.lemma.tags";
	public static String tokenFilename = "lrdata/semeval.fulldev.sentences";
	public static String alphafilename = null;
	/**
	 * @brief for each line of feature indices, the corresponding span sentence
	 *        number , frame name, frame element name.
	 */
	public static String spanfilename = null;
	public static String candidateFilename = "candidate_file_not_in_use";
	public static String tagFilename = "lrdata/semeval.fulldev.sentences.lemma.tags";
	public static String tempFilename = "lrdata/temp.txt";
	public static String predictionFilename = "prediction_";
	public static String modelFilename = "model.out";
	public static String predictfilename = "prediction_";
	public static String outfefilename = "predict.frame.elements";
	public static String outXMLFileName = "predict.xml";
	public static int startIndex, endIndex;
	public static int offSet[] = { 146, 178, 280, 329 };
	// public static int offSet[]={0,67,106};

	// reranking files
	public static String listfilename = "lrdata/train.list";
	public static String rrkfefilename = "rerank.train.frame.elements";
	public static String rrkmodelfilename = "";
	public static String rrkalphafilename = "";

	public static final int NUM_DEP_PARSES = 1;

	public static boolean useUnlabeledSpans = false;
	public static String unlabeledMatchedSpansFile = "lrdata/matched_spans";
	public static THashMap<String, Integer> unlabeledSpans = null;

	public static int KBestParse = 1;
	public static String KBestParseDirectory = null;

}
