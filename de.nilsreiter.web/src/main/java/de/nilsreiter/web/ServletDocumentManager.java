package de.nilsreiter.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.nilsreiter.util.StringMap;
import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.HasTokens;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;

public class ServletDocumentManager implements DataStreamProvider {

	@Override
	public InputStream findStreamFor(String objectName) throws IOException {
		String fName = objectName;
		if (!fName.endsWith(".xml"))
			fName = fName + ".xml";
		return Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(fName);

	}

	public Map<Token, String> getClassesForTokens(Alignment<Event> alignment) {
		Map<Token, String> classesForTokens = this
				.getClassesForTokens(alignment.getDocuments());
		for (Link<Event> link : alignment.getAlignments()) {
			for (Event event : link.getElements()) {
				classesForTokens.put(event.firstToken(),
						classesForTokens.get(event.firstToken())
								+ " alignment " + link.getId());
			}
		}
		return classesForTokens;
	}

	public Map<Token, String> getClassesForTokens(Collection<Document> documents) {

		StringMap<Token> classesForTokens = new StringMap<Token>();
		for (Document document : documents) {
			for (Frame frame : document.getFrames()) {
				if (!classesForTokens.containsKey(frame.firstToken())) {
					classesForTokens.put(frame.firstToken(), "");
				}
				classesForTokens.append(frame.firstToken(),
						" frame " + frame.getId());
			}
			for (Event frame : document.getEvents()) {
				if (!classesForTokens.containsKey(((HasTokens) frame
						.getAnchor()).firstToken())) {
					classesForTokens.append(
							((HasTokens) frame.getAnchor()).firstToken(), "");
				}
				classesForTokens.put(
						((HasTokens) frame.getAnchor()).firstToken(), " event "
								+ frame.getId());

			}
			for (Mention mention : document.getMentions()) {
				for (Token token : mention) {
					classesForTokens.append(token,
							" mention " + mention.getGlobalId());
				}
			}
		}
		return classesForTokens;
	}

	public List<String> getDocuments() {
		return Arrays.asList("r0003", "r0009");
	}

	public List<String> getAlignmentDocuments() {
		return Arrays.asList("gold");
	}

}
