package com.group9.columbus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Basic Swagger Configuration for API Management.
 * 
 * @author amit
 * 
 * http://blog.napagoda.com/2017/09/integrating-swagger-with-spring-boot.html
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.group9.columbus.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
