package com.sickhack.base.storage;

import static org.junit.Assert.assertEquals;

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
		assertEquals(UUID.fromString("01020304-0506-0708-090a-0b0c0d0e0f10"), converter.decode(UUID.class, bin));

		// http://3t.io/blog/best-practices-uuid-mongodb/
		bin = new Binary(BsonBinarySubType.UUID_LEGACY, bytes);
		assertEquals(UUID.fromString("08070605-0403-0201-100f-0e0d0c0b0a09"), converter.decode(UUID.class, bin));
	}

}
