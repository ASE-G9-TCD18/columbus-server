package com.group9.columbus.test.login;

import com.group9.columbus.Application;
import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.repository.UserRepository;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

/**
 * Unit Test Class for LoginController
 * Created by yang on 08/02/2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@WebAppConfiguration
public class UserSignInTest {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    // Matcher for Bearer Authorization
    private Matcher<String> authorMatcher = Matchers.startsWith("Bearer");

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private String loginId;
    private String pwd;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        this.loginId = UUID.randomUUID().toString();
        this.pwd = UUID.randomUUID().toString();;
    }

    @Test
    public void signInTest() throws Exception {

        mockMvc.perform(
                post("/signup")
                        .content(this.json(new ApplicationUser(this.loginId, this.pwd, "Test", "Test", "20",
                                "test@test.id", "000000")))
                        .contentType(contentType))
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(
                post("/login").content(this.json(new ApplicationUser(this.loginId, this.pwd, "Test", "Test", "20",
                        "test@test.id", "000000")))
                .contentType(contentType)
                )
                .andExpect(status().is2xxSuccessful())

                .andExpect(header().string("Authorization", authorMatcher));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
