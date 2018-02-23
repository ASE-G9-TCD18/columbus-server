package com.group9.columbus.test.usermanagement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.group9.columbus.service.UserManagementService;
import com.group9.columbus.test.TestConfig;
import com.group9.columbus.utils.JsonUtils;

@SpringBootTest(classes = {TestConfig.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class UserManagementControllerTest {

	@Autowired
	@Qualifier("mvc")
	private MockMvc mvc;

	@MockBean
	private UserManagementService userManagementService;
	
	@Autowired
	@Qualifier("httpMsgConverter")
	HttpMessageConverter httpMsgConverter;

	private String loginId = "testuser";
	private String password = "testuser";

	@Test
	public void testEditUser(){

		ApplicationUser user = new ApplicationUser();
		user.setLoginId(loginId);
		user.setPassword(password);
		user.setActive(true);
		user.setAge("40");
		user.setContactNumber("1234567890");
		user.setEmailId("temp@example.com");
		user.setFirstName("ABC");
		user.setLastName("Agrahari");
		user.setUserRating(5.0);
		ResponseEntity<String> response = JsonUtils.getJsonForResponse(user);

		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		
		try {
			
		httpMsgConverter.write(user, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		
		String content = mockHttpOutputMessage.getBodyAsString();
		
		// Test
		given(userManagementService.editUser(loginId, user)).willReturn(user);

		MvcResult res = mvc.perform(
				put("/user/" + loginId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
				.with(user(loginId).password(password)))
				.andExpect(status().isOk())
				.andReturn();
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
