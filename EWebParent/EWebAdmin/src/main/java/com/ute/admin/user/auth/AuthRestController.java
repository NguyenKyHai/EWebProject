package com.ute.admin.user.auth;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ute.admin.jwt.JwtTokenUtil;
import com.ute.admin.request.AuthRequest;
import com.ute.admin.response.AuthResponse;
import com.ute.admin.response.ResponseMessage;
import com.ute.admin.user.UserService;
import com.ute.common.constants.Constants;
import com.ute.common.entity.User;

@RestController
@RequestMapping("/api")
public class AuthRestController {

	@Autowired
	AuthenticationManager authManager;
	@Autowired
	JwtTokenUtil jwtUtil;
	@Autowired
	UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {

		Optional<User> userCheck = userService.findUserByEmail(request.getEmail());

		if (userCheck.get().getStatus().equals(Constants.STATUS_BLOCKED)) {
			return new ResponseEntity<>(new ResponseMessage("The user have been blocked"), HttpStatus.BAD_REQUEST);
		}

		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			User user = (User) authentication.getPrincipal();
			String accessToken = jwtUtil.generateAccessToken(user);
			Set<String> roles = new HashSet<>();
			user.getRoles().forEach(role -> roles.add(role.getName()));
			userService.updateStatus(user.getId(),Constants.STATUS_ACTIVE);
			AuthResponse response = new AuthResponse(user.getId(),user.getEmail(), accessToken, user.getFirstName(),
					user.getLastName(),user.getPhoneNumber(),user.getAddress(),user.getStatus(), roles);

			return ResponseEntity.ok().body(response);

		} catch (BadCredentialsException ex) {
			return new ResponseEntity<>(new ResponseMessage("Please check your email or password!"),
					HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/logout/{id}")
	public ResponseEntity<?> logout(@PathVariable Integer id) {
		try {
			userService.updateStatus(id, Constants.STATUS_LOGOUT);
			return new ResponseEntity<>(new ResponseMessage("You have been logout!"), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.OK);
		}
	}
}
