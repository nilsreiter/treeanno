package de.ustu.ims.reiter.treeanno.uima;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

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
import de.ustu.ims.reiter.treeanno.tree.Tree;
import de.ustu.ims.reiter.treeanno.tree.TreeSegmentComparator;
import de.ustu.ims.reiter.treeanno.tree.Walker;

public class GraphExporter extends JCasConsumer_ImplBase {

	public static final String PARAM_OUTPUT_DIRECTORY = "Output Directory";
	public static final String PARAM_WALKER_CLASS_NAME = "Walker Class";

	public static final TreeSegmentComparator tsComparator = new TreeSegmentComparator();

	@ConfigurationParameter(name = PARAM_OUTPUT_DIRECTORY)
	String outputLocationPath;

	@ConfigurationParameter(name = PARAM_WALKER_CLASS_NAME)
	String walkerClassName;

	File outputLocation;

	Class<? extends Walker<?>> walkerClass;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(final UimaContext context) throws ResourceInitializationException {
		super.initialize(context);

		outputLocation = new File(outputLocationPath);
		if (!outputLocation.isDirectory())
			throw new ResourceInitializationException();

		try {
			walkerClass = (Class<? extends Walker<?>>) Class.forName(walkerClassName);
		} catch (ClassNotFoundException e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		String treeString;
		try {
			treeString = getTreeString(jcas, (Walker<TreeSegment>) walkerClass.newInstance());
		} catch (InstantiationException | IllegalAccessException e1) {
			throw new AnalysisEngineProcessException(e1);
		}

		String did = DocumentMetaData.get(jcas).getDocumentId();
		File outFile = new File(outputLocation, did + ".par");
		Writer os = null;
		try {
			os = new FileWriter(outFile);
			os.write(treeString);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(os);
		}

	}


	public static Tree<TreeSegment> getTree(JCas jcas) {
		Tree<TreeSegment> tree = new Tree<TreeSegment>(tsComparator);
		tree.setRoot(new Node<TreeSegment>(null, tsComparator));

		// this only works if the ordering has not changed, because the parents
		// always precede their children
		// SortedSet<TreeSegment> waiters = new
		// SortedSet<TreeSegment>(JCasUtil.select(jcas, type))
		List<TreeSegment> waiters = new LinkedList<TreeSegment>(JCasUtil.select(jcas, TreeSegment.class));

		while (!waiters.isEmpty()) {
			TreeSegment ts = waiters.remove(0);
			if (ts.getParent() == null) {
				tree.addChild(null, ts);
			} else {
				try {
					tree.addChild(ts.getParent(), ts);
				} catch (NullPointerException e) {
					waiters.add(ts);
				}
			}

		}
		return tree;
	}

	public static String getTreeString(JCas jcas, Walker<TreeSegment> walker) {
		Tree<TreeSegment> tree = getTree(jcas);
		tree.getRoot().depthFirstWalk(walker);
		return walker.toString();
	}

}
