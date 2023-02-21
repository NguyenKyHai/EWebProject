package com.ute.shopping.response;

public class LoginResponse {
	private Integer id;
	private String email;
	private String accessToken;
	private String fullName;
	private String phoneNumber;
	private String address;
	private String status;

	public LoginResponse() {
		super();
	}

	

	public LoginResponse(Integer id, String email, String accessToken, String fullName) {
		super();
		this.id = id;
		this.email = email;
		this.accessToken = accessToken;
		this.fullName = fullName;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
