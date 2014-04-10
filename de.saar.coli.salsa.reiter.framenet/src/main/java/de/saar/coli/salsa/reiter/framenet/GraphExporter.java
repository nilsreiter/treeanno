/**
 * 
 * Copyright 2007-2010 by Nils Reiter.
 * 
 * This FrameNet API is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3.
 *
 * This FrameNet API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this FrameNet API.  If not, see www.gnu.org/licenses/gpl.html.
 * 
 */
package de.saar.coli.salsa.reiter.framenet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;

/**
 * The methods in this class can be used to export data from FrameNet to a graph
 * object and to DOT-files http://en.wikipedia.org/wiki/DOT_language.
 * 
 * The class (currently) has an empty constructor.
 * 
 * All graphs are directed.
 * 
 * @author Nils Reiter
 * @since 0.4
 * 
 */
public class GraphExporter extends Exporter {

	/**
	 * Constructor. Does nothing.
	 */
	public GraphExporter() {

	}

	/**
	 * Export a graph of semantic types.
	 * 
	 * @param fn
	 *            The FrameNet object
	 * @return A directed graph of SemanticType-objects
	 */
	public DirectedGraph<SemanticType, SemanticTypeEdge> semanticTypeGraph(
			FrameNet fn) {
		DirectedGraph<SemanticType, SemanticTypeEdge> graph = new DefaultDirectedGraph<SemanticType, SemanticTypeEdge>(
				SemanticTypeEdge.class);

		// Add all semantic types
		for (SemanticType st : fn.getAllSemanticTypes()) {
			graph.addVertex(st);
		}

		// Add the relations between the semantic types
		for (SemanticType st : fn.getAllSemanticTypes()) {
			for (SemanticType sst : st.getSubTypes()) {
				graph.addEdge(st, sst, new SemanticTypeEdge());
			}
			for (SemanticType sst : st.getSuperTypes()) {
				graph.addEdge(sst, st, new SemanticTypeEdge());
			}

		}
		return graph;
	}

	/**
	 * This method generates a graph containing the frame elements in FrameNet
	 * 
	 * @param fn
	 *            The FrameNet object
	 * @param relation
	 *            The relation that provides the edges in the graph
	 * @param filterUnconnected
	 *            If set to true, unconnected frame elements are not shown in
	 *            the graph.
	 * @return The graph
	 */
	public DirectedGraph<FrameElement, FrameElementRelation> frameElementGraph(
			FrameNet fn, FrameNetRelation relation, boolean filterUnconnected) {
		DirectedGraph<FrameElement, FrameElementRelation> graph = new DefaultDirectedGraph<FrameElement, FrameElementRelation>(
				FrameElementRelation.class);

		// Add the frame elements
		for (Frame frame : fn.getFrames()) {
			for (FrameElement fe : frame.getFrameElements().values()) {
				if (!filterUnconnected || relation.isRelated(fe))
					graph.addVertex(fe);
			}
		}

		// Add the relations between the frame elements
		for (FrameRelation frelation : relation.getFrameRelations()) {
			for (FrameElementRelation ferel : frelation
					.getFrameElementRelations()) {
				graph.addEdge(ferel.getSubFrameElement(), ferel
						.getSuperFrameElement(), ferel);
			}
		}
		return graph;
	}

	/**
	 * This method generates a directed graph of frame objects, containing the
	 * frame relations that are specifed in the second argument.
	 * 
	 * @param fn
	 *            The FrameNet object
	 * @param relations
	 *            A collection of relations we want to have in the graph
	 * @return The graph
	 */
	public DirectedGraph<Frame, FrameRelation> frameGraph(FrameNet fn,
			Collection<FrameNetRelation> relations) {
		DirectedGraph<Frame, FrameRelation> graph = new DefaultDirectedGraph<Frame, FrameRelation>(
				FrameRelation.class);
		for (Frame frame : fn.getFrames()) {
			graph.addVertex(frame);
		}
		for (FrameNetRelation relation : relations) {
			for (FrameRelation frelation : relation.getFrameRelations()) {
				graph.addEdge(frelation.getSubFrame(), frelation
						.getSuperFrame(), frelation);
			}
		}
		return graph;
	}

