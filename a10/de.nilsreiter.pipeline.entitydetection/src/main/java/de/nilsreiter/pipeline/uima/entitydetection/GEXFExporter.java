package de.nilsreiter.pipeline.uima.entitydetection;

import it.uniroma1.dis.wsngroup.gexf4j.core.EdgeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.Gexf;
import it.uniroma1.dis.wsngroup.gexf4j.core.Graph;
import it.uniroma1.dis.wsngroup.gexf4j.core.Mode;
import it.uniroma1.dis.wsngroup.gexf4j.core.Node;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.entitydetection.type.Entity;
import de.nilsreiter.pipeline.uima.entitydetection.type.Relation;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

public class GEXFExporter extends JCasConsumer_ImplBase {
	public static final String PARAM_OUTPUT_DIRECTORY = "Output Directory";

	@ConfigurationParameter(name = PARAM_OUTPUT_DIRECTORY)
	String outputDirectory;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Gexf gexf = new GexfImpl();
		Calendar date = Calendar.getInstance();

		gexf.getMetadata().setLastModified(date.getTime())
				.setCreator("Gephi.org");
		Graph graph = gexf.getGraph();
		graph.setDefaultEdgeType(EdgeType.UNDIRECTED).setMode(Mode.STATIC);

		Map<String, Node> nodeIndex = new HashMap<String, Node>();
		int entityId = 0;
		for (Entity entity : JCasUtil.select(jcas, Entity.class)) {
			if (!nodeIndex.containsKey(entityId)
					&& entity.getEntityType() != null
					&& entity.getEntityType().equalsIgnoreCase("Person")) {
				Node eNode = graph.createNode(entity.getIdentifier());
				nodeIndex.put(entity.getIdentifier(), eNode);
				eNode.setLabel(entity.getName());
			}
		}

		int relationNumber = 0;
		for (Relation relation : JCasUtil.select(jcas, Relation.class)) {
			Node n1 =
					nodeIndex.get(relation.getArguments(0).getEntity()
							.getIdentifier());
			Node n2 =
					nodeIndex.get(relation.getArguments(1).getEntity()
							.getIdentifier());
			try {

				n1.connectTo("rel" + relationNumber++, relation.getName(), n2);
			} catch (java.lang.IllegalArgumentException e) {} catch (NullPointerException e) {}
		}

		StaxGraphWriter graphWriter = new StaxGraphWriter();

		File outDir = new File(outputDirectory);
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		File f =
				new File(outDir, JCasUtil.selectSingle(jcas,
						DocumentMetaData.class).getDocumentId()
						+ ".gexf");
		if (f.exists()) {
			f.delete();
		}
		Writer out;
		try {
			out = new FileWriter(f, false);
			graphWriter.writeToStream(gexf, out, "UTF-8");
			System.out.println(f.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
