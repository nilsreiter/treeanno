package de.uniheidelberg.cl.a10.main;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.cluster.ClusteringAlgorithm;
import de.uniheidelberg.cl.a10.cluster.ClusteringAlgorithmFactory;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.cluster.impl.WordOverlap_impl;
import de.uniheidelberg.cl.a10.cluster.io.PartitionListWriter;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class ClusterMain extends MainWithInputDocuments {

	ClusterConf config = new ClusterConf();

	public static void main(final String[] args)
			throws ParserConfigurationException, SAXException, IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Main.initProperties();
		ClusterMain asm = new ClusterMain();
		asm.config.clusteringOptions.put("number", "1");
		asm.processArguments(args, asm.config);
		asm.run();
	}

	private void run() throws ParserConfigurationException, SAXException,
			IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		List<? extends IPartition<Document>> result = this.experiment();
		// System.err.println(result);
		PartitionListWriter writer = new PartitionListWriter(
				this.getOutputStreamForFileOption(config.output, System.out));
		writer.write(result);
	}

	public List<? extends IPartition<Document>> experiment()
			throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		ClusteringAlgorithmFactory<Document> factory = new ClusteringAlgorithmFactory<Document>();
		ClusteringAlgorithm<Document, Probability> algor = factory
				.getClusteringAlgorithm(config.algorithm);

		algor.setDocumentSimilarityFunction(new WordOverlap_impl());

		algor.parseOptionMap(config.clusteringOptions);

		algor.cluster(getDocuments());

		return algor.getPartitionHistory();
	}
}
