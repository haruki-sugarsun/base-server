package com.sickhack.base.storage;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.sickhack.base.TestContextConfig;
import com.sickhack.base.dbmodel.UserDbModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestContextConfig.class, loader = AnnotationConfigContextLoader.class)
public class UserStorageIntegrationTest {
	private static final Logger logger = LoggerFactory.getLogger(UserStorageIntegrationTest.class);

	@Autowired
	UserStorage userStorage;

	@Test
	public void test() {
		// Create
		UserDbModel user1 = userStorage.createUser();
		logger.info("Created {}", user1);
		UserDbModel user2 = userStorage.createUser();
		logger.info("Created {}", user2);
		UUID id1 = user1.getUserId();
		UUID id2 = user2.getUserId();

		// Write
		user1.setName("user1");
		user2.setName("user2");
		userStorage.writeUser(user1);
		userStorage.writeUser(user2);

		// Read
		user1 = null;
		user2 = null;
		user1 = userStorage.getUser(id1);
		user2 = userStorage.getUser(id2);

		assertEquals("user1", user1.getName());
		assertEquals("user2", user2.getName());
	}

}
