package de.uniheidelberg.cl.a10.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.dom4j.DocumentException;
import org.kohsuke.args4j.Option;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseText;
import de.uniheidelberg.cl.a10.data.io.CoNLL09Reader;
import de.uniheidelberg.cl.a10.parser.dep.StanfordDependency;
import de.uniheidelberg.cl.a10.postagging.IPartOfSpeechTagger;
import de.uniheidelberg.cl.a10.postagging.OpenNLPPartOfSpeechTagger;
import de.uniheidelberg.cl.reiter.pos.PTB;

public class Main extends de.uniheidelberg.cl.a10.Main {

	@Option(name = "--parser")
	Parser parserName = null;

	@Option(name = "--parsermodel")
	File parserModel = null;

	@Option(name = "--input")
	File input = null;

	@Option(name = "--output")
	File output = null;

	@Option(name = "--dataformat")
	DataFormat dataFormat = DataFormat.CoNLL09;

	@Option(name = "--postagging")
	boolean postagging = false;

	@Option(name = "--posmodel")
	File taggingModel = null;

	IDependencyParser parser = null;

	IPartOfSpeechTagger tagger = null;

	public static void main(final String[] args) throws IOException {
		Main m = new Main();
		m.processArguments(args);
		m.init();
		m.run();
	}

	public void init() {

		switch (parserName) {
		case STANFORD:
			parser = new Stanford(StanfordDependency.class, this.parserModel);
			break;
		default:
		case MATE:
			parser = new Mate(StanfordDependency.class, this.parserModel);
			break;
		}

		if (postagging) {
			try {
				if (taggingModel != null) {
					tagger =
							new OpenNLPPartOfSpeechTagger(PTB.class,
									taggingModel);
				} else {
					tagger =
							new OpenNLPPartOfSpeechTagger(
									PTB.class,
									new File(
											"uima/resources/OpenNLP_Model_POS-Tagging_WSJ+Rit+/10.bin.gz"),
											new File("lib/dictionary.xml"));
				}
			} catch (IOException e) {
				System.err.println("POS tagger can't be loaded");
				System.err.println(e.getLocalizedMessage());
				System.exit(1);
			} catch (DocumentException e) {
				System.err.println("POS tagger dictionary can't be loaded");
				System.err.println(e.getLocalizedMessage());
				System.exit(1);
			}
		}

	};

	public void run() throws IOException {
		CoNLL09Reader cnl =
				new CoNLL09Reader(this.input, StanfordDependency.class,
						PTB.class);
		OutputStreamWriter fw = null;
		try {
			if (this.output != null) {
				fw = new FileWriter(this.output);
			} else {
				fw = new OutputStreamWriter(System.out);
			}
			BaseText bt = cnl.getSentences();
			for (BaseSentence bs : bt) {
				if (this.postagging) {
					bs.clearPartOfSpeechAnnotation();
					tagger.tag(bs);
				}
				bs.clearDependencyAnnotations();
				if (this.parser.parse(bs)) {
					fw.write(bs.getSentenceData09().toString());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fw.close();
		}

	};

}
