package de.nilsreiter.pipeline.uima.event;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.uima.event.type.Event;
import de.nilsreiter.pipeline.uima.event.type.Role;
import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader15;
import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticArgument;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticPredicate;

@TypeCapability(inputs = {
		"de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticPredicate",
		"de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticArgument" },
		outputs = { "de.nilsreiter.pipeline.uima.event.type.Event",
				"de.nilsreiter.pipeline.uima.event.type.Role" })
public class EventAnnotator extends JCasAnnotator_ImplBase {

	public static final String PARAM_DETECTION_STYLE = "Detection Style";
	public static final String PARAM_FNHOME = "FrameNet home";
	public static final String PARAM_ROOTFRAME = "Root Selector Frame";

	@ConfigurationParameter(name = PARAM_DETECTION_STYLE, mandatory = false)
	Detection detection = Detection.All;

	@ConfigurationParameter(name = PARAM_FNHOME, mandatory = false)
	String fnhome = null;

	@ConfigurationParameter(name = PARAM_ROOTFRAME, mandatory = false,
			defaultValue = "Event")
	String rootFrame = "Event";

	public static enum Detection {
		All, FNEventInheritance
	};

	LocalUIMAEventDetector detector;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		switch (detection) {
		case FNEventInheritance:
			FrameNet frameNet = new FrameNet();
			try {
				frameNet.readData(new FNDatabaseReader15(new File(fnhome),
						false));
				detector = new FNBasedEventDetector(frameNet, rootFrame);
			} catch (FrameNotFoundException e) {
				throw new ResourceInitializationException(e);
			} catch (FileNotFoundException e) {
				throw new ResourceInitializationException(e);
			}
			break;
		case All:
		default:
			detector = new AllFramesEventDetector();
		}

	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (SemanticPredicate pred : JCasUtil.select(jcas,
				SemanticPredicate.class)) {
			if (detector.isEvent(pred)) {
				Event event = new Event(jcas);
				event.setBegin(pred.getBegin());
				event.setEnd(pred.getEnd());
				event.setName(pred.getCategory());
				event.setArguments(new FSArray(jcas, pred.getArguments().size()));
				event.setAnchor(pred);
				int i = 0;

				for (SemanticArgument arg : JCasUtil.select(
						pred.getArguments(), SemanticArgument.class)) {
					Role role = new Role(jcas);
					role.setBegin(arg.getBegin());
					role.setEnd(arg.getEnd());
					role.setName(arg.getRole());
					role.addToIndexes();
					event.setArguments(i++, role);
				}

				event.addToIndexes();
			}
		}

		if (!JCasUtil.exists(jcas, SemanticPredicate.class)) {
			Event event = new Event(jcas);
			event.setBegin(1);
			event.setEnd(2);
		}

	}

	class AllFramesEventDetector implements LocalUIMAEventDetector {

		@Override
		public boolean isEvent(SemanticPredicate anchor) {
			return true;
		}

	}

	class FNBasedEventDetector implements LocalUIMAEventDetector {

		FrameNet frameNet = null;

		String rootFrame = null;

		Frame referenceFrame;

		public FNBasedEventDetector(FrameNet frameNet, String rootFrame)
				throws FrameNotFoundException {
			this.frameNet = frameNet;
			this.rootFrame = rootFrame;
			this.referenceFrame = frameNet.getFrame(rootFrame);
		}

		@Override
		public boolean isEvent(SemanticPredicate anchor) {
			try {
				Frame fnFrame = frameNet.getFrame(anchor.getCategory());

				Collection<Frame> inh = fnFrame.allInheritedFrames();

				return inh.contains(this.referenceFrame);
			} catch (FrameNotFoundException e) {
				return false;
			}
		}

	}

}
