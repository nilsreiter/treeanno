package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.tree.PrintParenthesesWalker;
import de.ustu.ims.reiter.treeanno.tree.PrintXmlWalker;
import de.ustu.ims.reiter.treeanno.tree.Walker;
import de.ustu.ims.reiter.treeanno.uima.GraphExporter;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class DocumentExport
 */
public class UserDocumentExport extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DataLayer dataLayer = CW.getDataLayer(getServletContext());
		int[] udocIds = Util.getAllUserDocumentIds(request, response);
		String fmt = request.getParameter("format");
		if (udocIds.length > 0) {
			try {
				UserDocument udoc = dataLayer.getUserDocument(udocIds[0]);
				JCas jcas = JCasConverter.getJCas(udoc.getXmi());
				Walker<TreeSegment, String> walker = null;
				if (fmt.equalsIgnoreCase("par"))
					walker = new PrintParenthesesWalker<TreeSegment>();
				else if (fmt.equalsIgnoreCase("xml"))
					walker = new PrintXmlWalker();
				else if (fmt.equalsIgnoreCase("xmi"))
					response.getWriter().print(udoc.getXmi());

				if (walker != null)
					response.getWriter().print(
							GraphExporter.getTreeString(jcas, walker));
			} catch (SQLException | UIMAException | SAXException e) {
				e.printStackTrace();
			}
		}
	}

}
