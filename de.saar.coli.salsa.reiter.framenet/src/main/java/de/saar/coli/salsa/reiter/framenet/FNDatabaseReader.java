package de.saar.coli.salsa.reiter.framenet;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.DateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * This class is used to read the original FrameNet data. The original FrameNet
 * data is usually stored in four different files:
 * <ul>
 * <li>frames.xml: The file containing the frame definitions, frame elements,
 * descriptions etc.</li>
 * <li>frRelation.xml: The file containing the links between the frames and
 * frame elements.</li>
 * <li>semtypes.xml: The file containing the semantic type hierarchy</li>
 * <li>framesDiff.xml: The file contains the differences between versions of
 * FrameNet</li>
 * </ul>
 * 
 * This class reads all of them and stores some of the information in the
 * appropriate objects.
 * 
 * @author Nils Reiter
 * @since 0.4
 * 
 */
public abstract class FNDatabaseReader extends DatabaseReader {

    /**
     * Path and name of the file "frames.xml".
     */
    private List<URI> framesFile = null;

    /**
     * Path and name of the file "frRelation.xml".
     */
    private List<URI> frRelationFile = null;

    /**
     * Path and name of the file "framesDiff.xml".
     */
    private List<URI> framesDiffFile = null;
    /**
     * Path and name of the file "semtypes.xml".
     */
    private List<URI> semtypesFile = null;

    /**
     * The home of the FrameNet data files.
     */
    private File fnhome = null;

    /**
     * The file name of the file containing the frame definitions.
     */
    protected String framesFilename = null;

    /**
     * The file name of the file containing the frame relations.
     */
    protected String frrelationFilename = null;

    /**
     * The file name of the file containing the semantic types.
     */
    protected String semtypesFilename = null;

    /**
     * A set of subdirectories of $FNHOME, in which we search.
     */
    private Set<String> xmldirs = null;

    private FrameNetFilesNotFoundException exc;

    private boolean validate = true;

    private final static long serialVersionUID = 2L;

    FrameNet frameNet = null;

    public DateFormat dateFormat;

    protected DateFormat getDateFormat() {
	return dateFormat;
    }

    /**
     * The constructor of the database interface
     * 
     * @param fnhome
     *            The main directory of FrameNet
     * @param validate
     *            If set to true, the class will validate the XML files before
     *            loading them
     * @throws FileNotFoundException
     *             If the main FrameNet directory does not exist
     * @throws SecurityException
     *             If the main FrameNet directory can not be read
     * @deprecated Use {@link #createInstance(File, FrameNetVersion)} instead.
     */
    @Deprecated
    public FNDatabaseReader(final File fnhome, final FrameNetVersion version,
	    final boolean validate) throws FileNotFoundException,
	    SecurityException {
	super();
    }

    /**
     * An empty constructor.
     */
    protected FNDatabaseReader() {

    };

    /**
     * Use this method to create a new FNDatabaseReader.
     * 
     * @param fnhome
     *            The FNHOME directory
     * @param version
     *            The version of FrameNet you want to process
     * @return The FNDatabaseReader
     * @throws FileNotFoundException
     *             If the FrameNet files are not found.
     */
    public static FNDatabaseReader createInstance(final File fnhome,
	    final FrameNetVersion version) throws FileNotFoundException {
	switch (version) {
	case V13:
	    return new FNDatabaseReader13(fnhome, false);
	case V15:
	    return new FNDatabaseReader15(fnhome, false);
	default:
	    return null;
	}
    }

    /**
     * This method creates an object specifying the four important files
     * directly. This way, we can use URIs pointing to remote sites.
     * 
     * @param frames
     *            Location of the file frames.xml
     * @param frRelation
     *            Location of the file frRelation.xml
     * @param framesDiff
     *            Location of the file framesDiff.xml
     * @param semtypes
     *            Location of the file semtypes.xml
     * @deprecated Use {@link #createInstance(File, FrameNetVersion)} instead.
     */
    @Deprecated
    public FNDatabaseReader(final FrameNetVersion frameNetVersion,
	    final URI frames, final URI frRelation, final URI framesDiff,
	    final URI semtypes) {
	fnhome = null;
	validate = false;
	xmldirs = null;

	framesFile = new LinkedList<URI>();
	frRelationFile = new LinkedList<URI>();
	framesDiffFile = new LinkedList<URI>();
	semtypesFile = new LinkedList<URI>();

	framesFile.add(frames);
	frRelationFile.add(frRelation);
	framesDiffFile.add(framesDiff);
	semtypesFile.add(semtypes);
    }

