package com.flockinger.poppynotes.userService.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * AuthUserResponse
 */

public class AuthUserResponse {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("recoveryEmail")
	private String recoveryEmail = null;

	@JsonProperty("status")
	private StatusEnum status = null;

	@JsonProperty("roles")
	private List<RolesEnum> roles = new ArrayList<RolesEnum>();

	@JsonProperty("cryptKey")
	private String cryptKey = null;

	/**
	 * Unique Identifier of the User.
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the User.")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Display name.
	 * 
	 * @return name
	 **/
	@ApiModelProperty(value = "Display name.")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Email for Two-factor authorization.
	 * 
	 * @return recoveryEmail
	 **/
	@ApiModelProperty(value = "Email for Two-factor authorization.")
	public String getRecoveryEmail() {
		return recoveryEmail;
	}

	public void setRecoveryEmail(String recoveryEmail) {
		this.recoveryEmail = recoveryEmail;
	}

	/**
	 * Status of the User.
	 * 
	 * @return status
	 **/
	@ApiModelProperty(value = "Status of the User.")
	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	/**
	 * Get roles
	 * 
	 * @return roles
	 **/
	@ApiModelProperty(value = "")
	public List<RolesEnum> getRoles() {
		return roles;
	}

	public void setRoles(List<RolesEnum> roles) {
		this.roles = roles;
	}

	/**
	 * Key for message encryption, should be manually & random created and very
	 * long.
	 * 
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
		AuthUserResponse authUserResponse = (AuthUserResponse) o;
		return Objects.equals(this.name, authUserResponse.name)
				&& Objects.equals(this.recoveryEmail, authUserResponse.recoveryEmail)
				&& Objects.equals(this.status, authUserResponse.status)
				&& Objects.equals(this.roles, authUserResponse.roles)
				&& Objects.equals(this.cryptKey, authUserResponse.cryptKey);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, recoveryEmail, status, roles, cryptKey);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AuthUserResponse {\n");

		sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
