package de.saar.coli.salsa.reiter.framenet.fncorpus.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultElement;

import de.saar.coli.salsa.reiter.framenet.FrameElement;
import de.saar.coli.salsa.reiter.framenet.FrameNetVersion;
import de.saar.coli.salsa.reiter.framenet.IToken;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;
import de.saar.coli.salsa.reiter.framenet.VersionException;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotatedLexicalUnit;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotatedLexicalUnit15;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotationCorpus15;
import de.saar.coli.salsa.reiter.framenet.fncorpus.Sentence15;
import de.saar.coli.salsa.reiter.framenet.fncorpus.SubCorpus15;
import de.uniheidelberg.cl.reiter.pos.BNC;
import de.uniheidelberg.cl.reiter.util.IO;

/**
 * 
 * @author reiter
 * 
 */
public class XMLExporter {

    File luXSLT;
    File luIndexXSLT;

    /**
     * This class is extending DefaultDocument from the dom4j API. The only
     * reason we need this is to override either {@link #clearContent()} or
     * {@link #setRootElement(Element)}, so that processing instructions are
     * preserved and <em>not</em> printed at the end of the document.
     * 
     * @author reiter
     * 
     */
    public class MyDocument extends DefaultDocument {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	@Override
	public void clearContent() {
	};

    }

    /**
     * 
     */
    private FrameNetVersion version;

    /**
     * Implicit version 1.5.
     * 
     * @throws VersionException
     *             This exception will never be thrown from this constructor.
     */
    public XMLExporter() throws VersionException {
	this(FrameNetVersion.V15);
    }

    /**
     * 
     * @param version
     *            The XML format version we want to export to.
     */
    public XMLExporter(final FrameNetVersion version) throws VersionException {
	if (version != FrameNetVersion.V15) {
	    throw new VersionException("Export not supported for version "
		    + version);
	}
	this.version = version;
    }

    public Document asDocument(final AnnotationCorpus15 annoCorpus) {
	Document document = new MyDocument();
	document.addProcessingInstruction("xml-stylesheet",
		"href=\"luIndex.xsl\" type=\"text/xsl\"");
	Element luIndex = new DefaultElement("fn:luIndex");
	luIndex.addNamespace("fn", "http://framenet.icsi.berkeley.edu");
	luIndex.addAttribute("xmlns", "http://framenet.icsi.berkeley.edu");
	luIndex.addAttribute("xsi:schemaLocation", "schema/luIndex.xsd");
	luIndex.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	document.setRootElement(luIndex);

	for (AnnotatedLexicalUnit alu_ : annoCorpus.getAnnotations()) {
	    AnnotatedLexicalUnit15 alu = (AnnotatedLexicalUnit15) alu_;
	    Element luElement = new DefaultElement("fn:lu");
	    luElement.addAttribute("name", alu.getName());
	    luElement.addAttribute("frameName", alu.getFrame().getName());
	    luElement.addAttribute("hasAnnotation", "true");
	    luElement.addAttribute("frameID", alu.getFrame().getIdString());
	    luElement.addAttribute("status", alu.getStatus());
	    luElement.addAttribute("ID", alu.getIdString());

	    luIndex.add(luElement);
	}

	return document;
    }

    public Document asDocument(
	    final Collection<? extends AnnotatedLexicalUnit> annoCorpus) {
	Document document = new MyDocument();
	document.addProcessingInstruction("xml-stylesheet",
		"href=\"luIndex.xsl\" type=\"text/xsl\"");
	Element luIndex = new DefaultElement("fn:luIndex");
	luIndex.addNamespace("fn", "http://framenet.icsi.berkeley.edu");
	luIndex.addAttribute("xmlns", "http://framenet.icsi.berkeley.edu");
	luIndex.addAttribute("xsi:schemaLocation", "schema/luIndex.xsd");
	luIndex.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	document.setRootElement(luIndex);

	for (AnnotatedLexicalUnit alu_ : annoCorpus) {
	    AnnotatedLexicalUnit15 alu = (AnnotatedLexicalUnit15) alu_;
	    Element luElement = new DefaultElement("fn:lu");
	    luElement.addAttribute("name", alu.getName());
	    luElement.addAttribute("frameName", alu.getFrame().getName());
	    luElement.addAttribute("hasAnnotation", "true");
	    luElement.addAttribute("frameID", alu.getFrame().getIdString());
	    luElement.addAttribute("status", alu.getStatus());
	    luElement.addAttribute("ID", alu.getIdString());

	    luIndex.add(luElement);
	}

	return document;
    }

