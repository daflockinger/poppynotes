package com.flockinger.poppynotes.userService.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * ShowUser
 */
public class ShowUser   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("recoveryEmail")
  private String recoveryEmail = null;

  @JsonProperty("roles")
  private Set<RolesEnum> roles = new HashSet<RolesEnum>();

  @JsonProperty("status")
  private StatusEnum status = null;
  
  public ShowUser name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Display name.
   * @return name
  **/
  @ApiModelProperty(value = "Display name.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ShowUser recoveryEmail(String recoveryEmail) {
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

  public ShowUser status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Status of the User.
   * @return status
  **/
  @ApiModelProperty(value = "Status of the User.")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public ShowUser roles(Set<RolesEnum> roles) {
    this.roles = roles;
    return this;
  }

  public ShowUser addRolesItem(RolesEnum rolesItem) {
    this.roles.add(rolesItem);
    return this;
  }

   /**
   * Get roles
   * @return roles
  **/
  @ApiModelProperty(value = "")
  public Set<RolesEnum> getRoles() {
    return roles;
  }

  public void setRoles(Set<RolesEnum> roles) {
    this.roles = roles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShowUser showUser = (ShowUser) o;
    return Objects.equals(this.name, showUser.name) &&
        Objects.equals(this.recoveryEmail, showUser.recoveryEmail) &&
        Objects.equals(this.status, showUser.status) &&
        Objects.equals(this.roles, showUser.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, recoveryEmail, status, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShowUser {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    recoveryEmail: ").append(toIndentedString(recoveryEmail)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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

