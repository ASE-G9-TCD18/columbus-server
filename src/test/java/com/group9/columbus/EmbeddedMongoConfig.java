package com.group9.columbus;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;

/**
 * Mocking MongoDB configuration
 */
@Configuration
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = { "com.group9.columbus"})
public class EmbeddedMongoConfig {

  private static final String DB_NAME = "test_columbus";
  private static final int DB_PORT = 12345;
  private static final String DB_HOST = "localhost";
  
  @Bean
  public MongoTemplate mongoTemplate() throws IOException {
      EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
      mongo.setBindIp(DB_HOST);
      //mongo.setPort(DB_PORT);
      MongoClient mongoClient = mongo.getObject();
      MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, DB_NAME);
      return mongoTemplate;
  }
 

}