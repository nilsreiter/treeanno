package de.ustu.ims.reiter.treeanno.uima;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;
import de.ustu.ims.reiter.treeanno.tree.Node;
import de.ustu.ims.reiter.treeanno.tree.PrintParenthesesWalker;

public class TestGraphExporter {
	JCas jcas;
	TreeSegment[] ts;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("Bla Bla Bla Bla");
		DocumentMetaData.create(jcas).setDocumentId("Test");
		ts =
				new TreeSegment[] {
				AnnotationFactory.createAnnotation(jcas, 0, 3,
						TreeSegment.class),
						AnnotationFactory.createAnnotation(jcas, 4, 7,
								TreeSegment.class),
								AnnotationFactory.createAnnotation(jcas, 8, 11,
										TreeSegment.class),
										AnnotationFactory.createAnnotation(jcas, 12, 14,
												TreeSegment.class) };
		ts[0].setId(0);
		ts[1].setId(1);
		ts[2].setId(2);
		ts[3].setId(3);
	}

	@Test
	public void testGraphExporter() throws AnalysisEngineProcessException,
			ResourceInitializationException, IOException {
		ts[1].setParent(ts[0]);

		String result = GraphExporter.getTreeString(jcas, new PrintParenthesesWalker<TreeSegment>());
		assertEquals("((())()())", result);
	}

	@Test
	public void testChildrensOrder2() {
		TreeSegment v1 = AnnotationFactory.createAnnotation(jcas, 0, 0, TreeSegment.class),
				v2 = AnnotationFactory.createAnnotation(jcas, 4, 4, TreeSegment.class);
		v1.setId(10);
		v2.setId(11);
		ts[0].setParent(v1);
		ts[1].setParent(v2);
		v2.setParent(v1);
		ts[2].setParent(v1);
		ts[3].setParent(v1);

		String result = GraphExporter.getTreeString(jcas,
				new PrintParenthesesWalker<TreeSegment>(PrintParenthesesWalker.treeSegmentId));
		System.out.println(result);
	}

	@Test
	public void testChildrensOrder() {
		ts[1].setParent(ts[0]);

		Node<TreeSegment> tree = GraphExporter.getTree(jcas).getRoot();
		assertNotNull(tree);
		assertEquals(3, tree.getChildren().size());

		TreeSegment v1 = AnnotationFactory.createAnnotation(jcas, 8, 8, TreeSegment.class),
				v2 = AnnotationFactory.createAnnotation(jcas, 8, 8, TreeSegment.class);

		v1.setId(10);
		v2.setId(11);

		// tree hierarchy:
		// root -> v1 -> v2 -> ts[2]

		v1.setParent(null);
		v2.setParent(v1);
		ts[2].setParent(v2);
		String result = GraphExporter.getTreeString(jcas,
				new PrintParenthesesWalker<TreeSegment>(PrintParenthesesWalker.treeSegmentId));
		assertEquals("(root(0(1))(10(11(2)))(3))", result);

		tree = GraphExporter.getTree(jcas).getRoot();
		assertEquals(3, tree.getChildren().size());

		// tree hierarchy
		// root -> {v1, v2, ts[2]}
		v1.setParent(ts[1]);
		v2.setParent(ts[1]);
		ts[2].setParent(ts[1]);

		tree = GraphExporter.getTree(jcas).getRoot();

		assertEquals(2, tree.getChildren().size());
		assertEquals("(root(0(1(11)(10)(2)))(3))", GraphExporter.getTreeString(jcas,
				new PrintParenthesesWalker<TreeSegment>(PrintParenthesesWalker.treeSegmentId)));
		

		// splitting a node
		ts[2].removeFromIndexes();
		ts[2] = AnnotationFactory.createAnnotation(jcas, 8, 9, TreeSegment.class);
		ts[2].setId(6);
		ts[2].setParent(v2);
		TreeSegment newRealSegment = AnnotationFactory.createAnnotation(jcas, 9, 11, TreeSegment.class);
		newRealSegment.setId(7);

		result = GraphExporter.getTreeString(jcas,
				new PrintParenthesesWalker<TreeSegment>(PrintParenthesesWalker.treeSegmentId));
		System.out.println(result);
	}
}
