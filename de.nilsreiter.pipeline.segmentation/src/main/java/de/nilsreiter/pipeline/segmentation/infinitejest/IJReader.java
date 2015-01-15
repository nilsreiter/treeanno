package de.nilsreiter.pipeline.segmentation.infinitejest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasBuilder;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Progress;

import de.nilsreiter.pipeline.segmentation.type.Segment;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;

@TypeCapability(
		outputs = { "de.nilsreiter.pipeline.segmentation.type.Segment" })
public class IJReader extends
		JCasResourceCollectionReader_ImplBase {

	public static final String PARAM_CREATE_BOUNDARY_ANNOTATION =
			"Create Boundary Annotation";
	public static final String PARAM_CREATE_SEGMENT_ANNOTATION =
			"Create Segment Annotation";

	boolean next = true;

	@ConfigurationParameter(name = PARAM_CREATE_BOUNDARY_ANNOTATION,
			defaultValue = "true")
	boolean createBoundaries = true;

	@ConfigurationParameter(name = PARAM_CREATE_SEGMENT_ANNOTATION,
			defaultValue = "true")
	boolean createSegments = true;

	List<String> restriction = Arrays.asList("AFR", "EHDRH", "ETA");

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return next;
	}

	@Override
	public Progress[] getProgress() {

		return new Progress[] {};
	}

	enum Mode {
		TEXT, PAR
	}

	@Override
	public void getNext(JCas jCas) throws IOException, CollectionException {
		Resource res = nextFile();
		initCas(jCas, res);

		// System.err.println("!");
		InputStreamReader isr = new InputStreamReader(res.getInputStream());

		Map<String, Integer> openSegments = new HashMap<String, Integer>();

		JCasBuilder jb = new JCasBuilder(jCas);
		StringBuilder sb = new StringBuilder();

		while (isr.ready()) {
			char ch = (char) isr.read();

			if (ch == '<') {
				jb.add(sb.toString());
				sb = new StringBuilder();
			} else if (ch == '>') {
				String tag = sb.toString();
				if (tag.startsWith("/")) {
					String rtag = tag.substring(1);
					if (openSegments.containsKey(rtag)) {
						if (restriction.contains(rtag)
								&& (createSegments || createBoundaries))
							jb.add(openSegments.get(rtag), Segment.class)
									.setValue(rtag);
						openSegments.remove(rtag);
					}
				} else {
					openSegments.put(tag, jb.getPosition());
				}

				sb = new StringBuilder();
			} else {
				sb.append(ch);
			}

		}

		isr.close();
		jb.close();

		if (createBoundaries)
			for (Segment segment : JCasUtil.select(jCas, Segment.class)) {
				AnnotationFactory.createAnnotation(jCas, segment.getBegin(),
						segment.getBegin() + 1, SegmentBoundary.class);
			}

		next = false;
	}
}
