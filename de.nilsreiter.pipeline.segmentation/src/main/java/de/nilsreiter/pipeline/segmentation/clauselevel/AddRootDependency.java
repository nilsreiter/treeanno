package de.nilsreiter.pipeline.segmentation.clauselevel;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;

@TypeCapability(
		inputs = { "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token",
		"de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency" },
		outputs = { "de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency" })
public class AddRootDependency extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Collection<Dependency> deps =
				new LinkedList<Dependency>(JCasUtil.select(jcas,
						Dependency.class));
		for (Dependency dep : deps) {
			Token govToken = dep.getGovernor();
			Collection<Dependency> cov =
					JCasUtil.selectCovering(jcas, Dependency.class, govToken);
			if (cov.isEmpty()) {
				Dependency newDep =
						AnnotationFactory.createAnnotation(jcas,
								govToken.getBegin(), govToken.getEnd(),
								Dependency.class);
				newDep.setDependent(govToken);
				newDep.setGovernor(null);
				newDep.setDependencyType("root");
				// getLogger().info("Adding dependency: " + );
			}
		}
	}
}
