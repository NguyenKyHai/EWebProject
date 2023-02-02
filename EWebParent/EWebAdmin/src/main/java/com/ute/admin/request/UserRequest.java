package com.ute.admin.request;

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
	@Length(min = 5, max = 10)
	private String password;

	@NotNull
	@Length(max = 50)
	private String firstName;

	@NotNull
	@Length(max = 50)
	private String lastName;

	private Set<String> roles;

	public UserRequest() {
	}

	public UserRequest(@NotNull @Email @Length(min = 5, max = 50) String email,
			@NotNull @Length(min = 5, max = 10) String password, @NotNull @Length(min = 5, max = 50) String firstName,
			@NotNull @Length(min = 5, max = 50) String lastName, Set<String> roles) {
		this.email = email;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
