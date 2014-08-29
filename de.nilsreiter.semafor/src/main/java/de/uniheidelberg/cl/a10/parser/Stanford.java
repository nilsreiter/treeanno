package de.uniheidelberg.cl.a10.parser;

import java.io.File;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.parser.dep.IDependency;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.parser.lexparser.ParserQuery;
import edu.stanford.nlp.trees.EnglishGrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.Filter;

public class Stanford extends AbstractDependencyParser {
	LexicalizedParser parser = null;

	public Stanford(final Class<? extends IDependency> depStyle,
			final File modelFile) {
		super(depStyle);
		Options op = new Options();
		op.doDep = true;
		op.setOptions("-outputFormat",
				"wordsAndTags,penn,typedDependenciesCollapsed");
		parser =
				LexicalizedParser.getParserFromFile(
						modelFile.getAbsolutePath(), op);
	}

	@Override
	public boolean parse(final BaseSentence parseTokenList) {
		ParserQuery lpq = this.parser.parserQuery();
		if (lpq.parse(parseTokenList)) {
			Tree tree = lpq.getBestParse();
			tree.indexLeaves();
			GrammaticalStructure grm =
					new EnglishGrammaticalStructure(tree, new Filter<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean accept(final String arg0) {
					return true;
				}
			});
			if (grm != null) {
				for (TypedDependency d : grm.typedDependencies()) {
					TreeGraphNode dep = d.dep();
					TreeGraphNode gov = d.gov();
					if (gov.index() == 0) {
						parseTokenList.get(dep.index() - 1)
						.setDependencyRelation(null);
						parseTokenList.get(dep.index() - 1).setGovernor(null);
					} else {
						parseTokenList.get(dep.index() - 1)
						.setDependencyRelation(
								fromString(d.reln().getShortName()));
						parseTokenList.get(dep.index() - 1).setGovernor(
								parseTokenList.get(gov.index() - 1));
					}
				}

			}
		}
		return true;
	}
}
