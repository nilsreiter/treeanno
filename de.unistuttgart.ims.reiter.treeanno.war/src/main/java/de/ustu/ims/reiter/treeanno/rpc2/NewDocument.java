package de.ustu.ims.reiter.treeanno.rpc2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

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
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response doPost(@FormDataParam("projectId") int projectId, @FormDataParam("segmenttype") String segmentType,
			@FormDataParam("files") List<FormDataBodyPart> bodyParts,
			@FormDataParam("files") List<FormDataContentDisposition> fileDetail) throws Exception {

		DataLayer dbio = CW.getDataLayer(context);

		File temp = File.createTempFile("treeanno-upload", "");
		temp.delete();
		temp.mkdir();

		PlainTextPreprocess<? extends Annotation> pp = null;

		for (int i = 0; i < bodyParts.size(); i++) {
			BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
			String fileName = bodyParts.get(i).getContentDisposition().getFileName();
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(temp, fileName)));
			IOUtils.copy(bodyPartEntity.getInputStream(), bw);
			bw.flush();
			bw.close();
		}

		if (segmentType.equalsIgnoreCase("token"))
			pp = new PlainTextPreprocess<Token>(Token.class);
		else
			pp = new PlainTextPreprocess<Sentence>(Sentence.class);

		Project p = dbio.getProject(projectId);

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
}
