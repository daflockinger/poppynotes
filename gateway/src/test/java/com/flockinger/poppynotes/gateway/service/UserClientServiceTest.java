package com.flockinger.poppynotes.gateway.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.flockinger.poppynotes.gateway.TestConfig;
import com.flockinger.poppynotes.gateway.exception.UnregisteredUserException;
import com.flockinger.poppynotes.gateway.model.AuthUserResponse;
import com.flockinger.poppynotes.gateway.model.Role;
import com.flockinger.poppynotes.gateway.model.Status;
import com.flockinger.poppynotes.gateway.service.impl.UserClientServiceImpl;

@ActiveProfiles({"test"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
@AutoConfigureStubRunner(ids = {"com.flockinger.poppynotes:user-service:+:stubs:8001"}, workOffline = true)
@DirtiesContext
@Import(TestConfig.class)
public class UserClientServiceTest {

	@Autowired
	private UserClientServiceImpl service;

	@Qualifier("testRestTemplate")
	@Autowired
	private RestTemplate restTemplate;
	
	@Mock
	private RestTemplate mockTemplate;
	
	@Before
	public void setup() {
		service.setHost("http://localhost:8001");
		service.setRestTemplate(restTemplate);
		
		service.clearCachedUser("sep@gmail.com");
	}
	
	@Test
	public void testGetUserInfoFromAuthEmail_withValidEmail_shouldReturnCorrectUserInfo() {
		
		AuthUserResponse supriseSuprise = service.getUserInfoFromAuthEmail("sep@gmail.com");
		
		assertNotNull("has returned not null", supriseSuprise);
		assertEquals("is id correct",2l,supriseSuprise.getId().longValue());
		assertEquals("is username correct","sepp",supriseSuprise.getName());
		assertEquals("is recovery email correct","sep@gmx.net",supriseSuprise.getRecoveryEmail());
		assertEquals("is role correct",Role.AUTHOR,supriseSuprise.getRoles().iterator().next());
		assertEquals("is status correct",Status.ACTIVE,supriseSuprise.getStatus());
		assertEquals("is cryptkey correct","89768iksgd",supriseSuprise.getCryptKey());
	}
	
	@Test(expected=UnregisteredUserException.class)
	public void testGetUserInfoFromAuthEmail_withNotExistingEmail_shouldReturnNotfound() {
		service.getUserInfoFromAuthEmail("nonexista@gmail.com");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCachedRequest_withThreeCalls_shouldReturnCachedOnSecondTime() {
		reset(mockTemplate);
		
		when(mockTemplate.exchange(anyString(), any(HttpMethod.class),any(HttpEntity.class),any(Class.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		service.setRestTemplate(mockTemplate);
		
		service.getUserInfoFromAuthEmail("sep@gmail.com");
		service.getUserInfoFromAuthEmail("sep@gmail.com");
		service.getUserInfoFromAuthEmail("sep@gmail.com");
		 
		
		verify(mockTemplate,times(1)).exchange(anyString(), any(HttpMethod.class),any(HttpEntity.class),any(Class.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCachedRequest_withThreeCallsAndClearCache_shouldReallyCallTwoTimes() {
		reset(mockTemplate);
		when(mockTemplate.exchange(anyString(), any(HttpMethod.class),any(HttpEntity.class),any(Class.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		service.setRestTemplate(mockTemplate);
		
		service.getUserInfoFromAuthEmail("sep@gmail.com");
		service.getUserInfoFromAuthEmail("sep@gmail.com");
		
		service.clearCachedUser("sep@gmail.com");
		
		service.getUserInfoFromAuthEmail("sep@gmail.com");
		service.getUserInfoFromAuthEmail("sep@gmail.com");
		service.getUserInfoFromAuthEmail("sep@gmail.com");
		
		verify(mockTemplate,times(2)).exchange(anyString(), any(HttpMethod.class),any(HttpEntity.class),any(Class.class));
	}
}
