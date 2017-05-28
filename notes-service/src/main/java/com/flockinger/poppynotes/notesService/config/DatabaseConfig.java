package com.flockinger.poppynotes.notesService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages="com.flockinger.poppynotes.notesService.dao")
public class DatabaseConfig {
}
