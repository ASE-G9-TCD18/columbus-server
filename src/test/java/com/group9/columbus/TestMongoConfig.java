package com.group9.columbus;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;

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
public class TestMongoConfig {
  private static final MongodStarter starter = MongodStarter.getDefaultInstance();

  @Autowired
  private MongoProperties properties;

  @Autowired(required = false)
  private MongoClientOptions options;

  @Bean(destroyMethod = "close")
  public Mongo mongo() throws IOException {
    Net net = mongod().getConfig().net();
    properties.setHost(net.getServerAddress().getHostName());
    properties.setPort(net.getPort());
    return properties.createMongoClient(this.options, null);
  }

  @Bean(destroyMethod = "stop")
  public MongodProcess mongod() throws IOException {
    return mongodExe().start();
  }

  @Bean(destroyMethod = "stop")
  public MongodExecutable mongodExe() throws IOException {
    return starter.prepare(mongodConfig());
  }

  @Bean
  public IMongodConfig mongodConfig() throws IOException {
    return new MongodConfigBuilder().version(Version.Main.DEVELOPMENT).build();
  }

}