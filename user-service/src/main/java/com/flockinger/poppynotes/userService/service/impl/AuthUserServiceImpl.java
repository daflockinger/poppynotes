package com.flockinger.poppynotes.userService.service.impl;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.poppynotes.userService.dao.UserRepository;
import com.flockinger.poppynotes.userService.dto.AuthUser;
import com.flockinger.poppynotes.userService.dto.CreatePin;
import com.flockinger.poppynotes.userService.dto.SendPin;
import com.flockinger.poppynotes.userService.dto.ShowUser;
import com.flockinger.poppynotes.userService.dto.Unlock;
import com.flockinger.poppynotes.userService.exception.InvalidEmailServerConfigurationException;
import com.flockinger.poppynotes.userService.exception.PinAlreadyExistingException;
import com.flockinger.poppynotes.userService.exception.UserNotFoundException;
import com.flockinger.poppynotes.userService.exception.WrongUnlockCodeException;
import com.flockinger.poppynotes.userService.model.User;
import com.flockinger.poppynotes.userService.model.UserStatus;
import com.flockinger.poppynotes.userService.service.AuthUserService;
import com.flockinger.poppynotes.userService.service.EmailService;

@Service
public class AuthUserServiceImpl implements AuthUserService {

	@Autowired
	private UserRepository dao;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private EmailService emailService;

	@Override
	@Transactional(readOnly = true)
	public ShowUser getUserByAuthEmailHash(AuthUser userAuth) throws UserNotFoundException {
		User user = dao.findByAuthEmail(userAuth.getAuthEmail());

		if (user == null) {
			throw new UserNotFoundException("User not found");
		}
		return map(user);
	}

	@Override
	@Transactional
	public boolean checkPin(SendPin pin) throws UserNotFoundException, InvalidEmailServerConfigurationException {
		User user = assertAndReturnUserById(pin.getId());

		if (StringUtils.equals(pin.getPin(), user.getPinCode())) {
			return true;
		} else {
			applyStrike(user);
			return false;
		}
	}

	private User assertAndReturnUserById(Long userId) throws UserNotFoundException {
		User user = dao.findOne(userId);
		if (user == null) {
			throw new UserNotFoundException("User not found with ID: " + userId);
		}
		return user;
	}

	private void applyStrike(User user) throws UserNotFoundException, InvalidEmailServerConfigurationException {
		switch (user.getStatus()) {
		case ACTIVE:
			saveStatus(user, UserStatus.STRIKE_ONE);
			break;
		case STRIKE_ONE:
			saveStatus(user, UserStatus.STRIKE_TWO);
			break;
		case STRIKE_TWO:
			saveStatus(user, UserStatus.LOCKED);
			lockUser(user.getId());
			break;
		case DISABLED:
			break;
		case LOCKED:
			break;
		default:
			break;
		}
	}

	private void saveStatus(User user, UserStatus status) {
		user.setStatus(status);
		dao.save(user);
	}

	@Override
	@Transactional
	public void createPin(CreatePin pin) throws PinAlreadyExistingException, UserNotFoundException {
		User user = assertAndReturnUserById(pin.getId());

		if (!StringUtils.equals(user.getRecoveryEmail(), pin.getRecoveryEmail())) {
			throw new UserNotFoundException("User not found with given id and recovery-email combination!");
		}
		if (user.getPinCode() != null) {
			throw new PinAlreadyExistingException("PIN code is already existing!");
		}

		user.setPinCode(pin.getPin());
		dao.save(user);
	}

	@Override
	@Transactional
	public void deletePin(Long userId) throws UserNotFoundException {
		User user = assertAndReturnUserById(userId);
		user.setPinCode(null);
		dao.save(user);
	}

	@Override
	@Transactional
	public void unlockUser(Unlock unlock) throws UserNotFoundException, WrongUnlockCodeException {
		User user = assertAndReturnUserById(unlock.getId());

		if (StringUtils.equals(unlock.getUnlockCode(), user.getUnlockCode())) {
			user.setUnlockCode(null);
			saveStatus(user, UserStatus.ACTIVE);
		} else {
			throw new WrongUnlockCodeException("Wrong Unlock Code Entered");
		}
	}

	@Override
	@Transactional
	public void lockUser(Long userId) throws UserNotFoundException, InvalidEmailServerConfigurationException {
		User user = assertAndReturnUserById(userId);
		user.setUnlockCode(generateUnlockCode());
		saveStatus(user, UserStatus.LOCKED);
		emailService.sendUnlockCodeEmailFor(user);
	}

	private String generateUnlockCode() {
		return RandomStringUtils.randomAlphabetic(5);
	}

	private ShowUser map(User user) {
		return mapper.map(user, ShowUser.class);
	}
}
