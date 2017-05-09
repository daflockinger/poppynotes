package com.flockinger.poppynotes.userService.exception;

public class WrongUnlockCodeException extends Exception {

	/**
	* 
	*/
	private static final long serialVersionUID = 347433300777708750L;

	public WrongUnlockCodeException(String message) {
		super(message);
	}
}
