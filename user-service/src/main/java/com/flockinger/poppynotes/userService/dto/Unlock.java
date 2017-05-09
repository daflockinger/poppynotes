package com.flockinger.poppynotes.userService.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * Unlock
 */
public class Unlock   {
  @JsonProperty("id")
  @NotNull
  private Long id = null;

  @JsonProperty("unlockCode")
  @NotEmpty
  private String unlockCode = null;

  public Unlock id(Long id) {
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

  public Unlock unlockCode(String unlockCode) {
    this.unlockCode = unlockCode;
    return this;
  }

   /**
   * Code to unlock the locked account (what the name says).
   * @return unlockCode
  **/
  @ApiModelProperty(value = "Code to unlock the locked account (what the name says).")
  public String getUnlockCode() {
    return unlockCode;
  }

  public void setUnlockCode(String unlockCode) {
    this.unlockCode = unlockCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Unlock unlock = (Unlock) o;
    return Objects.equals(this.id, unlock.id) &&
        Objects.equals(this.unlockCode, unlock.unlockCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, unlockCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Unlock {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    unlockCode: ").append(toIndentedString(unlockCode)).append("\n");
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

