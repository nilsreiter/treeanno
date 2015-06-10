package de.ustu.ims.reiter.treeanno.tools;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class ConvertDocuments {

	public static void main(String[] args) throws UIMAException, IOException {
		Options options = CliFactory.parseArguments(Options.class, args);

		CollectionReaderDescription crd =
				CollectionReaderFactory.createReaderDescription(
						XmiReader.class, XmiReader.PARAM_SOURCE_LOCATION,
						options.getInputDirectory() + File.separator + "*.xmi",
						XmiReader.PARAM_LENIENT, true);

		SimplePipeline.runPipeline(crd, AnalysisEngineFactory
				.createEngineDescription(MapToTreeAnnoClass.class,
						MapToTreeAnnoClass.PARAM_CLASSNAME,
						options.getSegmentClassName()), AnalysisEngineFactory
						.createEngineDescription(XmiWriter.class,
						XmiWriter.PARAM_TARGET_LOCATION,
						options.getOutputDirectory()));
	}

	interface Options {
		@Option
		File getInputDirectory();

		@Option
		File getOutputDirectory();

		@Option
		String getSegmentClassName();
	}

	public static class MapToTreeAnnoClass extends JCasAnnotator_ImplBase {

		public static final String PARAM_CLASSNAME = "Class name";

		@ConfigurationParameter(name = PARAM_CLASSNAME)
		String className;

		@Override
		public void process(JCas jcas) throws AnalysisEngineProcessException {
			try {
				Class<? extends Annotation> clObj =
						(Class<? extends Annotation>) Class.forName(className);
				int c = 0;
				for (Annotation anno : JCasUtil.select(jcas, clObj)) {
					AnnotationFactory.createAnnotation(jcas, anno.getBegin(),
							anno.getEnd(), TreeSegment.class).setId(c++);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new AnalysisEngineProcessException(e);
			} catch (ClassCastException e) {
				e.printStackTrace();
				throw new AnalysisEngineProcessException(e);
			}
		}

	}

}
