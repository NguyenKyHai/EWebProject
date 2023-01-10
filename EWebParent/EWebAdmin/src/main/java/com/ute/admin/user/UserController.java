package com.ute.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ute.admin.response.ResponseMessage;
import com.ute.common.entity.User;

@RestController
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping("/users")
	public ResponseEntity<?> getListUsers() {
		List<User> listUsers = service.listAll();
		if (listUsers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(listUsers, HttpStatus.OK);
	}

	@PostMapping("/user/save")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		// multiple valid here
		service.save(user);
		return new ResponseEntity<>(new ResponseMessage("Create User Successfully!"), HttpStatus.OK);
	}
}
