package com.sickhack.base.storage;

import java.util.UUID;

import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
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
		// TODO: Implement.
		return null;
	}
}
