package com.sickhack.base.storage;

import static org.junit.Assert.fail;

import java.util.UUID;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.Test;

public class MyUuidConverterTest {

	MyUuidConverter converter = new MyUuidConverter();

	@Test
	public void test() {
		byte[] bytes = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16}; 
		
		Binary bin = new Binary(BsonBinarySubType.UUID_STANDARD, bytes);
		
		converter.decode(UUID.class, bin);
		
		fail("Not yet implemented");
	}

}
