package com.sickhack.base.ignite;

import javax.cache.CacheException;
// Example from http://apacheignite.gridgain.org/v1.8/docs/service-example
public interface MyCounterService {
	/**
	 * Increment counter value and return the new value.
	 */
	int increment() throws CacheException;

	/**
	 * Get current counter value.
	 */
	int get() throws CacheException;
}