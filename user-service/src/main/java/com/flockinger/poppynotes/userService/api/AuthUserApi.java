package com.flockinger.poppynotes.userService.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.flockinger.poppynotes.userService.dto.AuthUser;
import com.flockinger.poppynotes.userService.dto.CreatePin;
import com.flockinger.poppynotes.userService.dto.Error;
import com.flockinger.poppynotes.userService.dto.SendPin;
import com.flockinger.poppynotes.userService.dto.ShowUser;
import com.flockinger.poppynotes.userService.dto.Unlock;
import com.flockinger.poppynotes.userService.exception.DtoValidationFailedException;
import com.flockinger.poppynotes.userService.exception.InvalidEmailServerConfigurationException;
import com.flockinger.poppynotes.userService.exception.PinAlreadyExistingException;
import com.flockinger.poppynotes.userService.exception.UserNotFoundException;
import com.flockinger.poppynotes.userService.exception.WrongUnlockCodeException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface AuthUserApi {

	@ApiOperation(value = "Check if user exists", notes = "Checkes if user exists and returns info if so.", response = ShowUser.class, tags = {
			"Users", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User info.", response = ShowUser.class),
			@ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
			@ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
			@ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
	@RequestMapping(value = "/api/v1/users/auth", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<ShowUser> apiV1UsersAuthPost(@ApiParam(value = "") @RequestBody AuthUser userAuth,
			BindingResult bindingResult) throws DtoValidationFailedException, UserNotFoundException;

	@ApiOperation(value = "Checks pin of user", notes = "Fetches User info with defined Id.", response = Void.class, tags = {
			"Users", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sent Pin is correct.", response = Void.class),
			@ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
			@ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
			@ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
	@RequestMapping(value = "/api/v1/users/pin/check", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Void> apiV1UsersPinCheckPost(@ApiParam(value = "") @RequestBody SendPin pinSend,
			BindingResult bindingResult)
			throws DtoValidationFailedException, UserNotFoundException, InvalidEmailServerConfigurationException;

	@ApiOperation(value = "Creates a new Pin", notes = "Creates a new Pin code for the user.", response = Void.class, tags = {
			"Users", })
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Pin created.", response = Void.class),
			@ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
			@ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
			@ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
	@RequestMapping(value = "/api/v1/users/pin", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Void> apiV1UsersPinPost(@ApiParam(value = "") @RequestBody CreatePin pinCreate,
			BindingResult bindingResult)
			throws DtoValidationFailedException, PinAlreadyExistingException, UserNotFoundException;

	@ApiOperation(value = "Deletes a Pin", notes = "Removes a Pin code.", response = Void.class, tags = { "Users", })
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Pin removed.", response = Void.class),
			@ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
			@ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
			@ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
	@RequestMapping(value = "/api/v1/users/pin/{userId}", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.DELETE)
	ResponseEntity<Void> apiV1UsersPinUserIdDelete(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId)
			throws UserNotFoundException;

	@ApiOperation(value = "Unlocks user", notes = "Unlocks a locked user with code", response = Void.class, tags = {
			"Users", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User unlocked.", response = Void.class),
			@ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
			@ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
			@ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
	@RequestMapping(value = "/api/v1/users/unlock", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Void> apiV1UsersUnlockPost(@ApiParam(value = "") @RequestBody Unlock userUnlock,
			BindingResult bindingResult)
			throws DtoValidationFailedException, UserNotFoundException, WrongUnlockCodeException;

	@ApiOperation(value = "Locks user", notes = "Locks a user and sends unlock email.", response = Void.class, tags = {
			"Users", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User locked.", response = Void.class),
			@ApiResponse(code = 400, message = "Bad request (validation failed).", response = Void.class),
			@ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
			@ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = Void.class),
			@ApiResponse(code = 409, message = "Request results in a conflict.", response = Void.class),
			@ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
	@RequestMapping(value = "/api/v1/users/lock/{userId}", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Void> apiV1UsersLockUserIdPost(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId)
			throws UserNotFoundException, InvalidEmailServerConfigurationException;
}
