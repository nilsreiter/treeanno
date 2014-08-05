package de.nilsreiter.event.similarity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.nilsreiter.event.similarity.impl.SimilarityDatabase_impl;
import de.nilsreiter.util.db.Database;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
import de.uniheidelberg.cl.a10.patterns.similarity.Operation;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class SimilarityFunctionFactory {

	Map<String, Class<? extends SimilarityFunction<Event>>> functions;

	SimilarityDatabase_impl<Event> simDB = null;

	public SimilarityFunctionFactory(SimilarityDatabase_impl<Event> db) {
		simDB = db;
		functions =
				new HashMap<String, Class<? extends SimilarityFunction<Event>>>();
		functions.put("FN", FrameNet.class);
		functions.put("WN", WordNet.class);
		functions.put("VN", VerbNet.class);
		functions.put("GD", GaussianDistanceSimilarity.class);
		functions.put("AT", ArgumentText.class);

	}

	@Deprecated
	public SimilarityFunction<Event> getSimilarityFunction(
			final SimilarityConfiguration simConf) {
		final List<Class<? extends SimilarityFunction<Event>>> l =
				new LinkedList<Class<? extends SimilarityFunction<Event>>>();
		final List<Double> weights = new LinkedList<Double>();
		for (int i = 0; i < simConf.similarityFunctions.length; i++) {
			weights.add(Double.valueOf(simConf.weights[i]));

			l.add(functions.get(simConf.similarityFunctions[i]));

		}
		return new SimilarityFunction<Event>() {

			@Override
			public Probability sim(Event arg0, Event arg1) {
				try {
					List<Probability> lp = new LinkedList<Probability>();
					for (Class<? extends SimilarityFunction<Event>> simFun : l) {
						lp.add(Probability.fromProbability(simDB.getSimilarity(
								simFun, arg0, arg1)));
					}
					switch (simConf.getCombination()) {
					case GEO:
						return PMath.geometricMean(lp, weights);
					case HARM:
					case MULT:
						throw new UnsupportedOperationException();
					case AVG:
					default:
						// TODO: This is currently not weighted!
						return PMath.arithmeticMean(lp);

					}
				} catch (SQLException e) {
					e.printStackTrace();
					return Probability.NULL;
				}
			}

			@Override
			public void readConfiguration(Object tc) {}
		};

	}

	public static EventSimilarityFunction getSimilarityFunction(
			Database database, SimilarityConfiguration configuration) {
		try {
			final SimilarityDatabase<Event> simDB =
					new SimilarityDatabase_impl<Event>(database);

			String[] classes = configuration.getSimilarityFunctions();
			String[] weights = configuration.getWeights();
			if (weights == null) weights = new String[0];

			List<Class<? extends EventSimilarityFunction>> functionList =
					new LinkedList<Class<? extends EventSimilarityFunction>>();
			List<Double> weightList = new LinkedList<Double>();

			for (int i = 0; i < classes.length; i++) {

				double weight = 1.0;
				if (weights.length > i) weight = Double.valueOf(weights[i]);

				String className = classes[i];
				Class<?> cl = Class.forName(className);
				if (EventSimilarityFunction.class.isAssignableFrom(cl)) {
					@SuppressWarnings("unchecked")
					Class<? extends EventSimilarityFunction> simClass =
					(Class<? extends EventSimilarityFunction>) cl;
					functionList.add(simClass);
					weightList.add(weight);
				}
			}

			return new CachingSimilarityFunction(functionList, weightList,
					simDB, configuration.getCombination());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	};

	static class CachingSimilarityFunction implements EventSimilarityFunction {

		List<Class<? extends EventSimilarityFunction>> functions;
		List<Double> weights;
		Logger logger = LoggerFactory
				.getLogger(CachingSimilarityFunction.class);
		SimilarityDatabase<Event> simDB;
		Map<String, Matrix<Event, Event, Double>> cache = null;
		Set<String> activePair = new HashSet<String>();
		Operation operation;

		public CachingSimilarityFunction(
				List<Class<? extends EventSimilarityFunction>> functions,
				List<Double> weights, SimilarityDatabase<Event> simDB,
				Operation comb) {
			this.functions = functions;
			this.weights = weights;
			this.simDB = simDB;
			this.operation = comb;
		}

		@Override
		public Probability sim(Event arg0, Event arg1) {

			if (!activePair.contains(arg0.getRitualDocument().getId())
					|| !activePair.contains(arg1.getRitualDocument().getId()))
				cache = null;

			double p = 0.0;
			if (cache == null) {
				try {
					logger.info("Retrieving similarities from database");
					cache =
							simDB.getSimilarities(arg0.getRitualDocument(),
									arg1.getRitualDocument());
					activePair.clear();
					activePair.add(arg0.getRitualDocument().getId());
					activePair.add(arg1.getRitualDocument().getId());
					logger.info("Retrieved similarities from database for "
							+ arg0.getRitualDocument().getId() + " and "
							+ arg1.getRitualDocument().getId());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			double wsum = 0.0;
			for (int i = 0; i < functions.size(); i++) {
				double w = weights.get(i);
				wsum += w;
				Class<? extends EventSimilarityFunction> func =
						functions.get(i);

				if (arg0.equals(arg1)) return Probability.ONE;
				String s = func.getSimpleName().substring(0, 4);
				Matrix<Event, Event, Double> m = cache.get(s);
				double p0 = m.get(arg0, arg1);
				switch (operation) {
				case GEO:
					p = p * (Math.pow(p0, w));
					break;
				default:
					p = p + (w * p0);
					break;
				}

			}
			logger.trace("Retrieved similarity (" + arg0.getGlobalId() + ","
					+ arg1.getGlobalId() + ")");

			double fp;
			switch (operation) {
			case GEO:
				fp = Math.pow(p, 1 / wsum);
				break;
			default:
				fp = p / wsum;
			}

			return Probability.fromProbability(fp);
		}

		@Override
		public String toString() {
			StringBuilder b = new StringBuilder();
			double wsum = 0;
			for (int i = 0; i < functions.size(); i++) {
				double w = weights.get(i);
				wsum += w;
				switch (operation) {
				case GEO:
					b.append(functions.get(i).getSimpleName()).append('^')
							.append(w).append('*');
					break;
				default:
					b.append(w).append('*')
							.append(functions.get(i).getSimpleName())
							.append("+");
					break;
				}

			}
			b.deleteCharAt(b.length() - 1);
			switch (operation) {
			case GEO:
				return b.insert(0, '(').append(')').append('^').append(1)
						.append('/').append(wsum).toString();
			default:
				return b.insert(0, '(').insert(0, wsum).insert(0, "/")
						.insert(0, 1).append(')').toString();
			}

		}

		@Override
		public void readConfiguration(Object tc) {

		}

	}

}