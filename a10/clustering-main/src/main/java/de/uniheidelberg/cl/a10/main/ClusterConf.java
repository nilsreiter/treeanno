package de.uniheidelberg.cl.a10.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.kohsuke.args4j.Option;

import de.uniheidelberg.cl.a10.cluster.EAlgorithm;
import de.uniheidelberg.cl.a10.cluster.measure.DocumentSimilarityMeasure;

public class ClusterConf {

	@Option(name = "--cache-directory",
			usage = "The directory, in which cache files are stored",
			aliases = { "-cd" })
	public File cache = new File("cache/storysim");

	@Option(name = "--cluster-options",
			aliases = "-co",
			usage = "Options for the clustering algorithm, e.g., number=5")
	public Map<String, String> clusteringOptions = new HashMap<String, String>();

	@Option(name = "--cache-file",
			usage = "Read cached similarities from this file, no need to specify the BMM configuration",
			aliases = { "-cf" })
	public File cacheFile = null;

	@Option(name = "--algorithm", usage = "The clustering algorithm to use")
	EAlgorithm algorithm = EAlgorithm.BottomUpMean;

	@Option(name = "--output", usage = "Target file for partition output")
	File output = null;

	@Option(name = "--documentsimilaritytype",
			aliases = { "-ds" },
			usage = "The document similarity measure to use")
	DocumentSimilarityMeasure.Type similarityType = DocumentSimilarityMeasure.Type.ONE;

}
