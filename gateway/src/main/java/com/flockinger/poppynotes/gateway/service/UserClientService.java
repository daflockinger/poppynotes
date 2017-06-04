package com.flockinger.poppynotes.gateway.service;

import org.springframework.stereotype.Service;

import com.flockinger.poppynotes.gateway.exception.UnregisteredUserException;
import com.flockinger.poppynotes.gateway.exception.UserNotCachedException;
import com.flockinger.poppynotes.gateway.model.AuthUserResponse;

@Service
public interface UserClientService {
	AuthUserResponse getUserInfoFromAuthEmail(String authEmail) throws UnregisteredUserException;
	AuthUserResponse getCachedUserById(Long userId) throws UserNotCachedException;
}
