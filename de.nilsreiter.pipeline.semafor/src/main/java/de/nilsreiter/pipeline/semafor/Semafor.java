package de.nilsreiter.pipeline.semafor;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.util.Level;

import de.nilsreiter.util.IOUtil;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticArgument;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticPredicate;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import edu.cmu.cs.lti.ark.fn.parsing.ParserDriver;

public class Semafor extends JCasAnnotator_ImplBase {

	public static final String PARAM_SENTENCE_LIMIT = "Sentence Limit";
	public static final String PARAM_MODEL = "Semafor Model";

	@ConfigurationParameter(name = PARAM_SENTENCE_LIMIT, mandatory = false)
	int limit = Integer.MAX_VALUE;

	@ConfigurationParameter(name = PARAM_MODEL)
	String modelDirectory;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		getLogger().setLevel(Level.ALL);
		try {
			// Creating temporal directory for
			// 1. the input files to semafor
			File tmpdirInput = IOUtil.createTempDir("semafor", ".input");
			System.err.println(tmpdirInput.getAbsolutePath());
			// 2. the output files of semafor (and a log file)
			File tmpdirOutput = IOUtil.createTempDir("semafor", ".output");
			System.err.println(tmpdirOutput.getAbsolutePath());

			File parserOutput = new File(tmpdirInput, "parser.conll");
			File tokenized = new File(tmpdirInput, "tokenized.txt");
			File postagged = new File(tmpdirInput, "postagged.txt");

			// Opening filewriters
			FileWriter fw = new FileWriter(parserOutput);
			FileWriter fw_tok = new FileWriter(tokenized);
			FileWriter fw_pos = new FileWriter(postagged);

			FileWriter missingSentences =
					new FileWriter(new File(tmpdirOutput,
							"missingSentences.txt"));
			FileWriter log = new FileWriter(new File(tmpdirOutput, "log.txt"));
			log.write("Using " + tmpdirInput.getAbsolutePath()
					+ " as temporary directory.\n");

			List<Sentence> sentenceList = new ArrayList<Sentence>();
			sentenceList.addAll(JCasUtil.select(jcas, Sentence.class));

			int sentenceCounter = 0;
			for (Sentence sentence : sentenceList) {
				int i = 0;
				List<Token> tokenList =
						JCasUtil.selectCovered(jcas, Token.class, sentence);
				for (Token pt : tokenList) {
					Dependency dep = null;
					if (JCasUtil.selectCovered(jcas, Dependency.class, pt)
							.size() > 0)
						dep =
								JCasUtil.selectCovered(jcas, Dependency.class,
										pt).get(0);
					fw.write(String.valueOf(++i));
					fw.write("\t");
					fw.write(pt.getCoveredText());
					fw.write("\t");
					fw.write(pt.getCoveredText());
					fw.write("\t");
					fw.write(pt.getPos().getPosValue());
					fw.write("\t");
					fw.write(pt.getPos().getPosValue());
					fw.write("\t");
					fw.write("-");
					fw.write("\t");
					fw.write(dep == null ? String.valueOf(0) : String
							.valueOf(tokenList.indexOf(dep.getGovernor()) + 1));
					fw.write("\t");
					fw.write(dep == null ? "ROOT" : dep.getDependencyType()
							.toUpperCase());
					fw.write("\t");
					fw.write("-");
					fw.write("\t");
					fw.write("-");
					fw.write("\t");
					fw.write("\n");

					fw_tok.write(pt.getCoveredText());
					fw_tok.write(" ");

					fw_pos.write(pt.getCoveredText());
					fw_pos.write("_");
					fw_pos.write(pt.getPos().getPosValue());
					fw_pos.write(" ");
				}
				fw.write("\n");
				fw_tok.write("\n");
				fw_pos.write("\n");

				sentenceCounter++;

				if (sentenceCounter == limit) break;

			};
			fw_tok.close();
			fw_pos.close();
			missingSentences.close();
			log.close();
			fw.close();

			File modelDir = new File(this.modelDirectory);
			File datadir = new File(modelDir, "datadir");
			File outputFile = new File(tmpdirOutput, "output.txt");
			File stopwords =
					new File(
							"/Users/reiterns/Documents/Resources/semafor-model/stopwords.txt");
			File wnProperties =
					new File(
							"/Users/reiterns/Documents/Resources/semafor-model/file_properties.xml");
			if (outputFile.exists()) {
				outputFile.delete();
			}
			try {
				ParserDriver.main(new String[] {
						"mstmode:noserver",
						"mstserver:localhost",
						"mstport:8080",
						"posfile:" + postagged.getAbsolutePath(),
						"test-parsefile:" + parserOutput.getAbsolutePath(),
						"stopwords-file:" + stopwords.getAbsolutePath(),
						"wordnet-configfile:" + wnProperties.getAbsolutePath(),
						"fnidreqdatafile:"
								+ new File(datadir, "reqData.jobj")
										.getAbsolutePath(),
						"goldsegfile:null",
						"userelaxed:no",
						"testtokenizedfile:" + tokenized.getAbsolutePath(),
						"idmodelfile:"
								+ new File(datadir, "idmodel.converted.dat")
										.getAbsolutePath(),
						"alphabetfile:"
								+ new File(new File(modelDirectory, "scan"),
										"parser.conf.unlabeled")
										.getAbsolutePath(),
						"framenet-femapfile:"
								+ new File(modelDirectory,
										"framenet.frame.element.map")
										.getAbsolutePath(),
						"eventsfile:"
								+ new File(tmpdirInput, "events.bin")
										.getAbsolutePath(),
						"spansfile:"
								+ new File(tmpdirInput, "spans")
										.getAbsolutePath(),
						"model:"
								+ new File(datadir, "argmodel.converted.dat")
										.getAbsolutePath(),
						"useGraph:null",
						"frameelementsoutputfile:"
								+ outputFile.getAbsolutePath(),
						"alllemmatagsfile:"
								+ new File(tmpdirInput, "lemmatags") });

			} catch (Exception e) {
				e.printStackTrace();
			}
			BufferedReader br = new BufferedReader(new FileReader(outputFile));
			while (br.ready()) {
				String line = br.readLine();
				String[] lparts = line.split("\t");
				Sentence sentence =
						sentenceList.get(Integer.valueOf(lparts[6]));
				List<Token> tokenList =
						JCasUtil.selectCovered(jcas, Token.class, sentence);
				int numberOfArguments = Integer.valueOf(lparts[1]) - 1;

				// Get the target/evoker token
				Token target = tokenList.get(Integer.valueOf(lparts[4]));

				SemanticPredicate predicate =
						createAnnotation(jcas, target.getBegin(),
								target.getEnd(), SemanticPredicate.class);
				predicate.setCategory(lparts[2]);
				predicate.setArguments(new FSArray(jcas, numberOfArguments));
				for (int i = 0; i < numberOfArguments; i++) {

					int argpos = 7 + i;
					String fe = lparts[argpos];
					String[] tokenRange = lparts[argpos + 1].split(":");
					Token beginToken =
							JCasUtil.selectCovered(jcas, Token.class, sentence)
									.get(Integer.valueOf(tokenRange[0]));
					Token endToken = beginToken;
					if (tokenRange.length > 1)
						endToken =
						JCasUtil.selectCovered(jcas, Token.class,
								sentence).get(
										Integer.valueOf(tokenRange[1]));
					SemanticArgument arg =
							createAnnotation(jcas, beginToken.getBegin(),
									endToken.getEnd(), SemanticArgument.class);
					arg.setRole(fe);
					predicate.setArguments(i, arg);
				}
			}
			br.close();

			// Delete temporary directories
			tmpdirInput.deleteOnExit();
			tmpdirOutput.deleteOnExit();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}