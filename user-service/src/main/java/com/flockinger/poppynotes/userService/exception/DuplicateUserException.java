package com.flockinger.poppynotes.userService.exception;

public class DuplicateUserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3476371382632538438L;

	public DuplicateUserException(String message) {
		super(message);
	}
}
