package com.flockinger.poppynotes.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Profile("default")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired 
	private OpenIdFilter filter;
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .addFilterAfter(new OAuth2ClientContextFilter(), 
          AbstractPreAuthenticatedProcessingFilter.class)
        .addFilterAfter(filter, 
          OAuth2ClientContextFilter.class)
        .httpBasic()
        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/google-login"))
        .and()
        .authorizeRequests()
        .antMatchers("/api/v1/users**").hasAuthority("ADMIN")
        .antMatchers("/api/v1/notes**").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers("/").hasAuthority("ADMIN")
        .antMatchers("/swagger-ui.html").hasAuthority("ADMIN");
    }
}
