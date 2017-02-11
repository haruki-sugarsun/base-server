package com.sickhack.base.storage;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bson.BSON;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWriter;
import org.bson.Transformer;
import org.bson.UuidRepresentation;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.UuidCodec;
import org.bson.types.Binary;

public class StorageUtils {

	private static final AtomicBoolean encodingHookforMongoUUIDAndMorphiaRegistered = new AtomicBoolean(false);

	// https://github.com/mongodb/morphia/issues/935
	// http://3t.io/blog/best-practices-uuid-mongodb/
	public static void makeSureEncodingHookforMongoUUIDAndMorphia() {
		synchronized (encodingHookforMongoUUIDAndMorphiaRegistered) {
			if (encodingHookforMongoUUIDAndMorphiaRegistered.get()) {
				// Already registered.
				return;
			}

			// Add a global encoding hook to transform a UUID into a Binary with
			// subtype of 4
			BSON.addEncodingHook(UUID.class, new Transformer() {
				@Override
				public Object transform(final Object objectToTransform) {
					UUID uuid = (UUID) objectToTransform;
					BsonDocument holder = new BsonDocument();

					// Use UUIDCodec to encode the UUID using binary subtype 4
					BsonDocumentWriter writer = new BsonDocumentWriter(holder);
					writer.writeStartDocument();
					writer.writeName("uuid");
					new UuidCodec(UuidRepresentation.STANDARD).encode(writer, uuid, EncoderContext.builder().build());
					writer.writeEndDocument();

					BsonBinary bsonBinary = holder.getBinary("uuid");
					return new Binary(bsonBinary.getType(), bsonBinary.getData());
				}
			});
			encodingHookforMongoUUIDAndMorphiaRegistered.set(true);
		}
	}

}
