package de.uniheidelberg.cl.a10.patterns.main;

import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import de.uniheidelberg.cl.a10.eval.Blanc;
import de.uniheidelberg.cl.a10.eval.Evaluation;
import de.uniheidelberg.cl.a10.eval.Results;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
import de.uniheidelberg.cl.a10.patterns.train.BMMConfiguration;
import de.uniheidelberg.cl.reiter.statistics.IHasPRF;

public abstract class Output {
	public enum Style {
		WIKI, LATEX, CSV, HUMAN, TIKZ
	};

	String numberFormatString = "%1$.5e";

	boolean percentage = false;

	boolean printHeader = true;

	public boolean isPrintHeader() {
		return printHeader;
	}

	public void setPrintHeader(final boolean printHeader) {
		this.printHeader = printHeader;
	}

	Map<String, String> headerTranslationMap = new HashMap<String, String>();

	public static Output getOutput(final Style style) {
		switch (style) {
		case LATEX:
			return new LaTeX();
		case CSV:
			return new CSV();
		default:
			return new Wiki();
		}
	}

	protected String getNameString(final BMMConfiguration bmmc) {
		return bmmc.getCommandLine();

	}

	protected String getNameString(final Object obj) {
		if (obj instanceof BMMConfiguration) {
			BMMConfiguration bmmc = (BMMConfiguration) obj;
			return this.getNameString(bmmc);
		}

		return obj.toString();
	}

	protected String getHeaderString(final List<? extends Object> labels) {
		StringBuilder b = new StringBuilder();
		b.append(this.getFirstCellSeparator());
		b.append(this.getCellSeparator());

		Iterator<? extends Object> iterator = labels.iterator();

		while (iterator.hasNext()) {
			String s = iterator.next().toString();
			if (headerTranslationMap.containsKey(s)) {
				b.append(encloseHeaderText(headerTranslationMap.get(s)));
			} else {
				b.append(encloseHeaderText(s));
			}
			if (iterator.hasNext()) {
				b.append(getCellSeparator());
			} else {
				b.append(getLastCellSeparator());
			}
		}

		return b.toString();
	}

	protected String getHeaderString(final SortedSet<? extends Object> labels) {
		StringBuilder b = new StringBuilder();
		b.append(getFirstCellSeparator());
		b.append(getCellSeparator());

		Iterator<? extends Object> iterator = labels.iterator();

		while (iterator.hasNext()) {
			String s = iterator.next().toString();
			if (headerTranslationMap.containsKey(s)) {
				b.append(encloseHeaderText(headerTranslationMap.get(s)));
			} else {
				b.append(encloseHeaderText(s));
			}
			if (iterator.hasNext()) {
				b.append(getCellSeparator());
			} else {
				b.append(getLastCellSeparator());
			}
		}

		return b.toString();
	}

	public String getString(final List<IHasPRF> s) {
		StringBuilder b = new StringBuilder();
		b.append(this.getTopRule());
		if (this.isPrintHeader()) {
			b.append(this.getHeaderString(Arrays.asList("p", "r", "f")));
			b.append(this.getLineSeparator());
			b.append(this.getMidRule());
		}
		for (IHasPRF res : s) {
			b.append(this.toString(res));
			b.append(this.getLineSeparator());
		}
		b.append(this.getBottomRule());
		return b.toString();
	}

	public String getString(final Results res) {
		StringBuilder b = new StringBuilder();
		b.append(getTopRule());
		if (this.isPrintHeader()) {
			b.append(getHeaderString(res.getMeasures()));
			b.append(getLineSeparator());
			b.append(getMidRule());
		}

		SortedMap<String, SingleResult> map = new TreeMap<String, SingleResult>();

		for (SingleResult sres : res.getConfigurations()) {
			map.put(sres.name(), sres);
		}

		for (SingleResult sres : map.values()) {
			b.append(getFirstCellSeparator());
			b.append(getNameString(sres.getIdentifier())).append(
					getCellSeparator());
			for (String m : res.getMeasures()) {
				b.append(getNumberString(sres.getScore(m)));
				b.append(getCellSeparator());
			}
			b.append(getLineSeparator());
		}
		b.append(this.getBottomRule());
		return b.toString();
	}

	public <R, C, V extends Number> String getString(final Matrix<R, C, V> res) {
		StringBuilder b = new StringBuilder();
		boolean first = true;
		SortedSet<R> rows = new TreeSet<R>(res.getRows());
		SortedSet<C> cols = new TreeSet<C>(res.getColumns());

		b.append(this.getTopRule());
		for (R row : rows) {

			if (first && this.isPrintHeader()) {
				b.append(getHeaderString(cols));
				b.append(this.getLineSeparator());
				b.append(this.getMidRule());
				first = false;
			}

			b.append(this.getFirstCellSeparator());
			b.append(this.getNameString(row.toString()));
			b.append(this.getCellSeparator());

			Iterator<C> colIterator = cols.iterator();
			while (colIterator.hasNext()) {
				C col = colIterator.next();
				if (res.get(row, col) != null)
					b.append(this.getNumberString(res.get(row, col)));
				if (colIterator.hasNext()) {
					b.append(this.getCellSeparator());
				} else {
					b.append(this.getLastCellSeparator());
				}
			}
			b.append(this.getLineSeparator());

		}
		b.append(this.getBottomRule());
		return b.toString();
	}

