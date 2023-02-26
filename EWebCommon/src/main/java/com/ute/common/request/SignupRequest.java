package com.ute.common.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class SignupRequest {
	@NotNull
	@Email
	@Length(min = 5, max = 50)
	private String email;

	@NotNull
	@Length(min = 5, max = 20)
	private String password;

	@NotNull
	@Length(min = 2, max = 64)
	private String fullName;

	public SignupRequest() {
		super();
	}

	public SignupRequest(@NotNull @Email @Length(min = 5, max = 50) String email,
			@NotNull @Length(min = 5, max = 20) String password, @NotNull @Length(min = 2, max = 64) String fullName) {
		super();
		this.email = email;
		this.password = password;
		this.fullName = fullName;
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
	
}
