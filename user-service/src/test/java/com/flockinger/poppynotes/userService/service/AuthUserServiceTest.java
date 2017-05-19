package com.flockinger.poppynotes.userService.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.transaction.Transactional;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.flockinger.poppynotes.userService.dao.UserRepository;
import com.flockinger.poppynotes.userService.dto.AuthUser;
import com.flockinger.poppynotes.userService.dto.CreatePin;
import com.flockinger.poppynotes.userService.dto.RolesEnum;
import com.flockinger.poppynotes.userService.dto.SendPin;
import com.flockinger.poppynotes.userService.dto.ShowUser;
import com.flockinger.poppynotes.userService.dto.StatusEnum;
import com.flockinger.poppynotes.userService.dto.Unlock;
import com.flockinger.poppynotes.userService.dto.UnlockResult;
import com.flockinger.poppynotes.userService.exception.InvalidEmailServerConfigurationException;
import com.flockinger.poppynotes.userService.exception.PinAlreadyExistingException;
import com.flockinger.poppynotes.userService.exception.UserNotFoundException;
import com.flockinger.poppynotes.userService.model.User;
import com.flockinger.poppynotes.userService.model.UserStatus;

@Transactional
public class AuthUserServiceTest extends BaseServiceTest {

	@Autowired
	private AuthUserService service;

	@Autowired
	private UserRepository dao;

	@MockBean
	private EmailService emailService;

