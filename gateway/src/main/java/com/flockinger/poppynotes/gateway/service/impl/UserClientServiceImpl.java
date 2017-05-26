package com.flockinger.poppynotes.gateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.flockinger.poppynotes.gateway.exception.UnregisteredUserException;
import com.flockinger.poppynotes.gateway.model.AuthUser;
import com.flockinger.poppynotes.gateway.model.UserInfo;
import com.flockinger.poppynotes.gateway.service.UserClientService;

@Service
@RibbonClient("user-service")
public class UserClientServiceImpl implements UserClientService {

	@Autowired
	RestTemplate restTemplate;
	private String host = "http://user-service";
	
	@Retryable(value={RestClientException.class},backoff=@Backoff(2000))
	@Override
	public UserInfo getUserInfoFromAuthEmail(String authEmail) throws UnregisteredUserException {
		AuthUser authUser = new AuthUser();
		authUser.setAuthEmail(authEmail);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<AuthUser> entity = new HttpEntity<AuthUser>(authUser, headers);
		ResponseEntity<UserInfo> response = restTemplate.exchange(host + "/api/v1/user-checks/auth", HttpMethod.POST, entity, UserInfo.class);

		if(response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			throw new UnregisteredUserException("User not found!");
		}
		
		return response.getBody();
	}
	
	public void setHost(String host) {
		this.host = host;
	}
}
