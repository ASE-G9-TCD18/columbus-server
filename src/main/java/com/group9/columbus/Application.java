package com.group9.columbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
@SpringBootApplication
public class Application {
	@Bean
	public Md5PasswordEncoder encoder() {
		return new Md5PasswordEncoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
