package de.ustu.creta.textgrid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import nu.xom.XPathContext;

import org.apache.commons.io.IOUtils;

import com.lexicalscope.jewel.cli.CliFactory;

public class Main {

	static String NSURL = "http://www.tei-c.org/ns/1.0";

	static XPathContext xpc = new XPathContext("t", NSURL);
	static Set<String> genres = null;

	public static void main(String[] args) throws FileNotFoundException,
	IOException {
		MainOptions options =
				CliFactory.parseArguments(MainOptions.class, args);

		genres =
				new HashSet<String>(IOUtils.readLines(new FileInputStream(
						options.getGenreList())));

		int id = options.getFirstId();

		for (File file : options.getFiles()) {

			Document doc;
			try {
				System.err.println("Parsing file " + file.getName());
				Builder parser = new Builder(false);
				doc = parser.build(file);

				// Identifying the author

				String author = doc.query("//t:author", xpc).get(0).getValue();
				List<TGText> gedichte = new LinkedList<TGText>();

				Nodes nodes = doc.getRootElement().query("//t:TEI", xpc);
				for (int i = 0; i < nodes.size(); i++) {
					Node node = nodes.get(i);
					TGText tgtext = getTGText((Element) node);
					if (tgtext != null) {
						tgtext.setAuthor(author);
						tgtext.setId(id++);

						if (tgtext.getGenre().matches("^Gedicht.*$"))
							gedichte.add(tgtext);
						else {
							StringBuilder b = new StringBuilder();
							b.append(tgtext.getGenre()).append(" - ");
							b.append(tgtext.getFrom()).append("-")
							.append(tgtext.getTo());
							b.append(" - ");
							b.append(tgtext.getAuthor()).append(" - ");
							b.append(tgtext.getTitle()).append(" - ");
							b.append(tgtext.getId());

							File outputFile =
									new File(options.getOutputDirectory(),
											b.toString() + ".txt");
							FileWriter fw = new FileWriter(outputFile);
							fw.write(tgtext.getText());
							fw.flush();
							IOUtils.closeQuietly(fw);
						}
					}
				}

				if (!gedichte.isEmpty()) {
					TGText first = gedichte.get(0);
					String filename =
							first.getGenre() + " - " + first.getFrom() + "-"
									+ first.getTo() + " - " + first.getAuthor()
									+ " - " + id++ + ".txt";
					File outputFile =
							new File(options.getOutputDirectory(), filename);
					FileWriter fw = new FileWriter(outputFile);
					for (TGText text : gedichte) {
						fw.write(text.getText());
						fw.write("\n\n\n\n\n\n\n");
					}
					fw.flush();
					IOUtils.closeQuietly(fw);
				}

			} catch (ValidityException ex) {
				doc = ex.getDocument();
				ex.printStackTrace();
			} catch (ParsingException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static TGText getTGText(Element node) {
		TGText tgtext = new TGText_impl();

		// Identifying the genre
		String aVal = node.getAttributeValue("n");
		try {
			tgtext.setGenre(aVal.split("/")[4]);
			if (!genres.contains(tgtext.getGenre())) return null;
		} catch (Exception e) {
			return null;
		}
		// Identifying the title
		tgtext.setTitle(node
				.query("t:teiHeader/t:fileDesc/t:titleStmt/t:title", xpc)
				.get(0).getValue());

		// Identifying writing date range
		try {
			Element creationNode =
					(Element) node.query(
							"t:teiHeader/t:profileDesc/t:creation/t:date", xpc)
							.get(0);
			if (creationNode.getAttribute("when") != null) {
				String w = creationNode.getAttributeValue("when");
				tgtext.setFrom(w);
				tgtext.setTo(w);
			} else {
				tgtext.setFrom(creationNode.getAttributeValue("notBefore"));
				tgtext.setTo(creationNode.getAttributeValue("notAfter"));
			}
		} catch (java.lang.IndexOutOfBoundsException e) {
			return null;
		}
		// Identifying the body text
		Nodes bodyNodes = node.query("t:text/t:body", xpc);
		if (bodyNodes.size() > 0) {
			Element teiElement = (Element) bodyNodes.get(0);

			tgtext.setText(teiElement.getValue());
		}
		return tgtext;
	}
}
