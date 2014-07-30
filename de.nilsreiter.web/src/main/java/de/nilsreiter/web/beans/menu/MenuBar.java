package de.nilsreiter.web.beans.menu;

import java.util.LinkedList;
import java.util.List;

public class MenuBar extends AbstractMenuEntry {
	List<MenuItem> menuItems = new LinkedList<MenuItem>();

	String currentDocument;

	Location.Area area;

	public Location.Area getArea() {
		return area;
	}

	public void setArea(Location.Area ar) {
		area = ar;
	}

	public void setArea(String ar) {
		area = Location.Area.valueOf(ar);
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	};

	public boolean add(MenuItem e) {
		return menuItems.add(e);
	}

	public void add(int index, MenuItem element) {
		menuItems.add(index, element);
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	public String getCurrentDocument() {
		return currentDocument;
	}

	public void setCurrentDocument(String currentDocument) {
		this.currentDocument = currentDocument;
	}

}
