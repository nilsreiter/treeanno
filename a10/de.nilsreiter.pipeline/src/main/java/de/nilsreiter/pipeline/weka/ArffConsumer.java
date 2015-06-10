package de.nilsreiter.pipeline.weka;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.io.IOUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.FeatureDescription;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * A UIMA component to generate a Weka-compatible arff-file from a set of
 * {@link weka.core.Instances}, based on an annotation type that has to be
 * specified. Features of type {@code uima.cas.String}, {@code uima.cas.Double}
 * and {@code uima.cas.Integer} are converted into string resp. numeric
 * features. This component generates a single arff file for the entire corpus,
 * which also means the entire Instances object has to fit into the memory.
 * 
 * @author reiterns
 *
 */
public class ArffConsumer extends JCasConsumer_ImplBase {

	public static final String PARAM_ANNOTATION_TYPE = "Annotation Type";
	public static final String PARAM_DATASET_NAME = "Dataset Name";
	public static final String PARAM_OUTPUT_FILE = "Output File";
	public static final String PARAM_CLASS_FEATURE = "Class Feature";

	/**
	 * The annotation type to use for instances.
	 */
	@ConfigurationParameter(name = PARAM_ANNOTATION_TYPE, mandatory = true)
	String annotationType =
			de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause.class
					.getCanonicalName();

	/**
	 * A name for the data set.
	 */
	@ConfigurationParameter(name = PARAM_DATASET_NAME, mandatory = false,
			defaultValue = "Data Set")
	String datasetName = "Data Set";

	/**
	 * The output file name
	 */
	@ConfigurationParameter(name = PARAM_OUTPUT_FILE)
	String outputFile;

	/**
	 * Which feature is the class feature? It will be the last feature in the
	 * generated file.
	 */
	@ConfigurationParameter(name = PARAM_CLASS_FEATURE, mandatory = true)
	String classFeatureName;

	Instances instances;

	TypeDescription typeDescription;

	FastVector attributes;
	Map<String, Integer> attributeMap;

	@Override
	public void initialize(final UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		attributeMap = new HashMap<String, Integer>();
		attributes = new FastVector();
		TypeSystemDescription tsd =
				TypeSystemDescriptionFactory.createTypeSystemDescription();
		typeDescription = tsd.getType(annotationType);
		Attribute classAttribute = null;

		if (typeDescription == null)
			throw new ResourceInitializationException(
					ResourceInitializationException.COULD_NOT_INSTANTIATE,
					new String[] { annotationType });
		FeatureDescription[] fds = typeDescription.getFeatures();
		int f = 0;
		for (int i = 0; i < fds.length; i++) {
			Attribute attr = null;
			FeatureDescription fd = fds[i];
			if (fd.getName().equals(classFeatureName)) {
				classAttribute = makeAttribute(fd);
			} else {
				attr = makeAttribute(fd);
			}
			if (attr != null) {
				attributes.addElement(attr);
				attributeMap.put(fd.getName(), f++);
			}
		}

		attributes.addElement(classAttribute);
		attributeMap.put(classFeatureName, f);
		instances = new Instances(datasetName, attributes, 0);
	}

	protected Attribute makeAttribute(FeatureDescription fd) {
		String rangeTypeName = fd.getRangeTypeName();

		Attribute attr = null;
		if (rangeTypeName.equals("uima.cas.String")) {
			attr = new Attribute(fd.getName(), (FastVector) null);
		} else if (rangeTypeName.equals("uima.cas.Integer")) {
			attr = new Attribute(fd.getName());
		} else if (rangeTypeName.equals("uima.cas.Double")) {
			attr = new Attribute(fd.getName());
		} else if (rangeTypeName.equals("uima.cas.Boolean")) {
			attr = new Attribute(fd.getName());
		}
		return attr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Class<? extends Annotation> cl = null;
		try {
			cl = (Class<? extends Annotation>) Class.forName(annotationType);
		} catch (ClassNotFoundException e) {
			throw new AnalysisEngineProcessException(e);
		} catch (ClassCastException e) {
			throw new AnalysisEngineProcessException(e);
		}
		Type type = jcas.getTypeSystem().getType(typeDescription.getName());
		for (Annotation anno : JCasUtil.select(jcas, cl)) {
			Instance instance = new Instance(attributes.size());
			instance.setDataset(instances);
			for (Feature feature : type.getFeatures()) {
				if (feature.getDomain().equals(type)) {
					if (feature.getRange().getName().equals("uima.cas.String")) {
						if (anno == null
								|| anno.getFeatureValueAsString(feature) == null) {
							instance.setMissing(attributeMap.get(feature
									.getShortName()));
						} else
							instance.setValue(
									attributeMap.get(feature.getShortName()),
									anno.getFeatureValueAsString(feature));
					} else if (feature.getRange().getName()
							.equals("uima.cas.Integer")) {
						instance.setValue(
								attributeMap.get(feature.getShortName()),
								anno.getIntValue(feature));
					} else if (feature.getRange().getName()
							.equals("uima.cas.Double")) {
						instance.setValue(
								attributeMap.get(feature.getShortName()),
								anno.getDoubleValue(feature));
					} else if (feature.getRange().getName()
							.equals("uima.cas.Boolean")) {
						instance.setValue(
								attributeMap.get(feature.getShortName()),
								(anno.getBooleanValue(feature) ? 1 : 0));
					}
				}
			}
			instances.add(instance);
		}

	}

	/**
	 * Prints the arff file using {@link weka.core.Instances#toString()}.
	 */
	@Override
	public void collectionProcessComplete()
			throws AnalysisEngineProcessException {
		super.collectionProcessComplete();
		FileWriter aw = null;
		// System.err.println(instances.toString());
		try {
			aw = new FileWriter(new File(outputFile));
			aw.write(instances.toString());
			aw.flush();
			aw.close();
		} catch (IOException e) {
			throw new AnalysisEngineProcessException(e);
		} finally {
			IOUtils.closeQuietly(aw);
		}
	}
}