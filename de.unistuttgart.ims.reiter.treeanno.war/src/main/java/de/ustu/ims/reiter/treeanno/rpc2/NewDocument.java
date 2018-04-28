package de.ustu.ims.reiter.treeanno.rpc2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;
import de.ustu.ims.reiter.treeanno.util.PlainTextPreprocess;

@Path("/NewDocument")
public class NewDocument {

	@javax.ws.rs.core.Context
	ServletContext context;

	@javax.ws.rs.core.Context
	HttpServletRequest request;

	@POST
	public Response doPost() throws Exception {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			File temp = File.createTempFile("treeanno-upload", "");
			temp.delete();
			temp.mkdir();

			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Configure a repository (to ensure a secure temp location is used)
			File repository = (File) context.getAttribute("javax.servlet.context.tempdir");
			factory.setRepository(repository);

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			DataLayer dbio = CW.getDataLayer(context);
			// Parse the request
			Project p = null;
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
							pp = new PlainTextPreprocess<Token>(Token.class);
						} else
							pp = new PlainTextPreprocess<Sentence>(Sentence.class);
					}
				} else {
					BufferedWriter bw = new BufferedWriter(new FileWriter(new File(temp, fi.getName())));
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
				document.setName(JCasUtil.selectSingle(jcas, DocumentMetaData.class).getDocumentId());
				document.setXmi(JCasConverter.getXmi(jcas));
				dbio.createNewDocument(document);
			}

			return Response.temporaryRedirect(new URI("../projects.jsp?projectId=" + p.getId())).build();

		}
		throw new NotFoundException();
	}
}
