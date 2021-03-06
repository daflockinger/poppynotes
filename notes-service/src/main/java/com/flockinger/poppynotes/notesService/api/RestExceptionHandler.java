package com.flockinger.poppynotes.notesService.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.flockinger.poppynotes.notesService.exception.AccessingOtherUsersNotesException;
import com.flockinger.poppynotes.notesService.exception.CantUseInitVectorTwiceException;
import com.flockinger.poppynotes.notesService.exception.DtoValidationFailedException;
import com.flockinger.poppynotes.notesService.exception.NoteNotFoundException;
import com.flockinger.poppynotes.notesService.exception.NoteSizeExceededException;
import com.flockinger.poppynotes.notesService.dto.Error;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
 
    
    @ExceptionHandler(value = {NoteNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        
    	return handleExceptionInternal(ex, createErrorModel(ex), 
          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    
    @ExceptionHandler(value = {DtoValidationFailedException.class, NoteSizeExceededException.class})
    protected ResponseEntity<Object> handleBadRequest(DtoValidationFailedException ex, WebRequest request) {
        
    	return handleExceptionInternal(ex, createErrorValidatedModel(ex), 
          new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
    
    
    @ExceptionHandler(value = {AccessingOtherUsersNotesException.class, 
        CantUseInitVectorTwiceException.class})
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        
    	return handleExceptionInternal(ex, createErrorModel(ex), 
          new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
        
    private Error createErrorValidatedModel(DtoValidationFailedException validationException){
    	Error error = createErrorModel(validationException);
    	
    	List<FieldError> fieldErrors = validationException.getErrors();
    	Map<String, String> fieldMap = fieldErrors.stream().collect(Collectors.toMap(FieldError::getField, value -> 
    	{return value.getDefaultMessage() + "[" + value.getRejectedValue() + "]";}));
    	
    	error.setFields(fieldMap);
    	return error;
    }
    
    private Error createErrorModel(Exception exception){
    	Error error = new Error();
    	error.setMessage(exception.getMessage());
    	
    	return error;
    }
}
