package de.nilsreiter.web.beans.menu;

public class DocumentMenuBar extends MenuBar {

	public DocumentMenuBar() {
		area = Location.Area.Document;

		MenuItem mi;

		mi = new MenuItem();
		mi.setName("View");
		mi.setHref("view-document");
		mi.setJspFile("/document/document.jsp");
		mi.setNeedsDocument(true);
		add(mi);

		mi = new MenuItem();
		mi.setName("Event Sequence");
		mi.setHref("view-event-sequence");
		mi.setNeedsDocument(true);
		mi.setJspFile("/document/event-sequence.jsp");
		add(mi);

		mi = new MenuItem();
		mi.setName("Event Similarities");
		mi.setHref("view-event-similarities-document");
		mi.setJspFile("/document/event-similarities.jsp");
		mi.setNeedsDocument(true);
		add(mi);

	}

}
