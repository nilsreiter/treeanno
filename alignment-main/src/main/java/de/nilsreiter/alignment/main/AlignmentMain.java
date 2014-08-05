package de.nilsreiter.alignment.main;

import java.io.IOException;
import java.util.List;

import de.nilsreiter.alignment.algorithm.AlgorithmFactory;
import de.nilsreiter.alignment.algorithm.AlignmentAlgorithm;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.AlignmentWriter;
import de.uniheidelberg.cl.a10.main.MainWithInputDocuments;

public class AlignmentMain extends MainWithInputDocuments {

	public static void main(String[] args) throws IOException {
		AlignmentMain r = new AlignmentMain();
		r.processArguments(args);
		r.init();
		r.run();
	}

	private void init() {

	}

	private void run() throws IOException {

		AlgorithmFactory af = AlgorithmFactory.getInstance();
		AlignmentAlgorithm<Event> algo =
				af.getAlgorithm(this.getConfiguration());
		List<Event> l1 = this.getDocuments().get(0).getEvents();
		List<Event> l2 = this.getDocuments().get(1).getEvents();
		Alignment<Event> alignment = algo.align(l1, l2);
		AlignmentWriter aw = new AlignmentWriter(System.out);
		aw.write(alignment);
		aw.close();

	}
}
