package de.nilsreiter.web.beans.menu;

public class MenuItem extends AbstractMenuEntry {
	boolean enabled;
	String href;
	String name;
	boolean needsDocument = false;
	String jspFile;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public boolean isNeedsDocument() {
		return needsDocument;
	}

	public void setNeedsDocument(boolean needsDocument) {
		this.needsDocument = needsDocument;
	}

	public String getJspFile() {
		return jspFile;
	}

	public void setJspFile(String jspFile) {
		this.jspFile = jspFile;
	}

}