	@Before
	public void setup() throws InvalidEmailServerConfigurationException {
		doNothing().when(emailService).sendUnlockCodeEmailFor(any(User.class));
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testGetUserByAuthEmailHash_withValidEmail_shouldReturnCorrect() throws UserNotFoundException {
		AuthUser auth = new AuthUser();
		auth.setAuthEmail("hons@gmail.com");

		ShowUser user = service.getUserByAuthEmailHash(auth);
		assertNotNull(user);
		assertEquals("verify user name", "hons", user.getName());
		assertEquals("verify recovery email", "hons@gmx.net", user.getRecoveryEmail());
		assertEquals("verify user role", RolesEnum.AUTHOR, user.getRoles().stream().findAny().get());
		assertEquals("verify user status", StatusEnum.ACTIVE, user.getStatus());
	}

	@Test(expected = UserNotFoundException.class)
	public void testGetUserByAuthEmailHash_withInValidEmail_shouldThrowException() throws UserNotFoundException {
		AuthUser auth = new AuthUser();
		auth.setAuthEmail("non@exista.cc");

		service.getUserByAuthEmailHash(auth);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testCheckPin_withValidPin_shouldReturnTrue()
			throws UserNotFoundException, InvalidEmailServerConfigurationException {
		SendPin pin = new SendPin();
		pin.setId(2l);
		pin.setPin("4321");

		assertTrue(service.checkPin(pin).getValid());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testCheckPin_withInvalidPin_shouldReturnFalse()
			throws UserNotFoundException, InvalidEmailServerConfigurationException {
		SendPin pin = new SendPin();
		pin.setId(2l);
		pin.setPin("0000");

		assertFalse(service.checkPin(pin).getValid());
	}

	@Test(expected = UserNotFoundException.class)
	public void testCheckPin_withInvalidUser_shouldThrowException()
			throws UserNotFoundException, InvalidEmailServerConfigurationException {
		SendPin pin = new SendPin();
		pin.setId(765757l);
		pin.setPin("4321");

		service.checkPin(pin);
	}

	@Test(expected = UserNotFoundException.class)
	public void testCheckPin_withInvalidUserAndInvalidPin_shouldThrowException()
			throws UserNotFoundException, InvalidEmailServerConfigurationException {
		SendPin pin = new SendPin();
		pin.setId(765757l);
		pin.setPin("0000");

		service.checkPin(pin);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testCheckPin_multipleTimesWrongPin_shouldGetLockedAfter2Strikes()
			throws InvalidEmailServerConfigurationException {
		SendPin pin = new SendPin();
		pin.setId(1l);
		pin.setPin("0000");
		assertEquals("should be strike 1", UserStatus.ACTIVE, dao.findOne(1l).getStatus());
		
		try {
			service.checkPin(pin);
		} catch (Exception e) {
		}
		assertEquals("should be strike 1", UserStatus.STRIKE_ONE, dao.findOne(1l).getStatus());

		try {
			service.checkPin(pin);
		} catch (Exception e) {
		}
		assertEquals("should be strike 2", UserStatus.STRIKE_TWO, dao.findOne(1l).getStatus());

		try {
			service.checkPin(pin);
		} catch (Exception e) {
		}
		assertEquals("should be Locked", UserStatus.LOCKED, dao.findOne(1l).getStatus());

		verify(emailService, times(1)).sendUnlockCodeEmailFor(any(User.class));
		reset(emailService);
		assertTrue(dao.findOne(1l).getUnlockCode() != null);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testCreatePin_withValidPinAndUser_shouldCreate()
			throws PinAlreadyExistingException, UserNotFoundException {
		CreatePin pin = new CreatePin();
		pin.setId(3l);
		pin.setPin("6983");
		pin.setRecoveryEmail("hons@gmx.net");

		service.createPin(pin);

		User hons = dao.getOne(3l);
		assertEquals("verify pin is created", "6983", hons.getPinCode());
	}

	@Test(expected = UserNotFoundException.class)
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testCreatePin_withInvalidUser_shouldCreate() throws PinAlreadyExistingException, UserNotFoundException {
		CreatePin pin = new CreatePin();
		pin.setId(76587l);
		pin.setPin("6983");
		pin.setRecoveryEmail("hons@gmx.net");

		service.createPin(pin);
	}

	@Test(expected = UserNotFoundException.class)
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testCreatePin_withValidPinAndUserButInvalidRecoveryEmail_shouldCreate()
			throws PinAlreadyExistingException, UserNotFoundException {
		CreatePin pin = new CreatePin();
		pin.setId(3l);
		pin.setPin("6983");
		pin.setRecoveryEmail("flo@gmx.net");

		service.createPin(pin);

		User hons = dao.getOne(3l);
		assertEquals("verify pin is created", "6983", hons.getPinCode());
	}

	@Test(expected = PinAlreadyExistingException.class)
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testCreatePin_withValidPinAndUserButWithAlreadyExistingPin_shouldThrowException()
			throws PinAlreadyExistingException, UserNotFoundException {
		CreatePin pin = new CreatePin();
		pin.setId(1l);
		pin.setPin("6983");
		pin.setRecoveryEmail("flo@gmx.net");

		service.createPin(pin);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testDeletePin_withValidUserId_shouldDelete() throws UserNotFoundException {
		service.deletePin(4l);

		assertNull(dao.findOne(4l).getPinCode());
	}

	@Test(expected = UserNotFoundException.class)
	public void testDeletePin_withValidUserId_shouldThrowException() throws UserNotFoundException {
		service.deletePin(654654l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testUnlockUser_withCorrectPinAndUser_shouldWork()
			throws UserNotFoundException {
		assertEquals("is user locked before", UserStatus.LOCKED, dao.findOne(4l).getStatus());

		Unlock unlock = new Unlock();
		unlock.setId(4l);
		unlock.setUnlockCode("s887q");

		UnlockResult result = service.unlockUser(unlock);
		assertTrue(result.getIsUnlocked());

		assertEquals("is user locked before", UserStatus.ACTIVE, dao.findOne(4l).getStatus());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testUnlockUser_withWrongPin_shouldThrowException()
			throws UserNotFoundException {
		Unlock unlock = new Unlock();
		unlock.setId(4l);
		unlock.setUnlockCode("0000");
		
		UnlockResult result = service.unlockUser(unlock);
		assertFalse("user is not unlocked, code wrong", result.getIsUnlocked());
	}

	@Test(expected = UserNotFoundException.class)
	public void testUnlockUser_withWrongUser_shouldThrowException()
			throws UserNotFoundException {
		Unlock unlock = new Unlock();
		unlock.setId(76575l);
		unlock.setUnlockCode("s887q");

		service.unlockUser(unlock);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/test" })
	public void testLockUser_withValidUserId_shouldLock()
			throws UserNotFoundException, InvalidEmailServerConfigurationException {
		service.lockUser(1l);

		assertEquals("should be Locked", UserStatus.LOCKED, dao.findOne(1l).getStatus());

		verify(emailService, times(1)).sendUnlockCodeEmailFor(any(User.class));
		reset(emailService);
		assertTrue(dao.findOne(1l).getUnlockCode() != null);
	}

	@Test(expected = UserNotFoundException.class)
	public void testLockUser_withInValidUserId_shouldThrowException()
			throws UserNotFoundException, InvalidEmailServerConfigurationException {
		service.lockUser(8768761l);
	}
}
