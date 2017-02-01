package com.sickhack.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
}
