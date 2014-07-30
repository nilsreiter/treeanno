package de.nilsreiter.web.beans.menu;

public class AlignmentMenuBar extends MenuBar {

	public AlignmentMenuBar() {

		area = Location.Area.Alignment;
		MenuItem mi;

		mi = new MenuItem();
		mi.setName("View");
		mi.setHref("view-alignment");
		mi.setJspFile("/alignment/alignment.jsp");
		mi.setNeedsDocument(true);
		add(mi);

		mi = new MenuItem();
		mi.setName("Event Similarities");
		mi.setHref("view-event-similarities-alignment");
		mi.setJspFile("/alignment/event-similarities.jsp");
		mi.setNeedsDocument(true);
		add(mi);

		mi = new MenuItem();
		mi.setName("Event Scores");
		mi.setHref("view-event-scores");
		mi.setJspFile("/alignment/event-scores.jsp");
		mi.setNeedsDocument(true);
		add(mi);

	}

}
