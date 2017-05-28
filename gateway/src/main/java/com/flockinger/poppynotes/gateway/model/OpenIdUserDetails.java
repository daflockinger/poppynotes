package com.flockinger.poppynotes.gateway.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class OpenIdUserDetails implements UserDetails {

	    private static final long serialVersionUID = 1L;

	    private String userId;
	    private String username;
	    private OAuth2AccessToken token;
	    private Set<GrantedAuthority> authorities;

	    public OpenIdUserDetails(Map<String, String> userInfo, OAuth2AccessToken token) {
	        this.userId = userInfo.get("sub");
	        this.username = userInfo.get("email");
	        this.token = token;
	    }

	    @Override
	    public String getUsername() {
	        return username;
	    }
	    
	    public void setAuthorities(Set<GrantedAuthority> roles) {
			this.authorities = roles;
		}

		@Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return authorities;
	    }

	    public String getUserId() {
	        return userId;
	    }

	    public void setUserId(String userId) {
	        this.userId = userId;
	    }

	    public OAuth2AccessToken getToken() {
	        return token;
	    }

	    public void setToken(OAuth2AccessToken token) {
	        this.token = token;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    @Override
	    public String getPassword() {
	        return null;
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }

}
