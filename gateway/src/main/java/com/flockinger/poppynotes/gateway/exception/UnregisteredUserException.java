package com.flockinger.poppynotes.gateway.exception;

import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;

public class UnregisteredUserException extends ClientAuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1999402779341326743L;

	public UnregisteredUserException(String msg) {
		super(msg);
	}

	@Override
	public int getHttpErrorCode() {
		return 403;
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_token";
	}
}
