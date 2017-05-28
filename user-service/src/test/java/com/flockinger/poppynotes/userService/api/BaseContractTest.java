package com.flockinger.poppynotes.userService.api;

import org.junit.Before;

import com.flockinger.poppynotes.userService.exception.UserNotFoundException;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

public abstract class BaseContractTest {

	@Before
	public void setup() throws UserNotFoundException {
		RestAssuredMockMvc.standaloneSetup(new EditUserApiController(), new AuthUserApiController());
	}
}
