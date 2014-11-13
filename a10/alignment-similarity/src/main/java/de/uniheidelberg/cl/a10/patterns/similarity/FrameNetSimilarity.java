package de.uniheidelberg.cl.a10.patterns.similarity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.saar.coli.salsa.reiter.framenet.DatabaseReader;
import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader15;
import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNetRelation;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameRelation;
import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.algorithms.shortestpath.Distance;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;

/**
 * http://www.cl.uni-heidelberg.de/trac/rituals/wiki/FrameNetSimilarity
 * 
 * @author reiter
 * 
 */
public class FrameNetSimilarity extends
AbstractSimilarityFunction<FrameTokenEvent> {
	public static final long serialVersionUID = 2l;

	FrameNet frameNet = null;

	transient Graph<Frame, FrameRelation> graph = null;

	transient Distance<Frame> distance = null;

	boolean debug = false;

	File fnhome = null;

	/**
	 * This is the maximal distance that two frames can have within FrameNet
	 */
	static int maximalDistance = 14;

	/**
	 * Relations: Inheritance, Using, Subframe, See_also, ReFraming_Mapping,
	 * Inchoative_of, Causative_of, Precedes, Perspective_on,
	 */
	Set<String> relations = null;

	public FrameNetSimilarity(final File fnhome) throws FileNotFoundException,
			SecurityException {
		this.frameNet = new FrameNet();
		this.fnhome = fnhome;
		DatabaseReader dr = new FNDatabaseReader15(fnhome, false);
		frameNet.readData(dr);
	}

	public FrameNetSimilarity() throws FileNotFoundException, SecurityException {
		this.frameNet = new FrameNet();
		this.fnhome = new File(System.getProperty("nr.FRAMENET"));
		DatabaseReader dr = new FNDatabaseReader15(fnhome, false);
		frameNet.readData(dr);

	}

	public FrameNetSimilarity(final File fnhome, final String... relations)
			throws FileNotFoundException, SecurityException {
		this.frameNet = new FrameNet();
		DatabaseReader dr = new FNDatabaseReader15(fnhome, false);
		frameNet.readData(dr);
		this.fnhome = fnhome;
		this.relations = new HashSet<String>(Arrays.asList(relations));

	}

	protected synchronized Distance<Frame> getDistance() {
		if (distance == null) {
			try {
				graph = this.getFrameNetGraph(frameNet);
				distance = new DijkstraDistance<Frame, FrameRelation>(graph);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return distance;
	}

	public Number getDistance(final String frameName1, final String frameName2) {
		try {
			Frame f1 = frameNet.getFrame(frameName1);
			Frame f2 = frameNet.getFrame(frameName2);
			Number n = getDistance().getDistance(f1, f2);
			return n;
		} catch (FrameNotFoundException e) {}
		return Integer.MAX_VALUE;
	}

	public Probability sim(final String frameName1, final String frameName2) {
		if (frameName1.equalsIgnoreCase(frameName2)) return Probability.ONE;
		Probability p = Probability.NULL;
		try {
			Frame f1 = frameNet.getFrame(frameName1);
			Frame f2 = frameNet.getFrame(frameName2);
			Number n = getDistance().getDistance(f1, f2);
			if (n != null)
				p = Probability.fromProbability(1.0 / (n.doubleValue() + 1.0));

		} catch (FrameNotFoundException e) {
			p = Probability.NULL;
		}
		return p;
	}

	@Override
	public Probability sim(final FrameTokenEvent arg0,
			final FrameTokenEvent arg1) throws SimilarityCalculationException {

		Probability p = this.getFromHistory(arg0, arg1);
		if (p != null) return p;

		if (arg0.source() == FrameTokenEvent.Source.Frame
				&& arg1.source() == FrameTokenEvent.Source.Frame) {
			String n1 = arg0.getFrame().getFrameName();
			String n2 = arg1.getFrame().getFrameName();
			p = sim(n1, n2);
			this.putInHistory(arg0, arg1, p);

			return p;
		}
		throw new SimilarityCalculationException();

	}

	protected UndirectedSparseMultigraph<Frame, FrameRelation>
	getFrameNetGraph(final FrameNet frameNet) {
		UndirectedSparseMultigraph<Frame, FrameRelation> graph =
				new UndirectedSparseMultigraph<Frame, FrameRelation>();

		for (Frame frame : frameNet.getFrames()) {
			graph.addVertex(frame);
		}

		for (FrameNetRelation fnr : frameNet.getFrameNetRelations()) {
			String n = fnr.getName();
			if (relations == null || relations.contains(n))
				for (FrameRelation fr : fnr.getFrameRelations()) {
					graph.addEdge(fr, fr.getSubFrame(), fr.getSuperFrame());
				}
		}

		return graph;
	}

	/**
	 * @return the relations
	 */
	public Set<String> getRelations() {
		return relations;
	}

	/**
	 * @param relations
	 *            the relations to set
	 */
	public void setRelations(final Set<String> rels) {
		relations = rels;
		distance = null;

	}

	/**
	 * @return the frameNet
	 */
	public FrameNet getFrameNet() {
		return frameNet;
	}

	@Override
	public String toString() {
		return super.toString() + "." + serialVersionUID
				+ (relations == null ? "" : "." + relations.toString());
	}
}
