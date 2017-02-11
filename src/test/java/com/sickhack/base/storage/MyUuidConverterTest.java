package com.sickhack.base.storage;

import static org.junit.Assert.fail;

import java.util.UUID;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyUuidConverterTest {
	private static final Logger logger = LoggerFactory.getLogger(MyUuidConverterTest.class);

	MyUuidConverter converter = new MyUuidConverter();

	@Test
	public void test() {
		byte[] bytes = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Binary bin = new Binary(BsonBinarySubType.UUID_STANDARD, bytes);
		logger.info("{}", converter.decode(UUID.class, bin));

		bin = new Binary(BsonBinarySubType.UUID_LEGACY, bytes);
		logger.info("{}", converter.decode(UUID.class, bin));

		bin = new Binary(BsonBinarySubType.UUID_STANDARD, bytes);
		logger.info("{}", converter.decode(UUID.class, bin));

		fail("Not yet implemented");
	}

}
