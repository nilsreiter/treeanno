package de.saar.coli.salsa.reiter.framenet.testing;

import static org.junit.Assert.assertEquals;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import de.saar.coli.salsa.reiter.framenet.STXExporter;

public class TestExporter extends TestBase15 {

	static String tigerXMLFile = System.getProperty("user.dir")
			+ "/src/test/resources/TigerXML.xml";

	@Test
	public void testSTXExporter() throws Exception {
		SAXReader reader = new SAXReader();
		reader.setStripWhitespaceText(true);
		reader.setMergeAdjacentText(true);
		Document document = reader.read(tigerXMLFile);
		STXExporter exporter = new STXExporter();
		Document newDoc = exporter.getDocument(frameNet, document);
		assertEquals("mat, version 3</format></meta><annotation><edgelab",
				newDoc.asXML().substring(100, 150));
		assertEquals("tional=\"false\"/><element name=\"Theme\" optional=\"fa",
				newDoc.asXML().substring(500, 550));

	}
}
