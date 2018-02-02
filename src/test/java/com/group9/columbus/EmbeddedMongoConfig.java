package com.group9.columbus;
import java.io.IOException;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;

/**
 * Mocking MongoDB configuration
 */
@Configuration
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = { "com.group9.columbus"})
public class EmbeddedMongoConfig {

  private static final String DB_NAME = "test_columbus";
  private static final String DB_HOST = "localhost";
  
  @Bean
  public MongoTemplate mongoTemplate() throws IOException {
      EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
      mongo.setBindIp(DB_HOST);
      MongoClient mongoClient = mongo.getObject();
      MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, DB_NAME);
      return mongoTemplate;
  }
 

}