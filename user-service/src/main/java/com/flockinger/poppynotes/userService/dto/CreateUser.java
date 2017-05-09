package com.flockinger.poppynotes.userService.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * CreateUser
 */
public class CreateUser   {
  @JsonProperty("name")
  @NotEmpty
  private String name = null;

  @JsonProperty("authEmail")
  @NotEmpty
  @Email
  private String authEmail = null;

  @JsonProperty("recoveryEmail")
  @Email
  @NotEmpty
  private String recoveryEmail = null;

  @JsonProperty("status")
  @NotNull
  private StatusEnum status = null;


  @JsonProperty("roles")
  @NotEmpty
  @Valid
  private Set<RolesEnum> roles = new HashSet<RolesEnum>();

  @JsonProperty("cryptKey")
  @NotEmpty
  private String cryptKey = null;

  public CreateUser name(String name) {
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

  public CreateUser authEmail(String authEmail) {
    this.authEmail = authEmail;
    return this;
  }

   /**
   * Hash of authorization email used in SSO
   * @return authEmail
  **/
  @ApiModelProperty(value = "Hash of authorization email used in SSO")
  public String getAuthEmail() {
    return authEmail;
  }

  public void setAuthEmail(String authEmail) {
    this.authEmail = authEmail;
  }

  public CreateUser recoveryEmail(String recoveryEmail) {
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

  public CreateUser status(StatusEnum status) {
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

  public CreateUser roles(Set<RolesEnum> roles) {
    this.roles = roles;
    return this;
  }

  public CreateUser addRolesItem(RolesEnum rolesItem) {
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

  public CreateUser cryptKey(String cryptKey) {
    this.cryptKey = cryptKey;
    return this;
  }

   /**
   * Key for message encryption, should be manually & random created and very long.
   * @return cryptKey
  **/
  @ApiModelProperty(value = "Key for message encryption, should be manually & random created and very long.")
  public String getCryptKey() {
    return cryptKey;
  }

  public void setCryptKey(String cryptKey) {
    this.cryptKey = cryptKey;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateUser createUser = (CreateUser) o;
    return Objects.equals(this.name, createUser.name) &&
        Objects.equals(this.authEmail, createUser.authEmail) &&
        Objects.equals(this.recoveryEmail, createUser.recoveryEmail) &&
        Objects.equals(this.status, createUser.status) &&
        Objects.equals(this.roles, createUser.roles) &&
        Objects.equals(this.cryptKey, createUser.cryptKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, authEmail, recoveryEmail, status, roles, cryptKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateUser {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    authEmail: ").append(toIndentedString(authEmail)).append("\n");
    sb.append("    recoveryEmail: ").append(toIndentedString(recoveryEmail)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("    cryptKey: ").append(toIndentedString(cryptKey)).append("\n");
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

