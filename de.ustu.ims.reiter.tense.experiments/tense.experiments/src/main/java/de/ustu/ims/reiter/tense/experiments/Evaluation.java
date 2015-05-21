package de.ustu.ims.reiter.tense.experiments;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import de.ustu.ims.reiter.tense.api.type.Future;

public class Evaluation {
	static Class<? extends Annotation> annoType = Future.class;

	static SummaryStatistics precision = new SummaryStatistics();
	static SummaryStatistics recall = new SummaryStatistics();

	public static void main(String[] args) throws UIMAException, IOException {
		File silverDirectory = new File("target/main/resources/silver");
		File goldDirectory = new File("target/main/resources/gold");

		TypeSystemDescription tsd =
				TypeSystemDescriptionFactory
						.createTypeSystemDescriptionFromPath(new File(
								goldDirectory, "typesystem.xml")
								.getAbsolutePath());

		for (File file : goldDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".xmi");
			}
		})) {
			String fname = file.getName();
			// System.err.println("Processing file " + fname);
			File sFile = new File(silverDirectory, fname);
			if (sFile.exists()) {
				JCas silv =
						JCasFactory.createJCas(sFile.getAbsolutePath(), tsd);
				JCas gold = JCasFactory.createJCas(file.getAbsolutePath(), tsd);
				evaluate2(gold, silv);
			}
		}
		System.out.println(precision.getSummary());
		System.out.println(recall.getSummary());
	}

	public static void evaluate2(JCas gold, JCas silver) {
		StringBuilder b = new StringBuilder();
		int tp = 0, fp = 0, fn = 0;

		int index = 0;
		Iterator<? extends Annotation> goldIterator =
				JCasUtil.select(gold, annoType).iterator();
		Iterator<? extends Annotation> silvIterator =
				JCasUtil.select(silver, annoType).iterator();
		Annotation goldAnno = null;
		Annotation silvAnno = null;
		boolean ga = true, sa = true;
		while ((goldIterator.hasNext() || !ga)
				&& (silvIterator.hasNext() || !sa)) {
			if (ga) goldAnno = goldIterator.next();
			if (sa) silvAnno = silvIterator.next();
			sa = true;
			ga = true;
			if (false)
				System.err.println("Comparing gold (" + goldAnno.getBegin()
						+ "," + goldAnno.getEnd() + ") with silver ("
						+ silvAnno.getBegin() + "," + silvAnno.getEnd() + ")");
			if ((goldAnno.getBegin() <= silvAnno.getEnd() && goldAnno
					.getBegin() >= silvAnno.getBegin())) {
				tp++;
			} else {
				if (goldAnno.getEnd() < silvAnno.getBegin()) {
					fn++;
					sa = false;
				}
				if (silvAnno.getEnd() < goldAnno.getBegin()) {
					fp++;
					ga = false;
				}

			}
		}

		double p = (double) tp / (fp + tp);
		double r = (double) tp / (fn + tp);
		precision.addValue(p);
		recall.addValue(r);
		b.append(tp).append('\t').append(fp).append('\t').append(fn)
				.append('\t').append(p).append('\t').append(r);
		System.out.println(b.toString());
	}
}
