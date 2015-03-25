package de.nilsreiter.pipeline.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ParsingException;
import nu.xom.Text;
import nu.xom.ValidityException;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasBuilder;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;

import de.nilsreiter.pipeline.uima.type.qsa.QSALocation;
import de.nilsreiter.pipeline.uima.type.qsa.QSAOrganization;
import de.nilsreiter.pipeline.uima.type.qsa.QSAPerson;
import de.nilsreiter.pipeline.uima.type.qsa.QSAType;
import de.nilsreiter.pipeline.uima.type.qsa.Quote;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Heading;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph;

public class QSAReader extends JCasCollectionReader_ImplBase {

	public static final String PARAM_INPUT_DIRECTORY = "Input Directory";

	@ConfigurationParameter(name = PARAM_INPUT_DIRECTORY)
	String corpusDirectory;

	File[] fileList;

	String namePattern = ".*.xml";

	int current = 0;

	@Override
	public void initialize(final UimaContext context)
			throws ResourceInitializationException {
		File dir = new File(corpusDirectory);
		fileList = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.matches(namePattern);
			}

		});
	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return current < fileList.length;
	}

	@Override
	public Progress[] getProgress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getNext(JCas jCas) throws IOException, CollectionException {
		File file = fileList[current++];
		BufferedReader br = new BufferedReader(new FileReader(file));

		Builder xBuilder = new Builder();

		Document doc = null;
		try {
			doc = xBuilder.build(br);

		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (doc == null) throw new CollectionException();

		Element rootElement = doc.getRootElement();

		JCasBuilder builder = new JCasBuilder(jCas);

		for (int i = 0; i < rootElement.getChildCount(); i++) {
			Node node = rootElement.getChild(i);
			this.processNode(builder, node);

		}
		DocumentMetaData meta =
				AnnotationFactory.createAnnotation(jCas, 0,
						builder.getPosition(), DocumentMetaData.class);
		meta.setDocumentId(file.getName());
		builder.close();
		jCas.setDocumentLanguage("en");

	}

	protected void processNode(JCasBuilder b, Node node) {
		if (node.getClass().equals(Text.class)) {
			b.add(node.getValue());
		} else if (node.getClass().equals(Element.class)) {
			this.processElement(b, (Element) node);
		}
	}

	protected void processElement(JCasBuilder b, Element element) {
		int start = b.getPosition();
		if (element.getLocalName().equalsIgnoreCase("PARAGRAPH")) {
			for (int i = 0; i < element.getChildCount(); i++)
				this.processNode(b, element.getChild(i));
			b.add(start, Paragraph.class);
		} else if (element.getLocalName().equalsIgnoreCase("HEADING")) {
			for (int i = 0; i < element.getChildCount(); i++)
				this.processNode(b, element.getChild(i));
			b.add(start, Heading.class);
		} else if (element.getLocalName().equalsIgnoreCase("QUOTE")) {
			for (int i = 0; i < element.getChildCount(); i++)
				this.processNode(b, element.getChild(i));
			b.add(start, Quote.class);
		} else {
			QSAType p = null;
			if (element.getLocalName().equalsIgnoreCase("PERSON")) {
				p = b.add(element.getValue(), QSAPerson.class);
			} else if (element.getLocalName().equalsIgnoreCase("ORGANIZATION")) {
				p = b.add(element.getValue(), QSAOrganization.class);
			} else if (element.getLocalName().equalsIgnoreCase("LOCATION")) {
				p = b.add(element.getValue(), QSALocation.class);
			} else if (element.getLocalName().equalsIgnoreCase("QUOTE")) {
				for (int i = 0; i < element.getChildCount(); i++)
					this.processNode(b, element.getChild(i));
			}
			if (p != null) {
				p.setEntityId(element.getAttributeValue("entity"));
				p.setGender(element.getAttributeValue("gender"));
				p.setMentionType(element.getAttributeValue("mentionType"));
				p.setId(element.getAttributeValue("id"));
			}
		}
	}
}
