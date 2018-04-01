package com.group9.columbus.test.login;

import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.test.TestConfig;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

/**
 * Unit Test Class for LoginController Created by yang on 08/02/2018.
 */

@SpringBootTest(classes = {TestConfig.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class UserLoginSignupTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	// Matcher for Bearer Authorization
	private Matcher<String> authorMatcher = Matchers.startsWith("Bearer");

	@Autowired
	@Qualifier("mvc")
	private MockMvc mockMvc;

	@Autowired
	@Qualifier("httpMsgConverter")
	HttpMessageConverter httpMsgConverter;

	private String loginId;
	private String pwd;

	@Before
	public void setup() throws Exception {
		this.loginId = "testuser";
		this.pwd = "testuser";
	}

	@Test
	public void a_signUpTest() {

		ApplicationUser user = new ApplicationUser(this.loginId, this.pwd, "Test", "User", "20", "test@test.id", "1234567890", 5.0, true, null);
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		try {
			
		httpMsgConverter.write(user, MediaType.APPLICATION_JSON, mockHttpOutputMessage);

		MvcResult res = mockMvc.perform(post("/signup")
				.content(mockHttpOutputMessage.getBodyAsString())
				.contentType(contentType))
				//.andExpect(status().is2xxSuccessful())
				.andReturn();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void b_signInTest() {

		ApplicationUser user = new ApplicationUser(this.loginId, this.pwd, "Test", "User", "20", "test@test.id", "1234567890", 5.0, true, null);
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		try {
		httpMsgConverter.write(user, MediaType.APPLICATION_JSON, mockHttpOutputMessage);

		mockMvc.perform(post("/login").content(mockHttpOutputMessage.getBodyAsString()).contentType(contentType))
				.andExpect(status().is2xxSuccessful())
				.andExpect(header().string("Authorization", authorMatcher));
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	}

}
