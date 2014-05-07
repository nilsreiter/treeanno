package de.uniheidelberg.cl.a10.main;

import java.io.File;
import java.io.IOException;

import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.cluster.IFullPartition;
import de.uniheidelberg.cl.a10.cluster.io.PartitionReader;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.eval.ClusterEvaluation;
import de.uniheidelberg.cl.a10.eval.ClusterEvaluationFactory;
import de.uniheidelberg.cl.a10.eval.Results;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.a10.eval.impl.Results_impl;
import de.uniheidelberg.cl.a10.patterns.main.Output;

public class ClusterEvaluationMain extends Main {

	ClusterEvaluationConf config = new ClusterEvaluationConf();

	public static void main(final String[] args) throws IOException {
		ClusterEvaluationMain clEval = new ClusterEvaluationMain();
		clEval.processArguments(args, clEval.config);
		clEval.run();
	}

	private void run() throws IOException {
		PartitionReader pr = new PartitionReader(config.dataDirectory);
		pr.setReadFullPartition(true);
		IFullPartition<Document> goldDocument = (IFullPartition<Document>) pr
				.read(config.gold);
		StringBuilder b = new StringBuilder();
		ClusterEvaluation<Document> evaluation = ClusterEvaluationFactory
				.getClusterEvaluation(config.style);

		if (config.arguments.isEmpty()) {
			IFullPartition<Document> silverDocument = (IFullPartition<Document>) pr
					.read(getInputStreamForFileOption(config.silver));
			if (config.clusterId != null)
				evaluation.setRestriction(config.clusterId);
			SingleResult res = evaluation.evaluate(goldDocument,
					silverDocument, null);
			Output output = Output.getOutput(config.outputStyle);
			output.setNumberFormatString("%1$.4f");
			output.setPercentage(false);
			b.append(output.getString(res));
		} else {
			Results resu = new Results_impl();
			for (File f : config.arguments) {
				IFullPartition<Document> silverDocument = (IFullPartition<Document>) pr
						.read(getInputStreamForFileOption(f));
				if (config.clusterId != null)
					evaluation.setRestriction(config.clusterId);
				SingleResult res = evaluation.evaluate(goldDocument,
						silverDocument, f.getName());
				resu.addResult(res);

			}
			Output output = Output.getOutput(config.outputStyle);
			output.setNumberFormatString("%1$.4f");
			output.setPercentage(false);
			b.append(output.getString(resu));

		}

		System.out.print(b.toString());

	}

}
