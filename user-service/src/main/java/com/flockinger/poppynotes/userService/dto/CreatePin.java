package com.flockinger.poppynotes.userService.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * CreatePin
 */
public class CreatePin   {
  @JsonProperty("id")
  @NotNull
  private Long id = null;

  @JsonProperty("pin")
  @Length(min=4)
  private String pin = null;

  @JsonProperty("recoveryEmail")
  @NotEmpty
  private String recoveryEmail = null;

  public CreatePin id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the User.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the User.")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CreatePin pin(String pin) {
    this.pin = pin;
    return this;
  }

   /**
   * Pin code accessing/editing notes (Third-factor).
   * @return pin
  **/
  @ApiModelProperty(value = "Pin code accessing/editing notes (Third-factor).")
  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public CreatePin recoveryEmail(String recoveryEmail) {
    this.recoveryEmail = recoveryEmail;
    return this;
  }

   /**
   * Email for Two-factor authorization.
   * @return recoveryEmail
  **/
  @ApiModelProperty(value = "Email for Two-factor authorization.")
  public String getRecoveryEmail() {
    return recoveryEmail;
  }

  public void setRecoveryEmail(String recoveryEmail) {
    this.recoveryEmail = recoveryEmail;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreatePin createPin = (CreatePin) o;
    return Objects.equals(this.id, createPin.id) &&
        Objects.equals(this.pin, createPin.pin) &&
        Objects.equals(this.recoveryEmail, createPin.recoveryEmail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, pin, recoveryEmail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreatePin {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
    sb.append("    recoveryEmail: ").append(toIndentedString(recoveryEmail)).append("\n");
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

