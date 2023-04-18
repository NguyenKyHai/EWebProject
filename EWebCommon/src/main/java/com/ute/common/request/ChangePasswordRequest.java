package com.ute.common.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class ChangePasswordRequest {
	@NotNull
	@Length(min = 5, max = 20)
	private String oldPassword;

	@NotNull
	@Length(min = 5, max = 20)
	private String changePassword;

	public ChangePasswordRequest() {
		super();
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getChangePassword() {
		return changePassword;
	}

	public void setChangePassword(String changePassword) {
		this.changePassword = changePassword;
	}

}
