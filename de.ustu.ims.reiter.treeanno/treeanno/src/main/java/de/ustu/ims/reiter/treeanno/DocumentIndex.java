package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.io.DatabaseIO;

public class DocumentIndex {
	Map<Integer, JCas> map = new HashMap<Integer, JCas>();
	DatabaseIO databaseIO;

	public DocumentIndex() throws ClassNotFoundException, NamingException {
		databaseIO = new DatabaseIO();
	}

	public JCas getDocument(int index) throws UIMAException, SQLException,
			IOException {
		if (!map.containsKey(index)) map.put(index, databaseIO.getJCas(index));
		return map.get(index);
	}

	public JCas getDocument(Document doc) throws UIMAException, SQLException,
	IOException {
		return this.getDocument(doc.getDatabaseId());
	}

	public Set<Integer> openDocuments() {
		return map.keySet();
	}

	public DatabaseIO getDatabaseIO() {
		return databaseIO;
	}

}
