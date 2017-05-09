package com.flockinger.poppynotes.userService.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Status of the User.
 */
public enum StatusEnum {
  ACTIVE("ACTIVE"),
  
  DISABLED("DISABLED"),
  
  STRIKE_ONE("STRIKE_ONE"),
  
  STRIKE_TWO("STRIKE_TWO"),
  
  LOCKED("LOCKED");

  private String value;

  StatusEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatusEnum fromValue(String text) {
    for (StatusEnum b : StatusEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
