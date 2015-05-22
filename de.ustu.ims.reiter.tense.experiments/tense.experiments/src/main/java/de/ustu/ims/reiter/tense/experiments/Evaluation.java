package de.ustu.ims.reiter.tense.experiments;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import de.ustu.ims.reiter.tense.api.type.Present;

public class Evaluation {
	static Class<? extends Annotation> annoType = Present.class;

	static ClassificationEvaluation classEval = new ClassificationEvaluation();

	static int numFiles = 10;

	public static void main(String[] args) throws UIMAException, IOException {
		File silverDirectory = new File("target/main/resources/silver");
		File goldDirectory = new File("target/main/resources/gold");

		TypeSystemDescription tsd =
				TypeSystemDescriptionFactory
				.createTypeSystemDescriptionFromPath(new File(
						goldDirectory, "typesystem.xml")
				.getAbsolutePath());

		int fileCounter = 0;
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
			fileCounter++;
			if (fileCounter >= numFiles) break;
		}
		System.out.println(classEval.toString());

	}

	public static void evaluate2(JCas gold, JCas silver) {

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
				classEval.tp();
			} else {
				if (goldAnno.getEnd() < silvAnno.getBegin()) {
					sa = false;
					classEval.fn();
				}
				if (silvAnno.getEnd() < goldAnno.getBegin()) {
					ga = false;
					classEval.fp();
				}

			}
		}

	}
}
