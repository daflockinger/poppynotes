package com.flockinger.poppynotes.userService.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"docker","cloud"})
@Configuration
@EnableDiscoveryClient
public class CloudConfig {

}
