package net.sickhack.base;

import java.util.Date;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.scheduler.SchedulerFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sickhack.base.ignite.ClusterWatcher;
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
		logger.error("Initializing. ignite=", ignite);

		// Ignite.
		IgniteServices svcs = ignite.services();
		svcs.deployClusterSingleton("myClusterSingleton", new MyCounterServiceImpl());
		logger.info("services {}", svcs);

		SchedulerFuture<?> scheduledWatcher = ignite.scheduler().scheduleLocal(new ClusterWatcher(), "5 * * * * *");
		ignite.scheduler().runLocal(new ClusterWatcher());

		// "0 * * * * * *" cause ArrayIndexOutOfBounds...?
		logger.info("Initializations finished. {}", scheduledWatcher);
		logger.info("Waiting ClusterWatcher first Run. {}", scheduledWatcher.get());
		logger.info("ClusterWatcher next run will be {}", new Date(scheduledWatcher.nextExecutionTime()));

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
