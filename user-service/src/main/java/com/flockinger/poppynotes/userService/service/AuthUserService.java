package com.flockinger.poppynotes.userService.service;

import com.flockinger.poppynotes.userService.dto.AuthUser;
import com.flockinger.poppynotes.userService.dto.CreatePin;
import com.flockinger.poppynotes.userService.dto.SendPin;
import com.flockinger.poppynotes.userService.dto.ShowUser;
import com.flockinger.poppynotes.userService.dto.Unlock;
import com.flockinger.poppynotes.userService.exception.InvalidEmailServerConfigurationException;
import com.flockinger.poppynotes.userService.exception.PinAlreadyExistingException;
import com.flockinger.poppynotes.userService.exception.UserNotFoundException;
import com.flockinger.poppynotes.userService.exception.WrongUnlockCodeException;

public interface AuthUserService {
	
	ShowUser getUserByAuthEmailHash(AuthUser userAuth) throws UserNotFoundException;
	
	boolean checkPin(SendPin pin) throws UserNotFoundException, InvalidEmailServerConfigurationException;
	void createPin(CreatePin pin) throws PinAlreadyExistingException, UserNotFoundException;
	void deletePin(Long userId) throws UserNotFoundException;
	
	void unlockUser(Unlock unlock) throws UserNotFoundException, WrongUnlockCodeException;
	void lockUser(Long userId) throws UserNotFoundException, InvalidEmailServerConfigurationException;
}
