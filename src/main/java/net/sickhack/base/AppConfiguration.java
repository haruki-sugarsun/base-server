package net.sickhack.base;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;

import java.util.concurrent.CompletableFuture;

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
	Server armeriaServer() {
		ServerBuilder sb = Server.builder();
		sb.annotatedService(new Object() {
			@Get("/hello/{name}")
			public HttpResponse hello(@Param("name") String name) {
				return HttpResponse.of(HttpStatus.OK,
						MediaType.PLAIN_TEXT_UTF_8,
						"Hello, %s!", name);
			}
		});

		Server server = sb.build();
		CompletableFuture<Void> future = server.start();
		// Wait until the server is ready.
		future.join();
		return server;
	}

}
