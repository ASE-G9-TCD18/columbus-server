package com.group9.columbus.db;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.group9.columbus.entity.User;
import com.group9.columbus.repository.UserRepository;

/**
 * Test class for checking persistence and retrieval functionality.
 * @author amit
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class DBTest {
	
	final static Logger logger = Logger.getLogger(DBTest.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Before
	public void init() {
		User testUser = new User();
		testUser.setLoginId("test_user");
		testUser.setFirstName("Test");
		testUser.setLastName("User");
		testUser.setAge(10);
		testUser.setEmailId("test.user@example.com");
		testUser.setPassword("testuser123");
		
		User response = userRepository.save(testUser);
		response.toString();
		logger.info("Inside INIT");
	}
	
	@Test
	public void findTestUser() {
		// given
		String LOGIN_ID = "test_user";
		
		// when
		User user = userRepository.findByLoginId(LOGIN_ID);

		// then
		assertThat(LOGIN_ID).isEqualTo(user.getLoginId());
		logger.info("Find user test completed.");
	}
	
}
