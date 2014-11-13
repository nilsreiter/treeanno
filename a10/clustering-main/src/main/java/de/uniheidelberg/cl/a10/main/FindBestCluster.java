package de.uniheidelberg.cl.a10.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import nu.xom.Builder;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;

import de.uniheidelberg.cl.a10.MainWithIO;
import de.uniheidelberg.cl.a10.cluster.CachedSimilarityFunction;
import de.uniheidelberg.cl.a10.cluster.IDocumentSimilarityFunction;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.cluster.NumberOfClusters;
import de.uniheidelberg.cl.a10.cluster.PartitionScorer;
import de.uniheidelberg.cl.a10.cluster.VarianceRatioCriterion;
import de.uniheidelberg.cl.a10.cluster.impl.WordOverlap_impl;
import de.uniheidelberg.cl.a10.cluster.io.PartitionListReader;
import de.uniheidelberg.cl.a10.cluster.io.PartitionReader;
import de.uniheidelberg.cl.a10.cluster.io.PartitionWriter;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class FindBestCluster extends MainWithIO {

	enum Style {
		VRC, NUM
	};

	@Option(name = "--cache-file", usage = "Read cached similarities from this file, no need to specify the BMM configuration", aliases = { "-cf" })
	public File cacheFile = null;

	@Option(name = "--style", usage = "How to identify the best partition")
	public Style style = Style.VRC;

	@Option(name = "--clusterNumber", aliases = { "-n" }, usage = "Number of clusters, if --style == NUM")
	public int clusterNumber = 0;

	public static void main(final String[] args) throws FileNotFoundException,
			IOException, ParserConfigurationException, SAXException {
		FindBestCluster fbc = new FindBestCluster();
		fbc.processArguments(args);
		fbc.run();
	}

	private void run() throws FileNotFoundException, IOException,
			ParserConfigurationException, SAXException {

		List<IPartition<Document>> partitionList = null;
		nu.xom.Document document = getDocument(this.getInputStream());
		if (PartitionListReader.isDocument(document)) {
			PartitionListReader plr = new PartitionListReader(
					this.getDataDirectory());
			partitionList = plr.read(document);
		} else if (PartitionReader.isDocument(document)) {
			partitionList = new LinkedList<IPartition<Document>>();
			PartitionReader pr = new PartitionReader(this.getDataDirectory());
			partitionList.add(pr.read(document));
		}

		PartitionScorer<Document> scorer = null;
		switch (style) {
		case NUM:
			NumberOfClusters<Document> num = new NumberOfClusters<Document>(
					this.clusterNumber);
			scorer = num;
			break;
		default:
			IDocumentSimilarityFunction<Document, Probability> csf;
			if (cacheFile != null)
				csf = new CachedSimilarityFunction(getDataDirectory(),
						cacheFile);
			else
				csf = new WordOverlap_impl();
			VarianceRatioCriterion<Document> vrc = new VarianceRatioCriterion<Document>();
			vrc.setSimilarityFunction(csf);
			scorer = vrc;
			break;
		}

		IPartition<Document> bestPartition = null;
		double bestVRC = 0.0;
		for (IPartition<Document> partition : partitionList) {
			double vrcresult = scorer.getScore(partition);
			if (vrcresult > bestVRC) {
				bestVRC = vrcresult;
				bestPartition = partition;
			}
			logger.info(style.toString() + "(k=" + partition.size() + ") = "
					+ vrcresult);
		}
		if (bestPartition != null) {
			PartitionWriter pw = new PartitionWriter(this.getOutputStream());
			pw.write(bestPartition);
		}

	}

	protected nu.xom.Document getDocument(InputStream is) {
		Builder xBuilder = new Builder();

		// DOMReader domReader = new DOMReader();

		nu.xom.Document doc;
		try {
			doc = xBuilder.build(is);
			return doc;
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// domReader.read(dBuilder.parse(is));
		return null;
	}
}
