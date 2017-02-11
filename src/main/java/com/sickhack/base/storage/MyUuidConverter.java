package com.sickhack.base.storage;

import java.util.UUID;

import org.bson.BsonBinary;
import org.bson.BsonBinarySubType;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonReader;
import org.bson.UuidRepresentation;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.UuidCodec;
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
		logger.info("Converting to {} from {}", targetClass, fromDBObject);
		if (!(fromDBObject instanceof Binary)) {
			throw new MappingException("Unsupported DBObject type. fromDBObject=" + fromDBObject);
		}
		Binary binary = (Binary) fromDBObject;

		// Support both subtypes 3 and 4.
		if (binary.getType() == 0x03) {
			return decodeFromLegacyUuidBinary(binary);
		} else if (binary.getType() == 0x04) {
			return decodeFromNewUuidBinary(binary);
		} else {
			throw new MappingException("Unsupported Binary subtype. fromDBObject=" + fromDBObject);
		}
	}

	private Object decodeFromLegacyUuidBinary(Binary binary) {
		BsonDocument holder = new BsonDocument();
		holder.append("uuid", new BsonBinary(BsonBinarySubType.UUID_LEGACY, binary.getData()));
		BsonReader reader = new BsonDocumentReader(holder);
		reader.readBinaryData("uuid");
		// TODO: Support customization for 'legacy' encoding. This assumes 'Java Legacy' encoding.
		return new UuidCodec(UuidRepresentation.JAVA_LEGACY).decode(reader, DecoderContext.builder().build());
	}

	private Object decodeFromNewUuidBinary(Binary binary) {
		BsonDocument holder = new BsonDocument();
		holder.append("uuid", new BsonBinary(BsonBinarySubType.UUID_STANDARD, binary.getData()));
		BsonReader reader = new BsonDocumentReader(holder);
		// Goes to the binary we need.
		reader.readBsonType(); // Should be DOCUMENT.
		logger.info(reader.readName());
		reader.readBsonType(); // state to 
		
		reader.readBinaryData("uuid");
		return new UuidCodec(UuidRepresentation.STANDARD).decode(reader, DecoderContext.builder().build());
	}
}
