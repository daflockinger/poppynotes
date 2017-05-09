package com.flockinger.poppynotes.userService.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Role of a User (authorized to to specific stuff).
 */
public enum RolesEnum {
  AUTHOR("AUTHOR"),
  
  ADMIN("ADMIN");

  private String value;

  RolesEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RolesEnum fromValue(String text) {
    for (RolesEnum b : RolesEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
