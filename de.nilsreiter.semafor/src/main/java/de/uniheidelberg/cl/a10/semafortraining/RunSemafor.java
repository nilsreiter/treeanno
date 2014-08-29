package de.uniheidelberg.cl.a10.semafortraining;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.args4j.Option;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.Sentence;
import de.saar.coli.salsa.reiter.framenet.salsatigerxml.SalsaTigerXML;
import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseToken;
import de.uniheidelberg.cl.reiter.util.IO;
import edu.cmu.cs.lti.ark.fn.identification.ConvertAlphabetFile;
import edu.cmu.cs.lti.ark.fn.parsing.ParserDriver;

public class RunSemafor extends AbstractSemaforMain {

	public static int ROOT_GOVERNOR = Integer.MIN_VALUE;

	@Option(name = "--output", required = true,
			usage = "The output/working directory. The model is assumed to be "
					+ "in this directory.")
	File targetDirectory;

	@Option(name = "--model")
	File modelDirectory;

	@Option(name = "--input", required = true,
			usage = "The input file or directory. If it is a directory,"
					+ " --fold F has to be specified.")
	File input;

	@Option(name = "--fold", usage = "The fold number [0-9]")
	int fold;

	@Option(name = "--process")
	int limit = Integer.MAX_VALUE;

	public void init() {
		logger.setLevel(Level.CONFIG);

		if (this.taggerName != null) {
			this.initPartOfSpeechTagger();
		}
		this.initMorphology();
		this.initParser();

		if (!this.targetDirectory.exists()) {
			this.targetDirectory.mkdirs();
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FrameElementNotFoundException
	 * @throws FrameNotFoundException
	 */
	public static void main(final String[] args) throws FrameNotFoundException,
	FrameElementNotFoundException, IOException {

		RunSemafor fc = new RunSemafor();
		fc.processArguments(args);
		fc.init();
		fc.run();

	}

	public void run() throws FrameNotFoundException,
	FrameElementNotFoundException, IOException {
		// FrameNet fn = new FrameNet();
		// SalsaTigerXML stx = new SalsaTigerXML(fn,
		// Logger.getAnonymousLogger());
		if (this.input.isFile()) {
			this.processFile(input, this.targetDirectory);

		} else {
			if (this.configuration == TC.RitAll) {
				for (File file : this.input.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(final File dir, final String name) {
						return name.endsWith(".xml");
					}
				})) {
					this.processFile(file,
							new File(this.targetDirectory, file.getName()));
				}
			} else {
				this.processFile(
						new File(this.getInput(), "test_" + this.fold + ".xml"),
						new File(this.targetDirectory, String
								.valueOf(this.fold)));

			}
		}
		return;

	}

