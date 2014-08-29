package de.uniheidelberg.cl.a10;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileLock;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.math3.util.Pair;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.xml.sax.SAXException;

import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader15;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IRealizedFrame;
import de.saar.coli.salsa.reiter.framenet.IToken;
import de.saar.coli.salsa.reiter.framenet.Sentence;
import de.saar.coli.salsa.reiter.framenet.salsatigerxml.SalsaTigerXML;
import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseToken;
import de.uniheidelberg.cl.a10.data.io.CoNLL09Reader;
import de.uniheidelberg.cl.a10.parser.IDependencyParser;
import de.uniheidelberg.cl.a10.parser.Mate;
import de.uniheidelberg.cl.a10.parser.dep.StanfordDependency;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import de.uniheidelberg.cl.reiter.pos.PTB;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TypedDependency;

public class Util {
	@Deprecated
	public enum SequenceBlock {
		DependencyRoot, FrameName
	};

	public static String toString(final Pair<?, ?> pair) {
		return pair.getKey().toString() + "," + pair.getValue().toString();
	}

	public static <R, C> short[][] getArrays(final Matrix<R, C, Short> matrix) {
		short[][] m =
				new short[matrix.getRowNumber()][matrix.getColumnNumber()];
		List<R> rowList = new LinkedList<R>();
		List<C> colList = new LinkedList<C>();
		rowList.addAll(matrix.getRows());
		colList.addAll(matrix.getColumns());
		for (int r = 0; r < rowList.size(); r++) {
			for (int c = 0; c < colList.size(); c++) {
				m[r][c] = matrix.get(rowList.get(r), colList.get(c));
			}
		}

		return m;
	}

	public static String md5(final String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5hash = new byte[32];
			md.update(text.getBytes("UTF-8"), 0, text.length());
			md5hash = md.digest();
			return Util.convertToHex(md5hash);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	private static String convertToHex(final byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static Document getDocument(final InputStream is)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		DOMReader domReader = new DOMReader();
		Document doc = domReader.read(dBuilder.parse(is));
		return doc;
	}

	public static List<String> getStringSequenceFromCoNLL09File(
			final File file, final SequenceBlock sb) throws IOException {
		CoNLL09Reader cnl =
				new CoNLL09Reader(file, StanfordDependency.class, PTB.class);
		List<String> seq = new LinkedList<String>();
		for (BaseSentence sentence : cnl.getSentences()) {
			seq.add(sentence.getRoot().getLemma());
		}

		return seq;

	}

	public static List<String> getStringSequenceFromSalsaTigerXMLFile(
			final File file, final SequenceBlock sb, final File fnhome)
					throws FileNotFoundException, SecurityException,
					FrameNotFoundException, FrameElementNotFoundException {
		FrameNet frameNet = new FrameNet();
		frameNet.readData(new FNDatabaseReader15(fnhome, false));
		List<String> ret = new LinkedList<String>();
		SalsaTigerXML stx1 = new SalsaTigerXML(frameNet, null);
		stx1.parse(file);
		switch (sb) {
		default:
		case DependencyRoot:
			IDependencyParser parser =
					new Mate(StanfordDependency.class, new File(
							"lib/mate-en-ptb-stanford.model"));
			for (Sentence s : stx1.getSentences()) {
				BaseSentence bs = Util.getParseTokenList(s, false);
				parser.parse(bs);
				if (bs.getRoot().getLemma() != null) {
					ret.add(bs.getRoot().getLemma().intern());
				} else {
					ret.add(bs.getRoot().getWord().toLowerCase().intern());
				}
			}
			break;
		case FrameName:
			for (IRealizedFrame rf : stx1.getRealizedFrames()) {
				ret.add(rf.getFrame().getName());
			}
			break;
		}
		return ret;
	}

	public static BaseSentence getParseTokenList(final Sentence sentence,
			final boolean convertPosTags) {
		BaseSentence parseTokenList = new BaseSentence();

		// 1. tokenized text
		Iterator<? extends IToken> tokenIterator = sentence.getTokenIterator();
		while (tokenIterator.hasNext()) {
			IToken token = tokenIterator.next();
			BaseToken pt = new BaseToken(token.toString());
			IPartOfSpeech pos = token.getPartOfSpeech();
			pt.setPartOfSpeech(pos);

			if (token.getProperty("LEMMA") != null) {
				pt.setLemma(token.getProperty("LEMMA"));
			}
			parseTokenList.add(pt);
		}
		return parseTokenList;
	}

	public static Object readObjectFromFile(final File file)
			throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);

		Object obj = ois.readObject();

		ois.close();
		fis.close();

