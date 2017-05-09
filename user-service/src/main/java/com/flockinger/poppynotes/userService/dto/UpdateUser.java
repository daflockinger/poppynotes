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
 * UpdateUser
 */
public class UpdateUser   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("name")
  @NotEmpty
  private String name = null;

  @JsonProperty("authEmail")
  @NotEmpty
  @Email
  private String authEmail = null;

  @JsonProperty("recoveryEmail")
  @NotEmpty
  @Email
  private String recoveryEmail = null;

  

  @JsonProperty("status")
  @NotNull
  private StatusEnum status = null;



  @JsonProperty("roles")
  @NotEmpty
  @Valid
  private Set<RolesEnum> roles = new HashSet<RolesEnum>();

  public UpdateUser id(Long id) {
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

  public UpdateUser name(String name) {
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

  public UpdateUser authEmail(String authEmail) {
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

  public UpdateUser recoveryEmail(String recoveryEmail) {
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

  public UpdateUser status(StatusEnum status) {
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

  public UpdateUser roles(Set<RolesEnum> roles) {
    this.roles = roles;
    return this;
  }

  public UpdateUser addRolesItem(RolesEnum rolesItem) {
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
    UpdateUser updateUser = (UpdateUser) o;
    return Objects.equals(this.id, updateUser.id) &&
        Objects.equals(this.name, updateUser.name) &&
        Objects.equals(this.authEmail, updateUser.authEmail) &&
        Objects.equals(this.recoveryEmail, updateUser.recoveryEmail) &&
        Objects.equals(this.status, updateUser.status) &&
        Objects.equals(this.roles, updateUser.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, authEmail, recoveryEmail, status, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateUser {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    authEmail: ").append(toIndentedString(authEmail)).append("\n");
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

