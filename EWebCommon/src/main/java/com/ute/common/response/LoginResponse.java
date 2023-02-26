package com.ute.common.response;

public class LoginResponse {
	
	private String email;
	private String accessToken;
	private String type="Bearer";

	public LoginResponse() {
		super();
	}

	public LoginResponse(String email, String accessToken) {
		super();
		this.email = email;
		this.accessToken = accessToken;
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
	
	
}
