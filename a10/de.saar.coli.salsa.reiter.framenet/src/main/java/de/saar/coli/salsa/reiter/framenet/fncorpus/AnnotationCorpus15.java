package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.io.File;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IToken;
import de.saar.coli.salsa.reiter.framenet.InteractiveLexeme;
import de.saar.coli.salsa.reiter.framenet.InteractiveLexicalUnit;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.uniheidelberg.cl.reiter.pos.FN;
import de.uniheidelberg.cl.reiter.util.BasicPair;

/**
 * This class represents lu annotations in FrameNet 1.5.
 * 
 * @author reiter
 * 
 */
public class AnnotationCorpus15 extends AnnotationCorpus {

    private static final String LEMMA = "LEMMA";

    public AnnotationCorpus15(final FrameNet frameNet, final Logger logger) {
	super(frameNet, logger);
    }

    @Override
    protected void parseFile(final File file) {
	// Make document
	Document document = null;
	try {
	    SAXReader reader = new SAXReader();
	    document = reader.read(file);
	} catch (DocumentException e) {
	    getLogger().severe(e.getMessage());
	}
	getLogger().fine(
		"XML Document " + file.getAbsolutePath() + " has been read.");
	Element luanno = document.getRootElement();

	try {

	    LexicalUnit lu =
		    frameNet.getLexicalUnit(Integer.valueOf(luanno
			    .attributeValue("ID")));
	    addAnnotation(lu, new AnnotatedLexicalUnit15(this, luanno, lu));
	} catch (NumberFormatException e) {
	    System.err.println(luanno.asXML());
	    e.printStackTrace();
	} catch (FrameNotFoundException e) {
	    e.printStackTrace();
	}
    }

    /**
     * This method is tailored for the purposes of A10 and has no universal
     * purposes.
     * 
     * @param subCorpusName
     *            the name of the SubCorpus
     * @param rf
     *            The realized frame using the lexical unit
     * @throws Exception
     *             If something happens
     */
    public void addLexicalUnit(final String subCorpusName,
	    final RealizedFrame rf) throws Exception {

	// 1. Finding the correct lexical unit
	BasicPair<String, FN> lemma = rf.getLemma();
	LexicalUnit lu =
		frameNet.getLexicalUnit(rf.getFrame(), lemma.getElement1(),
			lemma.getElement2());

	if (!rf.getTargetString().matches(".*[a-z A-Z].*")) {
	    return;
	}

	if (lu == null) {
	    // 1.1 we create a new lexical unit, if none can be found
	    InteractiveLexicalUnit ilu =
		    new InteractiveLexicalUnit(rf.getFrame());
	    for (IToken target : rf.getTargetList()) {

		InteractiveLexeme il = new InteractiveLexeme();
		if (target.getProperty(AnnotationCorpus15.LEMMA) == null) {
		    il.setValue(target.toString());
		} else {
		    il.setValue(target.getProperty(AnnotationCorpus15.LEMMA));
		}
		il.setPartOfSpeech(target.getPartOfSpeech());
		ilu.addLexeme(il);
	    }
	    ilu.setDefinition("(A10)");
	    ilu.populate();
	    ilu.setPartOfSpeech(lemma.getElement2());
	    lu = ilu;
	    frameNet.addLexicalUnit(lu);
	}

	// 2. Getting the annotated lexical unit for this lu
	AnnotatedLexicalUnit15 alu =
		(AnnotatedLexicalUnit15) this.getAnnotation(lu);
	if (alu == null) {
	    // 2.1 If there is none, we create a new one
	    alu = new AnnotatedLexicalUnit15(this, lu);
	    alu.setFrame(rf.getFrame());
	    alu.setName(lemma.getElement1() + "."
		    + lemma.getElement2().toShortString());
	    alu.setStatus("Created");
	    // alu.addSentence(realizedFrame.getSentence());
	    this.addAnnotation(lu, alu);
	}

	// 3. Getting the appropriate sub corpus
	SubCorpus15 subCorpus = alu.getSubCorpus(subCorpusName);
	if (subCorpus == null) {
	    // 3.1 if there is none, we create a new one
	    subCorpus = new SubCorpus15(alu, subCorpusName);
	    alu.addSubCorpus(subCorpus);
	}

	/*
	 * // 4. Creating a new sentence for this annotation Sentence15
	 * fnSentence = new Sentence15(subCorpus,
	 * realizedFrame.getSentence().getText());
	 * subCorpus.addSentence(fnSentence); Map<IToken, IToken> tokenMap = new
	 * HashMap<IToken, IToken>(); for (IToken token :
	 * realizedFrame.getSentence().getTokenList()) { IToken newToken =
	 * fnSentence.addToken(token.getCharacterRange()); for (String prop :
	 * token.getPropertyKeys()) { newToken.setProperty(prop,
	 * token.getProperty(prop)); }
	 * newToken.setPartOfSpeech(token.getPartOfSpeech());
	 * tokenMap.put(token, newToken); }
	 * 
	 * // 5. Creating a realized frame RealizedFrame rf =
	 * realizedFrame.getFrame().realize(
	 * tokenMap.get(realizedFrame.getTarget(0))); for (RealizedFrameElement
	 * realizedFrameElement : realizedFrame .frameElements()) { List<IToken>
	 * target = new LinkedList<IToken>(); for (IToken oldToken :
	 * realizedFrameElement.getTargetList()) { IToken newTok =
	 * tokenMap.get(oldToken); if (newTok != null) { target.add(newTok); }
	 * else { // In case the frame element annotation links to a //
	 * nonterminal node, we have to search for the terminal // nodes if
	 * (oldToken.getClass() == TreeNonTerminal.class) { TreeNonTerminal
	 * ntNode = (TreeNonTerminal) oldToken; for (TreeTerminal oldTerminal :
	 * ntNode .getTerminalNodes()) { target.add(tokenMap.get(oldTerminal));
	 * } } } } try { rf.addRealizedFrameElement(realizedFrameElement
	 * .getFrameElement().getName(), target); } catch
	 * (FrameElementNotFoundException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } }
	 * 
	 * // 6. Creating a new annotation set for this annotation
	 * AnnotationSet15 annotationSet = new AnnotationSet15(fnSentence, rf);
	 * fnSentence.addAnnotationSet(annotationSet);
	 * 
	 * this.getSentences().add(fnSentence);
	 */
    }

}
