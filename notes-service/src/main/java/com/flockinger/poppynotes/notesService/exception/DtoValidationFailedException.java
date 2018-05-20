package com.flockinger.poppynotes.notesService.exception;

import java.util.List;

import org.springframework.validation.FieldError;

public class DtoValidationFailedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9110322794181495490L;
	
	private List<FieldError> errors;

	public DtoValidationFailedException(String message, List<FieldError> errors){
		super(message);
		this.errors = errors;
	}

	public List<FieldError> getErrors() {
		return errors;
	}
}
