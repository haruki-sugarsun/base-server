package com.sickhack.base.dbmodel;

import java.util.UUID;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by Haruki Sato.
 */
@Entity("user")
@Indexes({ @Index(fields = @Field("userId"), options = @IndexOptions(unique = true)) })
@Data
@Accessors(chain = true)
public class UserDbModel {
	@Id
	private ObjectId id;

	// Unique ID for users.
	private UUID userId;

	// Display Name.
	private String name;

	// Display Snippet.
	private String snippet;
}
