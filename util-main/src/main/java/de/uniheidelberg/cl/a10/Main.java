package de.uniheidelberg.cl.a10;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Handler;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.kohsuke.args4j.ClassParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import de.nilsreiter.util.StringUtil;

/**
 * This is the super class for all classes with main methods. All the option
 * parsing is done here. Every class with a main method inherits from this one.
 * 
 * @author reiter
 * 
 */
public abstract class Main {

	@Deprecated
	public enum Corpus {
		Rituals, Fables, FablesSieve
	}

	@Deprecated
	@Option(name = "--corpus", aliases = { "-c" })
	protected Corpus corpus = Corpus.Rituals;

	@Option(name = "--help", usage = "Prints this usage screen",
			aliases = { "-h" })
	boolean printUsage = false;

	@Deprecated
	@Option(name = "--loglevel", aliases = { "-l" })
	protected String logLevel = "WARNING";

	@Option(name = "--config",
			usage = "Configuration file. Default: configuration.ini")
	protected File configFile = new File("configuration.ini");

	protected Logger logger = LoggerFactory.getLogger(Main.class);

	@Deprecated
	public static final String defaultRitualDataDirectory = "data2/silver";
	@Deprecated
	public static final String defaultFableDataDirectory = "data/atu/data2";

	protected String commandLine = null;

	Configuration configuration;

	@Deprecated
	public File getDataDirectory() {
		switch (corpus) {
		case Fables:
			return new File(Main.defaultFableDataDirectory);
		case FablesSieve:
			return new File("data/atu-sieve/data2");
		default:
			return new File(Main.defaultRitualDataDirectory);
		}
	}

	@Deprecated
	public enum Host {
		/**
		 * My old laptop ...
		 */
		KLEINTI,
		/**
		 * This is actually gillespie, ellington or any other server
		 */
		GILLESPIE,
		/**
		 * The new laptop
		 */
		LAYNE,
		/** a cluster node */
		CLUSTER
	};

	@Deprecated
	public static Host host;

