package com.flockinger.poppynotes.userService.service;

import static org.junit.Assert.*;


import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.poppynotes.userService.dao.UserRepository;
import com.flockinger.poppynotes.userService.dto.CreateUser;
import com.flockinger.poppynotes.userService.dto.RolesEnum;
import com.flockinger.poppynotes.userService.dto.ShowUser;
import com.flockinger.poppynotes.userService.dto.StatusEnum;
import com.flockinger.poppynotes.userService.dto.UpdateUser;
import com.flockinger.poppynotes.userService.exception.DuplicateUserException;
import com.flockinger.poppynotes.userService.exception.UserNotFoundException;
import com.flockinger.poppynotes.userService.model.User;
import com.flockinger.poppynotes.userService.model.UserRole;
import com.flockinger.poppynotes.userService.model.UserStatus;
import com.google.common.collect.ImmutableSet;

@Transactional
public class EditUserServiceTest extends BaseServiceTest {

	@Autowired
	private EditUserService service;

	@Autowired
	private UserRepository dao;

	@Test
	@Transactional
	public void testCreateUser_withValidNewUser_shouldCreate() throws DuplicateUserException {
		CreateUser user = new CreateUser();
		user.setAuthEmail("fronz@gmail.com");
		user.setCryptKey("slkfs98if>7ya9owy|ra|hf,klj3bjkfql}{32r8iuqa3oryikwar23o'i[5r3");
		user.setName("fronz");
		user.setRecoveryEmail("fronz@yahoo.at");
		user.setRoles(ImmutableSet.of(RolesEnum.AUTHOR));
		user.setStatus(StatusEnum.ACTIVE);
		service.createUser(user);

		User createdUser = dao.findByAuthEmail("fronz@gmail.com");
		assertNotNull("user existing", createdUser);
		assertEquals("crypt key valid", "slkfs98if>7ya9owy|ra|hf,klj3bjkfql}{32r8iuqa3oryikwar23o'i[5r3",
				user.getCryptKey());
		assertEquals("name valid", "fronz", createdUser.getName());
		assertEquals("recovery mail valid", "fronz@yahoo.at", createdUser.getRecoveryEmail());
		assertEquals("role valid", UserRole.AUTHOR, createdUser.getRoles().stream().findFirst().get());
		assertEquals("status valid", UserStatus.ACTIVE, createdUser.getStatus());
	}

	@Test(expected=DuplicateUserException.class)
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testCreateUser_withAlreadyExistingUser_shouldThrowException() throws DuplicateUserException {
		CreateUser user = new CreateUser();
		user.setAuthEmail("flo@gmail.com");
		user.setCryptKey("slkfs98if>7ya9owy|ra|hf,klj3bjkfql}{32r8iuqa3oryikwar23o'i[5r3");
		user.setName("fronz");
		user.setRecoveryEmail("fronz@yahoo.at");
		user.setRoles(ImmutableSet.of(RolesEnum.AUTHOR));
		user.setStatus(StatusEnum.ACTIVE);
		service.createUser(user);
	}

	@Test
	@Transactional
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testUpdateUser_withValidUser_shouldUpdate() throws UserNotFoundException, DuplicateUserException {
		UpdateUser user = new UpdateUser();
		user.setId(1l);
		user.setAuthEmail("flo@gmail.com");
		user.setName("fronz");
		user.setRecoveryEmail("fronz@yahoo.at");
		user.setRoles(ImmutableSet.of(RolesEnum.AUTHOR));
		user.setStatus(StatusEnum.ACTIVE);
		service.updateUser(user);
		
		User updatedUser = dao.findOne(1l);
		assertNotNull("user existing", updatedUser);
		assertEquals("name valid", "fronz", updatedUser.getName());
		assertEquals("recovery mail valid", "fronz@yahoo.at", updatedUser.getRecoveryEmail());
		assertEquals("role valid", UserRole.AUTHOR, updatedUser.getRoles().stream().findFirst().get());
		assertEquals("status valid", UserStatus.ACTIVE, updatedUser.getStatus());
	}

	@Test(expected=DuplicateUserException.class)
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testUpdateUser_withCreatingDuplicateUser_shouldThrowException() throws UserNotFoundException, DuplicateUserException {
		UpdateUser user = new UpdateUser();
		user.setId(1l);
		user.setAuthEmail("sep@gmail.com");
		user.setName("fronz");
		user.setRecoveryEmail("fronz@yahoo.at");
		user.setRoles(ImmutableSet.of(RolesEnum.AUTHOR));
		user.setStatus(StatusEnum.ACTIVE);
		service.updateUser(user);
	}

	@Test(expected=UserNotFoundException.class)
	public void testUpdateUser_withNonExistingUser_shouldThrowException() throws UserNotFoundException, DuplicateUserException {
		UpdateUser user = new UpdateUser();
		user.setId(1765765l);
		user.setAuthEmail("flo@gmail.com");
		user.setName("fronz");
		user.setRecoveryEmail("fronz@yahoo.at");
		user.setRoles(ImmutableSet.of(RolesEnum.AUTHOR));
		user.setStatus(StatusEnum.ACTIVE);
		service.updateUser(user);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testDeleteUser_withExistingUser_shouldDelete() throws UserNotFoundException {
		service.deleteUser(1l);
		
		assertFalse(dao.exists(1l));
	}

	@Test(expected=UserNotFoundException.class)
	public void testDeleteUser_withNonExistingUser_shouldThrowException() throws UserNotFoundException {
		service.deleteUser(176576l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testGetUserById_withValidId_shouldReturnUserInfo() throws UserNotFoundException {
		ShowUser loadedUser = service.getUserById(2l);
		
		assertNotNull("user with id 2 exists",loadedUser);
		assertEquals("is name correct","sepp",loadedUser.getName());
		assertEquals("recovery email correct","sep@gmx.net",loadedUser.getRecoveryEmail());
		assertTrue("has 1 role", loadedUser.getRoles().size() == 1);
		assertEquals("has role author", RolesEnum.AUTHOR,loadedUser.getRoles().stream().findFirst().get());
		assertEquals("has status Active",StatusEnum.ACTIVE,loadedUser.getStatus());
	}

	@Test(expected=UserNotFoundException.class)
	public void testGetUserById_withNonExistingId_shouldThrowException() throws UserNotFoundException {
		service.getUserById(65462l);
	}
}
