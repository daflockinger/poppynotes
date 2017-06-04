package com.flockinger.poppynotes.gateway.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.flockinger.poppynotes.gateway.TestConfig;
import com.flockinger.poppynotes.gateway.exception.UnregisteredUserException;
import com.flockinger.poppynotes.gateway.exception.UserNotCachedException;
import com.flockinger.poppynotes.gateway.model.Role;
import com.flockinger.poppynotes.gateway.model.Status;
import com.flockinger.poppynotes.gateway.model.AuthUserResponse;
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
	RestTemplate restTemplate;
	
	@Before
	public void setup() {
		service.setHost("http://localhost:8001");
		service.setRestTemplate(restTemplate);
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
	
	@Test
	public void testGetCachedUserById_withValidIdAndCachedBefore_shouldReturnCorrectUserInfo() throws UserNotCachedException {
		service.getUserInfoFromAuthEmail("sep@gmail.com");
		
		AuthUserResponse cachedUser = service.getCachedUserById(2l);
		
		assertNotNull("has returned not null", cachedUser);
		assertEquals("is id correct",2l,cachedUser.getId().longValue());
		assertEquals("is username correct","sepp",cachedUser.getName());
		assertEquals("is recovery email correct","sep@gmx.net",cachedUser.getRecoveryEmail());
		assertEquals("is role correct",Role.AUTHOR,cachedUser.getRoles().iterator().next());
		assertEquals("is status correct",Status.ACTIVE,cachedUser.getStatus());
		assertEquals("is cryptkey correct","89768iksgd",cachedUser.getCryptKey());
	}
	
	@Test(expected=UserNotCachedException.class)
	public void testGetCachedUserById_withValidIdButNotCachedBefore_shouldThrowException() throws UserNotCachedException {
		service.getCachedUserById(4l);
	}
	
	@Test(expected=UserNotCachedException.class)
	public void testGetCachedUserById_withValidIdButTimedOutCache_shouldThrowException() throws UserNotCachedException, InterruptedException {
		service.getUserInfoFromAuthEmail("sep@gmail.com");
		Thread.sleep(3000);
		service.getCachedUserById(2l);
	}
}
