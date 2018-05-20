package com.flockinger.poppynotes.notesService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.flockinger.poppynotes.notesService.api.RateLimitInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${notes.security.allowed-origin}")
  private String allowedOrigin;

  @Autowired
  private RateLimitInterceptor rateLimiter;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(allowedOrigin)
        .allowedMethods("*")
        .allowedHeaders("*")
        .maxAge(3600);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(rateLimiter);
  }
}
