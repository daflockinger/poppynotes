package com.flockinger.poppynotes.gateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Status of the User.
 */
public enum Status {
  ACTIVE("ACTIVE"),
  
  DISABLED("DISABLED"),
  
  STRIKE_ONE("STRIKE_ONE"),
  
  STRIKE_TWO("STRIKE_TWO"),
  
  LOCKED("LOCKED");

  private String value;

  Status(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Status fromValue(String text) {
    for (Status b : Status.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
