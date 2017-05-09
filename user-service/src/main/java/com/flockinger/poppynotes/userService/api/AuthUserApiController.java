package com.flockinger.poppynotes.userService.api;

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
import com.flockinger.poppynotes.userService.dto.SendPin;
import com.flockinger.poppynotes.userService.dto.ShowUser;
import com.flockinger.poppynotes.userService.dto.Unlock;
import com.flockinger.poppynotes.userService.exception.DtoValidationFailedException;
import com.flockinger.poppynotes.userService.exception.InvalidEmailServerConfigurationException;
import com.flockinger.poppynotes.userService.exception.PinAlreadyExistingException;
import com.flockinger.poppynotes.userService.exception.UserNotFoundException;
import com.flockinger.poppynotes.userService.exception.WrongUnlockCodeException;
import com.flockinger.poppynotes.userService.service.AuthUserService;

import io.swagger.annotations.ApiParam;

@RestController
public class AuthUserApiController implements AuthUserApi {

	@Autowired
	private AuthUserService service;

	public ResponseEntity<ShowUser> apiV1UsersAuthPost(@ApiParam(value = "") @Valid @RequestBody AuthUser userAuth,
			BindingResult bindingResult) throws DtoValidationFailedException, UserNotFoundException {

		assertRequest(bindingResult);
		ShowUser userInfo = service.getUserByAuthEmailHash(userAuth);
		return new ResponseEntity<ShowUser>(userInfo, HttpStatus.OK);
	}

	public ResponseEntity<Void> apiV1UsersPinCheckPost(@ApiParam(value = "") @Valid @RequestBody SendPin pinSend,
			BindingResult bindingResult)
			throws DtoValidationFailedException, UserNotFoundException, InvalidEmailServerConfigurationException {

		assertRequest(bindingResult);
		boolean isPinCorrect = service.checkPin(pinSend);
		
		if(isPinCorrect) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
	}

	public ResponseEntity<Void> apiV1UsersPinPost(@ApiParam(value = "") @Valid @RequestBody CreatePin pinCreate,
			BindingResult bindingResult)
			throws DtoValidationFailedException, PinAlreadyExistingException, UserNotFoundException {

		assertRequest(bindingResult);
		service.createPin(pinCreate);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	public ResponseEntity<Void> apiV1UsersPinUserIdDelete(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId)
			throws UserNotFoundException {

		service.deletePin(userId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<Void> apiV1UsersUnlockPost(@ApiParam(value = "") @Valid @RequestBody Unlock userUnlock,
			BindingResult bindingResult)
			throws DtoValidationFailedException, UserNotFoundException, WrongUnlockCodeException {

		assertRequest(bindingResult);
		service.unlockUser(userUnlock);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<Void> apiV1UsersLockUserIdPost(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId)
			throws UserNotFoundException, InvalidEmailServerConfigurationException {

		service.lockUser(userId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	private void assertRequest(BindingResult result) throws DtoValidationFailedException {
		if (result.hasErrors()) {
			throw new DtoValidationFailedException("Validation failed!", result.getFieldErrors());
		}
	}
}
