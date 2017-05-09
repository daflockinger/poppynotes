package com.flockinger.poppynotes.userService.api;

import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.poppynotes.userService.dto.AuthUser;
import com.flockinger.poppynotes.userService.dto.CreatePin;
import com.flockinger.poppynotes.userService.dto.CreateUser;
import com.flockinger.poppynotes.userService.dto.SendPin;
import com.flockinger.poppynotes.userService.dto.ShowUser;
import com.flockinger.poppynotes.userService.dto.Unlock;
import com.flockinger.poppynotes.userService.dto.UpdateUser;
import com.flockinger.poppynotes.userService.exception.DtoValidationFailedException;
import com.flockinger.poppynotes.userService.exception.DuplicateUserException;
import com.flockinger.poppynotes.userService.exception.UserNotFoundException;
import com.flockinger.poppynotes.userService.service.EditUserService;

import io.swagger.annotations.ApiParam;

@RestController
public class EditUserApiController implements EditUserApi {

	@Autowired
	private EditUserService service;

	public ResponseEntity<Void> apiV1UsersPost(
			@ApiParam(value = "", required = true) @Valid @RequestBody CreateUser userCreate,
			BindingResult bindingResult) throws DtoValidationFailedException, DuplicateUserException {

		assertRequest(bindingResult);
		service.createUser(userCreate);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	public ResponseEntity<Void> apiV1UsersPut(
			@ApiParam(value = "", required = true) @Valid @RequestBody UpdateUser userUpdate,
			BindingResult bindingResult) throws DtoValidationFailedException, UserNotFoundException, DuplicateUserException {

		assertRequest(bindingResult);
		assertUserIdNotNullOnUpdate(userUpdate.getId());;
		service.updateUser(userUpdate);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<Void> apiV1UsersUserIdDelete(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId) throws UserNotFoundException {
		
		service.deleteUser(userId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<ShowUser> apiV1UsersUserIdGet(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId) throws UserNotFoundException {
		
		ShowUser user = service.getUserById(userId);
		return new ResponseEntity<ShowUser>(user,HttpStatus.OK);
	}

	private void assertUserIdNotNullOnUpdate(Long userId) throws DtoValidationFailedException {
		if (userId == null) {
			throw new DtoValidationFailedException("Id must not be null on update!", new ArrayList<>());
		}
	}

	private void assertRequest(BindingResult bindingResult) throws DtoValidationFailedException {
		if (bindingResult.hasErrors()) {
			throw new DtoValidationFailedException("Validation failed!", bindingResult.getFieldErrors());
		}
	}
}
