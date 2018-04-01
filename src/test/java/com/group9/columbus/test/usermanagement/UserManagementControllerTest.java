package com.group9.columbus.test.usermanagement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.repository.UserRepository;
import com.group9.columbus.test.TestConfig;

@SpringBootTest(classes = { TestConfig.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class UserManagementControllerTest {

	@Autowired
	@Qualifier("mvc")
	private MockMvc mvc;

	@MockBean
	private UserRepository userRepo;

	@Autowired
	@Qualifier("httpMsgConverter")
	HttpMessageConverter httpMsgConverter;

	private String loginId = "testuser";
	private String password = "testuser";

	ApplicationUser user;

	@Before
	public void init() {

		user = new ApplicationUser();
		user.setLoginId(loginId);
		user.setPassword(password);
		user.setActive(true);
		user.setAge("40");
		user.setContactNumber("1234567890");
		user.setEmailId("temp@example.com");
		user.setFirstName("ABC");
		user.setLastName("Agrahari");
		user.setUserRating(5.0);

	}

	@Test
	public void testEditUserForSuccess() {

		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();

		try {

			httpMsgConverter.write(user, MediaType.APPLICATION_JSON, mockHttpOutputMessage);

			String content = mockHttpOutputMessage.getBodyAsString();

			// Test
			//given(userManagementService.editUser(loginId, user)).willReturn(user);
			given(userRepo.findByLoginId(loginId)).willReturn(user);

			MvcResult res = mvc.perform(put("/user/" + loginId).contentType(MediaType.APPLICATION_JSON).content(content)
					.with(user(loginId).password(password))).andExpect(status().isOk()).andReturn();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testEditUserForPasswordChange() {

		ApplicationUser changedUser = user.getCopy();
		changedUser.setPassword("new password");

		// Mocking the user repository
		given(userRepo.findByLoginId(changedUser.getLoginId())).willReturn(user);

		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();

		try {

			// Serializing the changed user to a JSON object
			httpMsgConverter.write(changedUser, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
			String content = mockHttpOutputMessage.getBodyAsString();

			// Mocking the request
			MvcResult res = mvc.perform(put("/user/" + loginId).contentType(MediaType.APPLICATION_JSON).content(content)
					.with(user(loginId).password(password))).andExpect(status().isBadRequest()).andReturn();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testEditUserForLoginIdChange() {

		ApplicationUser changedUser = user.getCopy();
		changedUser.setLoginId("changed user id");

		// Mocking the user repository
		given(userRepo.findByLoginId(changedUser.getLoginId())).willReturn(user);

		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();

		try {

			// Serializing the changed user to a JSON object
			httpMsgConverter.write(changedUser, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
			String content = mockHttpOutputMessage.getBodyAsString();

			// Mocking the request
			MvcResult res = mvc.perform(put("/user/" + loginId).contentType(MediaType.APPLICATION_JSON).content(content)
					.with(user(loginId).password(password))).andExpect(status().isBadRequest()).andReturn();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
