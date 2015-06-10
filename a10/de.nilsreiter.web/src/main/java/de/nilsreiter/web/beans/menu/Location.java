package de.nilsreiter.web.beans.menu;

import java.util.Map;

import de.nilsreiter.web.beans.AbstractState;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

public class Location {

	AbstractState<Document> documentState = new AbstractState<Document>(
			Document.class);
	AbstractState<DocumentSet> documentSetState =
			new AbstractState<DocumentSet>(DocumentSet.class);
	AbstractState<Alignment<Event>> alignmentState =
			new AbstractState<Alignment<Event>>(Alignment.class);

	public Location() {}

	public Location(Area area) {
		this.area = area;
	}

	public static enum Area {
		Corpus, Document, DocumentSet, Alignment
	}

	@Deprecated
	String corpus;

	@Deprecated
	Area area = Area.Document;

	@Deprecated
	String documentOrAlignment;

	public String getCorpus() {
		return corpus;
	}

	public void setCorpus(String corpus) {
		this.corpus = corpus;
	}

	public Area getCurrentArea() {
		return area;
	}

	@Deprecated
	public Area getArea() {
		return area;
	}

	@Deprecated
	public void setArea(Area state) {
		this.area = state;
	}

	@Deprecated
	public String getDocumentOrAlignment() {
		return documentOrAlignment;
	}

	@Deprecated
	public void setDocumentOrAlignment(String documentOrAlignment) {
		this.documentOrAlignment = documentOrAlignment;
	}

	public String getCurrentObject() {
		switch (this.getCurrentArea()) {
		case Alignment:
			return this.alignmentState.getCurrentObject();
		case Corpus:
			return null;
		default:
		case Document:
			return this.documentState.getCurrentObject();
		case DocumentSet:
			return this.documentSetState.getCurrentObject();

		}
	}

	@Deprecated
	public void setCurrentObject(Area ara, String obj) {
		switch (ara) {
		case Alignment:
			this.alignmentState.setCurrent(obj);
			break;
		case Corpus:
			break;
		case Document:
			this.documentState.setCurrent(obj);
			break;
		case DocumentSet:
			this.documentSetState.setCurrent(obj);
			break;
		default:
			break;
		}
	}

	public Map<String, ? extends Object> getOpenObjects() {
		return getOpenObjects(getCurrentArea());
	}

	public Map<String, ? extends Object> getOpenObjects(Area area) {
		switch (area) {
		case Alignment:
			return alignmentState.getOpen();
		case Corpus:
			return null;
		default:
		case Document:
			return documentState.getOpen();
		case DocumentSet:
			return documentSetState.getOpen();
		}
	}

	public MenuBar getCurrentMenu() {
		return getMenu(getCurrentArea());
	}

	@Deprecated
	public MenuBar getMenu(Area area) {
		switch (area) {
		case Alignment:
			return new AlignmentMenuBar();
		case Corpus:
			return null;
		default:
		case Document:
			return new DocumentMenuBar();
		case DocumentSet:
			return new DocumentSetMenuBar();
		}
	}

	public String getOpenTarget() {
		switch (this.getArea()) {
		case Alignment:
			return "select-alignment";
		case Corpus:
			return null;
		default:
		case Document:
			return "select-document";
		case DocumentSet:
			return "select-document-set";

		}
	}

	public String getViewTarget() {
		switch (this.getArea()) {
		case Alignment:
			return "view-alignment";
		case Corpus:
			return null;
		default:
		case Document:
			return "document";
		case DocumentSet:
			return "view-document-set";

		}
	}

	public void addOpenObject(Area area, String id, Object obj) {
		switch (area) {
		case Alignment:
			alignmentState.addOpen(id, obj);
			break;
		case Corpus:
			break;
		case Document:
			documentState.addOpen(id, obj);
			System.err.println(documentState.getOpen());
			break;
		case DocumentSet:
			documentSetState.addOpen(id, obj);
			break;
		default:
			break;
		}
	}

	public MenuBar getAdditionalMenuEntries() {
		switch (getCurrentArea()) {
		case Corpus:
			MenuBar mb = new MenuBar();
			MenuItem mi = new MenuItem();
			mi.setName("Manage");
			mi.setHref("manage-alignments");
			mi.setJspFile("/alignment/manage.jsp");
			mb.add(mi);
			return mb;
		default:
			return new MenuBar();
		}
	}

}