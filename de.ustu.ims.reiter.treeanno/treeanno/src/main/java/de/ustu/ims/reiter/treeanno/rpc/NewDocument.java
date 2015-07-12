package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NewDocument
 */
public class NewDocument extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream is = request.getInputStream();
		while (is.available() > 0) {
			System.err.println((char) is.read());
		}

		/*
		 * String[] p = request.getParameterValues("files");
		 * for (int i = 0; i < p.length; i++) {
		 * System.err.println(p[i]);
		 * }
		 */
	}
}
