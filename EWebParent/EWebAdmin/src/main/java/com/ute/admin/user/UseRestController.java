package com.ute.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.ute.admin.jwt.JwtTokenUtil;
import com.ute.admin.request.AuthRequest;
import com.ute.admin.response.AuthResponse;
import com.ute.admin.response.ResponseMessage;
import com.ute.common.entity.User;

@RestController
@RequestMapping("/api")
public class UseRestController {

	@Autowired
	private IUserService userService;
	@Autowired
	AuthenticationManager authManager;
	@Autowired
	JwtTokenUtil jwtUtil;

	@GetMapping("/users")
	@RolesAllowed("ROLE_ADMIN")
	public ResponseEntity<?> getListUsers() {
		List<User> listUsers = userService.getAllUsers();
		if (listUsers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(listUsers, HttpStatus.OK);
	}

	@PostMapping("/user/save")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		// multiple valid here

		if (userService.existsByEmail(user.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("The email is existed"), HttpStatus.BAD_REQUEST);
		}
		userService.save(user);
		return new ResponseEntity<>(new ResponseMessage("Create User Successfully!"), HttpStatus.OK);
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User user) {
		try {
			User u = userService.findUserById(id);
			userService.save(user);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.OK);
		}
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable Integer id) {

		try {
			User user = userService.findUserById(id);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.OK);
		}

	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id) {

		try {
			userService.deleteUserById(id);
			return new ResponseEntity<>(new ResponseMessage("Delete user successfully"), HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.OK);
		}
	}

	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			User user = (User) authentication.getPrincipal();
//			String accessToken = jwtUtil.generateAccessToken(user);
//			AuthResponse response = new AuthResponse(user.getEmail(), accessToken);
			ResponseCookie jwtCookie = jwtUtil.generateJwtCookie(user);

			AuthResponse response = new AuthResponse(user.getEmail(), jwtCookie.getValue().toString());

			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);

		} catch (BadCredentialsException ex) {
			return new ResponseEntity<>(new ResponseMessage("Not permission"),HttpStatus.UNAUTHORIZED);

		}
	}
	 @PostMapping("/logout")
	  public ResponseEntity<?> logoutUser() {
	    ResponseCookie cookie = jwtUtil.getCleanJwtCookie();
	    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
	        .body(new ResponseMessage("You've been signed out!"));
	  }
}
