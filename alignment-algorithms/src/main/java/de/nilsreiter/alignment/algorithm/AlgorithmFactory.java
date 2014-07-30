package de.nilsreiter.alignment.algorithm;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;

import de.nilsreiter.alignment.algorithm.impl.NeedlemanWunsch_impl;
import de.nilsreiter.event.similarity.SimilarityFunctionFactory;
import de.nilsreiter.util.db.impl.DatabaseDataSource_impl;
import de.uniheidelberg.cl.a10.data2.Event;

public class AlgorithmFactory {
	public static final String CONFIG_KEY_ALGORITHM = "alignment.algorithm";

	public static <T extends Event> AlgorithmFactory getInstance() {
		AlgorithmFactory af = new AlgorithmFactory();

		return af;
	};

	public AlignmentAlgorithm<Event> getAlgorithm(Configuration configuration) {

		try {

			DataSource dataSource;
			ConnectionFactory connectionFactory =
					new DriverManagerConnectionFactory(
							configuration.getString("database.url"),
							configuration.getString("database.username"),
							configuration.getString("database.password"));
			PooledObjectFactory<PoolableConnection> poolableConnectionFactory =
					new PoolableConnectionFactory(connectionFactory, null);

			GenericObjectPool<? extends Connection> connectionPool =
					new GenericObjectPool<PoolableConnection>(
							poolableConnectionFactory);
			dataSource =
					new PoolingDataSource<Connection>(
							(ObjectPool<Connection>) connectionPool);

			Class<?> cl;
			cl = Class.forName(configuration.getString(CONFIG_KEY_ALGORITHM));
			if (NeedlemanWunsch.class.isAssignableFrom(cl)) {
				NeedlemanWunsch<Event> algo =
						new NeedlemanWunsch_impl<Event>(
								configuration
								.getDouble(NeedlemanWunsch.PARAM_THRESHOLD),
								SimilarityFunctionFactory
								.getSimilarityFunction(
										new DatabaseDataSource_impl(
												dataSource),
												configuration));
				return algo;
			} else if (BayesianModelMerging.class.isAssignableFrom(cl)) {

			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
