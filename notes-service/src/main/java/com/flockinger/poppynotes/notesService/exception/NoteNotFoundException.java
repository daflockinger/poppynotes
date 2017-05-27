package com.flockinger.poppynotes.notesService.exception;

public class NoteNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7004818446124511720L;

	
	public NoteNotFoundException(String message) {
		super(message);
	}
}
