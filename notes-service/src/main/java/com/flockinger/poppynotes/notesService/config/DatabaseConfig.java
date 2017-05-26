package com.flockinger.poppynotes.notesService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = "com.flockinger.poppynotes.notesService.dao")
public class DatabaseConfig {

}
