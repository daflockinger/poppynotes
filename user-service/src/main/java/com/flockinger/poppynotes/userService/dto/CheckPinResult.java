package com.flockinger.poppynotes.userService.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * CheckPinResult
 */
public class CheckPinResult   {
  @JsonProperty("valid")
  private Boolean valid = null;

   /**
   * Returns true if sent PIN code is valid (correct).
   * @return valid
  **/
  @ApiModelProperty(value = "Returns true if sent PIN code is valid (correct).")
  public Boolean getValid() {
    return valid;
  }

  public void setValid(Boolean valid) {
    this.valid = valid;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheckPinResult checkPinResult = (CheckPinResult) o;
    return Objects.equals(this.valid, checkPinResult.valid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheckPinResult {\n");
    
    sb.append("    valid: ").append(toIndentedString(valid)).append("\n");
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

