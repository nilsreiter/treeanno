package de.uniheidelberg.cl.a10.semafortraining;

import gnu.trove.THashMap;
import gnu.trove.THashSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import org.kohsuke.args4j.Option;

import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader15;
import de.saar.coli.salsa.reiter.framenet.FrameElement;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IRealizedFrame;
import de.saar.coli.salsa.reiter.framenet.IToken;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;
import de.saar.coli.salsa.reiter.framenet.Sentence;
import de.saar.coli.salsa.reiter.framenet.VersionException;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotatedLexicalUnit;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotationCorpus15;
import de.saar.coli.salsa.reiter.framenet.fncorpus.export.XMLExporter;
import de.saar.coli.salsa.reiter.framenet.salsatigerxml.SalsaTigerXML;
import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseToken;
import de.uniheidelberg.cl.a10.semafortraining.uima.FileWriterHandling;
import de.uniheidelberg.cl.a10.semafortraining.uima.SemaforFrameAnnotation;
import de.uniheidelberg.cl.a10.semafortraining.uima.SemaforSentence;
import de.uniheidelberg.cl.reiter.pos.BNC;
import de.uniheidelberg.cl.reiter.pos.FN;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import de.uniheidelberg.cl.reiter.util.Range;

/**
 * This is the main class for controlling the conversion of annotated frame data
 * into semafor format
 * 
 * <ol>
 * <li>Initialize FrameNet</li>
 * <li>Read in lexical unit files from lu-directory</li>
 * </ol>
 * 
 * @author reiter
 * 
 */
public class FormatConverter extends AbstractSemaforMain {

	static String[] illegalLUFiles = new String[] { "lu6702.xml", "lu6751.xml",
			"lu5302.xml", "lu13151.xml", "lu3217.xml", "lu3914.xml",
			"lu5160.xml", "lu453.xml", "lu9966.xml", "lu1173.xml",
			"lu2907.xml", "lu6752.xml", "lu3928.xml", "lu1134.xml",
			"lu3916.xml", "lu5954.xml", "lu15925.xml", "lu3908.xml",
			"lu740.xml", "lu6713.xml", "lu3913.xml", "lu3907.xml",
			"lu7714.xml", "lu3915.xml", "lu3186.xml", "lu6751.xml",
			"lu1678.xml", "lu6747.xml", "lu3929.xml", "lu2868.xml",
			"lu4704.xml", "lu5751.xml", "lu2872.xml", "lu2880.xml",
			"lu5816.xml", "lu6702.xml", "lu6240.xml", "lu8923.xml",
			"lu14672.xml", "lu1238.xml", "lu2869.xml", "lu8503.xml",
			"lu2873.xml", "lu7978.xml", "lu1246.xml", "lu5867.xml",
			"lu3185.xml" };

	@Option(name = "--stx",
			required = true,
			usage = "Directory containing the salsa-tiger input files")
	File input = null;

	@Option(name = "--fnhome",
			usage = "Sets the FrameNet directory",
			required = true)
	File fnhome = null;

	@Option(name = "--output", required = true, usage = "Output directory")
	File targetDir = null;
	FrameNet frameNet = null;
	AnnotationCorpus15 annotationCorpus = null;

	@Option(name = "--folds", usage = "Sets the number of folds")
	int folds = 1;

	@Option(name = "--sanitycheck",
			usage = "Enables the sanity check (overfitting)")
	boolean sanityCheck = false;
	boolean printParses = false;

