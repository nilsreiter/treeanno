package de.nilsreiter.event.similarity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;

import de.nilsreiter.event.similarity.impl.SimilarityDatabase_impl;
import de.nilsreiter.util.db.Database;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
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

	public SimilarityFunction<Event> getSimilarityFunction(
			final SimilarityConfiguration simConf) {
		final List<Class<? extends SimilarityFunction<Event>>> l =
				new LinkedList<Class<? extends SimilarityFunction<Event>>>();
		final List<Double> weights = new LinkedList<Double>();
		for (int i = 0; i < simConf.similarityFunctions.size(); i++) {
			weights.add(Double.valueOf(simConf.weights.get(i)));

			l.add(functions.get(simConf.similarityFunctions.get(i)));

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
					switch (simConf.combination) {
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
			Database database, Configuration configuration) {
		try {
			final SimilarityDatabase<Event> simDB =
					new SimilarityDatabase_impl<Event>(database);

			Iterator<String> keyIter = configuration.getKeys("similarity");
			Map<Class<? extends EventSimilarityFunction>, Double> weightMap =
					new HashMap<Class<? extends EventSimilarityFunction>, Double>();

			while (keyIter.hasNext()) {
				String key = keyIter.next();
				double weight = configuration.getDouble(key);

				String className = key.substring(11);
				Class<?> cl = Class.forName(className);
				if (EventSimilarityFunction.class.isAssignableFrom(cl)) {
					@SuppressWarnings("unchecked")
					Class<? extends EventSimilarityFunction> simClass =
					(Class<? extends EventSimilarityFunction>) cl;
					weightMap.put(simClass, weight);
				}
			}

			return new CachingSimilarityFunction(weightMap, simDB);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	};

	static class CachingSimilarityFunction implements EventSimilarityFunction {
		Map<Class<? extends EventSimilarityFunction>, Double> fWeightMap;
		Logger logger = Logger.getAnonymousLogger();
		SimilarityDatabase<Event> simDB;
		Map<String, Matrix<Event, Event, Double>> cache = null;
		Set<String> activePair = new HashSet<String>();

		public CachingSimilarityFunction(
				Map<Class<? extends EventSimilarityFunction>, Double> weightMap,
				SimilarityDatabase<Event> simDB) {
			this.fWeightMap = weightMap;
			this.simDB = simDB;
		}

		@Override
		public Probability sim(Event arg0, Event arg1) {

			if (!activePair.contains(arg0.getRitualDocument().getId())
					&& !activePair.contains(arg1.getRitualDocument().getId()))
				cache = null;

			double p = 0.0;
			for (Class<? extends EventSimilarityFunction> func : fWeightMap
					.keySet()) {
				try {
					double w = fWeightMap.get(func);
					if (cache == null) {
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

					}
					if (arg0.equals(arg1)) return Probability.ONE;
					String s = func.getSimpleName().substring(0, 4);
					Matrix<Event, Event, Double> m = cache.get(s);
					double p0 = m.get(arg0, arg1);
					p = p + (w * p0);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			logger.finer("Retrieved similarity (" + arg0.getGlobalId() + ","
					+ arg1.getGlobalId() + ")");

			return Probability.fromProbability(p);
		}

		@Override
		public String toString() {
			StringBuilder b = new StringBuilder();

			for (Class<? extends EventSimilarityFunction> func : fWeightMap
					.keySet()) {
				double w = fWeightMap.get(func);
				b.append(w).append('*').append(func.getSimpleName())
				.append("+");

			}
			return b.deleteCharAt(b.length() - 1).toString();

		}

		@Override
		public void readConfiguration(Object tc) {

		}

	}

}