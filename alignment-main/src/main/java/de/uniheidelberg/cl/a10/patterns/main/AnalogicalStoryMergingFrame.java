package de.uniheidelberg.cl.a10.patterns.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.patterns.io.ModelWriter;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.train.BMMConfiguration;
import de.uniheidelberg.cl.a10.patterns.train.BMMFactory;
import de.uniheidelberg.cl.a10.patterns.train.BayesianModelMerging;

@Deprecated
public class AnalogicalStoryMergingFrame extends MainWithInputSequences {

	@Option(
			name = "--model",
			usage = "If set, the trained model will be saved in the given file."
					+ " File suffix controls the type (.ser = binary, .xml = XML)."
					+ " If not set, an XML representation of the model will be"
					+ " written to System.out.")
	File model = null;

	@Option(name = "--nomerging",
			usage = "Initialises a hidden markov model without any merging")
	boolean nomerging = false;

	BMMConfiguration bmmConf = new BMMConfiguration();

	public static void main(final String[] args) throws SecurityException,
			FrameNotFoundException, FrameElementNotFoundException, IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Main.initProperties();
		AnalogicalStoryMergingFrame asm = new AnalogicalStoryMergingFrame();
		asm.processArguments(args, asm.bmmConf);
		asm.run();
	}

	public void writeModel(final SEHiddenMarkovModel_impl<FrameTokenEvent> hmm)
			throws IOException {
		if (this.model != null) {
			OutputStream os =
					this.getOutputStreamForFileOption(this.model, null);
			ModelWriter<FrameTokenEvent> mw =
					new ModelWriter<FrameTokenEvent>(os);
			try {
				mw.write(hmm);
				mw.close();
				os.flush();
				os.close();
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
			} finally {
				mw.close();
				os.close();
			}
		} else {
			ModelWriter<FrameTokenEvent> mw =
					new ModelWriter<FrameTokenEvent>(
							this.getOutputStreamForFileOption(this.model,
									System.out));

			try {
				mw.write(hmm);
				mw.close();
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
			} finally {
				mw.close();
			}
		}
	}

	protected String getWiki() {
		return bmmConf.getWikiDescription();
	}

	public void run() throws SecurityException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		// System.err.println(bmmConf.getLineDescription());
		try {
			Collection<List<FrameTokenEvent>> sequences;
			sequences = this.getSequences();
			runMerging(sequences);
			return;
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SAXException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	protected void
	runMerging(final Collection<List<FrameTokenEvent>> sequences)
			throws FileNotFoundException, SecurityException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		BMMFactory<FrameTokenEvent> trainingFactory =
				new BMMFactory<FrameTokenEvent>();

		BayesianModelMerging<FrameTokenEvent> bmm =
				trainingFactory.getTrainer(bmmConf);
		// Settings
		Level level = Level.parse(logLevel.toUpperCase());
		bmm.setLogLevel(level);
		SEHiddenMarkovModel_impl<FrameTokenEvent> hmm;

		if (nomerging) {
			hmm = bmm.init(sequences.iterator());
		} else {

			// Training
			hmm = bmm.train(sequences.iterator());
		}
		int i = 0;
		for (File f : getArguments()) {
			hmm.setProperty("source" + (i++), f.getName());
		}
		hmm.setProperty("commandline", commandLine);
		hmm.setTrainingConfiguration(bmmConf);

		try {
			writeModel(hmm);
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

}
