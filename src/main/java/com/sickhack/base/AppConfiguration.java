package com.sickhack.base;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;

/**
 * Core configuration for the base-server.
 *
 * @author Haruki Sato
 */
@Configuration
@ComponentScan
public class AppConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(AppConfiguration.class);

	/**
	 * Fake bean creation to do initializations.
	 */
	@Bean
	Void initializations() {
		logger.info("Initializing.");
		return null;
	}

	@Bean
	Datastore morphiaDatastore() {
		final Morphia morphia = new Morphia();
		// tell Morphia where to find your classes
		// can be called multiple times with different packages or classes
		morphia.mapPackage("com.sickhack.base.dbmodel");
		// create the Datastore connecting to the default port on the local host
		// using the testing DB.
		Datastore morphiaDatastore = morphia.createDatastore(new MongoClient(), "base_server_testing");
		morphiaDatastore.ensureIndexes();

		return morphiaDatastore;
	}
}
