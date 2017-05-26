package com.flockinger.poppynotes.gateway.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ShowUser
 */
public class UserInfo   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("recoveryEmail")
  private String recoveryEmail = null;

  @JsonProperty("roles")
  private Set<Role> roles = new HashSet<Role>();

  @JsonProperty("status")
  private Status status = null;

   /**
   * Display name.
   * @return name
  **/
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UserInfo recoveryEmail(String recoveryEmail) {
    this.recoveryEmail = recoveryEmail;
    return this;
  }

   /**
   * Email for Two-factor authorization.
   * @return recoveryEmail
  **/
  public String getRecoveryEmail() {
    return recoveryEmail;
  }

  public void setRecoveryEmail(String recoveryEmail) {
    this.recoveryEmail = recoveryEmail;
  }

   /**
   * Status of the User.
   * @return status
  **/
  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

   /**
   * Get roles
   * @return roles
  **/
  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
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
    UserInfo showUser = (UserInfo) o;
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