	/**
	 * This method generates a directed graph of frame objects, containing a
	 * single frame relation that is specifed in the second argument.
	 * 
	 * @param fn
	 *            The FrameNet object
	 * @param relation
	 *            The relation providing the edges
	 * @param filterUnconnected
	 *            If set to true, unconnected, i.e., isolated, frames are not
	 *            shown in the graph
	 * @return The graph
	 */
	public DirectedGraph<Frame, FrameRelation> frameGraph(FrameNet fn,
			FrameNetRelation relation, boolean filterUnconnected) {
		DirectedGraph<Frame, FrameRelation> graph = new DefaultDirectedGraph<Frame, FrameRelation>(
				FrameRelation.class);
		for (Frame frame : fn.getFrames()) {
			if (!filterUnconnected || relation.isRelated(frame))
				graph.addVertex(frame);
		}
		for (FrameRelation frelation : relation.getFrameRelations()) {
			graph.addEdge(frelation.getSubFrame(), frelation.getSuperFrame(),
					frelation);
		}
		return graph;
	}

	/**
	 * This methods generates a DOTExporter, which can be used to export the
	 * graph in the DOT language.
	 * 
	 * @param <V>
	 *            The class for Vertices (i.e., Frame, FrameElement,
	 *            SemanticType)
	 * @param <E>
	 *            The class for Edges (i.e., FrameRelation,
	 *            FrameElementRelation, SemanticTypeEdge)
	 * @return A DOTExporter
	 */
	protected <V extends IHasNameAndID, E extends IHasName> DOTExporter<V, E> DOTConverter() {
		DOTExporter<V, E> dot = new DOTExporter<V, E>(
				new VertexNameProvider<V>() {
					public String getVertexName(V vertex) {
						return vertex.getIdString();
					}
				}, new VertexNameProvider<V>() {
					public String getVertexName(V vertex) {
						if (vertex
								.getClass()
								.getCanonicalName()
								.equals(
										"de.saar.coli.salsa.reiter.framenet.FNFrameElement")) {
							FrameElement fe = (FrameElement) vertex;
							return fe.getFrame().getName() + "." + fe.getName();
						} else {
							return vertex.getName();
						}
					}
				}, new EdgeNameProvider<E>() {
					public String getEdgeName(E edge) {
						return edge.getName();
					}
				});
		return dot;
	}

	/**
	 * Writes the given graph directly to the writer specified by the first
	 * argument
	 * 
	 * @param <V>
	 *            The class for Vertices (i.e., Frame, FrameElement,
	 *            SemanticType)
	 * @param <E>
	 *            The class for Edges (i.e., FrameRelation,
	 *            FrameElementRelation, SemanticTypeEdge)
	 * @param fw
	 *            The writer to which the DOT expressions are written
	 * @param graph
	 *            The graph we want to export
	 */
	public <V extends IHasNameAndID, E extends IHasName> void writeToDOTFile(
			Writer fw, DirectedGraph<V, E> graph) {
		DOTExporter<V, E> exporter = this.DOTConverter();
		exporter.export(fw, graph);
	}

	/**
	 * Writes the given graph directly to the specified filename
	 * 
	 * @param <V>
	 *            The class for Vertices (i.e., Frame, FrameElement,
	 *            SemanticType)
	 * @param <E>
	 *            The class for Edges (i.e., FrameRelation,
	 *            FrameElementRelation, SemanticTypeEdge)
	 * @param filename
	 *            The file to be written.
	 * @param graph
	 *            The graph we want to export
	 * @throws IOException
	 *             If something happens to/with the file, an exception is thrown
	 */
	public <V extends IHasNameAndID, E extends IHasName> void writeToDOTFile(
			String filename, DirectedGraph<V, E> graph) throws IOException {
		FileWriter fw = new FileWriter(new File(filename));
		this.writeToDOTFile(fw, graph);
	};
}
