package com.flockinger.poppynotes.gateway.config;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flockinger.poppynotes.gateway.exception.UnregisteredUserException;
import com.flockinger.poppynotes.gateway.model.OpenIdUserDetails;
import com.flockinger.poppynotes.gateway.model.Role;
import com.flockinger.poppynotes.gateway.model.UserInfo;
import com.flockinger.poppynotes.gateway.service.UserClientService;

@Component
public class OpenIdFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private OAuth2RestOperations restTemplate;
	
	@Autowired
	private UserClientService userService;
	
	@Autowired
	public OpenIdFilter(AuthenticationManager authenticationManager){
		this("/google-login");
		setAuthenticationManager(authenticationManager);
	}
	
	private OpenIdFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		OAuth2AccessToken accessToken;
		try {
			accessToken = restTemplate.getAccessToken();
		} catch (OAuth2Exception e) {
			throw new BadCredentialsException("Could not obtain access token", e);
		}
		try {
			String idToken = accessToken.getAdditionalInformation().get("id_token").toString();
			Jwt tokenDecoded = JwtHelper.decode(idToken);
			@SuppressWarnings("unchecked")
			Map<String, String> authInfo = new ObjectMapper().readValue(tokenDecoded.getClaims(), Map.class);

			OpenIdUserDetails user = new OpenIdUserDetails(authInfo, accessToken);
			UserInfo info = userService.getUserInfoFromAuthEmail(user.getUsername());
			user.setAuthorities(rolesToAthorities(info.getRoles()));
			
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		} catch (InvalidTokenException e) {
			throw new BadCredentialsException("Could not obtain user details from token", e);
		} catch (UnregisteredUserException unregisteredException) {
			throw new BadCredentialsException("User is not registered for using the service", unregisteredException);
		}
	}
	
	private Set<GrantedAuthority> rolesToAthorities(Set<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toSet());
	}
}
