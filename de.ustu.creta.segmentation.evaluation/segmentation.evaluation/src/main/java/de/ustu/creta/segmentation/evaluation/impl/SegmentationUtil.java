package de.ustu.creta.segmentation.evaluation.impl;

import java.util.Collection;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.uniheidelberg.cl.reiter.util.Counter;

public class SegmentationUtil {

	public static int[] getMassTuple(JCas jcas,
			Class<? extends Annotation> boundaryType) {
		Collection<? extends Annotation> boundaries =
				JCasUtil.select(jcas, boundaryType);
		Counter<Annotation> segUnits = new Counter<Annotation>();
		for (Annotation su : JCasUtil.select(jcas, SegmentationUnit.class)) {
			segUnits.add(su);
		}
	
		int units = segUnits.size();
		int[] masses = new int[boundaries.size() + 1];
		int i = 0, end = jcas.getDocumentText().length();
		Annotation prevAnno = null;
		Collection<? extends Annotation> coll;
	
		for (Annotation anno : boundaries) {
			if (prevAnno == null) {
				// Case before the first segment
				coll =
						JCasUtil.selectPreceding(SegmentationUnit.class, anno,
								Integer.MAX_VALUE);
			} else {
				// cases between the first and last segment boundary
				coll =
						JCasUtil.selectBetween(SegmentationUnit.class,
								prevAnno, anno);
			}
	
			// System.err.println(JCasUtil.toText(coll));
			masses[i++] = coll.size();
			segUnits.subtractAll(coll);
			prevAnno = anno;
		}
		// after the last segment boundary
		coll = null;
		if (prevAnno == null) {
			coll = JCasUtil.select(jcas, SegmentationUnit.class);
		} else
			coll =
			JCasUtil.selectBetween(SegmentationUnit.class, prevAnno,
					new Annotation(jcas, end + 1, end + 1));
		if (coll != null) {
			// System.err.println(i + ": " + coll.toString());
			masses[i] = coll.size();
			// System.err.println(JCasUtil.toText(coll));
			segUnits.subtractAll(coll);
	
		}
		int s = 0;
		for (int j = 0; j < masses.length; j++) {
			s += masses[j];
		}
		if (s != units) {
			System.err.println("units: " + units + ". Mass string: " + s);
		}
		if (segUnits.getHighestCount() > 0) {
			System.err.println(segUnits.getKeysWithMaxCount());
			System.err.println(JCasUtil.toText(segUnits.getKeysWithMaxCount()));
		}
		return masses;
	}

}
