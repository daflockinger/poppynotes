package com.flockinger.poppynotes.userService.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * SendPin
 */
public class SendPin   {
  @JsonProperty("id")
  @NotNull
  private Long id = null;

  @JsonProperty("pin")
  @Length(min=4)
  private String pin = null;

  public SendPin id(Long id) {
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

  public SendPin pin(String pin) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendPin sendPin = (SendPin) o;
    return Objects.equals(this.id, sendPin.id) &&
        Objects.equals(this.pin, sendPin.pin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, pin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendPin {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
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

