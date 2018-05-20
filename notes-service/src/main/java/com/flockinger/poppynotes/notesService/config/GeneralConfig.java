package com.flockinger.poppynotes.notesService.config;

import java.util.concurrent.TimeUnit;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;

@Configuration
public class GeneralConfig {

  @Value("${notes.rate-limit.time-range-minutes}")
  private Integer timeRangeMinutes;
  
  @Value("${notes.rate-limit.call-limit}")
  private Integer callLimit;

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public RequestRateLimiter rateLimiter() {
    RequestRateLimiter rateLimiter = new InMemorySlidingWindowRequestRateLimiter(
        ImmutableSet.of(RequestLimitRule.of(timeRangeMinutes, TimeUnit.MINUTES, callLimit)));
    return rateLimiter;
  }
}
