package com.sickhack.base;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.mongodb.MongoClient;
import com.sickhack.base.storage.UserStorage;

@Configuration
public class TestContextConfig {
	@Lazy
	@Bean
	UserStorage userStorage() {
		final Morphia morphia = new Morphia();
		// tell Morphia where to find your classes
		// can be called multiple times with different packages or classes
		morphia.mapPackage("com.sickhack.base.dbmodel");
		// create the Datastore connecting to the default port on the local host
		// using the testing DB.
		Datastore morphiaDatastore = morphia.createDatastore(new MongoClient(), "base_server_testing");
		morphiaDatastore.ensureIndexes();

		return new UserStorage(morphiaDatastore);
	}
}