	protected void processFile(final File salsaTigerXMLFile,
			final File targetDirectory) throws FrameNotFoundException,
			FrameElementNotFoundException, IOException {
		if (!targetDirectory.exists()) targetDirectory.mkdirs();
		FrameNet fn = new FrameNet();

		SalsaTigerXML stx = new SalsaTigerXML(fn, Logger.getAnonymousLogger());

		stx.parse(salsaTigerXMLFile);
		// Creating file objects
		File tmpdir = IO.createTempDir("semafor-" + configuration.name(), "");
		File parserOutput = new File(tmpdir, "parser.conll");
		File tokenized = new File(tmpdir, "tokenized.txt");
		File postagged = new File(tmpdir, "postagged.txt");

		// Opening filewriters
		FileWriter fw = new FileWriter(parserOutput);
		FileWriter fw_tok = new FileWriter(tokenized);
		FileWriter fw_pos = new FileWriter(postagged);
		FileWriter missingSentences =
				new FileWriter(
						new File(targetDirectory, "missingSentences.txt"));
		FileWriter log = new FileWriter(new File(targetDirectory, "log.txt"));

		log.write("Using " + tmpdir.getAbsolutePath()
				+ " as temporary directory.\n");
		log.write("Parser: " + this.parserName + ", model: "
				+ this.parserModel.getCanonicalPath());
		int sentenceCounter = 0;

		for (Sentence sentence : stx.getSentences()) {
			BaseSentence ptList =
					de.uniheidelberg.cl.a10.Util.getParseTokenList(sentence,
							false);

			if (this.taggerName != null) {
				tagger.tag(ptList);
			}
			lemmatizer.lemmatize(ptList);
			if (this.parser.parse(ptList) && ptList.isSane()) {
				for (int i = 0; i < ptList.size(); i++) {
					// TODO: This should be replaced by general export code from
					// BaseSentence
					BaseToken pt = ptList.get(i);
					fw.write(String.valueOf(i + 1));
					fw.write("\t");
					fw.write(pt.word());
					fw.write("\t");
					fw.write(pt.word());
					fw.write("\t");
					fw.write(ptList.get(i).getPartOfSpeech().toShortString());
					fw.write("\t");
					fw.write(ptList.get(i).getPartOfSpeech().toShortString());
					fw.write("\t");
					fw.write("-");
					fw.write("\t");
					fw.write(pt.getGovernor() == null ? String.valueOf(0)
							: String.valueOf(ptList.indexOf(pt.getGovernor()) + 1));
					fw.write("\t");
					fw.write(pt.getDependencyRelation() == null ? "ROOT" : pt
							.getDependencyRelation().toString().toUpperCase());
					fw.write("\t");
					fw.write("-");
					fw.write("\t");
					fw.write("-");
					fw.write("\t");
					fw.write("\n");

					fw_tok.write(pt.word());
					fw_tok.write(" ");

					fw_pos.write(pt.word());
					fw_pos.write("_");
					fw_pos.write(pt.getPartOfSpeech().toShortString());
					fw_pos.write(" ");
				}
				fw.write("\n");

				fw_tok.write("\n");
				fw_pos.write("\n");

				sentenceCounter++;
			} else {
				log.write("Sentence " + sentence.getIdString()
						+ " is not wellformed.");
				log.write(ptList.toString());
				missingSentences.write(sentence.getIdString());
				missingSentences.write(new char[] { '\n' });
			}

			if (sentenceCounter == this.getLimit()) break;

		}
		fw_tok.close();
		fw_pos.close();
		missingSentences.close();
		log.close();
		fw.close();
		File datadir = new File(modelDirectory, "datadir");

		File outputFile = new File(targetDirectory, "output.txt");
		if (outputFile.exists()) {
			outputFile.delete();
		}

		String[] cmd =
				new String[] { "/bin/ln", "-s", tmpdir.getAbsolutePath(),
						new File(targetDirectory, "tmp").getAbsolutePath() };
		System.out.println(Arrays.toString(cmd));
		Runtime.getRuntime().exec(cmd);

		try {
			ParserDriver
					.main(new String[] {
							"mstmode:noserver",
							"mstserver:localhost",
							"mstport:8080",
							"posfile:" + postagged.getAbsolutePath(),
							"test-parsefile:" + parserOutput.getAbsolutePath(),
							"stopwords-file:tasks/semafor-training/stopwords.txt",
							"wordnet-configfile:"
									+ new File(
											"tasks/semafor-training/file_properties.xml")
											.getAbsolutePath(),
							"fnidreqdatafile:"
									+ new File(datadir, "reqData.jobj")
											.getAbsolutePath(),
							"goldsegfile:null",
							"userelaxed:no",
							"testtokenizedfile:" + tokenized.getAbsolutePath(),
							"idmodelfile:"
									+ this.getModelFile().getAbsolutePath(),
							"alphabetfile:"
									+ new File(
											new File(modelDirectory, "scan"),
											"parser.conf.unlabeled")
											.getAbsolutePath(),
							"framenet-femapfile:"
									+ new File(modelDirectory,
											"framenet.frame.element.map")
											.getAbsolutePath(),
							"eventsfile:"
									+ new File(tmpdir, "events.bin")
											.getAbsolutePath(),
							"spansfile:"
									+ new File(tmpdir, "spans")
											.getAbsolutePath(),
							"model:" + this.getArgModelFile().getAbsolutePath(),
							"useGraph:null",
							"frameelementsoutputfile:"
									+ outputFile.getAbsolutePath(),
							"alllemmatagsfile:" + new File(tmpdir, "lemmatags") });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File getArgModelFile() {
		File dir = new File(modelDirectory, "datadir");
		File bestModel = null;
		for (File argModelFile : dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(final File arg0, final String arg1) {
				return (arg1.startsWith("argmodel") && arg1.contains(".dat") && !arg1
						.equals("argmodel.converted.dat"));
			}
		})) {
			if (bestModel == null)
				bestModel = argModelFile;
			else {
				String bestName = bestModel.getName();
				String candName = argModelFile.getName();
				if (!candName.matches("argmodel.dat")) {
					int bestIndex =
							Integer.parseInt(bestName.substring(bestName
									.indexOf("_") + 1));
					int candIndex =
							Integer.parseInt(candName.substring(candName
									.indexOf("_") + 1));
					if (candIndex > bestIndex) bestModel = argModelFile;
				}
			}
		}
		File toModel =
				new File(bestModel.getParentFile(), "argmodel.converted.dat");
		if (toModel.exists()) {
			toModel.delete();
		}
		try {
			this.copy(bestModel, toModel);
		} catch (IOException e) {
			return bestModel;
		}
		return toModel;
	}

