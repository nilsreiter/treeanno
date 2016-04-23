package de.ustu.ims.reiter.treeanno.uima;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;
import de.ustu.ims.reiter.treeanno.tree.Node;
import de.ustu.ims.reiter.treeanno.tree.PrintParenthesesWalker;
import de.ustu.ims.reiter.treeanno.tree.Tree;
import de.ustu.ims.reiter.treeanno.tree.Walker;

public class GraphExporter extends JCasConsumer_ImplBase {

	public static final String PARAM_OUTPUT_DIRECTORY = "Output Directory";

	@ConfigurationParameter(name = PARAM_OUTPUT_DIRECTORY)
	String outputLocationPath;

	File outputLocation;

	@Override
	public void initialize(final UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);

		outputLocation = new File(outputLocationPath);
		if (!outputLocation.isDirectory())
			throw new ResourceInitializationException();
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		Tree<TreeSegment> tree = new Tree<TreeSegment>();
		tree.setRoot(new Node<TreeSegment>(null));

		// this only works if the ordering has not changed, because the parents
		// always precede their children
		for (TreeSegment ts : JCasUtil.select(jcas, TreeSegment.class)) {
			if (ts.getParent() == null) {
				tree.addChild(null, ts);
			} else {
				tree.addChild(ts.getParent(), ts);
			}
		}
		Walker<TreeSegment> walker = new PrintParenthesesWalker<TreeSegment>();
		tree.depthFirstWalk(walker);
		// System.err.println(walker.toString());

		String did = DocumentMetaData.get(jcas).getDocumentId();
		File outFile = new File(outputLocation, did + ".par");
		Writer os = null;
		try {
			os = new FileWriter(outFile);
			os.write(walker.toString());
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(os);
		}

	}

}
