package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import de.saar.coli.salsa.reiter.framenet.CorpusReader;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;

/**
 * This class can be used to access the luXML files, i.e., the examples in the
 * FrameNet annotation. It's heavily inter- weaved with and based on the
 * FrameNetCorpus class.
 * 
 * 
 * @author reiter
 * @since 0.4
 * 
 */
public abstract class AnnotationCorpus extends CorpusReader {

	int maximalId = 0;

	/**
	 * An index of AnnotatedLexicalUnits.
	 */
	private Map<LexicalUnit, AnnotatedLexicalUnit> annotations = null;

	/**
	 * A constructor, just as in FrameNetCorpus.
	 * 
	 * @param frameNet
	 *            The FrameNet object
	 * @param logger
	 *            The logger
	 */
	public AnnotationCorpus(final FrameNet frameNet, final Logger logger) {
		super(frameNet, logger);
		annotations = new HashMap<LexicalUnit, AnnotatedLexicalUnit>();
	}

	/**
	 * Returns the annotated examples with the given lexical unit. If no
	 * annotation has been found, the method returns null.
	 * 
	 * @param lu
	 *            The lexical unit
	 * @return An object of the class AnnotatedLexicalUnit
	 */
	public AnnotatedLexicalUnit getAnnotation(final LexicalUnit lu) {
		try {
			return annotations.get(lu);
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public void parse(final File directory) {
		parse(directory, ".xml");
	}

	/**
	 * An extension of {@link #parse(File)}. Allows the specification of a
	 * pattern, which must be contained in the filename of the files to be
	 * loaded.
	 * 
	 * Example: If you specify "2442" as pattern string, only lu-files with 2442
	 * in their names will be loaded.
	 * 
	 * @param directory
	 *            The luXML directory
	 * @param pattern
	 *            A pattern
	 */
	public void parse(final File directory, final String pattern) {
		if (directory.isDirectory() && directory.canRead()) {
			for (File file : directory.listFiles()) {
				if (file.getName().contains(pattern)) {
					parseFile(file);
				}
			}
		}
	}

	/**
	 * An extension of {@link #parse(File)}. Allows the specification of a list
	 * of file names to be excluded.
	 * 
	 * Example: If you specify "lu2442.xml", every file except lu2442.xml will
	 * be loaded.
	 * 
	 * @param directory
	 *            The luXML directory
	 * @param names
	 *            A list of file names
	 */
	public void parseWithout(final File directory, final String... names) {
		if (directory.isDirectory() && directory.canRead()) {
			for (File file : directory.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(final File arg0, final String arg1) {
					return arg1.endsWith(".xml") && !contains(names, arg1);
				}
			})) {
				parseFile(file);
			}
		}
	}

	public static boolean contains(final String[] array, final String key) {
		for (String a : array) {
			if (a.equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Parses the file.
	 * 
	 * @param file
	 *            The file to parse
	 */
	protected abstract void parseFile(File file);

	/**
	 * Adds an annotated lexical unit to the index of lexical units.
	 * 
	 * @param lu
	 *            The lexical unit
	 * @param alu
	 *            The annotated lexical unit
	 */
	public void addAnnotation(final LexicalUnit lu,
			final AnnotatedLexicalUnit alu) {
		annotations.put(lu, alu);
		if (alu.getId() < 0) {
			alu.setId(getNextId());
		} else {
			if (alu.getId() > maximalId) {
				maximalId = alu.getId();
			}
		}
	}

	private int getNextId() {
		return ++maximalId;
	}

	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	public Set<LexicalUnit> getLexicalUnits() {
		return annotations.keySet();
	}

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	public int getNumberOfAnnotations() {
		return annotations.size();
	}

	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	public Collection<AnnotatedLexicalUnit> getAnnotations() {
		return annotations.values();
	}

}