    public Document asDocument(final AnnotatedLexicalUnit15 annotatedLexicalUnit) {
	Document document = new MyDocument();
	document.addProcessingInstruction("xml-stylesheet",
		"href=\"lexUnit.xsl\" type=\"text/xsl\"");

	Element lexUnit = new DefaultElement("lexUnit");
	lexUnit.add(Namespace.get("http://framenet.icsi.berkeley.edu"));
	lexUnit.add(Namespace.get("fn", "http://framenet.icsi.berkeley.edu"));
	// lexUnit.addAttribute("xmlns", "http://framenet.icsi.berkeley.edu");
	lexUnit.addAttribute("xsi:schemaLocation", "schema/lexUnit.xsd");
	lexUnit.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

	try {
	    lexUnit.addAttribute("POS", annotatedLexicalUnit.getPartOfSpeech()
		    .toShortString().toUpperCase());
	} catch (NullPointerException e) {
	    System.err.println(annotatedLexicalUnit.getName() + "-"
		    + annotatedLexicalUnit.getIdString());
	    throw e;
	}
	lexUnit.addAttribute("frame", annotatedLexicalUnit.getFrame().getName());
	lexUnit.addAttribute("status", annotatedLexicalUnit.getStatus());
	lexUnit.addAttribute("name", annotatedLexicalUnit.getName());
	lexUnit.addAttribute("ID", annotatedLexicalUnit.getIdString());
	document.setRootElement(lexUnit);

	// header
	Element headerElement = new DefaultElement("fn:header");
	// headerElement.add(Namespace.get("http://framenet.icsi.berkeley.edu"));
	Element headerFrameElement = new DefaultElement("fn:frame");
	// headerFrameElement.add(Namespace
	// .get("http://framenet.icsi.berkeley.edu"));
	int i = 0;
	for (FrameElement fe : annotatedLexicalUnit.getFrame().frameElements()) {
	    Element feElement = new DefaultElement("fn:FE");
	    feElement.addAttribute("fgColor", Colors.getColors().get(i)
		    .getElement1());
	    feElement.addAttribute("bgColor", Colors.getColors().get(i)
		    .getElement2());

	    feElement.addAttribute("name", fe.getName());
	    feElement.addAttribute("type", fe.getCoreTypeString());
	    feElement.addAttribute("abbrev", fe.getAbbreviation());
	    headerFrameElement.add(feElement);
	    i++;
	}
	headerElement.add(headerFrameElement);
	lexUnit.add(headerElement);

	// definition
	if (annotatedLexicalUnit.getDefinition() != null) {
	    Element definitionElement = new DefaultElement("fn:definition");
	    // definitionElement.add(Namespace
	    // .get("http://framenet.icsi.berkeley.edu"));
	    definitionElement.addText(annotatedLexicalUnit.getDefinition());
	    lexUnit.add(definitionElement);

	}

	/*
	 * // lexeme(s) for (Lexeme lexeme :
	 * annotatedLexicalUnit.getLexicalUnit().getLexemes()) { if
	 * (lexeme.getPartOfSpeech() != null) { Element lexemeElement = new
	 * DefaultElement("fn:lexeme"); //
	 * lexemeElement.add(Namespace.get("http://framenet.icsi.berkeley.edu"
	 * )); lexemeElement.addAttribute("name", lexeme.getValue());
	 * lexemeElement.addAttribute("POS", lexeme.getPartOfSpeech()
	 * .asFN().toShortString().toUpperCase());
	 * lexemeElement.addAttribute("headword",
	 * Boolean.toString(lexeme.isHeadword()));
	 * lexemeElement.addAttribute("breakBefore",
	 * Boolean.toString(lexeme.isBreakBefore()));
	 * lexUnit.add(lexemeElement); } }
	 */

	// semtype
	if (annotatedLexicalUnit.getSemanticType() != null) {
	    Element semTypeElement = new DefaultElement("fn:semType");
	    semTypeElement.addAttribute("name", annotatedLexicalUnit
		    .getSemanticType().getName());
	    semTypeElement.addAttribute("ID", annotatedLexicalUnit
		    .getSemanticType().getIdString());
	    lexUnit.add(semTypeElement);
	}

	// subCorpus
	for (SubCorpus15 sc : annotatedLexicalUnit.getSubCorpora()) {
	    Element scElement = new DefaultElement("fn:subCorpus");
	    // scElement.add(Namespace.get("http://framenet.icsi.berkeley.edu"));
	    scElement.addAttribute("name", sc.getName());
	    for (Sentence15 sentence : sc.getSentences()) {
		Element sentenceElement = new DefaultElement("fn:sentence");
		// sentenceElement.add(Namespace
		// .get("http://framenet.icsi.berkeley.edu"));
		sentenceElement.addAttribute("ID", sentence.getIdString());
		sentenceElement.addAttribute("sentNo", sentence
			.getSentenceNumber().toString());

		Element textElement = new DefaultElement("fn:text");
		textElement.addText(sentence.getText());
		// textElement.add(Namespace
		// .get("http://framenet.icsi.berkeley.edu"));
		sentenceElement.add(textElement);

		Element bncElement = new DefaultElement("fn:annotationSet");
		// bncElement.add(Namespace
		// .get("http://framenet.icsi.berkeley.edu"));
		bncElement.addAttribute("status", "UNANN");
		Element bncLayerElement = new DefaultElement("fn:layer");
		// bncLayerElement.add(Namespace
		// .get("http://framenet.icsi.berkeley.edu"));
		bncLayerElement.addAttribute("rank", "1");
		boolean posDeclareDone = false;
		for (IToken token : sentence.getTokenList()) {
		    if (!posDeclareDone) {
			if (token.getPartOfSpeech() != null
				&& token.getPartOfSpeech().getClass() == BNC.class) {
			    bncLayerElement.addAttribute("name", "BNC");
			} else {
			    bncLayerElement.addAttribute("name", "PENN");
			}
			posDeclareDone = true;
		    }
		    Element labelElement = new DefaultElement("fn:label");
		    // labelElement.add(Namespace
		    // .get("http://framenet.icsi.berkeley.edu"));
		    labelElement.addAttribute("start", token
			    .getCharacterRange().getElement1().toString());
		    labelElement.addAttribute("end", String.valueOf(token
			    .getCharacterRange().getElement2() - 1));
		    if (token.getPartOfSpeech() != null) {
			labelElement.addAttribute("name", token
				.getPartOfSpeech().toString());
		    }
		    bncLayerElement.add(labelElement);
		}
		bncElement.add(bncLayerElement);
		sentenceElement.add(bncElement);

		// Annotation sets for frame annotations
		for (RealizedFrame rf : sentence.getRealizedFrames()) {
		    Element asElement = new DefaultElement("fn:annotationSet");
		    // asElement.add(Namespace
		    // .get("http://framenet.icsi.berkeley.edu"));
		    asElement.addAttribute("status", "MANUAL");

		    // Target
		    Element targetLayerElement = new DefaultElement("fn:layer");
		    // targetLayerElement.add(Namespace
		    // .get("http://framenet.icsi.berkeley.edu"));
		    targetLayerElement.addAttribute("name", "Target");
		    targetLayerElement.addAttribute("rank", "1");

		    Element targetLabelElement = new DefaultElement("fn:label");
		    // targetLabelElement.add(Namespace
		    // .get("http://framenet.icsi.berkeley.edu"));
		    targetLabelElement
			    .addAttribute("start", rf.getTargetCharacterRange()
				    .getElement1().toString());
		    targetLabelElement.addAttribute("end", rf
			    .getTargetCharacterRange().copy(0, -1)
			    .getElement2().toString());
		    targetLabelElement.addAttribute("name", "Target");

		    targetLayerElement.add(targetLabelElement);

		    // FEs
		    Element feLayerElement = new DefaultElement("fn:layer");
		    // feLayerElement.add(Namespace
		    // .get("http://framenet.icsi.berkeley.edu"));
		    feLayerElement.addAttribute("name", "FE");
		    feLayerElement.addAttribute("rank", "1");
		    for (RealizedFrameElement rfe : rf.frameElements()) {
			// TODO: Need to add null instantiated elements
			if (!rfe.isNullInstantiated()) {
			    targetLabelElement = new DefaultElement("fn:label");
			    if (rfe.getTargetList().isEmpty()) {
				System.err.print("");
			    }
			    targetLabelElement.addAttribute("start", rfe
				    .getTargetCharacterRange().getElement1()
				    .toString());
			    targetLabelElement.addAttribute("end", rfe
				    .getTargetCharacterRange().copy(0, -1)
				    .getElement2().toString());
			    targetLabelElement.addAttribute("name", rfe
				    .getFrameElement().getName());
			    feLayerElement.add(targetLabelElement);
			} else {

			}
		    }

		    asElement.add(targetLayerElement);
		    asElement.add(feLayerElement);
		    sentenceElement.add(asElement);
		}
		scElement.add(sentenceElement);
	    }
	    lexUnit.add(scElement);
	}

	return document;
    }

