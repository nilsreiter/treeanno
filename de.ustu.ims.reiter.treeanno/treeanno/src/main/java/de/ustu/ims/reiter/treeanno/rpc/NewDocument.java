package de.ustu.ims.reiter.treeanno.rpc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.xml.sax.SAXException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.util.PlainTextPreprocess;

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
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			File temp = File.createTempFile("treeanno-upload", "");
			temp.delete();
			temp.mkdir();

			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Configure a repository (to ensure a secure temp location is used)
			ServletContext servletContext =
					this.getServletConfig().getServletContext();
			File repository =
					(File) servletContext
							.getAttribute("javax.servlet.context.tempdir");
			factory.setRepository(repository);

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			DataLayer dbio = CW.getDataLayer(getServletContext());
			// Parse the request
			try {
				Project p = null;
				List<FileItem> items = upload.parseRequest(request);
				for (FileItem fi : items) {
					if (fi.isFormField()) {
						String fname = fi.getFieldName();
						String value = fi.getString();
						if (fname.equalsIgnoreCase("projectId")) {
							p = dbio.getProject(Integer.valueOf(value));
						}
					} else {
						IOUtils.copy(fi.getInputStream(), new BufferedWriter(
								new FileWriter(new File(temp, fi.getName()))));
					}
				}
				PlainTextPreprocess<Sentence> pp =
						new PlainTextPreprocess<Sentence>(Sentence.class);
				Iterator<JCas> iterator = pp.process(temp, "de");
				while (iterator.hasNext()) {
					Document document = dbio.getNewDocument(p);
					dbio.updateJCas(document, iterator.next());

				}
				response.sendRedirect("projects.jsp");
				return;
			} catch (FileUploadException | ResourceInitializationException
					| SQLException | SAXException e) {
				throw new ServletException(e);
			}
		}
	}
}
