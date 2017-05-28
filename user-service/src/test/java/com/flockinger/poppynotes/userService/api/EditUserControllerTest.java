package com.flockinger.poppynotes.userService.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;

import javax.transaction.Transactional;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flockinger.poppynotes.userService.dao.UserRepository;
import com.flockinger.poppynotes.userService.dto.CreateUser;
import com.flockinger.poppynotes.userService.dto.RolesEnum;
import com.flockinger.poppynotes.userService.dto.StatusEnum;
import com.flockinger.poppynotes.userService.dto.UpdateUser;
import com.flockinger.poppynotes.userService.model.User;
import com.flockinger.poppynotes.userService.model.UserRole;
import com.flockinger.poppynotes.userService.model.UserStatus;
import com.google.common.collect.ImmutableSet;

public class EditUserControllerTest extends BaseControllerTest {

	@Autowired
	private UserRepository dao;

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	@Transactional
	public void testApiV1UsersPost_withValidUser_shouldCreate() throws Exception {
		CreateUser user = new CreateUser();
		user.setAuthEmail("no@gmail.com");
		user.setCryptKey("klsjdf9o8if709aofhl3kqfhrlb3,kjpw");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(post("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isCreated());

		User savedUser = dao.findOne(5l);
		assertEquals("verify saved ", "no@gmail.com", savedUser.getAuthEmail());
		assertEquals("verify saved ", "klsjdf9o8if709aofhl3kqfhrlb3,kjpw", savedUser.getCryptKey());
		assertEquals("verify saved ", "no", savedUser.getName());
		assertEquals("verify saved ", "no@yahoo.co.jp", savedUser.getRecoveryEmail());
		assertEquals("verify saved ", UserRole.ADMIN, savedUser.getRoles().iterator().next());
		assertEquals("verify saved ", UserStatus.DISABLED, savedUser.getStatus());
	}

	@Test
	public void testApiV1UsersPost_withInvalidAuthEmail_shouldReturnBadRequest() throws Exception {
		CreateUser user = new CreateUser();
		user.setAuthEmail("www.nogmail.com");
		user.setCryptKey("klsjdf9o8if709aofhl3kqfhrlb3,kjpw");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(post("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testApiV1UsersPost_withEmptyAuthEmail_shouldReturnBadRequest() throws Exception {
		CreateUser user = new CreateUser();
		user.setAuthEmail("");
		user.setCryptKey("klsjdf9o8if709aofhl3kqfhrlb3,kjpw");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(post("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testApiV1UsersPost_withEmptyName_shouldReturnBadRequest() throws Exception {
		CreateUser user = new CreateUser();
		user.setAuthEmail("no@gmail.com");
		user.setCryptKey("klsjdf9o8if709aofhl3kqfhrlb3,kjpw");
		user.setName("");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(post("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testApiV1UsersPost_withNoRole_shouldReturnBadRequest() throws Exception {
		CreateUser user = new CreateUser();
		user.setAuthEmail("no@gmail.com");
		user.setCryptKey("klsjdf9o8if709aofhl3kqfhrlb3,kjpw");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setStatus(StatusEnum.DISABLED);
		user.setRoles(new HashSet<>());

		mockMvc.perform(post("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testApiV1UsersPost_withNullStatus_shouldReturnBadRequest() throws Exception {
		CreateUser user = new CreateUser();
		user.setAuthEmail("no@gmail.com");
		user.setCryptKey("klsjdf9o8if709aofhl3kqfhrlb3,kjpw");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(null);

		mockMvc.perform(post("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testApiV1UsersPost_withEmptyCryptKey_shouldReturnBadRequest() throws Exception {
		CreateUser user = new CreateUser();
		user.setAuthEmail("no@gmail.com");
		user.setCryptKey("");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(post("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testApiV1UsersPost_withAlreadyExistingAuthEmail_shouldReturnConflict() throws Exception {
		CreateUser user = new CreateUser();
		user.setAuthEmail("sep@gmail.com");
		user.setCryptKey("klsjdf9o8if709aofhl3kqfhrlb3,kjpw");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(post("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isConflict());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	@Transactional
	public void testApiV1UsersPut_withValidUser_shouldCreate() throws Exception {
		UpdateUser user = new UpdateUser();
		user.setId(4l);
		user.setAuthEmail("no@gmail.com");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(put("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isOk());

		User savedUser = dao.findOne(4l);
		assertEquals("verify saved ", "no@gmail.com", savedUser.getAuthEmail());
		assertEquals("verify saved ", "no", savedUser.getName());
		assertEquals("verify saved ", "no@yahoo.co.jp", savedUser.getRecoveryEmail());
		assertEquals("verify saved ", UserRole.ADMIN, savedUser.getRoles().iterator().next());
		assertEquals("verify saved ", UserStatus.DISABLED, savedUser.getStatus());
	}

	@Test
	public void testApiV1UsersPut_withNotExistingUserId_shouldReturnNotFound() throws Exception {
		UpdateUser user = new UpdateUser();
		user.setId(464654l);
		user.setAuthEmail("flo@gmail.com");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(put("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testApiV1UsersPut_withInvalidAuthEmail_shouldReturnBadRequest() throws Exception {
		UpdateUser user = new UpdateUser();
		user.setId(4l);
		user.setAuthEmail("nomail.com");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(put("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testApiV1UsersPut_withEmptyAuthEmail_shouldReturnBadRequest() throws Exception {
		UpdateUser user = new UpdateUser();
		user.setId(4l);
		user.setAuthEmail("");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(put("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testApiV1UsersPut_withEmptyName_shouldReturnBadRequest() throws Exception {
		UpdateUser user = new UpdateUser();
		user.setId(4l);
		user.setAuthEmail("no@gmail.com");
		user.setName("");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(put("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testApiV1UsersPut_withNoRole_shouldReturnBadRequest() throws Exception {
		UpdateUser user = new UpdateUser();
		user.setId(4l);
		user.setAuthEmail("no@gmail.com");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(null);
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(put("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testApiV1UsersPut_withNullStatus_shouldReturnBadRequest() throws Exception {
		UpdateUser user = new UpdateUser();
		user.setId(4l);
		user.setAuthEmail("no@gmail.com");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(null);

		mockMvc.perform(put("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testApiV1UsersPut_withAlreadyExistingDifferentAuthEmail_shouldReturnConflict() throws Exception {
		UpdateUser user = new UpdateUser();
		user.setId(4l);
		user.setAuthEmail("flo@gmail.com");
		user.setName("no");
		user.setRecoveryEmail("no@yahoo.co.jp");
		user.setRoles(ImmutableSet.of(RolesEnum.ADMIN));
		user.setStatus(StatusEnum.DISABLED);

		mockMvc.perform(put("/api/v1/users").content(json(user)).contentType(jsonContentType))
				.andExpect(status().isConflict());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testApiV1UsersUserIdDelete_withCorrectId_shouldDelete() throws Exception {

		mockMvc.perform(delete("/api/v1/users/1").contentType(jsonContentType)).andExpect(status().isOk());

		assertFalse(dao.exists(1l));
	}

	@Test
	public void testApiV1UsersUserIdDelete_withNotExistingUser_shouldRetunNotFound() throws Exception {

		mockMvc.perform(delete("/api/v1/users/1765765").contentType(jsonContentType)).andExpect(status().isNotFound());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testApiV1UsersUserIdGet_withCorrectId_shouldReturnUser() throws Exception {
		mockMvc.perform(get("/api/v1/users/1").contentType(jsonContentType)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("flo"))).andExpect(jsonPath("$.recoveryEmail", is("flo@gmx.net")))
				.andExpect(jsonPath("$.roles", hasSize(2)))
				.andExpect(jsonPath("$.status", is("ACTIVE")));
	}

	@Test
	public void testApiV1UsersUserIdGet_withNotExistingUser_shouldRetunNotFound() throws Exception {

		mockMvc.perform(get("/api/v1/users/1765765").contentType(jsonContentType)).andExpect(status().isNotFound());
	}
}
