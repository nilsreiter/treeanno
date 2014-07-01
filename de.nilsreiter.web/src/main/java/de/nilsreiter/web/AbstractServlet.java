package de.nilsreiter.web;

import javax.servlet.http.HttpServlet;

public abstract class AbstractServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected ServletDocumentManager docMan;

	@Override
	public void init() {
		docMan = (ServletDocumentManager) this.getServletContext()
				.getAttribute("docman");
	}
}
