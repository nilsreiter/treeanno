package de.nilsreiter.alignment.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.nilsreiter.alignment.algorithm.AlgorithmFactory;
import de.nilsreiter.alignment.algorithm.AlignmentAlgorithm;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.AlignmentWriter;
import de.uniheidelberg.cl.a10.main.MainWithInputDocuments;

public class AlignmentMain extends MainWithInputDocuments {

	Logger logger = LoggerFactory.getLogger(AlignmentMain.class);

	@Option(name = "--output",
			usage = "Output directory for pairwise alignment files",
			required = true)
	File outputDirectory = null;

	public static void main(String[] args) throws IOException {
		AlignmentMain r = new AlignmentMain();
		r.processArguments(args);
		r.init();
		r.run();
	}

	private void init() {
		if (!outputDirectory.exists()) outputDirectory.mkdirs();
	}

	private void run() throws IOException {

		AlgorithmFactory af = AlgorithmFactory.getInstance();
		AlignmentAlgorithm<Event> algo =
				af.getAlgorithm(this.getConfiguration());
		for (int i = 0; i < this.getDocuments().size(); i++) {
			List<Event> l1 = this.getDocuments().get(i).getEvents();
			for (int j = i + 1; j < this.getDocuments().size(); j++) {
				List<Event> l2 = this.getDocuments().get(j).getEvents();
				String alignmentId =
						"alignment-" + getDocuments().get(i) + "-"
								+ getDocuments().get(j);

				logger.info("Running alignment for {} and {}.", getDocuments()
						.get(i), getDocuments().get(j));
				Alignment<Event> alignment = algo.align(alignmentId, l1, l2);
				alignment.setTitle(alignmentId);
				OutputStream os =
						new FileOutputStream(new File(this.outputDirectory,
								alignmentId + ".xml"));
				AlignmentWriter aw = new AlignmentWriter(os);
				aw.write(alignment);
				aw.close();
			}

		}

	}
}
