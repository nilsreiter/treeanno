package de.nilsreiter.alignment.main;

import java.io.IOException;
import java.util.List;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.kohsuke.args4j.Option;

import de.nilsreiter.alignment.algorithm.AlgorithmFactory;
import de.nilsreiter.alignment.algorithm.AlignmentAlgorithm;
import de.nilsreiter.alignment.algorithm.NeedlemanWunsch;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.AlignmentWriter;
import de.uniheidelberg.cl.a10.main.MainWithInputDocuments;

public class Run extends MainWithInputDocuments {

	@Option(name = "--algorithm",
			usage = "fully specified class name of algorihm")
	String algorithmClass = null;

	public static void main(String[] args) throws IOException {
		Run r = new Run();
		r.processArguments(args);
		r.run();
	}

	public Run() {
		super();

	}

	private void run() throws IOException {
		AlgorithmFactory af = AlgorithmFactory.getInstance();
		Configuration configuration = new BaseConfiguration();
		configuration.setProperty(AlgorithmFactory.CONFIG_KEY_ALGORITHM,
				algorithmClass);
		configuration.setProperty("database.url",
				"jdbc:mysql://127.0.0.1:53903/reiter");
		configuration.setProperty("database.username", "reiterns");
		configuration.setProperty("database.password", "bybNoaKni");
		configuration.setProperty(NeedlemanWunsch.PARAM_THRESHOLD, "0.5");
		configuration.setProperty(
				"similarity.de.nilsreiter.event.similarity.WordNet", "1.0");
		AlignmentAlgorithm<Event> algo = af.getAlgorithm(configuration);
		List<Event> l1 = this.getDocuments().get(0).getEvents();
		List<Event> l2 = this.getDocuments().get(1).getEvents();
		Alignment<Event> alignment = algo.align(l1, l2);
		AlignmentWriter aw = new AlignmentWriter(System.out);
		aw.write(alignment);
		aw.close();

	}
}
