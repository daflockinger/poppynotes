package com.flockinger.poppynotes.userService.service;

import com.flockinger.poppynotes.userService.dto.AuthUser;
import com.flockinger.poppynotes.userService.dto.AuthUserResponse;
import com.flockinger.poppynotes.userService.dto.CheckPinResult;
import com.flockinger.poppynotes.userService.dto.CreatePin;
import com.flockinger.poppynotes.userService.dto.SendPin;
import com.flockinger.poppynotes.userService.dto.Unlock;
import com.flockinger.poppynotes.userService.dto.UnlockResult;
import com.flockinger.poppynotes.userService.exception.InvalidEmailServerConfigurationException;
import com.flockinger.poppynotes.userService.exception.PinAlreadyExistingException;
import com.flockinger.poppynotes.userService.exception.UserNotFoundException;

public interface AuthUserService {
	
	AuthUserResponse getUserByAuthEmailHash(AuthUser userAuth) throws UserNotFoundException;
	
	CheckPinResult checkPin(SendPin pin) throws UserNotFoundException, InvalidEmailServerConfigurationException;
	void createPin(CreatePin pin) throws PinAlreadyExistingException, UserNotFoundException;
	void deletePin(Long userId) throws UserNotFoundException;
	
	UnlockResult unlockUser(Unlock unlock) throws UserNotFoundException;
	void lockUser(Long userId) throws UserNotFoundException, InvalidEmailServerConfigurationException;
}
