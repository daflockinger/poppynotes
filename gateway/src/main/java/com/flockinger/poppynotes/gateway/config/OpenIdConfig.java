package com.flockinger.poppynotes.gateway.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class OpenIdConfig {

	@Value("${google.clientId}")
	private String clientId;

	@Value("${google.clientSecret}")
	private String clientSecret;

	@Value("${google.accessTokenUri}")
	private String accessTokenUri;

	@Value("${google.userAuthorizationUri}")
	private String userAuthorizationUri;

	@Value("${google.redirectUri}")
	private String redirectUri;

	@Bean(name="authenticationManagerBean")
	public AuthenticationManager getAuthenticationManager(){
		return new AuthenticationManager() {
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	            throw new UnsupportedOperationException("No authentication should be done with this AuthenticationManager");
			}
		};
	}
	
	@Bean
	public OAuth2ProtectedResourceDetails googleOpenId() {
		AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
		details.setClientId(clientId);
		details.setClientSecret(clientSecret);
		details.setAccessTokenUri(accessTokenUri);
		details.setUserAuthorizationUri(userAuthorizationUri);
		details.setScope(Arrays.asList("openid", "email"));
		details.setPreEstablishedRedirectUri(redirectUri);
		details.setUseCurrentUri(false);
		return details;
	}

	@Bean
	public OAuth2RestTemplate googleOpenIdTemplate(OAuth2ClientContext clientContext) {
		return new OAuth2RestTemplate(googleOpenId(), clientContext);
	}
}