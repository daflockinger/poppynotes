package com.flockinger.poppynotes.gateway.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.flockinger.poppynotes.gateway.exception.UnregisteredUserException;
import com.flockinger.poppynotes.gateway.model.Role;
import com.flockinger.poppynotes.gateway.model.Status;
import com.flockinger.poppynotes.gateway.model.UserInfo;
import com.flockinger.poppynotes.gateway.service.impl.UserClientServiceImpl;

@ActiveProfiles({"test"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
@AutoConfigureStubRunner(ids = {"com.flockinger.poppynotes:user-service:+:stubs:8001"}, workOffline = true)
@DirtiesContext
@Import(RestTemplateConfig.class)
public class UserClientServiceTest {

	@Autowired
	private UserClientServiceImpl service;
	
	@Test
	public void testGetUserInfoFromAuthEmail_withValidEmail_shouldReturnCorrectUserInfo() {
		service.setHost("http://localhost:8001");
		UserInfo supriseSuprise = service.getUserInfoFromAuthEmail("sep@gmail.com");
		
		assertNotNull("has returned not null", supriseSuprise);
		assertEquals("is username correct","sepp",supriseSuprise.getName());
		assertEquals("is recovery email correct","sep@gmx.net",supriseSuprise.getRecoveryEmail());
		assertEquals("is role correct",Role.AUTHOR,supriseSuprise.getRoles().iterator().next());
		assertEquals("is status correct",Status.ACTIVE,supriseSuprise.getStatus());
	}
	
	@Test(expected=UnregisteredUserException.class)
	public void testGetUserInfoFromAuthEmail_withNotExistingEmail_shouldReturnNotfound() {
		service.setHost("http://localhost:8001");
		service.getUserInfoFromAuthEmail("nonexista@gmail.com");
		
	}
	
}
