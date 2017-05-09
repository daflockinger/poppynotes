package com.flockinger.poppynotes.userService.exception;

public class UserNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7893313682122863055L;

	public UserNotFoundException(String message){
		super(message);
	}
}
