package com.flockinger.poppynotes.gateway.service;

import org.springframework.stereotype.Service;

import com.flockinger.poppynotes.gateway.exception.UnregisteredUserException;
import com.flockinger.poppynotes.gateway.model.UserInfo;

@Service
public interface UserClientService {
	UserInfo getUserInfoFromAuthEmail(String authEmail) throws UnregisteredUserException;
}
