package de.nilsreiter.alignment.algorithm;

import java.io.FileNotFoundException;

import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.nilsreiter.alignment.algorithm.impl.BayesianModelMerging_impl;
import de.nilsreiter.alignment.algorithm.impl.Harmonic_impl;
import de.nilsreiter.alignment.algorithm.impl.MRSystemConfiguration;
import de.nilsreiter.alignment.algorithm.impl.MRSystem_impl;
import de.nilsreiter.alignment.algorithm.impl.NeedlemanWunschConfiguration;
import de.nilsreiter.alignment.algorithm.impl.NeedlemanWunsch_impl;
import de.nilsreiter.alignment.algorithm.impl.SameLemma_impl;
import de.nilsreiter.alignment.algorithm.impl.SameSurface_impl;
import de.nilsreiter.alignment.algorithm.impl.WeightedHarmonic_impl;
import de.nilsreiter.event.similarity.SimilarityFunctionFactory;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.impl.DatabaseDataSource_impl;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;
import de.uniheidelberg.cl.a10.patterns.train.BMMConfiguration;

public class AlgorithmFactory {
	Logger logger = LoggerFactory.getLogger(AlgorithmFactory.class);

	public static final String CONFIG_KEY_ALGORITHM = "alignment.algorithm";

	public static <T extends Event> AlgorithmFactory getInstance() {
		AlgorithmFactory af = new AlgorithmFactory();

		return af;
	};

	public AlignmentAlgorithm<Event> getAlgorithm(Database database,
			Configuration configuration) throws ClassNotFoundException,
			FileNotFoundException, SecurityException, InstantiationException,
			IllegalAccessException {
		ConfigurationConverter conv = new ConfigurationConverter();

		Class<?> cl;
		try {
			cl = Class.forName(configuration.getString(CONFIG_KEY_ALGORITHM));
		} catch (ClassNotFoundException e) {
			cl =
					Class.forName("de.nilsreiter.alignment.algorithm."
							+ configuration.getString(CONFIG_KEY_ALGORITHM));
		}
		if (NeedlemanWunsch.class.isAssignableFrom(cl)) {
			NeedlemanWunschConfiguration nwConf =
					(NeedlemanWunschConfiguration) conv.getConfiguration(
							NeedlemanWunschConfiguration.class, configuration);
			NeedlemanWunsch<Event> algo =
					new NeedlemanWunsch_impl<Event>(nwConf,
							SimilarityFunctionFactory.getSimilarityFunction(
									database, nwConf));
			return algo;
		} else if (BayesianModelMerging.class.isAssignableFrom(cl)) {
			BMMConfiguration bmmConf =
					(BMMConfiguration) conv.getConfiguration(
							BMMConfiguration.class, configuration);
			SimilarityFunction<Event> func =
					SimilarityFunctionFactory.getSimilarityFunction(database,
							bmmConf);
			return new BayesianModelMerging_impl<Event>(func, bmmConf);
		} else if (MRSystem.class.isAssignableFrom(cl)) {
			MRSystemConfiguration conf =
					(MRSystemConfiguration) conv.getConfiguration(
							MRSystemConfiguration.class, configuration);
			MRSystem_impl<Event> mrs = new MRSystem_impl<Event>();
			mrs.setConfig(conf);
			mrs.setSimilarityFunction(SimilarityFunctionFactory
					.getSimilarityFunction(database, conf));
			return mrs;
		} else if (WeightedHarmonic.class.isAssignableFrom(cl)) {
			return new WeightedHarmonic_impl<Event>();
		} else if (Harmonic.class.isAssignableFrom(cl)) {
			return new Harmonic_impl<Event>();
		} else if (SameLemma.class.isAssignableFrom(cl)) {
			return new SameLemma_impl<Event>();
		} else if (SameSurface.class.isAssignableFrom(cl)) {
			return new SameSurface_impl<Event>();
		}
		return null;
	}

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

			GenericObjectPool<PoolableConnection> connectionPool =
					new GenericObjectPool<PoolableConnection>(
							poolableConnectionFactory);
			dataSource =
					new PoolingDataSource<PoolableConnection>(connectionPool);
			return this.getAlgorithm(new DatabaseDataSource_impl(dataSource),
					configuration);
		} catch (ClassNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
