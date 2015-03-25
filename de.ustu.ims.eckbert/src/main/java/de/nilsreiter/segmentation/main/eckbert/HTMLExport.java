package de.nilsreiter.segmentation.main.eckbert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.ustu.narr.StoryPart;

public class HTMLExport extends JCasConsumer_ImplBase {
	public static final String PARAM_OUTPUT_DIRECTORY = "Output Directory";

	@ConfigurationParameter(name = PARAM_OUTPUT_DIRECTORY)
	String outputDirectoryName = "";

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		DocumentMetaData dmd =
				JCasUtil.selectSingle(jcas, DocumentMetaData.class);
		try {
			FileWriter fw =
					new FileWriter(new File(outputDirectoryName,
							dmd.getDocumentId() + ".html"));
			StringBuilder b = new StringBuilder();
			b.append("<html><head></head><body>");
			b.append("<h1>").append(dmd.getDocumentTitle()).append("</h1>");
			for (StoryPart sp : JCasUtil.select(jcas, StoryPart.class)) {
				b.append("<div class=\"part\">").append(sp.getCoveredText())
						.append("</div>");
				b.append("<hr/>");
			}
			b.append("</body></html>");
			fw.write(b.toString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
