package com.ute.admin.user.auth;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ute.admin.jwt.JwtTokenUtil;
import com.ute.admin.request.AuthRequest;
import com.ute.admin.request.UserRequest;
import com.ute.admin.response.AuthResponse;
import com.ute.admin.response.ResponseMessage;
import com.ute.admin.role.RoleService;
import com.ute.admin.user.UserService;
import com.ute.common.entity.Role;
import com.ute.common.entity.User;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthRestController {

	@Autowired
	AuthenticationManager authManager;
	@Autowired
	JwtTokenUtil jwtUtil;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			User user = (User) authentication.getPrincipal();
			String accessToken = jwtUtil.generateAccessToken(user);
			Set<String> roles = new HashSet<>();
			user.getRoles().forEach(role -> roles.add(role.getName()));

			AuthResponse response = new AuthResponse(user.getEmail(), accessToken, user.getFirstName(),
					user.getLastName(), roles);

			return ResponseEntity.ok().body(response);

		} catch (BadCredentialsException ex) {
			return new ResponseEntity<>(new ResponseMessage("Please check your email or password!"),
					HttpStatus.UNAUTHORIZED);

		}
	}

	@PostMapping("/auth/user/create")
	public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest) {
		if (userService.existsByEmail(userRequest.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("The email is existed"), HttpStatus.NOT_ACCEPTABLE);
		}

		User user = new User(userRequest.getEmail(), userRequest.getPassword(), userRequest.getFirstName(),
				userRequest.getLastName());
		Set<String> strRole = userRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		strRole.forEach(role -> {
			switch (role) {
			case "ROLE_ADMIN":
				Role adminRole = roleService.findByName("ROLE_ADMIN")
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(adminRole);
				break;
			case "ROLE_SALESPERSON":
				Role salesRole = roleService.findByName("ROLE_SALESPERSON")
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(salesRole);
				break;
			case "ROLE_ASSISTANT":
				Role assistantRole = roleService.findByName("ROLE_ASSISTANT")
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(assistantRole);
				break;
			case "ROLE_SHIPPER":
				Role shipperRole = roleService.findByName("ROLE_SHIPPER")
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(shipperRole);
				break;	
			default:
				Role editorRole = roleService.findByName("ROLE_EDITOR")
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(editorRole);
			}
		});

		user.setRoles(roles);
		userService.save(user);
		return new ResponseEntity<>(new ResponseMessage("Create a new user success!"), HttpStatus.CREATED);
	}
}
