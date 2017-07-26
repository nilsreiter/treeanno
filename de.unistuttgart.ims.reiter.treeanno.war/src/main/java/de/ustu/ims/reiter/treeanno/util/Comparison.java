package de.ustu.ims.reiter.treeanno.util;

import java.util.Iterator;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class Comparison {
	public static boolean equalSegmentation(JCas jcas1, JCas jcas2)
			throws UIMAException {
		Iterator<TreeSegment> iter1 =
				JCasUtil.iterator(jcas1, TreeSegment.class);
		Iterator<TreeSegment> iter2 =
				JCasUtil.iterator(jcas2, TreeSegment.class);
		int s1 = 0, s2 = 0;
		while (iter1.hasNext() && iter2.hasNext()) {
			TreeSegment ts1 = iter1.next();
			TreeSegment ts2 = iter2.next();
			if (ts1.getBegin() != ts2.getBegin()) return false;
			if (ts1.getEnd() != ts2.getEnd()) return false;
			s1++;
			s2++;
		}

		// if one of the CAS still has segments
		if (iter1.hasNext() || iter2.hasNext()) return false;

		return s1 == s2;
	}
}