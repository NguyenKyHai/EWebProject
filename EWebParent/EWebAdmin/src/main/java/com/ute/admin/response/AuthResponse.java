package com.ute.admin.response;

import java.util.Set;

public class AuthResponse {
	private String email;
	private String accessToken;
	private String firstName;
	private String lastName;
	private Set<String>roles;

	public AuthResponse() {
		
	}

	public AuthResponse(String email, String accessToken, String firstName, String lastName, Set<String> roles) {
		super();
		this.email = email;
		this.accessToken = accessToken;
		this.firstName = firstName;
		this.lastName = lastName;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	
}