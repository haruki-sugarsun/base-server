package com.sickhack.base.ignite;

import javax.cache.CacheException;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.MutableEntry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class MyCounterServiceImpl implements Service, MyCounterService {
	private static final Logger logger = LoggerFactory.getLogger(MyCounterServiceImpl.class);

	/** Auto-injected instance of Ignite. */
	@IgniteInstanceResource
	private Ignite ignite;

	/** Distributed cache used to store counters. */
	private IgniteCache<String, Integer> cache;

	/** Service name. */
	private String svcName;

	ThreadPoolTaskScheduler scheduler;

	/**
	 * Service initialization.
	 */
	@Override
	public void init(ServiceContext ctx) {
		// Pre-configured cache to store counters.
		cache = ignite.cache("myCounterCache");

		svcName = ctx.name();

		System.out.println("Service was initialized: " + svcName);
		logger.info("MyCounterService ({}) initialized (scheduler:{})", MyCounterServiceImpl.this, scheduler);
	}

	/**
	 * Cancel this service.
	 */
	@Override
	public void cancel(ServiceContext ctx) {
		// Remove counter from cache.
		cache.remove(svcName);

		System.out.println("Service was cancelled: " + svcName);
		logger.info("MyCounterService ({}) cancelled (scheduler:{})", this, scheduler);
		scheduler.destroy();
	}

	/**
	 * Start service execution.
	 */
	@Override
	public void execute(ServiceContext ctx) {
		// Since our service is simply represented by a counter
		// value stored in cache, there is nothing we need
		// to do in order to start it up.
		System.out.println("Executing distributed service: " + svcName);
		
		// Use a new scheduler to have a singleton clock.
		scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(1);
		scheduler.initialize();
		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				logger.info("MyCounterService ({}) loop (scheduler:{})", MyCounterServiceImpl.this, scheduler);
			}
		}, 10000L /* 10 sec */);
		logger.info("MyCounterService ({}) executed (scheduler:{})", MyCounterServiceImpl.this, scheduler);
	}

	@Override
	public int get() throws CacheException {
		Integer i = cache.get(svcName);

		return i == null ? 0 : i;
	}

	@Override
	public int increment() throws CacheException {
		return cache.invoke(svcName, new CounterEntryProcessor());
	}

	/**
	 * Entry processor which atomically increments value currently stored in
	 * cache.
	 */
	private static class CounterEntryProcessor implements EntryProcessor<String, Integer, Integer> {
		@Override
		public Integer process(MutableEntry<String, Integer> e, Object... args) {
			int newVal = e.exists() ? e.getValue() + 1 : 1;

			// Update cache.
			e.setValue(newVal);

			return newVal;
		}
	}
}