    /**
     * 
     * @param arg0
     *            The FNHOME directory
     * @param arg1
     *            Whether we want to validate the directories
     * @param subdirs
     *            A list of sub directories we want to search
     * @throws FileNotFoundException
     *             If there was a problem with one of the files or directories
     *             (check the message of the exception)
     */
    protected void init(final File arg0, final boolean arg1,
	    final Set<String> subdirs) throws FileNotFoundException {
	if (!arg0.exists()) {
	    throw new FileNotFoundException(arg0.getAbsolutePath()
		    + " does not exist.");
	}
	if (!arg0.isDirectory()) {
	    throw new FileNotFoundException(arg0.getAbsolutePath()
		    + " is not a directory.");
	}
	if (!arg0.canRead()) {
	    throw new SecurityException(arg0.getAbsolutePath()
		    + " is not readable.");
	}

	this.fnhome = arg0;
	this.validate = arg1;
	xmldirs = subdirs;

	xmldirs.add("");
	xmldirs.add("frXML");
	// Removed for compatibility with framenet-1.4alpha
	// xmldirs.add("frDiffXML");
	xmldirs.add("xml");

	exc = new FrameNetFilesNotFoundException();

	framesFile = new LinkedList<URI>();
	frRelationFile = new LinkedList<URI>();
	framesDiffFile = new LinkedList<URI>();
	semtypesFile = new LinkedList<URI>();

	File frames = initFile(framesFilename);
	if (frames != null) {
	    framesFile.add(frames.toURI());
	}

	File frRelation = initFile(frrelationFilename);
	if (frRelation != null) {
	    frRelationFile.add(frRelation.toURI());
	    // Removed for compatibility with framenet-1.4alpha
	    // framesDiffFile.add(this.initFile("framesDiff.xml").toURI());
	}

	File semtypes = initFile(semtypesFilename);
	if (semtypes != null) {
	    semtypesFile.add(semtypes.toURI());
	}

	if (!exc.getNotFound().isEmpty()) {
	    throw exc;
	} else {
	    exc = null;
	}
    }

    /**
     * This method initialises one file. In fact, we check whether we can read
     * the file and try to validate the XML.
     * 
     * @param filename
     *            The file we need to check
     * @return The file, if everything is cool. null otherwise.
     */
    protected File initFile(final String filename) {
	for (String xmldirname : xmldirs) {
	    File xmldir = new File(fnhome, xmldirname);
	    File file = new File(xmldir, filename);
	    if (!file.exists()) {
		continue;
	    }
	    if (!file.canRead()) {
		continue;
	    }
	    if (validate) {
		if (!validate(file)) {
		    continue;
		}
	    }
	    return file;
	}
	exc.notFound(filename);
	return null;
    }

    /**
     * Returns the semantic types as XML document.
     * 
     * @return An XML document
     * @throws ParsingException
     *             If the document can't be parsed.
     * @throws FrameNetFilesNotFoundException
     *             If the file is not found.
     */
    public Document getSemTypesDocument() throws ParsingException,
	    FrameNetFilesNotFoundException {
	return parse(getSemtypesURI());
    }

    /**
     * Returns the frame relations as XML document.
     * 
     * @return An XML document
     * @throws ParsingException
     *             If the document can't be parsed.
     * @throws FrameNetFilesNotFoundException
     *             If the file is not found.
     */
    public Document getFrRelationDocument() throws ParsingException,
	    FrameNetFilesNotFoundException {
	return parse(getFrRelationURI());
    }

    /**
     * Returns the frames-Index as XML document.
     * 
     * @return An XML document
     * @throws ParsingException
     *             If the document can't be parsed.
     * @throws FrameNetFilesNotFoundException
     *             If the file is not found.
     */
    public Document getFramesDocument() throws ParsingException,
	    FrameNetFilesNotFoundException {
	return parse(getFramesURI());
    }

