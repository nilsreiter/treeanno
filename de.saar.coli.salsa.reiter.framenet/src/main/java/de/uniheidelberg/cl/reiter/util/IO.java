package de.uniheidelberg.cl.reiter.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A collection of various unsorted utility functions.
 * 
 * @author reiter
 * 
 */
public final class IO {

    /**
     * Private constructor. No instanciation allowed.
     */
    private IO() {
    };

    /**
     * This method creates a new empty directory by calling
     * {@link File#createTempFile(String, String)}, deleting the result and
     * using {@link File#mkdir()} to create a new directory.
     * 
     * @param prefix
     *            The prefix string to be used in generating the file's name;
     *            must be at least three characters long
     * @param suffix
     *            The suffix string to be used in generating the file's name;
     *            may be null, in which case the suffix ".tmp" will be used
     * @return An abstract pathname denoting a newly-created empty directory
     * @throws IOException
     *             If a file could not be created
     */
    public static File createTempDir(final String prefix, final String suffix)
	    throws IOException {
	File f = File.createTempFile(prefix, suffix);
	f.delete();
	f.mkdir();
	return f;
    }

    /**
     * This method copies one file to another file.
     * 
     * @param inputFile
     *            The file to copy
     * @param outputFile
     *            The target of the copy operation
     * @throws IOException
     *             If reading or writing the file causes trouble.
     */
    public static void copy(final File inputFile, final File outputFile)
	    throws IOException {
	FileReader in = new FileReader(inputFile);
	FileWriter out = new FileWriter(outputFile);
	int c;

	while ((c = in.read()) != -1) {
	    out.write(c);
	}

	in.close();
	out.close();
    }
}
