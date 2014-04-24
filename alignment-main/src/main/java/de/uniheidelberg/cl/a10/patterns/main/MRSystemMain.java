package de.uniheidelberg.cl.a10.patterns.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.AlignmentWriter;
import de.uniheidelberg.cl.a10.patterns.mroth.MRSystem;
import de.uniheidelberg.cl.a10.patterns.mroth.impl.MRSystemFactory;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;

public class MRSystemMain extends MainWithInputSequences {
	@Option(name = "--output", usage = "Output file for XML output")
	File output = null;
	SimilarityConfiguration similarityConf = new SimilarityConfiguration();

	public static void main(final String[] args) throws SecurityException,
			FrameNotFoundException, FrameElementNotFoundException, IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Main.initProperties();
		MRSystemMain asm = new MRSystemMain();
		asm.processArguments(args, asm.similarityConf);
		asm.run();
	}

	protected String getWiki() {
		return similarityConf.getWikiDescription();
	}

	protected Alignment<FrameTokenEvent> align() throws ParserConfigurationException,
			SAXException, IOException, SecurityException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, IncompatibleException {
		List<List<FrameTokenEvent>> sequences;
		sequences = this.getSequences();
		MRSystem<FrameTokenEvent> mrs = MRSystemFactory.getInstance(similarityConf);
		mrs.setLogger(logger);
		mrs.setSequence1(sequences.get(0));
		mrs.setSequence2(sequences.get(1));
		return mrs.getAlignment();
	}

	public void run() throws SecurityException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		try {

			AlignmentWriter dw = new AlignmentWriter(
					this.getOutputStreamForFileOption(output, System.out));
			dw.write(new EventTokenConverter().convert(align()));
			// dw.write(al);
			dw.close();
			return;
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncompatibleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
