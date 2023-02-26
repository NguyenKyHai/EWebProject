package com.ute.common.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class UserRequest {
	@NotNull
	@Email
	@Length(min = 5, max = 50)
	private String email;

	@NotNull
	@Length(min = 5, max = 20)
	private String password;

	@NotNull
	@Length(max = 64)
	private String fullName;
	
	@Length(max = 12)
	private String phoneNumber;

	@Length(max = 128)
	private String address;
	
	private Set<String> roles;

	public UserRequest() {
	}

	
	public UserRequest(@NotNull @Email @Length(min = 5, max = 50) String email,
			@NotNull @Length(min = 5, max = 20) String password, @NotNull @Length(max = 64) String fullName,
			Set<String> roles) {
		super();
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.roles = roles;
	}


	public UserRequest(@NotNull @Email @Length(min = 5, max = 50) String email,
			@NotNull @Length(min = 5, max = 20) String password, @NotNull @Length(max = 64) String fullName,
			@Length(max = 12) String phoneNumber, @Length(max = 128) String address, Set<String> roles) {
		super();
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.roles = roles;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}


}
