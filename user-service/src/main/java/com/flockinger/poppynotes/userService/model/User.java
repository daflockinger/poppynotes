package com.flockinger.poppynotes.userService.model;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "authEmail" }) }
,indexes={@Index(columnList="authEmail")})
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;

	@NotEmpty
	private String authEmail;

	@NotEmpty
	private String recoveryEmail;

	private String pinCode;

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Set<UserRole> roles;

	private String unlockCode;

	@Lob
	private String cryptKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthEmail() {
		return authEmail;
	}

	public void setAuthEmail(String authEmail) {
		this.authEmail = authEmail;
	}

	public String getRecoveryEmail() {
		return recoveryEmail;
	}

	public void setRecoveryEmail(String recoveryEmail) {
		this.recoveryEmail = recoveryEmail;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public String getUnlockCode() {
		return unlockCode;
	}

	public void setUnlockCode(String unlockCode) {
		this.unlockCode = unlockCode;
	}

	public String getCryptKey() {
		return cryptKey;
	}

	public void setCryptKey(String cryptKey) {
		this.cryptKey = cryptKey;
	}
}
