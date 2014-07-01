package de.nilsreiter.web.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public class TestJSONDocumentWriter {

	Document document;
	JSONDocumentWriter jsdw = new JSONDocumentWriter();

	@Before
	public void setUp() throws Exception {
		document = new DataReader().read(this.getClass().getResourceAsStream(
				"/r0003.xml"));

	}

	@Test
	public void testGetJSONDocument() {
		assertNotNull(document);
		JSONObject json = jsdw.getJSON(document);
		assertNotNull(json);
		assertNotNull(json.getJSONArray("tokens"));
		assertNotNull(json.getJSONArray("events"));
	}

	@Test
	public void testGetJSONToken() {
		fail("Not yet implemented");
	}

}
