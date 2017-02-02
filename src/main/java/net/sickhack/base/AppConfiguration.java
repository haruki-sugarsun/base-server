package net.sickhack.base;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sickhack.base.ignite.MyCounterServiceImpl;

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
	Void initializations(Ignite ignite) {
		logger.info("Initializing.");
		
		// Ignite.
		IgniteServices svcs = ignite.services();
		svcs.deployClusterSingleton("myClusterSingleton", new MyCounterServiceImpl());
		logger.info("services {}", svcs);
		
		return null;
	}

	@Bean
	Ignite ignite(IgniteConfiguration igniteConfiguration) {
		Ignite ignite = Ignition.start(igniteConfiguration);
		return ignite;
	}

	@Bean
	IgniteConfiguration igniteConfiguration() {
		IgniteConfiguration config = new IgniteConfiguration();
		return config;
	}
}
