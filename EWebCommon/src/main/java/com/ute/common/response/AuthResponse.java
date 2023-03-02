package com.ute.common.response;

import java.util.Set;

public class AuthResponse {
	
	private String email;
	private String accessToken;
	private String type="Bearer";
	private Set<String>roles;

	public AuthResponse() {
		
	}

	public AuthResponse(String email, String accessToken, Set<String> roles) {
		super();
		this.email = email;
		this.accessToken = accessToken;
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	
}