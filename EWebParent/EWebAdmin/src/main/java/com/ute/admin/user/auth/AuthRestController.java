package com.ute.admin.user.auth;

import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ut.common.request.AuthRequest;
import com.ut.common.request.ChangePassword;
import com.ut.common.response.AuthResponse;
import com.ut.common.response.ResponseMessage;
import com.ute.admin.jwt.JwtTokenFilter;
import com.ute.admin.jwt.JwtTokenUtil;
import com.ute.admin.user.IUserService;
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
	IUserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	JwtTokenFilter jwtTokenFilter;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {

		Optional<User> userCheck = userService.findUserByEmail(request.getEmail());

		if (userCheck.isPresent() && userCheck.get().getStatus().equals(Constants.STATUS_BLOCKED)) {
			return new ResponseEntity<>(new ResponseMessage("The user have been blocked"), HttpStatus.BAD_REQUEST);
		}

		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			User user = (User) authentication.getPrincipal();
			String accessToken = jwtUtil.generateAccessToken(user);
			Set<String> roles = new HashSet<>();
			user.getRoles().forEach(role -> roles.add(role.getName()));
			userService.updateStatus(user.getId(), Constants.STATUS_ACTIVE);
			AuthResponse response = new AuthResponse(user.getId(), user.getEmail(), accessToken, user.getFullName(),
					user.getPhoneNumber(), user.getAddress(), user.getStatus(), roles);

			return ResponseEntity.ok().body(response);

		} catch (BadCredentialsException ex) {
			return new ResponseEntity<>(new ResponseMessage("Please check your email or password!"),
					HttpStatus.UNAUTHORIZED);
		}
	}

	@PutMapping("/user/change-password")
	public ResponseEntity<?> changePassword(HttpServletRequest request, @RequestBody @Valid ChangePassword authRequest)
			throws IOException {
		String jwt = jwtTokenFilter.getAccessToken(request);
		if (jwt == null)
			return new ResponseEntity<>(new ResponseMessage("Token not found"), HttpStatus.NOT_FOUND);
		User user = (User) jwtTokenFilter.getUserDetails(jwt);
		if (user == null)
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.NOT_FOUND);
		User userCheck = userService.findUserByEmail(user.getEmail()).get();
		boolean matches = passwordEncoder.matches(authRequest.getOldPassword(), userCheck.getPassword());
		if (matches) {
			userCheck.setPassword(authRequest.getChangePassword());
			userService.save(userCheck);
		} else {
			return new ResponseEntity<>(new ResponseMessage("Password does not match!"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new ResponseMessage("Update password successfully"), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		String jwt = jwtTokenFilter.getAccessToken(request);
		if (jwt == null)
			return new ResponseEntity<>(new ResponseMessage("Token not found"), HttpStatus.NOT_FOUND);
		User user = (User) jwtTokenFilter.getUserDetails(jwt);
		if (user == null)
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.NOT_FOUND);
		User userCheck = userService.findUserByEmail(user.getEmail()).get();
		try {
			userService.updateStatus(userCheck.getId(), Constants.STATUS_LOGOUT);
			return new ResponseEntity<>(new ResponseMessage("You have been logout!"), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.OK);
		}
	}
}
