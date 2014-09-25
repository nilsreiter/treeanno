package de.nilsreiter.web.beans.menu;

public class DocumentSetMenuBar extends MenuBar {

	public DocumentSetMenuBar() {
		area = Location.Area.DocumentSet;

		MenuItem mi;

		mi = new MenuItem();
		mi.setName("View");
		mi.setHref("view-document-set");
		mi.setJspFile("/documentset/documentset.jsp");
		mi.setNeedsDocument(true);
		add(mi);

		mi = new MenuItem();
		mi.setName("Event Similarities");
		mi.setHref("view-event-similarities-document-set");
		mi.setJspFile("/documentset/event-similarities.jsp");
		mi.setNeedsDocument(true);
		add(mi);

		mi = new MenuItem();
		mi.setName("Event Search");
		mi.setHref("event-search");
		mi.setJspFile("/event-search");
		mi.setNeedsDocument(true);
		add(mi);

	}

}
