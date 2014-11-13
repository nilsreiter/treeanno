package de.nilsreiter.web.rpc.verbal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.nilsreiter.web.rpc.RPCServlet;

public abstract class VerbalServlet extends RPCServlet {

	private static final long serialVersionUID = 1L;

	protected abstract void processVerb(Verb verb);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	protected Verb getVerb(HttpServletRequest request) {
		String v = request.getParameterValues("verb")[0];
		try {
			Verb verb =
					(Verb) Class.forName("de.nilsreiter.web.rpc.verbal." + v)
					.newInstance();

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
