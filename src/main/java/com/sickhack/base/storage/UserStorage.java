package com.sickhack.base.storage;

import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mongodb.DuplicateKeyException;
import com.sickhack.base.controller.HomeController;
import com.sickhack.base.dbmodel.UserDbModel;

/**
 * Created by Haruki Sato.
 */
@Component
public class UserStorage {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	private static final int MAX_CREATE_USER_TRIAL = 3;
	// Morphia Datastore
	private final Datastore datastore;

	public UserStorage(Datastore morphiaDatastore) {
		this.datastore = morphiaDatastore;
	}

	public UserDbModel createUser() {
		for (int i = 0; i < MAX_CREATE_USER_TRIAL; i++) {
			UUID newUserId = UUID.randomUUID();

			logger.info("Creating a user with ID {}", newUserId);
			UserDbModel newUser = new UserDbModel().setUserId(newUserId);
			try {
				Key<UserDbModel> key = datastore.save(newUser);
				newUser.setId((ObjectId) key.getId());
				return newUser;
			} catch (DuplicateKeyException e) {
				logger.info("Failed to create a new User.", e);
			}
		}

		// effectively this should never happen.
		throw new RuntimeException("Unable to create new user.");
	}

	public UserDbModel getUser(UUID userId) {
		Query<UserDbModel> x = datastore.find(UserDbModel.class).field("userId").equal(userId);
		return x.get();
	}

	public void writeUser(UserDbModel user) {
		datastore.save(user);
	}

	public List<UserDbModel> getUsers() {
		return datastore.find(UserDbModel.class).asList();
	}
}
