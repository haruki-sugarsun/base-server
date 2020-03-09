package com.sickhack.base;

import static org.junit.Assert.*;

import org.junit.Test;

import it.sauronsoftware.cron4j.SchedulingPattern;

public class CronFormatTest {

	@Test
	public void test() {
		assertTrue(SchedulingPattern.validate("5 * * * * *"));
		fail("Not yet implemented");
	}

}
