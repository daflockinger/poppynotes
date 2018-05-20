package com.flockinger.poppynotes.notesService.exception;

import java.util.ArrayList;

public class NoteSizeExceededException extends DtoValidationFailedException {
  /**
   * 
   */
  private static final long serialVersionUID = 1730489610874422960L;

  public NoteSizeExceededException(String message) {
    super(message, new ArrayList<>());
  }
}
