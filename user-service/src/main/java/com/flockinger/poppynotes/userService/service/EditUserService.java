package com.flockinger.poppynotes.userService.service;

import com.flockinger.poppynotes.userService.dto.CreateUser;
import com.flockinger.poppynotes.userService.dto.ShowUser;
import com.flockinger.poppynotes.userService.dto.UpdateUser;
import com.flockinger.poppynotes.userService.exception.DuplicateUserException;
import com.flockinger.poppynotes.userService.exception.UserNotFoundException;

public interface EditUserService {
	void createUser(CreateUser user) throws DuplicateUserException;
	void updateUser(UpdateUser user) throws UserNotFoundException, DuplicateUserException;
	void deleteUser(Long userId) throws UserNotFoundException;
	ShowUser getUserById(Long userId) throws UserNotFoundException;
}
