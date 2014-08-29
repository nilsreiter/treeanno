package de.uniheidelberg.cl.a10.semafortraining;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.dom4j.DocumentException;
import org.kohsuke.args4j.Option;

import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.parser.Dummy;
import de.uniheidelberg.cl.a10.parser.IDependencyParser;
import de.uniheidelberg.cl.a10.parser.Mate;
import de.uniheidelberg.cl.a10.parser.Stanford;
import de.uniheidelberg.cl.a10.parser.dep.StanfordDependency;
import de.uniheidelberg.cl.a10.postagging.DummyPartOfSpeechTagger;
import de.uniheidelberg.cl.a10.postagging.ILemmatizer;
import de.uniheidelberg.cl.a10.postagging.IPartOfSpeechTagger;
import de.uniheidelberg.cl.a10.postagging.OpenNLPPartOfSpeechTagger;
import de.uniheidelberg.cl.a10.postagging.StanfordMorphology;
import de.uniheidelberg.cl.reiter.pos.PTB;

public abstract class AbstractSemaforMain extends Main {

	protected static Logger logger = Logger.getLogger("Semafor");
	IDependencyParser parser = null;
	IPartOfSpeechTagger tagger = null;

	@Option(name = "--parsername")
	String parserName = null;

	@Option(name = "--taggername")
	String taggerName = null;

	@Option(name = "--parsermodel")
	File parserModel = null;

	@Option(name = "--taggermodel")
	File taggerModel = null;
	ILemmatizer lemmatizer;

	@Option(name = "--configuration")
	TC configuration;

	protected void initPartOfSpeechTagger() {
		if (this.taggerName.equalsIgnoreCase("OpenNLPPartOfSpeechTagger")) {
			try {
				tagger = new OpenNLPPartOfSpeechTagger(PTB.class, taggerModel,
						new File("lib/dictionary.xml"));
			} catch (IOException e) {
				e.printStackTrace();
				tagger = new DummyPartOfSpeechTagger();
			} catch (DocumentException e) {
				e.printStackTrace();
				tagger = new DummyPartOfSpeechTagger();
			}
		} else {
			tagger = new DummyPartOfSpeechTagger();
		}
	}

	protected void initParser() {
		if (this.parserName.equalsIgnoreCase("mate")) {
			parser = new Mate(StanfordDependency.class, parserModel);
		} else if (this.parserName.equalsIgnoreCase("dummy")) {
			parser = new Dummy();
		} else {
			parser = new Stanford(StanfordDependency.class, parserModel);
		}
	}

	protected void initMorphology() {
		this.lemmatizer = new StanfordMorphology();
	}

	/**
	 * @return the parser
	 */
	public IDependencyParser getParser() {
		return parser;
	}

}
