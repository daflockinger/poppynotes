package com.flockinger.poppynotes.notesService.exception;

public class CantUseInitVectorTwiceException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = -8602789783105527154L;
  
  public CantUseInitVectorTwiceException(String message) {
    super(message);
  }

}