	@Deprecated
	public static Host initProperties() {
		Host host = Host.GILLESPIE;
		try {
			if (java.net.InetAddress.getLocalHost().getHostName()
					.startsWith("kleinti")) {
				host = Host.KLEINTI;
			} else if (java.net.InetAddress.getLocalHost().getHostName()
					.startsWith("layne")) {
				host = Host.LAYNE;
			} else if (java.net.InetAddress.getLocalHost().getHostName()
					.startsWith("node")) {
				host = Host.CLUSTER;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		switch (host) {
		case LAYNE:
		case KLEINTI:
			System.setProperty("nr.FRAMENET", "framenet-1.5");
			System.setProperty("nr.NOMBANK", "nombank-1.0");
			System.setProperty("nr.WORDNET", "lib/wordnet-3.0");
			System.setProperty("nr.JWS.WORDNET", "lib/wordnet");
			System.setProperty("nr.SEMLINK", "lib/semlink1.1");
			break;
		case CLUSTER:
			System.setProperty("nr.FRAMENET", "framenet-1.5");
			System.setProperty("nr.NOMBANK", "nombank-1.0");
			System.setProperty("nr.WORDNET", "lib/wordnet-3.0");
			System.setProperty("nr.JWS.WORDNET", "lib/wordnet");
			System.setProperty("nr.SEMLINK", "lib/semlink1.1");
			break;
		default:
			System.setProperty("nr.FRAMENET",
					"/resources/corpora/monolingual/annotated/framenet-1.5");
			System.setProperty("nr.WORDNET", "lib/wordnet-3.0");
			System.setProperty("nr.JWS.WORDNET", "lib/wordnet");
			System.setProperty("nr.NOMBANK",
					"/resources/corpora/monolingual/annotated/nombank-1.0");
			System.setProperty("nr.SEMLINK", "lib/semlink1.1");

		}

		Locale.setDefault(Locale.US);

		return host;
	}

	protected void processArguments(final String[] args,
			final Object... optionsBeans) {
		host = initProperties();
		ClassParser cp = new ClassParser();
		CmdLineParser parser = new CmdLineParser(this);

		if (System.getenv("COLUMNS") != null) {
			parser.setUsageWidth(Integer.valueOf(System.getenv("COLUMNS")));
		}

		for (Object o : optionsBeans) {
			cp.parse(o, parser);
		}

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			parser.printUsage(System.err);
			System.exit(1);
		}

		if (args != null && args.length > 0)
			this.commandLine = StringUtil.join(args, " ", 0);
		if (this.printUsage) {
			// System.err.println(parser.printExample(ExampleMode.ALL));
			parser.printUsage(System.err);
			System.exit(0);
		}

		try {
			this.configuration =
					new CompositeConfiguration(Arrays.asList(
							new HierarchicalINIConfiguration(this.configFile),
							new HierarchicalINIConfiguration(
									getDefaultConfigFile())));
		} catch (ConfigurationException e) {
			this.logger.error(e.getLocalizedMessage());
			if (this.configFile.exists()) {
				try {
					this.configuration =
							new HierarchicalINIConfiguration(this.configFile);
				} catch (ConfigurationException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	private URL getDefaultConfigFile() {
		URL url = Main.class.getResource("/default-configuration.ini");
		return url;
	}

	/**
	 * This class returns a Writer for the given file. If the file parameter is
	 * null, a Writer to out is returned. This way, the method can produce
	 * output in the terminal or a file. If any error occurs, the output is
	 * written to standard out and an error message is printed to System.err.
	 * 
	 * @deprecated Use {@link #getOutputStreamForFileOption(File, PrintStream)}
	 *             instead.
	 * 
	 * @param file
	 * @param the
	 *            fallback writer
	 * @return
	 * 
	 */
	@Deprecated
	public Writer
	getWriterForFileOption(final File file, final PrintStream out) {
		if (file == null) {
			try {
				return new OutputStreamWriter(out, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return new OutputStreamWriter(out);
			}
		} else {
			try {
				return new java.io.FileWriter(file);
			} catch (Exception e) {
				System.err.println(e.getLocalizedMessage());
				try {
					return new OutputStreamWriter(out, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					return new OutputStreamWriter(out);
				}
			}
		}
	}

	public OutputStream getOutputStreamForFileOption(final File file,
			final PrintStream out) {
		if (file == null) {
			return out;
		} else {
			if (!file.getAbsoluteFile().getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (file.getName().endsWith(".gz")) {
				try {
					return new GZIPOutputStream(new FileOutputStream(file));
				} catch (Exception e) {
					return out;

				}
			} else {
				try {
					return new FileOutputStream(file);
				} catch (Exception e) {
					System.err.println(e.getLocalizedMessage());
					return out;
				}
			}
		}
	}

	/**
	 * The class returns an input stream. If the given argument is null, the
	 * input stream reads from System.in. If not, it reads from the given file.
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public InputStream getInputStreamForFileOption(final File file)
			throws FileNotFoundException {
		if (file == null) {
			return System.in;
		} else if (file.getName().endsWith(".gz")) {
			try {
				return new GZIPInputStream(new FileInputStream(file));
			} catch (IOException e) {
				return new FileInputStream(file);
			}
		} else {
			return new FileInputStream(file);
		}
	}

	public void removeHandlersFromRootLogger() {
		java.util.logging.Logger logger = java.util.logging.Logger.getGlobal();
		for (Handler hdl : logger.getHandlers()) {
			logger.removeHandler(hdl);
		}
	}

	public Main(final String[] args) {

		this.removeHandlersFromRootLogger();
		// add SLF4JBridgeHandler to j.u.l's root logger, should be done once
		// during
		// the initialization phase of your application
		SLF4JBridgeHandler.install();
		processArguments(args);
	}

	public Main() {
		this.removeHandlersFromRootLogger();

		// add SLF4JBridgeHandler to j.u.l's root logger, should be done once
		// during
		// the initialization phase of your application
		SLF4JBridgeHandler.install();

	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
}
