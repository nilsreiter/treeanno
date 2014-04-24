package de.uniheidelberg.cl.a10.patterns.main;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;

import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.AlignmentWriter;
import de.uniheidelberg.cl.a10.patterns.baseline.Baseline;
import de.uniheidelberg.cl.a10.patterns.baseline.BaselineFactory;
import de.uniheidelberg.cl.a10.patterns.baseline.WeightedBaseline;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunctionFactory;

public class BaselineMain extends MainWithInputSequences {

	Config config = new Config();

	/**
	 * @param args
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 */
	public static void main(final String[] args)
			throws ParserConfigurationException, SAXException, IOException,
			SecurityException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		BaselineMain bl = new BaselineMain();
		bl.processArguments(args, bl.config);
		bl.run();
	}

	protected void run() throws ParserConfigurationException, SAXException,
			IOException, SecurityException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		EventTokenConverter etc = new EventTokenConverter();
		BaselineFactory<FrameTokenEvent> factory = new BaselineFactory<FrameTokenEvent>();
		Baseline<FrameTokenEvent> baseline = factory.getBaseline(config.type);
		if (factory.isWeighted(config.type)) {
			SimilarityFunctionFactory<FrameTokenEvent> sfFactory = new SimilarityFunctionFactory<FrameTokenEvent>();
			((WeightedBaseline<FrameTokenEvent>) baseline)
					.setSimilarityFunction(sfFactory
							.getSimilarityFunction(config));
		}
		Alignment<FrameTokenEvent> alignment = baseline.getAlignment(getSequences());
		OutputStream os = this.getOutputStreamForFileOption(config.output,
				System.out);
		AlignmentWriter aw = new AlignmentWriter(os);
		aw.write(etc.convert(alignment));
		os.close();
	}

	class Config extends SimilarityConfiguration {
		@Option(name = "--type", usage = "How to calculate the baseline")
		Baseline.Type type = Baseline.Type.NoAlignment;

		@Option(name = "--output", usage = "Output")
		File output = null;
	}
}
