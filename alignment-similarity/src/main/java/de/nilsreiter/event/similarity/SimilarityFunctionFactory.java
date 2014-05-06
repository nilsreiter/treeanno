package de.nilsreiter.event.similarity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityDatabase;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class SimilarityFunctionFactory {

	Map<String, Class<? extends SimilarityFunction<Event>>> functions;

	SimilarityDatabase simDB = null;

	public SimilarityFunctionFactory(SimilarityDatabase db) {
		simDB = db;
		functions = new HashMap<String, Class<? extends SimilarityFunction<Event>>>();
		functions.put("FN", FrameNet.class);
		functions.put("WN", WordNet.class);
		functions.put("VN", VerbNet.class);
		functions.put("GD", GaussianDistanceSimilarity.class);
		functions.put("AT", ArgumentText.class);

	}

	public SimilarityFunction<Event> getSimilarityFunction(
			final SimilarityConfiguration simConf) {
		final List<Class<? extends SimilarityFunction<Event>>> l = new LinkedList<Class<? extends SimilarityFunction<Event>>>();
		for (int i = 0; i < simConf.similarityFunctions.size(); i++) {
			if (simConf.weights.size() > i) {
				for (int j = 0; j < Integer.valueOf(simConf.weights.get(i)); i++) {
					l.add(functions.get(simConf.similarityFunctions.get(i)));
				}
			} else {
				l.add(functions.get(simConf.similarityFunctions.get(i)));
			}
		}
		return new SimilarityFunction<Event>() {

			@Override
			public Probability sim(Event arg0, Event arg1)
					throws IncompatibleException {
				try {
					List<Probability> lp = new LinkedList<Probability>();
					for (Class<? extends SimilarityFunction<Event>> simFun : l) {
						lp.add(Probability.fromProbability(simDB.getSimilarity(
								simFun, arg0, arg1)));
					}
					switch (simConf.combination) {
					case GEO:
						return PMath.geometricMean(lp);

					case HARM:
						return PMath.harmonicMean(lp);

					case MULT:
						return PMath.multiply(lp);
					case AVG:
					default:
						return PMath.arithmeticMean(lp);

					}
				} catch (SQLException e) {
					e.printStackTrace();
					return Probability.NULL;
				}
			}

			@Override
			public void readConfiguration(SimilarityConfiguration tc) {
			}
		};

	}
}