    /**
     * Parses the XML file specified by the given URI.
     * 
     * @param uri
     *            The URI of the file.
     * @return The parsed XML document.
     * @throws ParsingException
     *             If a parsing exception occurs.
     */
    protected Document parse(final URI uri) throws ParsingException {

	try {

	    SAXReader reader = new SAXReader(false);
	    reader.setValidation(false);
	    reader.setIncludeExternalDTDDeclarations(false);
	    Document document;
	    // if (uri.getScheme().equals("file")) {
	    document = reader.read(uri.toURL());
	    /*
	     * } else { URLConnection conn = uri.toURL().openConnection();
	     * document = reader.read(conn.getInputStream()); }
	     */
	    return document;
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (DocumentException e) {
	    e.printStackTrace();
	    throw new ParsingException("Could not parse " + uri.toString());
	}
	return null;
    }

    protected boolean validate(final File file) {
	try {
	    parse(file.toURI());
	} catch (ParsingException e) {
	    return false;
	}
	return true;
    }

    /**
     * Reads all data from the XML files into the FrameNet object.
     */
    @Override
    protected boolean read(final FrameNet fn) {
	frameNet = fn;
	frameNet.setDateFormat(this.getDateFormat());
	try {
	    loadAllSemanticTypes(fn);
	    loadAllFrames(fn);
	    loadAllFrameRelations(fn);
	} catch (FileNotFoundException e) {
	    fn.log(Level.SEVERE, e.getMessage());
	    return false;
	}
	return true;
    }

    /**
     * Returns the FrameNet object.
     * 
     * @return the FrameNet object
     */
    protected FrameNet getFrameNet() {
	return frameNet;
    }

    /**
     * Called internally to load the frame relations.
     * 
     * @param fn
     *            The FrameNet object
     * @throws FileNotFoundException
     *             If the FrameNet database files can not be found.
     */
    protected abstract void loadAllFrameRelations(FrameNet fn)
	    throws FileNotFoundException;

    /**
     * This method loads all frames in the frames.xml file. For each frame, a
     * complete Frame-object is created and stores all its data. It is *much*
     * faster to run this method before doing any framewise work. So it is
     * recommended to run this method directly after the start.
     * 
     * @return The number of frames loaded from the XML file.
     * @throws FileNotFoundException
     *             If the FrameNet database files can not be found.
     * @param fn
     *            The FrameNet object
     */
    protected abstract int loadAllFrames(FrameNet fn)
	    throws FileNotFoundException;

    protected abstract void loadAllSemanticTypes(FrameNet fn)
	    throws FrameNetFilesNotFoundException;

    /**
     * This method is exactly like {@link FrameNet#loadAllFrames()}, except that
     * it takes a Thread object as argument that implements the
     * {@link IPingable} interface. The method calls the method
     * {@link IPingable#ping()} for every frame it successfully loads. This
     * allows the display of progress bar, for instance.
     * 
     * 
     * 
     * @param thread
     *            The thread to be pinged
     * @return
     * @throws FileNotFoundException
     *             If the FrameNet files can not be found
     * @throws XPathExpressionException
     *             Should not happen
     */
    /*
     * public int loadAllFrames(IPingable thread) throws FileNotFoundException,
     * XPathExpressionException { XPath xpath =
     * XPathFactory.newInstance().newXPath(); NodeList frames = (NodeList)
     * xpath.evaluate("/frames/frame", new
     * InputSource(framesFile.getAbsolutePath()), XPathConstants.NODESET);
     * thread.setMax(frames.getLength()+1); thread.ping(); for (int i = 0; i <
     * frames.getLength(); i++ ) { loadFrameNode((Element) frames.item(i));
     * thread.ping(); } return frames.getLength(); }
     */
    /**
     * Loads a frame from a xml node out of the frames.xml file. The method
     * creates the frame and adds it to the internal cache.
     * 
     * @param node
     *            The node from the XML document.
     */
    protected abstract Frame loadFrameNode(FrameNet frameNet, Node node);

    protected abstract FrameNetRelation loadFrameRelationNode(
	    FrameNet frameNet, Element node);

    protected abstract void loadSemanticTypeNode(FrameNet frameNet, Element node);

    /**
     * Returns a URI for the frames diff file.
     * 
     * @return the framesDiffFile
     */
    public URI getFramesDiffURI() {
	return framesDiffFile.get(0);
    }

    /**
     * Returns a URI for the frames file.
     * 
     * @return the framesFile
     */
    public URI getFramesURI() {
	return framesFile.get(0);
    }

    /**
     * Returns a URI for the frame relation file.
     * 
     * @return the frRelationFile
     */
    public URI getFrRelationURI() {
	return frRelationFile.get(0);
    }

    /**
     * Returns a URI for the semantic types file.
     * 
     * @return the semtypesFile
     */
    public URI getSemtypesURI() {
	return semtypesFile.get(0);
    };

    /**
     * Adds a subdirectory to the list, such that we search also in
     * <code>dir</code> for the XML data files.
     * 
     * @param dir
     *            The new directory
     */
    public void addSubDirectory(final String dir) {
	xmldirs.add(dir);
    }

    /**
     * Manually sets the file framesDiff.xml.
     * 
     * @param framesDiffFile
     *            The file
     */
    public void addFramesDiffFile(final File framesDiffFile) {
	this.framesDiffFile.add(framesDiffFile.toURI());
    }

    /**
     * Manually sets the file frames.xml.
     * 
     * @param framesFile
     *            The file
     */
    public void addFramesFile(final File framesFile) {
	this.framesFile.add(framesFile.toURI());
    }

    /**
     * Manually sets the file frRelation.xml.
     * 
     * @param frRelationFile
     *            The file
     */
    public void addFrRelationFile(final File frRelationFile) {
	this.frRelationFile.add(frRelationFile.toURI());
    }

    /**
     * Manually sets the file semtypes.xml.
     * 
     * @param semtypesFile
     *            The file
     */
    public void addSemtypesFile(final File semtypesFile) {
	this.semtypesFile.add(semtypesFile.toURI());
    }

    @Override
    protected void cleanup() {
	exc = null;
	System.gc();
    }

    /**
     * Set the default aliases for this version.
     */
    public abstract void addDefaultFrameAliases();
}
