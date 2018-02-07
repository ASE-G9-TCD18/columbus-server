package com.group9.columbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Import;

@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
@SpringBootApplication
@Profile("development")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
