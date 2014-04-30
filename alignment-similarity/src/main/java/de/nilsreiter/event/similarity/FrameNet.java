package de.nilsreiter.event.similarity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.saar.coli.salsa.reiter.framenet.DatabaseReader;
import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader15;
import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameNetRelation;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameRelation;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.algorithms.shortestpath.Distance;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;

public class FrameNet implements SimilarityFunction<Event> {

	de.saar.coli.salsa.reiter.framenet.FrameNet frameNet = null;

	transient Graph<Frame, FrameRelation> graph = null;

	transient Distance<Frame> distance = null;

	boolean debug = false;

	File fnhome = null;

	/**
	 * Relations: Inheritance, Using, Subframe, See_also, ReFraming_Mapping,
	 * Inchoative_of, Causative_of, Precedes, Perspective_on,
	 */
	Set<String> relations = null;

	public FrameNet(final File fnhome) throws FileNotFoundException,
			SecurityException {
		this.frameNet = new de.saar.coli.salsa.reiter.framenet.FrameNet();
		this.fnhome = fnhome;
		this.frameNet.readData(new FNDatabaseReader15(fnhome, false));
	}

	public FrameNet(final File fnhome, final String... relations)
			throws FileNotFoundException, SecurityException {
		this.frameNet = new de.saar.coli.salsa.reiter.framenet.FrameNet();
		DatabaseReader dr = new FNDatabaseReader15(fnhome, false);
		frameNet.readData(dr);
		this.fnhome = fnhome;
		this.relations = new HashSet<String>(Arrays.asList(relations));

	}

	@Override
	public Probability sim(Event arg0, Event arg1) {
		Probability p = Probability.NULL;
		if (arg0.equals(arg1))
			return Probability.ONE;
		try {
			Frame f1 = frameNet.getFrame(arg0.getEventClass());
			Frame f2 = frameNet.getFrame(arg1.getEventClass());
			Number n = getDistance().getDistance(f1, f2);
			if (n != null)
				p = Probability.fromProbability(1.0 / (n.doubleValue() + 1.0));

		} catch (FrameNotFoundException e) {
			p = Probability.NULL;
		}
		return p;
	}

	@Override
	public void readConfiguration(SimilarityConfiguration tc) {
		// TODO Auto-generated method stub

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

	protected UndirectedSparseMultigraph<Frame, FrameRelation> getFrameNetGraph(
			final de.saar.coli.salsa.reiter.framenet.FrameNet frameNet) {
		UndirectedSparseMultigraph<Frame, FrameRelation> graph = new UndirectedSparseMultigraph<Frame, FrameRelation>();

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

}