    /**
     * @return the version
     */
    public FrameNetVersion getVersion() {
	return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(final FrameNetVersion version) {
	this.version = version;
    }

    public void writeToFile(final Document document, final File file)
	    throws IOException {
	if (!file.getParentFile().exists()) {
	    file.getParentFile().mkdirs();
	}
	if (file.exists()) {
	    file.delete();
	}
	FileWriter fw = new FileWriter(file);
	OutputFormat of = OutputFormat.createPrettyPrint();
	of.setTrimText(false);
	XMLWriter xw = new XMLWriter(fw, of);
	xw.write(document);
	xw.close();
	fw.close();
    }

    public void writeToDirectory(final Collection<AnnotatedLexicalUnit15> ac,
	    final File directory) throws IOException {

	File luDir = new File(directory, "lu");
	if (!luDir.exists()) {
	    luDir.mkdir();
	}

	for (AnnotatedLexicalUnit alu_ : ac) {
	    AnnotatedLexicalUnit15 alu = (AnnotatedLexicalUnit15) alu_;
	    String id = alu.getIdString();
	    File targetFile = new File(luDir, "lu" + id + ".xml");
	    Document doc = asDocument(alu);
	    // System.err.println("Writing file " +
	    // targetFile.getAbsolutePath());
	    try {
		writeToFile(doc, targetFile);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	Document indexDoc = asDocument(ac);
	File file = new File(directory, "luIndex.xml");
	writeToFile(indexDoc, file);

	if (this.getLuXSLT() != null) {
	    IO.copy(getLuXSLT(), new File(luDir, "lexUnit.xsl"));
	}
	if (this.getLuIndexXSLT() != null) {
	    IO.copy(getLuIndexXSLT(), new File(directory, "luIndex.xsl"));
	}
	System.err.println(" Done printing.");
    }

    public void writeToDirectory(final AnnotationCorpus15 ac,
	    final File directory) throws IOException {
	Document indexDoc = asDocument(ac);
	File file = new File(directory, "luIndex.xml");
	writeToFile(indexDoc, file);
	File luDir = new File(directory, "lu");
	if (!luDir.exists()) {
	    luDir.mkdir();
	}
	for (AnnotatedLexicalUnit alu : ac.getAnnotations()) {
	    String id = alu.getIdString();
	    File targetFile = new File(luDir, "lu" + id + ".xml");
	    Document doc = asDocument((AnnotatedLexicalUnit15) alu);
	    writeToFile(doc, targetFile);
	}
    }

    /**
     * @return the luXSLT
     */
    public File getLuXSLT() {
	return luXSLT;
    }

    /**
     * @param luXSLT
     *            the luXSLT to set
     */
    public void setLuXSLT(final File luXSLT) {
	this.luXSLT = luXSLT;
    }

    /**
     * @return the luIndexXSLT
     */
    public File getLuIndexXSLT() {
	return luIndexXSLT;
    }

    /**
     * @param luIndexXSLT
     *            the luIndexXSLT to set
     */
    public void setLuIndexXSLT(final File luIndexXSLT) {
	this.luIndexXSLT = luIndexXSLT;
    }
}
