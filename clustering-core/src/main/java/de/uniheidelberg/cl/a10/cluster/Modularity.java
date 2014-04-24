package de.uniheidelberg.cl.a10.cluster;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import de.uniheidelberg.cl.a10.cluster.io.PartitionReader;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

public class Modularity<T> {
	IDocumentSimilarityFunction<T, Probability> similarity;

	public Modularity(final IDocumentSimilarityFunction<T, Probability> simfun) {
		similarity = simfun;
	}

	public double getModularityBrandes(final IPartition<T> partition) {
		double d = 0.0;
		double m = 0.0;
		Matrix<T, T, Boolean> used = new MapMatrix<T, T, Boolean>(false);
		for (T doc1 : partition.getObjects()) {
			for (T doc2 : partition.getObjects()) {
				if (doc1 != doc2 && !used.get(doc1, doc2)) {
					m += similarity.getSimilarity(doc1, doc2).getProbability();

					used.put(doc1, doc2, true);
					used.put(doc2, doc1, true);
				}
			}
		}
		// System.err.println("m=" + m);
		for (ICluster<T> cluster : partition.getClusters()) {
			// System.err.println("C=" + cluster);
			double d_this = (this.getSumBrandesWithin(cluster) / m);

			double d_sec = 0.0;
			for (T doc : cluster) {
				d_sec += this.getDegree(doc, partition.getObjects());
			}
			double d_r = d_this - (Math.pow((d_sec / (2 * m)), 2.0));
			// System.err.println("d_r=" + d_r);
			d += d_r;
		}

		return Math.abs(d);
	}

	protected double getDegree(final T doc, final Collection<T> documents) {
		double d = 0.0;
		for (T docprime : documents) {
			if (docprime != doc)
				d += similarity.getSimilarity(doc, docprime).getProbability();
		}
		// System.err.println(" deg(" + doc + ")=" + d);
		return d;
	}

	protected double getSumBrandesWithin(final ICluster<T> cluster) {
		double s = 0.0;
		Matrix<T, T, Boolean> used = new MapMatrix<T, T, Boolean>(false);

		for (T doc1 : cluster) {
			for (T doc2 : cluster) {
				if (doc1 != doc2 && !used.get(doc1, doc2)) {
					s += similarity.getSimilarity(doc1, doc2).getProbability();
					used.put(doc1, doc2, true);
					used.put(doc2, doc1, true);
				}
			}
		}
		return s;
	}

	public double getModularityNewman(final IPartition<T> partition) {
		double d = 0.0;
		Matrix<T, T, Boolean> used = new MapMatrix<T, T, Boolean>(false);

		double fullweight = 0.0;
		for (T doc1 : partition.getObjects()) {
			for (T doc2 : partition.getObjects()) {
				if (doc1 != doc2 && !used.get(doc1, doc2)) {
					fullweight += similarity.getSimilarity(doc1, doc2)
							.getProbability();
					used.put(doc1, doc2, true);

					// used.put(doc2, doc1, true);
				}
			}
		}

		System.err.println("fullweight: " + fullweight);
		for (ICluster<T> cluster : partition.getClusters()) {
			double d0 = 0.0;
			for (ICluster<T> icluster : partition.getClusters()) {
				d0 += this.getSumNewman(cluster, icluster, fullweight, used);
			}
			double pself = this
					.getSumNewman(cluster, cluster, fullweight, used);
			System.err.println(cluster + ": " + pself + " - " + Math.sqrt(d0));
			d += (pself - Math.sqrt(d0));
		}
		System.err.println(d);
		return d;
	}

	public double getModularitySimple(final IPartition<T> clustering) {
		Matrix<T, T, Boolean> used = new MapMatrix<T, T, Boolean>(false);

		double withinCluster = 0.0;
		double crossing = 0.0;

		for (T doc1 : clustering.getObjects()) {
			for (T doc2 : clustering.getObjects()) {
				if (!used.get(doc1, doc2)) {
					if (clustering.together(doc1, doc2)) {
						withinCluster += similarity.getSimilarity(doc1, doc2)
								.getProbability();
					} else {
						crossing += similarity.getSimilarity(doc1, doc2)
								.getProbability();
					}
					used.put(doc1, doc2, true);
					used.put(doc2, doc1, true);
				}
			}
		}

		System.err.println("Within: " + withinCluster);
		System.err.println("Crossing: " + crossing);
		return (withinCluster) / (crossing); // Math.abs(d);

	}

	public double getModularity(final IPartition<T> clustering) {
		return this.getModularityBrandes(clustering);

	}

	protected double getSumNewman(final ICluster<T> cluster1,
			final ICluster<T> cluster2, final double i,
			final Matrix<T, T, Boolean> used) {
		double d = 0.0;
		for (T elem1 : cluster1) {
			for (T elem2 : cluster2) {
				if (elem1 != elem2) {
					d += similarity.getSimilarity(elem1, elem2)
							.getProbability();
				}
			}
		}
		return (d / (2 * i));
	}

	protected double getSum(final ICluster<T> cluster1,
			final ICluster<T> cluster2, final double i,
			final Matrix<T, T, Boolean> used) {
		double d = 0.0;
		for (T elem1 : cluster1) {
			for (T elem2 : cluster2) {
				if (elem1 != elem2) {
					if (!used.get(elem1, elem2)) {
						d += similarity.getSimilarity(elem1, elem2)
								.getProbability();
						used.put(elem1, elem2, true);
					}
				}
			}
		}
		return (d / (i));
	}

	public static void main(final String[] args) throws IOException {
		PartitionReader pr = new PartitionReader(new File("data2/silver"));
		IPartition<Document> partition = pr.read(new File(
				"data/ritualpartition.xml"));

		for (File file : new File("cache/storysim").listFiles()) {
			IDocumentSimilarityFunction<Document, Probability> sf = new CachedSimilarityFunction(
					new File("data2/silver"), file);
			Modularity<Document> mod = new Modularity<Document>(sf);
			System.out.println(mod.getModularity(partition));
		}

	}
}