	private boolean checkDirectory(final File file,
			final boolean needsWriteAccess) {
		if (file.exists()) {
			if (file.canRead()) {
				if (needsWriteAccess) {
					if (file.canWrite()) {
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else {
				logger.severe(file.getAbsolutePath() + " can't be read.");
				return false;
			}
		} else {
			logger.severe(file.getAbsolutePath() + " doesn't exist.");
			return false;
		}
	}

	protected void initLogger() {
		logger.setUseParentHandlers(false);
		Handler h = new ConsoleHandler();
		h.setLevel(Level.INFO);
		logger.addHandler(h);
		try {
			h = new FileHandler("semafor.log", 99999, 4);
			h.setLevel(Level.ALL);
			h.setFormatter(new SimpleFormatter());
			logger.addHandler(h);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.setLevel(Level.ALL);

	}

	public boolean init() throws FileNotFoundException, SecurityException {
		initLogger();

		if (this.configuration == TC.RitAll)
			this.folds = 1;

		if (!this.checkDirectory(input, false)) {
			return false;
		}
		if (!this.checkDirectory(fnhome, false)) {
			return false;
		}
		if (!this.checkDirectory(targetDir, true)) {
			return false;
		}

		targetDir = new File(targetDir, configuration.name());
		targetDir.mkdir();
		initParser();
		initMorphology();
		if (this.taggerName != null) {
			initPartOfSpeechTagger();
		}

		logger.config("stx: " + input);
		logger.config("fnhome: " + fnhome);
		logger.config("output: " + targetDir);
		logger.config("configuration: " + configuration);
		logger.config("folds: " + folds);
		logger.config("parser: " + parserName);

		this.initFrameNet();

		return true;
	};

	private void initFrameNet() throws FileNotFoundException, SecurityException {
		logger.info("Parsing FrameNet database");
		frameNet = new FrameNet();
		frameNet.readData(new FNDatabaseReader15(fnhome, false));

		annotationCorpus = new AnnotationCorpus15(frameNet,
				frameNet.getLogger());

		logger.info("Parsing Lexical Unit database");

		if (configuration != TC.Rit) {
			annotationCorpus.parseWithout(new File(fnhome, "lu"),
					illegalLUFiles);
			logger.info("Finished loading annotation corpus.");
		}
	}

	protected void runFold(final int f) {
		logger.info("Running fold " + f + ".");
		List<Sentence> allSentences = new LinkedList<Sentence>();
		SalsaTigerXML stx;
		Collection<AnnotatedLexicalUnit> alus;
		switch (configuration) {
		case RitAll:
			stx = new SalsaTigerXML(frameNet, logger);
			for (File file : input.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(final File dir, final String name) {
					return name.startsWith("") && name.endsWith(".xml");
				}
			})) {
				try {
					stx.parse(file);
				} catch (FrameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FrameElementNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					System.err.println(e.getMessage());
					System.err.println("Error parsing file "
							+ file.getAbsolutePath());
					System.exit(1);
				}
			}
			allSentences.addAll(stx.getSentences());
			break;
		case Rit:
			stx = new SalsaTigerXML(frameNet, logger);
			try {
				stx.parse(new File(input, (this.isSanityCheck() ? "test_"
						: "training_") + String.valueOf(f) + ".xml"));
				allSentences.addAll(stx.getSentences());
			} catch (FrameNotFoundException e) {
			} catch (FrameElementNotFoundException e) {
			}
			break;
		case FrameNet:
			alus = this.annotationCorpus.getAnnotations();
			for (AnnotatedLexicalUnit alu : alus) {
				allSentences.addAll(alu.getSentences());
			}
			break;
		case Rit_FrameNet:
			stx = new SalsaTigerXML(frameNet, logger);
			try {
				stx.parse(new File(input, (this.isSanityCheck() ? "test_"
						: "training_") + String.valueOf(f) + ".xml"));
				allSentences.addAll(stx.getSentences());
			} catch (FrameNotFoundException e) {
			} catch (FrameElementNotFoundException e) {
			}
			alus = this.annotationCorpus.getAnnotations();
			for (AnnotatedLexicalUnit alu : alus) {
				allSentences.addAll(alu.getSentences());
			}
			break;
		case Rit_FrameNetDown:
			stx = new SalsaTigerXML(frameNet, logger);
			try {
				stx.parse(new File(input, (this.isSanityCheck() ? "test_"
						: "training_") + String.valueOf(f) + ".xml"));
				allSentences.addAll(stx.getSentences());
			} catch (FrameNotFoundException e) {
			} catch (FrameElementNotFoundException e) {
			}
			List<Sentence> fnSentences = new ArrayList<Sentence>();
			alus = this.annotationCorpus.getAnnotations();
			for (AnnotatedLexicalUnit alu : alus) {
				fnSentences.addAll(alu.getSentences());
			}
			Random r = new Random();
			int ritSize = allSentences.size();
			while (allSentences.size() < ritSize * 2) {
				allSentences
						.add(fnSentences.get(r.nextInt(fnSentences.size() - 1)));
			}
			break;
		case RitUp_FrameNet:
			alus = this.annotationCorpus.getAnnotations();
			for (AnnotatedLexicalUnit alu : alus) {
				allSentences.addAll(alu.getSentences());
			}
			int fnSize = allSentences.size();
			List<Sentence> rSentences = new ArrayList<Sentence>();

			stx = new SalsaTigerXML(frameNet, logger);
			try {
				stx.parse(new File(input, (this.isSanityCheck() ? "test_"
						: "training_") + String.valueOf(f) + ".xml"));
				rSentences.addAll(stx.getSentences());
			} catch (FrameNotFoundException e) {
			} catch (FrameElementNotFoundException e) {
			}
			while (allSentences.size() < fnSize * 2) {
				allSentences.addAll(rSentences);
			}
			break;
		}

		int sentenceNumber = 0;
		logger.info("Starting to process " + allSentences.size()
				+ " sentences.");

		for (Sentence sentence : allSentences) {
			if (!sentence.getRealizedFrames().isEmpty()) {
				boolean processThisSentence = true;
				for (IRealizedFrame rf : sentence.getRealizedFrames()) {
					processThisSentence = processThisSentence
							&& rf.getTargetList() != null
							&& rf.getTargetList().size() > 0
							&& rf.getTargetList().first().getPartOfSpeech()
									.asFN() != null
							&& ((RealizedFrame) rf).getLemma().getElement2() != null;
				}
				if (processThisSentence) {
					for (IToken token : sentence.getTokenList()) {
						processThisSentence = processThisSentence
								&& token.getPartOfSpeech() != null
								&& token.getPartOfSpeech().getClass() != BNC.class;
					}
				}
				if (processThisSentence) {
					try {
						// System.err.println("Processing sentence "
						// + sentenceNumber);
						processSentence(sentence, f, sentenceNumber++);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		try {
			FileWriterHandling.get(
					new File(new File(this.targetDir, String.valueOf(f)),
							Constants.FILENAME_SENTENCES_TOKENIZED)).close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			FileWriterHandling.get(
					new File(new File(this.targetDir, String.valueOf(f)),
							Constants.FILENAME_SENTENCES_FRAMES)).close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			FileWriterHandling.get(
					new File(new File(this.targetDir, String.valueOf(f)),
							Constants.FILENAME_SENTENCES_SYNTAX)).close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			logger.info("Printing lu files.");
			this.printLexicalUnitFiles(new File(new File(targetDir, String
					.valueOf(f)), "lu"));
			logger.info("Printing maps.");
			this.printMaps(f, allSentences);
		} catch (IOException e) {
			logger.warning(e.getMessage());
		}
		logger.info("Fold " + f + " is done.");
	}

	public void run() {
		for (int f = 0; f < folds; f++) {
			this.runFold(f);
		}

	}

	/**
	 * @param args
	 * @throws SecurityException
	 * @throws FileNotFoundException
	 */
	public static void main(final String[] args) throws FileNotFoundException,
			SecurityException {
		FormatConverter fc = new FormatConverter();
		fc.processArguments(args);
		fc.printSettings();
		fc.init();
		fc.run();
	}

	/**
	 * This method for non-UIMA sentences
	 * 
	 * @param sentence
	 * @param fileWriters
	 * @param sentenceNumber
	 * @throws IOException
	 */
	protected void processSentence(
			final de.saar.coli.salsa.reiter.framenet.Sentence sentence,
			final int fold, final int sentenceNumber) throws IOException {

		try {
			BaseSentence baseSentence = new BaseSentence();

			// 1. tokenized text
			Iterator<? extends IToken> tokenIterator = sentence
					.getTokenIterator();
			while (tokenIterator.hasNext()) {
				IToken token = tokenIterator.next();

				BaseToken pt = new BaseToken(token.toString());
				pt.setPartOfSpeech(token.getPartOfSpeech());
				if (token.getProperty("LEMMA") == null) {
					lemmatizer.lemmatize(pt);
				} else {
					pt.setLemma(token.getProperty("LEMMA"));
				}
				baseSentence.add(pt);
			}

			SemaforSentence semSentence = this.createSemaforSentence(
					sentenceNumber, baseSentence);

			// 2. frame data
			// <number of frames+roles> <tab> <frame name> <tab>
			// <lexical unit> <tab> <token number (s) in the sentence>
			// <tab> <actual word/phrase in the sentence> <tab>
			// <sentence number in the tokenized file> (<tab> <role
			// name> <token number(s) in the sentence>)*

			for (IRealizedFrame irf : sentence.getRealizedFrames()) {
				RealizedFrame rf = (RealizedFrame) irf;
				if (rf.getTargetList() != null
						&& rf.getTargetList().size() > 0
						&& rf.getTargetList().first().getPartOfSpeech().asFN() != null) {
					String lexicalUnitString = getLexicalUnitString(rf);
					SemaforFrameAnnotation semFa = new SemaforFrameAnnotation(
							semSentence);
					if (rf != null && rf.getLemma().getElement2() != null) {
						try {
							// System.err.println(rf);

							annotationCorpus.addLexicalUnit("Rituals", rf);
						} catch (Exception e) {
							e.printStackTrace();
						}

						semSentence.addFrameAnnotation(semFa);
						semFa.setFrameName(rf.getFrame().getName());
						semFa.setLexicalUnitName(lexicalUnitString);
						for (IToken token : rf.getTargetList()) {
							semFa.addTargetToken(sentence.getTokenList()
									.indexOf(token));
						}

						for (RealizedFrameElement rfe : rf.overtFrameElements()) {
							Range r = rfe.getTargetTokenRange();
							semFa.addFrameElementAnnotation(rfe
									.getFrameElement().getName(), r);

						}
					}
				}

			}
			semSentence.print(new File(this.targetDir, String.valueOf(fold)));

		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
			logger.throwing("FormatConverter", "processSentence", e);
			logger.severe(e.getMessage() + "\nSentence: " + sentence.toString());

		}

	}

	/**
	 * This method takes a list of ParseToken-objects and generates a
	 * SemaforSentence-object containing the tokenization and syntactic
	 * information. The parsing and pos-tagging happens in this method.
	 * 
	 * @param parseSentence
	 *            A list of ParseToken-objects. Lemma and POS should be set.
	 * @param sentenceNumber
	 *            The sentence number
	 * @return
	 */
	protected SemaforSentence createSemaforSentence(final int sentenceNumber,
			final BaseSentence parseSentence) {

		if (this.taggerName != null) {
			tagger.tag(parseSentence);
		}

		// Do the parsing

		if (this.parser.parse(parseSentence)) {
			return parseSentence.getSemaforSentence(sentenceNumber);
		}
		return null;
	}

	public static String getLexicalUnitString(final IRealizedFrame rf) {
		StringBuffer buf = new StringBuffer();
		IPartOfSpeech pos = null;
		for (IToken tok : rf.getTargetList()) {
			if (tok.getProperty("LEMMA") != null) {
				buf.append(tok.getProperty("LEMMA"));
			} else {
				buf.append(tok.toString());
			}
			buf.append('_');
			if (pos == null) {
				pos = tok.getPartOfSpeech();
			}
		}
		String s = buf.toString();
		return s.substring(0, s.length() - 1) + "."
				+ (pos.asFN() == FN.Adverb ? "r" : pos.asFN().toShortString());
	}

	protected void addToMaps(final THashMap<String, THashSet<String>> luMap,
			final THashMap<String, THashSet<String>> feMap,
			final Collection<Sentence> sentences) {
		for (Sentence sent : sentences) {
			// Sentence15 sentence = (Sentence15) sent;
			for (IRealizedFrame rf : sent.getRealizedFrames()) {
				StringBuffer buf = new StringBuffer();
				for (IToken tok : rf.getTargetList()) {
					buf.append(tok.toString()
							+ "_"
							+ tok.getPartOfSpeech().toShortString()
									.toUpperCase());
					buf.append(' ');
				}
				if (!luMap.containsKey(rf.getFrame().getName())) {
					luMap.put(rf.getFrame().getName(), new THashSet<String>());
				}
				if (!feMap.containsKey(rf.getFrame().getName())) {
					feMap.put(rf.getFrame().getName(), new THashSet<String>());
				}
				luMap.get(rf.getFrame().getName()).add(buf.toString().trim());
				for (FrameElement fe : rf.getFrame().frameElements()) {

					feMap.get(rf.getFrame().getName()).add(fe.getName());
				}
			}
		}
	}

	/*
	 * protected void addToMaps(final THashMap<String, THashSet<String>> luMap,
	 * final THashMap<String, THashSet<String>> feMap, final AnnotationCorpus
	 * annotations) {
	 * 
	 * for (AnnotatedLexicalUnit alu : annotations.getAnnotations()) { if
	 * (!alu.getLexicalUnit().containsMultiWordLexeme()) { Frame frame =
	 * alu.getLexicalUnit().getFrame(); if (!luMap.containsKey(frame.getName()))
	 * { luMap.put(frame.getName(), new THashSet<String>()); } StringBuffer buf
	 * = new StringBuffer(); for (Lexeme lexeme :
	 * alu.getLexicalUnit().getLexemes()) { if (lexeme.getValue() != null &&
	 * lexeme.getPartOfSpeech() != null) { buf.append(lexeme.getValue() + "_" +
	 * lexeme.getPartOfSpeech().asFN() .toShortString()); buf.append(' '); } }
	 * if (buf.toString().length() > 0) {
	 * luMap.get(frame.getName()).add(buf.toString().trim()); } if
	 * (!feMap.containsKey(frame.getName())) { feMap.put(frame.getName(), new
	 * THashSet<String>()); } for (FrameElement fe : frame.frameElements()) {
	 * feMap.get(frame.getName()).add(fe.getName()); } } } }
	 */
	public void printMaps(final int fold, final Collection<Sentence> sentences)
			throws IOException {
		THashMap<String, THashSet<String>> luMap = new THashMap<String, THashSet<String>>();
		THashMap<String, THashSet<String>> feMap = new THashMap<String, THashSet<String>>();

		this.addToMaps(luMap, feMap, sentences);

		de.uniheidelberg.cl.a10.Util.writeObjectToFile(luMap, new File(
				new File(targetDir, String.valueOf(fold)),
				"framenet.original.map"));
		de.uniheidelberg.cl.a10.Util.writeObjectToFile(feMap, new File(
				new File(targetDir, String.valueOf(fold)),
				"framenet.frame.element.map"));

		if (luMap.isEmpty() || feMap.isEmpty()) {
			System.err.println("Map empty.");
			System.err.println("luMap: " + luMap.size());
			System.err.println("feMap: " + feMap.size());
			System.exit(1);
		}
		// System.err.println(luMap);
		// System.err.println(feMap);

	}

	public void printLexicalUnitFiles(final File directory) throws IOException {
		if (!directory.exists()) {
			directory.mkdirs();
		}

		try {
			XMLExporter export = new XMLExporter();
			export.setLuXSLT(new File("lexUnit.xsl"));
			export.setLuIndexXSLT(new File("luIndex.xsl"));
			export.writeToDirectory(annotationCorpus, directory);
		} catch (VersionException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the sanityCheck
	 */
	public boolean isSanityCheck() {
		return sanityCheck;
	}

	public void printSettings() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nconfiguration  ").append(this.configuration);
		sb.append("\nfnhome         ").append(this.fnhome.getAbsolutePath());
		sb.append("\nfolds          ").append(this.folds);
		sb.append("\nparsername     ").append(this.parserName);
		sb.append("\nprintParses    ").append(this.printParses);
		sb.append("\nsanityCheck    ").append(this.sanityCheck);
		sb.append("\nstxDirectory   ").append(this.input.getAbsolutePath());
		sb.append("\ntaggername     ").append(this.taggerName);
		sb.append("\ntargetDir      ").append(this.targetDir);
		sb.append("\n");
		System.err.println(sb.toString());
	}

}
