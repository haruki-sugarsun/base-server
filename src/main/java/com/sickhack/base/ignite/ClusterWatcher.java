package com.sickhack.base.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.resources.IgniteInstanceResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClusterWatcher implements Runnable {
	/** Auto-injected instance of Ignite. */
	@IgniteInstanceResource
	private Ignite ignite;

	@Override
	public void run() {
		log.error("Run. {}", ignite);
		// TODO Auto-generated method stub

	}

}