	public String getString(final SingleResult res) {
		StringBuilder b = new StringBuilder();
		b.append(this.getTopRule());
		if (this.isPrintHeader()) {
			b.append(this.getHeaderString(res.getMeasures()));
			b.append(this.getLineSeparator());
			b.append(this.getMidRule());
		}
		b.append(this.getFirstCellSeparator());
		b.append(res.name()).append(this.getCellSeparator());
		for (String m : res.getMeasures()) {
			b.append(this.getNumberString(res.getScore(m)));
			b.append(this.getCellSeparator());
		}
		b.append(this.getLineSeparator());

		b.append(this.getBottomRule());
		return b.toString();
	}

	protected String encloseHeaderText(final String head) {
		return head;
	}

	protected String getFirstCellSeparator() {
		return this.getCellSeparator();
	}

	protected String getLastCellSeparator() {
		return this.getCellSeparator();
	}

	protected String getLineSeparator() {
		return "\n";
	}

	public String toString(final String name, final IHasPRF res) {
		StringBuilder b = new StringBuilder();
		b.append(this.getFirstCellSeparator());
		b.append(name);
		b.append(this.getCellSeparator());
		b.append(getNumberString(res.p()));
		b.append(this.getCellSeparator());
		b.append(getNumberString(res.r()));
		b.append(this.getCellSeparator());
		b.append(getNumberString(res.f()));
		b.append(this.getCellSeparator());
		return b.toString();
	}

	protected String getNumberString(final Number d) {
		String s;
		Formatter formatter = new Formatter();
		if (d instanceof Probability) {
			s = formatter
					.format(numberFormatString,
							((Probability) d).getBDProbability()).out()
					.toString();

		} else {
			s = formatter
					.format(numberFormatString,
							(this.percentage ? d.doubleValue() * 100 : d))
					.out().toString();
		}
		formatter.close();
		return s;
	}

	public String toString(final IHasPRF res) {
		return this.toString(res.name(), res);
	}

	protected abstract String getCellSeparator();

	protected String getMidRule() {
		return "";
	};

	protected String getTopRule() {
		return "";
	}

	protected String getBottomRule() {
		return "";
	}

	/**
	 * @return the numberFormatString
	 */
	protected String getNumberFormatString() {
		return numberFormatString;
	}

	/**
	 * @param numberFormatString
	 *            the numberFormatString to set
	 */
	public void setNumberFormatString(final String numberFormatString) {
		this.numberFormatString = numberFormatString;
	}

	/**
	 * @return the percentage
	 */
	public boolean isPercentage() {
		return percentage;
	}

	/**
	 * @param percentage
	 *            the percentage to set
	 */
	public void setPercentage(final boolean percentage) {
		this.percentage = percentage;
	}
}

class Wiki extends Output {

	@Override
	protected String getFirstCellSeparator() {
		return "|| ";
	}

	@Override
	protected String getCellSeparator() {
		return " || ";
	}

	@Override
	protected String encloseHeaderText(final String head) {
		return "'''" + head + "'''";
	}

	@Override
	protected String getNameString(final BMMConfiguration bmmc) {
		return bmmc.getWikiDescription();
	}
}

class CSV extends Output {

	@Override
	protected String getFirstCellSeparator() {
		return "";
	}

	@Override
	protected String getCellSeparator() {
		return ";";
	}

}

class LaTeX extends Output {

	protected LaTeX() {
		headerTranslationMap.put(Evaluation.PRECISION, "Precision");
		headerTranslationMap.put(Evaluation.RECALL, "Recall");
		headerTranslationMap.put(Evaluation.FSCORE, "F-Score");
		headerTranslationMap.put(Blanc.C_PRECISION, "C: Precision");
		headerTranslationMap.put(Blanc.C_RECALL, "C: Recall");
		headerTranslationMap.put(Blanc.C_FSCORE, "C: F-Score");
		headerTranslationMap.put(Blanc.NC_PRECISION, "NC: Precision");
		headerTranslationMap.put(Blanc.NC_RECALL, "NC: Recall");
		headerTranslationMap.put(Blanc.NC_FSCORE, "NC: F-Score");
	}

	@Override
	protected String getLineSeparator() {
		return " \\\\\n";
	}

	@Override
	protected String getFirstCellSeparator() {
		return "";
	}

	@Override
	protected String getLastCellSeparator() {
		return "";
	}

	@Override
	protected String getCellSeparator() {
		return " & ";
	}

	@Override
	protected String getTopRule() {
		return "\\begin{tabular}{}\n\\toprule\n";
	}

	@Override
	protected String getMidRule() {
		return "\\midrule\n";
	}

	@Override
	protected String getBottomRule() {
		return "\\bottomrule\n\\end{tabular}\n";
	}

}
