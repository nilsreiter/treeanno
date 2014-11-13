package de.nilsreiter.pipeline.uima;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;

import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase;

public class PdfReader extends ResourceCollectionReaderBase {

	@Override
	public void getNext(CAS aCAS) throws IOException, CollectionException {
		Resource resource = nextFile();
		initCas(aCAS, resource, null);

		InputStream is = null;
		try {
			is = resource.getInputStream();
			PDDocument document = PDDocument.load(is);
			PDFTextStripper stripper = new PDFTextStripper();
			aCAS.setDocumentText(stripper.getText(document));
			document.close();
		} finally {
			closeQuietly(is);
		}

	}
}
