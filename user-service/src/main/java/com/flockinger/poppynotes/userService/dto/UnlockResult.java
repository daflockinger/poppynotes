package com.flockinger.poppynotes.userService.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * UnlockResult
 */

public class UnlockResult   {
  @JsonProperty("isUnlocked")
  private Boolean isUnlocked = null;

   /**
   * Returns true if sent unlock code is valid (correct) and user is unlocked.
   * @return isUnlocked
  **/
  @ApiModelProperty(value = "Returns true if sent unlock code is valid (correct) and user is unlocked.")
  public Boolean getIsUnlocked() {
    return isUnlocked;
  }

  public void setIsUnlocked(Boolean isUnlocked) {
    this.isUnlocked = isUnlocked;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnlockResult unlockResult = (UnlockResult) o;
    return Objects.equals(this.isUnlocked, unlockResult.isUnlocked);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isUnlocked);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnlockResult {\n");
    
    sb.append("    isUnlocked: ").append(toIndentedString(isUnlocked)).append("\n");
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

