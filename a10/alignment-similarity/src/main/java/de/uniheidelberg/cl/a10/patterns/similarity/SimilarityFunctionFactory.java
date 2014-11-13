package de.uniheidelberg.cl.a10.patterns.similarity;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SimilarityFunctionFactory<T> {
	boolean useCache = false;
	Map<String, SimilarityFunction<T>> functionIndex;

	Map<String, String> functionAliases = new HashMap<String, String>();

	Map<Class<? extends SimilarityFunction<T>>, SimilarityFunction<T>> functions =
			new HashMap<Class<? extends SimilarityFunction<T>>, SimilarityFunction<T>>();

	public SimilarityFunctionFactory() {
		this.functionIndex = new HashMap<String, SimilarityFunction<T>>();

		this.addFunctionAlias("GDS", "GaussianDistanceSimilarity");
		this.addFunctionAlias("WNS", "WordNetSimilarity");
		this.addFunctionAlias("FNS", "FrameNetSimilarity");
		this.addFunctionAlias("VNS", "VerbNetSimilarity");
		this.addFunctionAlias("ATS", "ArgumentTextSimilarity");
		this.addFunctionAlias("MS", "MantraSimilarity");
		this.addFunctionAlias("RND", "RandomSimilarity");
	}

	public SimilarityFunction<T> getSimilarityFunction(
			final Class<? extends SimilarityFunction<T>> clazz)
					throws InstantiationException, IllegalAccessException {
		if (!functions.containsKey(clazz)) {
			functions.put(clazz, clazz.newInstance());
		}
		return clazz.newInstance(); // 0 functions.get(clazz);
	}

	public synchronized SimilarityFunction<T> getSimilarityFunction(
			final String functionName, final SimilarityConfiguration sConf)
					throws InstantiationException, IllegalAccessException {
		if (this.functionIndex.containsKey(functionName
				+ sConf.getCommandLine())) {
			return this.functionIndex
					.get(functionName + sConf.getCommandLine());
		}
		Class<?> cl = null;
		try {
			cl =
					Class.forName(this.getClass().getPackage().getName() + "."
							+ functionName);
		} catch (ClassNotFoundException e) {

		}
		if (cl != null && SimilarityFunction.class.isAssignableFrom(cl)) {
			@SuppressWarnings("unchecked")
			SimilarityFunction<T> sf =
					this.getSimilarityFunction((Class<? extends SimilarityFunction<T>>) cl);
			this.functionIndex.put(functionName + sConf.getCommandLine(), sf);
			return sf;
		}
		return null;
	}

	public SimilarityFunction<T> getSimilarityFunction(
			final SimilarityConfiguration bmmc) throws FileNotFoundException,
			SecurityException, InstantiationException, IllegalAccessException {
		CombinedSimilarityFunction<T> csf = new CombinedSimilarityFunction<T>();

		csf.setOperation(bmmc.combination);
		int i = 0;
		Collection<String> similFunctions = new HashSet<String>();

		for (String sf : bmmc.similarityFunctions) {
			if (sf.equals("all")) {
				similFunctions.addAll(Arrays.asList(
						"GaussianDistanceSimilarity", "WordNetSimilarity",
						"FrameNetSimilarity", "VerbNetSimilarity",
						"ArgumentTextSimilarity"));
			} else {
				similFunctions.add(sf);
			}
		}

		for (String sf : similFunctions) {
			SimilarityFunction<T> simFun = null;
			double w = 1;
			try {
				w = Double.valueOf(bmmc.getWeights()[i]);
			} catch (IndexOutOfBoundsException e) {}
			if (this.functionAliases.containsKey(sf)) {
				simFun =
						this.getSimilarityFunction(functionAliases.get(sf),
								bmmc);
			} else {
				simFun = this.getSimilarityFunction(sf, bmmc);
			}
			simFun.readConfiguration(bmmc);
			if (w != 1) {
				csf.addFeature(simFun, w);
			} else {
				csf.addFeature(simFun);
			}
			i++;
		}
		csf.readConfiguration(bmmc);
		return csf;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public String addFunctionAlias(final String arg0, final String arg1) {
		return functionAliases.put(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public String getFunctionAlias(final Object arg0) {
		return functionAliases.get(arg0);
	}
}
