package com.flockinger.poppynotes.userService.dto;

import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * AuthUser
 */
public class AuthUser   {
  @JsonProperty("authEmail")
  @NotEmpty
  private String authEmail = null;

  public AuthUser authEmail(String authEmail) {
    this.authEmail = authEmail;
    return this;
  }

   /**
   * Hash of authorization email.
   * @return authEmail
  **/
  @ApiModelProperty(value = "Hash of authorization email.")
  public String getAuthEmail() {
    return authEmail;
  }

  public void setAuthEmail(String authEmail) {
    this.authEmail = authEmail;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthUser authUser = (AuthUser) o;
    return Objects.equals(this.authEmail, authUser.authEmail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authEmail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthUser {\n");
    
    sb.append("    authEmail: ").append(toIndentedString(authEmail)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

