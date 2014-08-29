package de.uniheidelberg.cl.a10.parser;

import is2.data.SentenceData09;
import is2.parser52LX2.Options;
import is2.parser52LX2.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseToken;
import de.uniheidelberg.cl.a10.parser.dep.IDependency;

public class Mate extends AbstractDependencyParser implements ITrainable,
IRetrainable {
	Parser parser = null;

	public Mate(final Class<? extends IDependency> depStyle,
			final File modelFile) {
		super(depStyle);
		loadModel(modelFile);

	}

	public Mate(final Class<? extends IDependency> depStyle) {
		super(depStyle);
	}

	private boolean loadModel(final File modelFile) {
		PrintStream out = System.out;

		System.setOut(System.err);
		parser =
				new Parser(new Options(new String[] { "-model",
						modelFile.getAbsolutePath(), "-decode", "proj" }));
		System.setOut(out);
		return parser != null;

	}

	@Override
	public boolean parse(final BaseSentence sentence) {
		SentenceData09 out = parser.apply(sentence.getSentenceData09());
		for (int i = 0; i < out.length(); i++) {
			BaseToken current = sentence.get(i);
			int parent = out.pheads[i];
			if (parent == 0) {
				current.setGovernor(null);
				current.setDependencyRelation(null);
			} else {
				BaseToken governor = sentence.get(parent - 1);
				current.setGovernor(governor);

				current.setDependencyRelation(fromString(out.plabels[i]
						.toLowerCase()));

			}
		}
		return true;
	}

	@Override
	public void train(final List<BaseSentence> corpus, final File modelFile,
			final Object... options) {
		try {
			File tmpFile = File.createTempFile("training", ".conll");
			FileWriter fw = new FileWriter(tmpFile);
			for (BaseSentence sentence : corpus) {
				if (sentence.hasDependencyAnnotation()) {
					fw.write(sentence.getSentenceData09().toString());
				}
			}
			fw.close();
			Parser.main(new String[] { "-train", tmpFile.getAbsolutePath(),
					"-model", modelFile.getAbsolutePath() });
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadModel(modelFile);
	}

	@Override
	public boolean retrain(final List<BaseSentence> corpus,
			final boolean saveModel, final Object... options) {
		boolean ret = true;
		for (BaseSentence sentence : corpus) {
			if (sentence.hasDependencyAnnotation()) {
				float updateFactor;
				int iterations;
				if (options.length > 1) {
					updateFactor = ((Float) options[0]).floatValue();
					iterations = ((Integer) options[1]).intValue();
				} else {
					updateFactor = 0.001f;
					iterations = 10;
				}
				System.err.println("Update factor: " + updateFactor);
				System.err.println("Iterations: " + iterations);

				boolean suc =
						parser.retrain(sentence.getSentenceData09(),
								updateFactor, iterations);
				if (!suc) {
					System.err.println("Retraining error with sentence \n"
							+ sentence.getSentenceData09().toString());
				}
				ret = ret && suc;
			}
		}
		if (saveModel) {
			try {
				parser.writeModell(parser.options, parser.params, null,
						parser.pipe.cl);
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		return ret;
	}

	public boolean retrain(final SentenceData09 sentence,
			final boolean saveModel, final Object... options) {
		boolean ret = true;
		float updateFactor;
		int iterations;
		if (options.length > 1) {
			updateFactor = ((Float) options[0]).floatValue();
			iterations = ((Integer) options[1]).intValue();
		} else {
			updateFactor = 0.001f;
			iterations = 10;
		}
		System.err.println("Update factor: " + updateFactor);
		System.err.println("Iterations: " + iterations);

		boolean suc = parser.retrain(sentence, updateFactor, iterations);
		if (!suc) {
			System.err.println("Retraining error with sentence \n"
					+ sentence.toString());
		}
		ret = ret && suc;

		if (saveModel) {
			try {
				parser.writeModell(parser.options, parser.params, null,
						parser.pipe.cl);
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		return ret;
	}

	@Override
	public boolean retrain(final BaseSentence sentence,
			final boolean saveModel, final Object... options) {
		boolean ret = true;
		if (sentence.hasDependencyAnnotation()) {
			float updateFactor;
			int iterations;
			if (options.length > 1) {
				updateFactor = ((Float) options[0]).floatValue();
				iterations = ((Integer) options[1]).intValue();
			} else {
				updateFactor = 0.001f;
				iterations = 10;
			}
			System.err.println("Update factor: " + updateFactor);
			System.err.println("Iterations: " + iterations);

			boolean suc =
					parser.retrain(sentence.getSentenceData09(), updateFactor,
							iterations);
			if (!suc) {
				System.err.println("Retraining error with sentence \n"
						+ sentence.getSentenceData09().toString());
			}
			ret = ret && suc;
		}
		if (saveModel) {
			try {
				parser.writeModell(parser.options, parser.params, null,
						parser.pipe.cl);
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		return ret;
	}
}
