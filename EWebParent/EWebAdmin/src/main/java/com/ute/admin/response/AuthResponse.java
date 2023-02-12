package com.ute.admin.response;

import java.util.Set;

public class AuthResponse {
	private Integer id;
	private String email;
	private String accessToken;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
	private String status;
	private Set<String>roles;

	public AuthResponse() {
		
	}
	
	public AuthResponse(Integer id, String email, String accessToken, String firstName, String lastName, String status,
			Set<String> roles) {
		super();
		this.id = id;
		this.email = email;
		this.accessToken = accessToken;
		this.firstName = firstName;
		this.lastName = lastName;
		this.status = status;
		this.roles = roles;
	}

	public AuthResponse(Integer id, String email, String accessToken, String firstName, String lastName,
			String phoneNumber, String address, String status, Set<String> roles) {
		super();
		this.id = id;
		this.email = email;
		this.accessToken = accessToken;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.status = status;
		this.roles = roles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	
}