package de.ustu.ims.reiter.tense.experiments;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

	static ClassificationEvaluation<Annotation> classEval =
			new ClassificationEvaluation<Annotation>();

	static int numFiles = 10;

	public static void main(String[] args) throws UIMAException, IOException {
		File silverDirectory = new File("target/main/resources/silver");
		File goldDirectory = new File("target/main/resources/gold");

		TypeSystemDescription tsd =
				TypeSystemDescriptionFactory
				.createTypeSystemDescriptionFromPath(new File(
						silverDirectory, "typesystem.xml")
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
				evaluate3(gold, silv);
			}
			fileCounter++;
			if (fileCounter >= numFiles) break;
		}
		System.out.println(classEval.toString());

		System.out.println("False Negatives");
		for (Annotation anno : classEval.getFalseNegatives()) {
			System.out.println(anno.getBegin() + "," + anno.getEnd() + "  "
					+ anno.getCoveredText());
		}

		System.out.println("False Positives");
		for (Annotation anno : classEval.getFalsePositives()) {
			System.out.println(anno.getBegin() + "," + anno.getEnd() + "  "
					+ anno.getCoveredText());
		}
	}

	public static void evaluate2(JCas gold, JCas silver) {

		Iterator<? extends Annotation> goldIterator =
				JCasUtil.select(gold, annoType).iterator();
		Iterator<? extends Annotation> silvIterator =
				JCasUtil.select(silver, annoType).iterator();

		// silver.getAnnotationIndex(JCasUtil.getType(silver, annoType)).;
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

	public static void evaluate3(JCas gold, JCas silver) {
		Set<? extends Annotation> goldColl =
				new HashSet<Annotation>(JCasUtil.select(gold, annoType));
		Set<? extends Annotation> silvColl =
				new HashSet<Annotation>(JCasUtil.select(silver, annoType));

		Iterator<? extends Annotation> goldIterator =
				JCasUtil.select(gold, annoType).iterator();

		Annotation goldAnno = null;
		Annotation silvAnno = null;
		int tp = 0;
		while ((goldIterator.hasNext())) {
			goldAnno = goldIterator.next();
			// System.err.println("Comparing gold (" + goldAnno.getBegin() + ","
			// + goldAnno.getEnd() + ") with silver ("
			// + silvAnno.getBegin() + "," + silvAnno.getEnd() + ")");
			List<? extends Annotation> silverAnnos =
					JCasUtil.selectBetween(
							silver,
							annoType,
							new Annotation(silver, goldAnno.getBegin(),
									goldAnno.getBegin()),
							new Annotation(silver, goldAnno.getEnd(), goldAnno
									.getEnd()));
			if (silverAnnos.isEmpty()) {
				classEval.fn(goldAnno);
			}
			for (Annotation sAnno : silverAnnos) {
				if (sAnno.getBegin() == goldAnno.getBegin()
						&& sAnno.getEnd() == goldAnno.getEnd()) {
					classEval.tp();
					silvColl.remove(sAnno);
				}
			}

		}

		for (Annotation anno : silvColl) {
			classEval.fp(anno);
		}
	}
}
