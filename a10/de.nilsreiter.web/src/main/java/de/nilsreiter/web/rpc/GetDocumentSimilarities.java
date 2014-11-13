package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.io.DBDocumentSimilarityReader;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

/**
 * Servlet implementation class GetDocumentSimilarities
 */
public class GetDocumentSimilarities extends RPCServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DocumentSet documentset = getDocumentSet(request);

		DBDocumentSimilarityReader dbsr =
				new DBDocumentSimilarityReader(docMan.getDatabase());
		try {
			Matrix<Document, Document, Probability> matrix =
					dbsr.read(documentset);
			JSONObject arr = new JSONObject();

			for (int i = 0; i < documentset.getSet().size(); i++) {
				arr.append("list", documentset.getSet().get(i).getId());
				for (int j = 0; j < documentset.getSet().size(); j++) {
					JSONArray arr2 = new JSONArray();
					arr2.put(i);
					arr2.put(j);
					arr2.put(matrix.get(documentset.getSet().get(i),
							documentset.getSet().get(j)));
					arr.append("data", arr2);
				}
			}
			this.returnJSON(response, arr);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
}
