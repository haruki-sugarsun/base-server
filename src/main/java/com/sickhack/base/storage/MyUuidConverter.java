package com.sickhack.base.storage;

import java.util.UUID;

import org.bson.BsonBinarySubType;
import org.bson.BsonSerializationException;
import org.bson.types.Binary;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Idea from https://gist.github.com/magro/742807
public class MyUuidConverter extends TypeConverter implements SimpleValueConverter {
	private static final Logger logger = LoggerFactory.getLogger(MyUuidConverter.class);

	public MyUuidConverter() {
		super(UUID.class);
	}

	@Override
	public Object encode(final Object value, final MappedField optionalExtraInfo) {
		logger.info("Encoding {}", value);
		// TODO: Implement.
		return null;
	}

	@Override
	public Object decode(Class<?> targetClass, Object fromDBObject, MappedField optionalExtraInfo) {
		logger.info("Decoding to {} from {}", targetClass, fromDBObject);
		if (!(fromDBObject instanceof Binary)) {
			throw new MappingException("Unsupported DBObject type. fromDBObject=" + fromDBObject);
		}
		Binary binary = (Binary) fromDBObject;

		// Support both subtypes 3 and 4.
		if (binary.getType() == 0x03 || binary.getType() == 0x04) {
			return decodeFromBinary(binary);
		} else {
			throw new MappingException("Unsupported Binary subtype. fromDBObject=" + fromDBObject);
		}
	}

	public UUID decodeFromBinary(Binary binary) {
		byte subType = binary.getType();
		byte[] bytes = binary.getData();

		if (bytes.length != 16) {
			throw new BsonSerializationException(String.format("Expected length to be 16, not %d.", bytes.length));
		}

		if (subType == BsonBinarySubType.UUID_LEGACY.getValue()) {
			// TODO: Support customization for 'legacy' encoding. This assumes
			// 'Java Legacy' encoding.
			reverseByteArray(bytes, 0, 8);
			reverseByteArray(bytes, 8, 8);
		}

		return new UUID(readLongFromArrayBigEndian(bytes, 0), readLongFromArrayBigEndian(bytes, 8));
	}

	private static void writeLongToArrayBigEndian(final byte[] bytes, final int offset, final long x) {
		bytes[offset + 7] = (byte) (0xFFL & (x));
		bytes[offset + 6] = (byte) (0xFFL & (x >> 8));
		bytes[offset + 5] = (byte) (0xFFL & (x >> 16));
		bytes[offset + 4] = (byte) (0xFFL & (x >> 24));
		bytes[offset + 3] = (byte) (0xFFL & (x >> 32));
		bytes[offset + 2] = (byte) (0xFFL & (x >> 40));
		bytes[offset + 1] = (byte) (0xFFL & (x >> 48));
		bytes[offset] = (byte) (0xFFL & (x >> 56));
	}

	private static long readLongFromArrayBigEndian(final byte[] bytes, final int offset) {
		long x = 0;
		x |= (0xFFL & bytes[offset + 7]);
		x |= (0xFFL & bytes[offset + 6]) << 8;
		x |= (0xFFL & bytes[offset + 5]) << 16;
		x |= (0xFFL & bytes[offset + 4]) << 24;
		x |= (0xFFL & bytes[offset + 3]) << 32;
		x |= (0xFFL & bytes[offset + 2]) << 40;
		x |= (0xFFL & bytes[offset + 1]) << 48;
		x |= (0xFFL & bytes[offset]) << 56;
		return x;
	}

	// reverse elements in the subarray data[start:start+length]
	public static void reverseByteArray(final byte[] data, final int start, final int length) {
		for (int left = start, right = start + length - 1; left < right; left++, right--) {
			// swap the values at the left and right indices
			byte temp = data[left];
			data[left] = data[right];
			data[right] = temp;
		}
	}
}
