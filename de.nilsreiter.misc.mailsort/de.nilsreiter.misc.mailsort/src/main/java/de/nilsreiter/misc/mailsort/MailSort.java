package de.nilsreiter.misc.mailsort;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class MailSort {

	IndexWriter indexWriter;

	Directory index;

	public static void main(String[] args) throws IOException,
	MessagingException {
		MailSort ms = new MailSort();
		ms.createIndex();

		ms.indexOverview();
	}

	private void indexOverview() throws IOException {
		IndexReader ir = DirectoryReader.open(index);
		System.out.println("documents: " + ir.numDocs());
		System.out.println(ir.getTermVectors(0));

	}

	public MailSort() throws IOException {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		index = new RAMDirectory();

		IndexWriterConfig config =
				new IndexWriterConfig(Version.LATEST, analyzer);

		indexWriter = new IndexWriter(index, config);
	}

	public void createIndex() throws MessagingException, IOException {
		String host = "imap.variomedia.de";
		int port = 993;
		String user = "work@nilsreiter.de";
		String password = "TaeF6ya0";

		Properties props = new Properties();

		Session session = Session.getInstance(props, null);
		session.setDebug(false);
		Store store = null;
		store = session.getStore("imaps");

		// Connect
		store.connect(host, port, user, password);

		// Open the Folder
		Folder folder = store.getDefaultFolder();

		for (Folder sFolder : folder.list("INBOX")) {
			indexMessages(sFolder);
		}

		store.close();
		indexWriter.close();

	}

	public void indexMessages(Folder folder) throws MessagingException,
			IOException {
		folder.open(Folder.READ_ONLY);
		for (Message msg : folder.getMessages()) {
			Document doc = new Document();
			doc.add(new TextField("subject", msg.getSubject(), Field.Store.YES));
			doc.add(new StringField("from", msg.getFrom()[0].toString(),
					Field.Store.YES));
			indexWriter.addDocument(doc);
			// System.err.println(doc);
		}
		folder.close(false);
	}
}
