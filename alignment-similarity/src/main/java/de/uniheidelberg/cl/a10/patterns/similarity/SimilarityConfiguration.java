package de.uniheidelberg.cl.a10.patterns.similarity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import org.kohsuke.args4j.Option;

import de.uniheidelberg.cl.a10.patterns.TrainingConfiguration;

public class SimilarityConfiguration implements TrainingConfiguration,
		Cloneable {

	@Option(name = "--sf_arg_coref", usage = "Include coreferring mentions into comparison")
	public boolean sf_arg_coref = false;

	@Option(name = "--sf_arg_idf", usage = "Weight tokens in bag of words according to idf value")
	public boolean sf_arg_idf = false;

	@Option(name = "--sf_gaussiandistance_var", usage = "This will be used as variance for the gauss curve.")
	public double sf_gaussiandistance_var = 0.2;

	@Option(name = "--sim", aliases = { "-s" }, multiValued = true, usage = "The similarity function(s) to use. Can be specified"
			+ " multiple times.")
	public List<String> similarityFunctions = new LinkedList<String>();

	@Option(name = "--weight", aliases = { "-w" }, multiValued = true, usage = "The weight(s) for the specified similarity functions."
			+ " Can be specified multiple times. If unspecified,"
			+ " all weights are equal.")
	public List<String> weights = new LinkedList<String>();

	@Option(name = "--threshold", usage = "The threshold for the similarity functions. If not set, a"
			+ " geometric distribution w/o threshold will be used.", aliases = { "-t" })
	public double threshold = Double.MIN_VALUE;

	/**
	 * TODO: Alias -o needs to be removed at some point, in order to make room
	 * for --output
	 */
	@Option(name = "--operation", usage = "Sets the way multiple similarity functions are combined. "
			+ "Default: GEO", aliases = { "-o" })
	public Operation combination = Operation.GEO;

	@Option(name = "--history", usage = "If true, the similarity functions try to save their history on disk.")
	public boolean saveHistory = false;

	public SimilarityConfiguration() {

	}

	public SimilarityConfiguration(final SimilarityConfiguration sc) {

	}

	@Override
	public String getWikiDescription() {
		StringBuilder b = new StringBuilder();
		b.append("o=").append(this.combination).append(" ");
		b.append("s=").append(this.similarityFunctions).append(" ");
		b.append("w=").append(this.weights).append(" ");
		if (this.threshold != Double.MIN_VALUE)
			b.append("t=").append(format("%1$3.1f", this.threshold))
					.append(" ");
		return b.toString();
	}

	@Override
	public String getInfoDescription() {
		return null;
	}

	@Override
	public Element getXML() {
		Element r = new Element("trainingconfiguration");
		r.addAttribute(new Attribute("type", this.getClass().getCanonicalName()));
		for (Field f : SimilarityConfiguration.class.getDeclaredFields()) {
			try {
				Element fieldElement = new Element("var");
				fieldElement.addAttribute(new Attribute("name", f.getName()));
				fieldElement.addAttribute(new Attribute("type", f.getType()
						.toString()));
				if (f.getType().equals(List.class)) {
					List<?> l = (List<?>) f.get(this);
					for (Object o : l) {
						Element li = new Element("li");
						li.appendChild(o.toString());
						fieldElement.appendChild(li);
					}
					r.appendChild(fieldElement);
				} else {
					fieldElement.addAttribute(new Attribute("type", f.getType()
							.toString()));
					fieldElement.addAttribute(new Attribute("value", f
							.get(this).toString()));
					r.appendChild(fieldElement);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		// System.err.println(r.asXML());
		return r;
	}

	@Override
	public boolean fromXML(final Element element) {
		Elements fields = element.getChildElements("var");
		for (int i = 0; i < fields.size(); i++) {
			Element fieldElement = fields.get(i);
			String name = fieldElement.getAttributeValue("name");
			String type = fieldElement.getAttributeValue("type");
			Field f = null;
			try {
				f = SimilarityConfiguration.class.getDeclaredField(name);
				if (type.startsWith("interface java.util.List")) {
					List<String> list = new LinkedList<String>();
					Elements listElements = fieldElement.getChildElements("li");
					for (int j = 0; j < listElements.size(); j++) {
						list.add(listElements.get(j).getValue());
					}
					f.set(this, list);
				} else {
					String value = fieldElement.getAttributeValue("value");
					f.set(this, fromString(value, type));
				}
			} catch (NoSuchFieldException e) {
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			}
		}
		return true;
	}

	public static Object fromString(final String s, final String type) {
		if (type.equals("boolean")) {
			return new Boolean(s);
		}
		if (type.equals("int")) {
			return new Integer(s);
		}
		if (type.equals("double"))
			return new Double(s);
		if (type.startsWith("class")) {
			String className = type.split(" ")[1];
			try {
				Class<?> cl = Class.forName(className);
				if (cl == File.class)
					return new File(s);
				if (cl == Operation.class) {
					return Operation.valueOf(s);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return s;
	}

	public static String format(final String s, final Object... ob) {
		StringBuilder b = new StringBuilder();
		Formatter f = new Formatter(b);
		f.format(s, ob);
		f.close();
		return b.toString();
	}

	@Override
	public String getCommandLine() {
		StringBuilder b = new StringBuilder();
		for (Field f : SimilarityConfiguration.class.getDeclaredFields()) {
			Option opt = f.getAnnotation(Option.class);
			try {

				if (opt != null) {
					if (opt.aliases() != null && opt.aliases().length > 0) {
						b.append(opt.aliases()[0]).append(" ");
					} else {
						b.append(opt.name()).append(" ");
					}
					if (f.get(this) instanceof Double) {
						b.append(format("%1$.1f", f.getDouble(this))).append(
								' ');
					} else {
						b.append(f.get(this)).append(" ");
					}
				}
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b.toString();
	}

	/**
	 * @return the threshold
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold
	 *            the threshold to set
	 */
	public void setThreshold(final double threshold) {
		this.threshold = threshold;
	}

	@Override
	public SimilarityConfiguration clone() throws CloneNotSupportedException {
		return (SimilarityConfiguration) super.clone();
	}

	@Override
	public String toString() {
		return this.getCommandLine();
	}
}
