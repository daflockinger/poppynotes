package com.flockinger.poppynotes.gateway.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableZuulProxy
@EnableDiscoveryClient
@EnableRetry
@RibbonClients(defaultConfiguration = RibbonConfig.class)
@EnableCircuitBreaker
@EnableCaching
public class CloudConfig {
	
	@Value("${cache.caffeine.spec}")
	private String caffeineSpec;
	
	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		RestTemplate template = new RestTemplate();
		template.setErrorHandler(getResponseErrorHandler());

		return template;
	}

	private ResponseErrorHandler getResponseErrorHandler() {
		return new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
					return true;
				}
				return false;
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
			}
		};
	}
	
	@Bean
    public CacheManager cacheManager() {
		CaffeineCacheManager caffeineManager = new CaffeineCacheManager("usersFromEmail");
		caffeineManager.setCacheSpecification(caffeineSpec);
        return caffeineManager;
    }
}