	public File getModelFile() {
		File dir = new File(modelDirectory, "datadir");
		File bestModel = null;

		for (File argModelFile : dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(final File arg0, final String arg1) {
				return (arg1.startsWith("idmodel") && arg1.contains(".dat_") && !arg1
						.equals("idmodel.converted.dat"));
			}
		})) {
			if (bestModel == null)
				bestModel = argModelFile;
			else {
				String bestName = bestModel.getName();
				String candName = argModelFile.getName();
				if (!candName.matches("idmodel.dat")) {
					int bestIndex =
							Integer.parseInt(bestName.substring(bestName
									.indexOf("_") + 1));
					int candIndex =
							Integer.parseInt(candName.substring(candName
									.indexOf("_") + 1));
					if (candIndex > bestIndex) bestModel = argModelFile;
				}
			}
		}
		File toModel =
				new File(bestModel.getParentFile(), "idmodel.converted.dat");

		if (toModel.exists()) {
			toModel.delete();
		}
		try {
			this.convertModel(new File(modelDirectory, "alphabet.ser"),
					bestModel, toModel);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		return toModel;
	}

	private void convertModel(final File modelFile, final File alphabetFile,
			final File outputFile) throws Exception {
		System.err.println("ConvertAlphabetFile " + modelFile.getAbsolutePath()
				+ " " + alphabetFile.getAbsolutePath() + " "

				+ outputFile.getAbsolutePath());
		ConvertAlphabetFile.main(new String[] { modelFile.getAbsolutePath(),
				alphabetFile.getAbsolutePath(), outputFile.getAbsolutePath() });
	}

	private boolean copy(final File fromFile, final File toFile)
			throws IOException {
		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1)
				to.write(buffer, 0, bytesRead); // write
		} finally {
			if (from != null) try {
				from.close();
			} catch (IOException e) {
				return false;
			}
			if (to != null) try {
				to.close();
			} catch (IOException e) {
				return false;

			}
		}
		return true;
	}

	/**
	 * @return the input
	 */
	public File getInput() {
		return input;
	}

	public boolean saneSentence(final int[] governors) {
		for (int i = 0; i < governors.length; i++) {
			if (governors[i] == Integer.MIN_VALUE) {
				return true;
			}
		}
		return false;

	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

}
