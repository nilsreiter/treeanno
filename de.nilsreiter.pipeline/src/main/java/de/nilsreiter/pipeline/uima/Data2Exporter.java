package de.nilsreiter.pipeline.uima;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.CasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.event.type.Event;
import de.nilsreiter.pipeline.uima.event.type.Role;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceChain;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticArgument;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticPredicate;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import de.tudarmstadt.ukp.dkpro.wsd.type.Sense;
import de.tudarmstadt.ukp.dkpro.wsd.type.WSDResult;
import de.uniheidelberg.cl.a10.data2.impl.Entity_impl;
import de.uniheidelberg.cl.a10.data2.impl.Event_impl;
import de.uniheidelberg.cl.a10.data2.impl.FrameElm_impl;
import de.uniheidelberg.cl.a10.data2.impl.Frame_impl;
import de.uniheidelberg.cl.a10.data2.impl.Mention_impl;
import de.uniheidelberg.cl.a10.data2.impl.Sense_impl;
import de.uniheidelberg.cl.a10.data2.impl.Sentence_impl;
import de.uniheidelberg.cl.a10.data2.impl.Token_impl;
import de.uniheidelberg.cl.a10.data2.io.DataWriter;

public class Data2Exporter extends CasConsumer_ImplBase {

	public static final String PARAM_CORPUS_NAME = "Corpus Name";
	public static final String PARAM_OUTPUT_DIRECTORY = "Output Directory";

	@ConfigurationParameter(name = PARAM_OUTPUT_DIRECTORY)
	String outputDirectory;

	@ConfigurationParameter(name = PARAM_CORPUS_NAME, mandatory = false)
	String corpusName = "test";

	public void processJCas(final JCas jcas) {
		UimaIdProvider idp = new UimaIdProvider();

		UimaData2Document uimaDoc =
				new UimaData2Document(JCasUtil.selectByIndex(jcas,
						DocumentMetaData.class, 0).getDocumentId());
		uimaDoc.setOriginalText(jcas.getDocumentText());
		uimaDoc.setCorpusName(corpusName);

		Map<Token, Token_impl> tokenMap = new HashMap<Token, Token_impl>();
		for (Sentence uSentence : JCasUtil.select(jcas, Sentence.class)) {
			Sentence_impl dSentence =
					new Sentence_impl(idp.getNextSentenceId());
			for (Token uToken : JCasUtil.selectCovered(jcas, Token.class,
					uSentence)) {
				Token_impl dToken =
						new Token_impl(idp.getNextTokenId(),
								uToken.getCoveredText());
				dToken.setBegin(uToken.getBegin());
				dToken.setEnd(uToken.getEnd());
				dToken.setPartOfSpeech(uToken.getPos().getPosValue());
				dToken.setLemma(uToken.getLemma().getValue());

				tokenMap.put(uToken, dToken);
				uimaDoc.addToken(dToken);
				dSentence.add(dToken);
			}

			for (Dependency dep : JCasUtil.selectCovered(jcas,
					Dependency.class, uSentence)) {
				Token_impl dependent = tokenMap.get(dep.getDependent());
				dependent.setGovernor(tokenMap.get(dep.getGovernor()));
				dependent.setDependencyRelation(dep.getDependencyType());
			}

			uimaDoc.addSentence(dSentence);
		}

		for (CoreferenceChain cc : JCasUtil
				.select(jcas, CoreferenceChain.class)) {
			Entity_impl entity = new Entity_impl(idp.getNextEntityId());
			CoreferenceLink cl = cc.getFirst();
			while (cl != null) {
				Mention_impl mention = new Mention_impl(idp.getNextMentionId());
				for (Token uToken : JCasUtil.selectCovered(jcas, Token.class,
						cl)) {
					mention.add(tokenMap.get(uToken));
				}
				entity.addMention(mention);
				cl = cl.getNext();
				uimaDoc.addMention(mention);
			}
			uimaDoc.addEntity(entity);
		}

		for (SemanticPredicate spred : JCasUtil.select(jcas,
				SemanticPredicate.class)) {
			Frame_impl frame = new Frame_impl(idp.getNextFrameId());
			frame.setFrameName(spred.getCategory());
			for (Token uToken : JCasUtil
					.selectCovered(jcas, Token.class, spred)) {
				frame.add(tokenMap.get(uToken));
			}
			for (SemanticArgument arg : JCasUtil.select(spred.getArguments(),
					SemanticArgument.class)) {
				FrameElm_impl fe =
						new FrameElm_impl(idp.getNextFrameElementId());
				fe.setName(arg.getRole());
				for (Token uToken : JCasUtil.selectCovered(jcas, Token.class,
						arg)) {
					fe.add(tokenMap.get(uToken));
				}

				frame.addFrameElm(fe);
				uimaDoc.addFrameElm(fe);
			}
			uimaDoc.addFrame(frame);
		}

		// Handling senses
		Map<String, Sense_impl> senseMap = new HashMap<String, Sense_impl>();
		for (WSDResult wsdr : JCasUtil.select(jcas, WSDResult.class)) {
			Sense sense = (Sense) wsdr.getSenses().get(0);
			Sense_impl dSense;
			if (senseMap.containsKey(sense.getId())) {
				dSense = senseMap.get(sense.getId());
			} else {
				dSense = new Sense_impl(idp.getNextSenseId());
				dSense.setWordNetId(sense.getId());
				senseMap.put(sense.getId(), dSense);
				uimaDoc.addSense(dSense);
			}
			if (JCasUtil.selectCovered(jcas, Token.class, wsdr.getWsdItem())
					.isEmpty()) {
				System.err.println();
			}
			Token uToken =
					JCasUtil.selectCovered(jcas, Token.class, wsdr.getWsdItem())
							.get(0);
			tokenMap.get(uToken).setSense(dSense);
		}

		// Handling events
		for (Event event : JCasUtil.select(jcas, Event.class)) {
			Event_impl ev = new Event_impl(idp.getNextEventId());
			ev.setEventClass(event.getName());
			for (Token token : JCasUtil.selectCovered(jcas, Token.class, event)) {
				ev.add(tokenMap.get(token));
			}
			for (Role role : JCasUtil.select(event.getArguments(), Role.class)) {
				List<Token_impl> toks = new LinkedList<Token_impl>();
				for (Token token : JCasUtil.selectCovered(jcas, Token.class,
						role)) {
					toks.add(tokenMap.get(token));
				}
				ev.putArgument(role.getName(), toks);
			}

			uimaDoc.addEvent(ev);
		}

		FileOutputStream fos;
		try {
			fos =
					new FileOutputStream(new File(this.outputDirectory,
							uimaDoc.getId() + ".xml"));
			DataWriter dw = new DataWriter(fos);
			dw.write(uimaDoc);
			fos.flush();
			fos.close();
			dw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void process(CAS arg0) throws AnalysisEngineProcessException {
		JCas jcas;
		try {
			jcas = arg0.getJCas();
		} catch (CASException e) {
			throw new AnalysisEngineProcessException(e);
		}
		processJCas(jcas);
	};
}
