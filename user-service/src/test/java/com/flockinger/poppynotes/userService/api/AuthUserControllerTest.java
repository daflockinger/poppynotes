package com.flockinger.poppynotes.userService.api;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.flockinger.poppynotes.userService.dao.UserRepository;
import com.flockinger.poppynotes.userService.dto.AuthUser;
import com.flockinger.poppynotes.userService.dto.CreatePin;
import com.flockinger.poppynotes.userService.dto.SendPin;
import com.flockinger.poppynotes.userService.dto.Unlock;
import com.flockinger.poppynotes.userService.model.User;
import com.flockinger.poppynotes.userService.model.UserStatus;
import com.flockinger.poppynotes.userService.service.EmailService;

public class AuthUserControllerTest extends BaseControllerTest {

	@Autowired
	private UserRepository dao;

	@MockBean
	private EmailService emailService;
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testApiV1UsersAuthPost_withValidEmail_shouldReturnUserInfo() throws Exception {
		AuthUser auth = new AuthUser();
		auth.setAuthEmail("sep@gmail.com")
		;
		mockMvc.perform(post("/api/v1/user-checks/auth").content(json(auth)).contentType(jsonContentType))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("sepp")))
				.andExpect(jsonPath("$.recoveryEmail", is("sep@gmx.net")))
				.andExpect(jsonPath("$.roles[0]", is("AUTHOR")))
				.andExpect(jsonPath("$.status", is("ACTIVE")));
	}

	@Test
	public void testApiV1UsersAuthPost_withNotExistingEmail_shouldReturnNotFound() throws Exception {
		AuthUser auth = new AuthUser();
		auth.setAuthEmail("hacker@gmail.com");

		mockMvc.perform(post("/api/v1/user-checks/auth").content(json(auth)).contentType(jsonContentType))
				.andExpect(status().isNotFound());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	@Transactional
	public void testApiV1UsersPinCheckPost_withValidPin_shouldReturnValid() throws Exception {
		SendPin pin = new SendPin();
		pin.setId(2l);
		pin.setPin("4321");
		
		mockMvc.perform(post("/api/v1/user-checks/pin/check").content(json(pin))
					.contentType(jsonContentType))
					.andExpect(jsonPath("$.valid", is(true)))
					.andExpect(status().isOk());
		
		assertEquals("should still be status active",UserStatus.ACTIVE,dao.getOne(2l).getStatus());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testApiV1UsersPinCheckPost_withInvalidPin_shouldReturnForbidden() throws Exception {
		SendPin pin = new SendPin();
		pin.setId(2l);
		pin.setPin("1234");
		
		mockMvc.perform(post("/api/v1/user-checks/pin/check").content(json(pin))
					.contentType(jsonContentType))
					.andExpect(jsonPath("$.valid", is(false)))
					.andExpect(status().isOk());
	}

	@Test
	public void testApiV1UsersPinCheckPost_withNotExistingUser_shouldReturnNotFound() throws Exception {
		SendPin pin = new SendPin();
		pin.setId(26577l);
		pin.setPin("4321");
		
		mockMvc.perform(post("/api/v1/user-checks/pin/check").content(json(pin))
					.contentType(jsonContentType))
					.andExpect(status().isNotFound());
	}
	
	@Test
	public void testApiV1UsersPinCheckPost_withNullUser_shouldReturnBadRequest() throws Exception {
		SendPin pin = new SendPin();
		pin.setId(null);
		pin.setPin("4321");
		
		mockMvc.perform(post("/api/v1/user-checks/pin/check").content(json(pin))
					.contentType(jsonContentType))
					.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1UsersPinCheckPost_withEmptyPin_shouldReturnBadRequest() throws Exception {
		SendPin pin = new SendPin();
		pin.setId(2l);
		pin.setPin("");
		
		mockMvc.perform(post("/api/v1/user-checks/pin/check").content(json(pin))
					.contentType(jsonContentType))
					.andExpect(status().isBadRequest());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	@Transactional
	public void testApiV1UsersPinCheckPost_withTryThreeTimesWrong_shouldReturnForbidden() throws Exception {
		doNothing().when(emailService).sendUnlockCodeEmailFor(any(User.class));
		
		SendPin pin = new SendPin();
		pin.setId(2l);
		pin.setPin("0000");
		
		mockMvc.perform(post("/api/v1/user-checks/pin/check").content(json(pin))
					.contentType(jsonContentType))
					.andExpect(jsonPath("$.valid", is(false)))
					.andExpect(status().isOk());
		
		mockMvc.perform(post("/api/v1/user-checks/pin/check").content(json(pin))
					.contentType(jsonContentType))
					.andExpect(jsonPath("$.valid", is(false)))
					.andExpect(status().isOk());
		
		mockMvc.perform(post("/api/v1/user-checks/pin/check").content(json(pin))
					.contentType(jsonContentType))
					.andExpect(jsonPath("$.valid", is(false)))
					.andExpect(status().isOk());
		
		assertEquals("after three times should be locked",UserStatus.LOCKED,dao.getOne(2l).getStatus());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	@Transactional
	public void testApiV1UsersPinPost_withValidPin_shouldCreate() throws Exception {
		CreatePin pin = new CreatePin();
		pin.setId(3l);
		pin.setRecoveryEmail("hons@gmx.net");
		pin.setPin("3867");
		
		mockMvc.perform(post("/api/v1/user-checks/pin").content(json(pin))
				.contentType(jsonContentType))
				.andExpect(status().isCreated());
		
		assertTrue(dao.findOne(3l).getPinCode().equals("3867"));
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testApiV1UsersPinPost_withAlreadyExistingPin_shouldReturnConflict() throws Exception {
		CreatePin pin = new CreatePin();
		pin.setId(1l);
		pin.setRecoveryEmail("flo@gmx.net");
		pin.setPin("3867");
		
		mockMvc.perform(post("/api/v1/user-checks/pin").content(json(pin))
				.contentType(jsonContentType))
				.andExpect(status().isConflict());
	}
	
	@Test
	public void testApiV1UsersPinPost_withNotExistingUserId_shouldReturnNotFound() throws Exception {
		CreatePin pin = new CreatePin();
		pin.setId(3765765l);
		pin.setRecoveryEmail("hons@gmx.net");
		pin.setPin("3867");
		
		mockMvc.perform(post("/api/v1/user-checks/pin").content(json(pin))
				.contentType(jsonContentType))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void testApiV1UsersPinPost_withEmptyUserId_shouldReturnBadRequest() throws Exception {
		CreatePin pin = new CreatePin();
		pin.setId(null);
		pin.setRecoveryEmail("hons@gmx.net");
		pin.setPin("3867");
		
		mockMvc.perform(post("/api/v1/user-checks/pin").content(json(pin))
				.contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1UsersPinPost_withEmptyRevoceryEmail_shouldReturnBadRequest() throws Exception {
		CreatePin pin = new CreatePin();
		pin.setId(3l);
		pin.setRecoveryEmail("");
		pin.setPin("3867");
		
		mockMvc.perform(post("/api/v1/user-checks/pin").content(json(pin))
				.contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	
	
	@Test
	public void testApiV1UsersPinPost_withEmptyPin_shouldReturnBadRequest() throws Exception {
		CreatePin pin = new CreatePin();
		pin.setId(3l);
		pin.setRecoveryEmail("hons@gmx.net");
		pin.setPin("");
		
		mockMvc.perform(post("/api/v1/user-checks/pin").content(json(pin))
				.contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	
	@Test
	public void testApiV1UsersPinPost_withTooShortPin_shouldReturnBadRequest() throws Exception {
		CreatePin pin = new CreatePin();
		pin.setId(3l);
		pin.setRecoveryEmail("hons@gmx.net");
		pin.setPin("367");
		
		mockMvc.perform(post("/api/v1/user-checks/pin").content(json(pin))
				.contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}
	
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	@Transactional
	public void testApiV1UsersPinUserIdDelete_withExistingUser_shouldDelete() throws Exception {
		mockMvc.perform(delete("/api/v1/user-checks/pin/1")
				.contentType(jsonContentType))
				.andExpect(status().isOk());
				
		assertTrue(dao.findOne(1l).getPinCode() == null);
	}
	
	@Test
	public void testApiV1UsersPinUserIdDelete_withNotExistingUser_shouldReturnNotFound() throws Exception {
		mockMvc.perform(delete("/api/v1/user-checks/pin/765761")
				.contentType(jsonContentType))
				.andExpect(status().isNotFound());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testApiV1UsersUnlockPost_withValidUnlockCode_shouldUnlock() throws Exception {
		Unlock unlock = new Unlock();
		unlock.setId(4l);
		unlock.setUnlockCode("s887q");
		
		mockMvc.perform(post("/api/v1/user-checks/unlock").content(json(unlock))
				.contentType(jsonContentType))
				.andExpect(jsonPath("$.isUnlocked", is(true)))
				.andExpect(status().isOk());
	}
	
	
	@Test
	public void testApiV1UsersUnlockPost_withNullId_shouldReturnBadRequest() throws Exception {
		Unlock unlock = new Unlock();
		unlock.setId(null);
		unlock.setUnlockCode("s887q");
		
		mockMvc.perform(post("/api/v1/user-checks/unlock").content(json(unlock))
				.contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void testApiV1UsersUnlockPost_withEmptyUnlockCode_shouldReturnBadRequest() throws Exception {
		Unlock unlock = new Unlock();
		unlock.setId(4l);
		unlock.setUnlockCode("");
		
		mockMvc.perform(post("/api/v1/user-checks/unlock").content(json(unlock))
				.contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testApiV1UsersUnlockPost_withWrongUnlockCode_shouldReturnForbidden() throws Exception {
		Unlock unlock = new Unlock();
		unlock.setId(4l);
		unlock.setUnlockCode("hack");
		
		mockMvc.perform(post("/api/v1/user-checks/unlock").content(json(unlock))
				.contentType(jsonContentType))
				.andExpect(jsonPath("$.isUnlocked", is(false)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testApiV1UsersUnlockPost_withNotExistingUser_shouldReturnNotFound() throws Exception {
		Unlock unlock = new Unlock();
		unlock.setId(8768764l);
		unlock.setUnlockCode("s887q");
		
		mockMvc.perform(post("/api/v1/user-checks/unlock").content(json(unlock))
				.contentType(jsonContentType))
				.andExpect(status().isNotFound());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	@Transactional
	public void testApiV1UsersLockUserIdPost_withExistingUser_shouldLock() throws Exception {
		doNothing().when(emailService).sendUnlockCodeEmailFor(any(User.class));
		
		mockMvc.perform(post("/api/v1/user-checks/lock/1")
				.contentType(jsonContentType))
				.andExpect(status().isOk());
		
		User lockedOne = dao.findOne(1l);
		assertTrue(lockedOne.getStatus().equals(UserStatus.LOCKED));
		assertFalse(lockedOne.getUnlockCode().isEmpty());
	}
	
	@Test
	public void testApiV1UsersLockUserIdPost_withNotExistingUser_shouldReturnNotFound() throws Exception {
		mockMvc.perform(post("/api/v1/user-checks/lock/876765")
				.contentType(jsonContentType))
				.andExpect(status().isNotFound());
	}
}