		return obj;
	}

	/**
	 * This method writes object obj into file file.
	 * 
	 * @param obj
	 *            The object to write
	 * @param file
	 *            The file to write to
	 * @return if the object was successfully written, return true. false
	 *         otherwise.
	 */
	public static boolean writeObjectToFile(final Object obj, final File file) {
		FileOutputStream fos = null;
		FileLock lock = null;
		ObjectOutputStream oos = null;
		if (file.exists()) {
			file.delete();
		}
		try {
			fos = new FileOutputStream(file);
			lock = fos.getChannel().lock();
			oos = new ObjectOutputStream(fos);

			oos.writeObject(obj);
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				lock.release();
			} catch (IOException e) {

			}
			try {
				fos.close();
			} catch (IOException e) {

			}
			try {
				oos.close();
			} catch (IOException e) {

			}
		}
		return false;
	}

	public static void printDependencyParse(final GrammaticalStructure grm,
			final File file) throws IOException {
		FileWriter fw = new FileWriter(file);
		fw.write("digraph G {\n");
		Map<TreeGraphNode, Integer> nodeMap =
				new HashMap<TreeGraphNode, Integer>();
		int n = 0;
		for (TypedDependency d : grm.typedDependencies()) {
			TreeGraphNode dep = d.dep();
			TreeGraphNode gov = d.gov();
			if (!nodeMap.containsKey(dep)) {
				nodeMap.put(dep, n++);
			}
			if (!nodeMap.containsKey(gov)) {
				nodeMap.put(gov, n++);
			}
			fw.write('\t');
			fw.write(nodeMap.get(dep).toString());
			fw.write(" -> ");
			fw.write(nodeMap.get(gov).toString());
			fw.write(" [label=\"" + d.reln().getShortName() + "\"]");
			fw.write('\n');
		}
		for (TreeGraphNode tgn : nodeMap.keySet()) {
			fw.write(nodeMap.get(tgn).toString());
			fw.write(" [label=\"" + tgn.toString() + "\"]\n");
		}
		fw.write("}\n");
		fw.close();
	}

	public static String join(final Collection<? extends Object> coll,
			final String delim) {
		StringBuffer buf = new StringBuffer();
		Iterator<?> colli = coll.iterator();
		buf.append(colli.next().toString().trim());
		while (colli.hasNext()) {
			buf.append(delim);
			buf.append(colli.next().toString().trim());
		}
		return buf.toString();
	}

	public static String join(final Collection<? extends Object> coll,
			final String delim, final String prefix) {
		StringBuffer buf = new StringBuffer();
		Iterator<?> colli = coll.iterator();

		buf.append(prefix);
		buf.append(colli.next().toString());
		while (colli.hasNext()) {
			buf.append(delim);
			buf.append(prefix);
			buf.append(colli.next().toString());
		}
		return buf.toString();
	}

	/**
	 * Checks, whether the String-array array the String obj contains.
	 * 
	 * @param array
	 *            The array
	 * @param obj
	 *            The string
	 * @return true or false
	 */
	public static boolean contains(final String[] array, final String obj) {
		for (String s : array) {
			if (s.equals(obj)) return true;
		}
		return false;
	}

	public static String join(final int[] array, final String delimiter) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			buf.append(String.valueOf(array[i]));
			buf.append(delimiter);
		}
		String r = buf.toString();
		return r.substring(0, r.length() - delimiter.length());
	}

	public static String join(final String[] array, final String delimiter,
			final int starting) {
		StringBuffer buf = new StringBuffer();
		for (int i = starting; i < array.length; i++) {
			buf.append(array[i]);
			buf.append(delimiter);
		}
		String r = buf.toString();
		return r.substring(0, r.length() - delimiter.length());
	}

	public static String
			joinWithIndex(final int[] array, final String delimiter) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			buf.append(String.valueOf(i)).append(':');
			buf.append(String.valueOf(array[i]));
			buf.append(delimiter);
		}
		String r = buf.toString();
		return r.substring(0, r.length() - delimiter.length());
	}

	public static String joinWithIndex(final String[] array,
			final String delimiter) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			buf.append(String.valueOf(i)).append(':');
			buf.append(array[i]);
			buf.append(delimiter);
		}
		String r = buf.toString();
		return r.substring(0, r.length() - delimiter.length());
	}

	public static String toString(final String[] arr) {
		StringBuffer buf = new StringBuffer("[");
		if (arr.length > 0) {
			buf.append(arr[0]);
			for (int i = 1; i < arr.length; i++) {
				buf.append(',');
				buf.append(arr[i]);
			}
		}
		buf.append(']');
		return buf.toString();
	}

	public static <T> T[] replace(final T[] arr, final T arg0, final T arg1) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(arg0)) {
				arr[i] = arg1;
			}
		}
		return arr;
	}

	public static <T> List<T>
			replace(final List<T> list, final T e1, final T e2) {
		while (list.indexOf(e1) >= 0) {
			list.set(list.indexOf(e1), e2);
		}
		return list;
	}

	public static <A, B> Map<List<A>, List<B>> mapCopy(
			final Map<List<A>, List<B>> map) {
		Map<List<A>, List<B>> r = new HashMap<List<A>, List<B>>();
		for (Entry<List<A>, List<B>> entry : map.entrySet()) {
			r.put(new LinkedList<A>(entry.getKey()),
					new LinkedList<B>(entry.getValue()));
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> copy(final List<T> l) {
		if (l instanceof ArrayList) {
			return (List<T>) ((ArrayList<T>) l).clone();
		}
		if (l instanceof LinkedList) {
			return (List<T>) ((LinkedList<T>) l).clone();
		}
		LinkedList<T> r = new LinkedList<T>();
		for (T e : l) {
			r.add(e);
		}
		return r;
	}

	/**
	 * This method does mathematical scaling of values, as described <a href =
	 * "http://stackoverflow.com/questions/5294955/how-to-scale-down-a-range-of
	 * -numbers-with-a-known-min-and-max-value">here</a>.
	 * 
	 * @param min
	 *            The minimal value of x
	 * @param max
	 *            The maximal value of x
	 * @param a
	 *            The lower bound of the target range
	 * @param b
	 *            The upper bound of the target range
	 * @param x
	 *            The actual value x
	 * @return A scaled version of x
	 */
	public static double scale(final double min, final double max,
			final double a, final double b, final double x) {
		double r = ((b - a) * (x - min) / (max - min)) + a;
		return r;
	}

	public static String format(final String s, final Object... ob) {
		StringBuilder b = new StringBuilder();
		Formatter f = new Formatter(b);
		f.format(s, ob);
		f.close();
		return b.toString();
	}

}
