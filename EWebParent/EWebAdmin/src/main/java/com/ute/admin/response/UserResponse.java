package com.ute.admin.response;


public class UserResponse {
	private String message;
	private String photo;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public UserResponse(String message, String photo) {
		this.message = message;
		this.photo = photo;
	}
	
	
}
