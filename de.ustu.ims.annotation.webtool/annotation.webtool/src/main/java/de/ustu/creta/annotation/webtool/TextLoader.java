package de.ustu.creta.annotation.webtool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

/**
 * Servlet implementation class TextLoader
 */
@Deprecated
public class TextLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TextLoader() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		TempStatic.text =
				IOUtils.toString(
						new FileInputStream(
								new File(
										"/Users/reiterns/Desktop/Der Blonde Eckbert.txt")),
						"UTF-8").trim();

		JSONObject object = new JSONObject();

		object.put("text", TempStatic.text);

		Util.returnJSON(response, object);
	}

}
