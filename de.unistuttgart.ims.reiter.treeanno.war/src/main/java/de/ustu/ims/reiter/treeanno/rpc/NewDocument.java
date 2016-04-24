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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.xml.sax.SAXException;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;
import de.ustu.ims.reiter.treeanno.util.PlainTextPreprocess;

/**
 * Servlet implementation class NewDocument
 */
@WebServlet("/rpc/NewDocument")
public class NewDocument extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
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
			Project p = null;
			try {
				List<FileItem> items = upload.parseRequest(request);
				PlainTextPreprocess<? extends Annotation> pp = null;
				for (FileItem fi : items) {
					if (fi.isFormField()) {
						String fname = fi.getFieldName();
						String value = fi.getString();
						if (fname.equalsIgnoreCase("projectId")) {
							p = dbio.getProject(Integer.valueOf(value));
						} else if (fname.equalsIgnoreCase("segmenttype")) {
							if (value.equalsIgnoreCase("token")) {
								pp =
										new PlainTextPreprocess<Token>(
												Token.class);
							} else
								pp =
										new PlainTextPreprocess<Sentence>(
												Sentence.class);
						}
					} else {
						BufferedWriter bw =
								new BufferedWriter(new FileWriter(new File(
										temp, fi.getName())));
						IOUtils.copy(fi.getInputStream(), bw);
						bw.flush();
						bw.close();
					}
				}
				Iterator<JCas> iterator = pp.process(temp, "de");
				while (iterator.hasNext()) {
					JCas jcas = iterator.next();
					Document document = new Document();
					document.setProject(p);
					document.setName(JCasUtil.selectSingle(jcas,
							DocumentMetaData.class).getDocumentId());
					document.setXmi(JCasConverter.getXmi(jcas));
					dbio.createNewDocument(document);
				}
				response.sendRedirect("../projects.jsp?projectId=" + p.getId());
				return;
			} catch (FileUploadException | ResourceInitializationException
					| SQLException | SAXException e) {
				throw new ServletException(e);
			}
		}
	}
}
