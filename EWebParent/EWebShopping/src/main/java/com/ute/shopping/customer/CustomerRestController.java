package com.ute.shopping.customer;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ute.common.constants.AuthProvider;
import com.ute.common.constants.Constants;
import com.ute.common.entity.Customer;
import com.ute.common.request.ChangePassword;
import com.ute.common.request.LoginRequest;
import com.ute.common.request.SignupRequest;
import com.ute.common.response.LoginResponse;
import com.ute.common.response.ResponseMessage;
import com.ute.common.util.MailUtil;
import com.ute.common.util.RandomString;
import com.ute.shopping.jwt.JwtTokenFilter;
import com.ute.shopping.jwt.JwtTokenUtil;
import com.ute.shopping.security.UserPrincipal;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtTokenUtil jwtUtil;
	@Autowired
	ICustomerService customerService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	JwtTokenFilter jwtTokenFilter;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {

		Optional<Customer> customerCheck = customerService.findCustomerByEmail(request.getEmail());

		if (customerCheck.isPresent() && customerCheck.get().getStatus().equals(Constants.STATUS_BLOCKED)) {
			return new ResponseEntity<>(new ResponseMessage("The customer have been blocked"), HttpStatus.BAD_REQUEST);
		}

		if (customerCheck.isPresent() && customerCheck.get().getStatus().equals(Constants.STATUS_VERIFY)) {
			return new ResponseEntity<>(new ResponseMessage("The customer is verifying"), HttpStatus.BAD_REQUEST);
		}

		try {
			Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                		request.getEmail(),
	                		request.getPassword()
	                )
	        );
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
	        String token = jwtUtil.generateAccessToken(authentication);
	        customerService.updateStatus(userPrincipal.getId(), Constants.STATUS_ACTIVE);
			LoginResponse response = new LoginResponse(userPrincipal.getEmail(), token);

			return ResponseEntity.ok().body(response);

		} catch (BadCredentialsException ex) {
			return new ResponseEntity<>(new ResponseMessage("Please check your email or password!"),
					HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> createCustomer(@RequestBody @Valid SignupRequest signupRequest) {

		if (customerService.existsByEmail(signupRequest.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("The email is existed"), HttpStatus.BAD_REQUEST);
		}

		Customer customer = new Customer(signupRequest.getEmail(), signupRequest.getPassword(),
				signupRequest.getFullName());
		customer.setPhotos("default.png");
		customer.setStatus(Constants.STATUS_VERIFY);
		customer.setCreatedTime(new Date());
		String randomString = RandomString.randomString();
		customer.setVerificationCode(randomString);
		customer.setProvider(AuthProvider.local);
		MailUtil.sendMail(signupRequest.getEmail(), "Ma code xac nhan",
				"Cam on ban da dang ky.\n Ma code xac nhan cua ban la: " + randomString);
		customerService.save(customer);
		return new ResponseEntity<>(new ResponseMessage("Create a new customer successfully!"), HttpStatus.CREATED);
	}

	@PostMapping("/customer/verify")
	public ResponseEntity<?> veryfyAccount(@RequestBody Map<String, String> param) {

		String code = param.get("code");

		Customer customer = customerService.findByVerificationCode(code);
		if (customer != null) {
			customerService.updateVerifycationCode(customer.getId(), null);
			customerService.updateStatus(customer.getId(), Constants.STATUS_ACTIVE);
			return new ResponseEntity<>(new ResponseMessage("Verify successfully"), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseMessage("Invalid code"), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/customer/change-password")
	public ResponseEntity<?> changePassword(HttpServletRequest request, @RequestBody @Valid ChangePassword authRequest)
			throws IOException {
		String jwt = jwtTokenFilter.getAccessToken(request);
		if (jwt == null)
			return new ResponseEntity<>(new ResponseMessage("Token not found"), HttpStatus.NOT_FOUND);
		String email = jwtUtil.getUerNameFromToken(jwt);
		Optional<Customer> customer = customerService.findCustomerByEmail(email);
		if (!customer.isPresent())
			return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.NOT_FOUND);
		boolean matches = passwordEncoder.matches(authRequest.getOldPassword(), customer.get().getPassword());
		if (matches) {
			customer.get().setPassword(authRequest.getChangePassword());
			customerService.save(customer.get());
		} else {
			return new ResponseEntity<>(new ResponseMessage("Password does not match!"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new ResponseMessage("Update password successfully"), HttpStatus.OK);
	}

	@PostMapping("/customer/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> param) {
		String email = param.get("email");
		Optional<Customer> customer = customerService.findCustomerByEmail(email);
		if (!customer.isPresent()) {
			return new ResponseEntity<>(new ResponseMessage("Email does not exist!"), HttpStatus.BAD_REQUEST);
		}
		String randomString = RandomString.randomString();
		customerService.updateVerifycationCode(customer.get().getId(), randomString);
//		MailUtil.sendMail(email, "Ma code xac nhan", "Ma code xac nhan cua ban la: " + randomString);

		return new ResponseEntity<>(new ResponseMessage("Please check your code that sent via your email"),
				HttpStatus.OK);
	}

	@PostMapping("customer/update-password")
	public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> param) {
		String email = param.get("email");
		String password = param.get("password");
		Optional<Customer> customer = customerService.findCustomerByEmail(email);
		if (!customer.isPresent()) {
			return new ResponseEntity<>(new ResponseMessage("Email does not exist!"), HttpStatus.BAD_REQUEST);
		}
		customer.get().setPassword(password);
		customerService.save(customer.get());
		return new ResponseEntity<>(new ResponseMessage("Change password successfully"), HttpStatus.OK);

	}

	@GetMapping("/customers")
	public ResponseEntity<?> getListCustomer() {
		List<Customer> listCustomers = customerService.getAllCustomers();
		if (listCustomers.isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("List of Customers is empty!"), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(listCustomers, HttpStatus.OK);
	}

	@GetMapping("/customer/profile")
	public ResponseEntity<?> getCurrentCustomer(HttpServletRequest request) {
		String jwt = jwtTokenFilter.getAccessToken(request);
		if (jwt == null)
			return new ResponseEntity<>(new ResponseMessage("Token not found"), HttpStatus.NOT_FOUND);
		String email = jwtUtil.getUerNameFromToken(jwt);
		Optional<Customer> customer = customerService.findCustomerByEmail(email);
		if (!customer.isPresent())
			return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(customer.get(), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		String jwt = jwtTokenFilter.getAccessToken(request);
		if (jwt == null)
			return new ResponseEntity<>(new ResponseMessage("Token not found"), HttpStatus.NOT_FOUND);
		String email = jwtUtil.getUerNameFromToken(jwt);
		Optional<Customer> customer = customerService.findCustomerByEmail(email);
		if (!customer.isPresent())
			return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.NOT_FOUND);

		customerService.updateStatus(customer.get().getId(), Constants.STATUS_LOGOUT);
		return new ResponseEntity<>(new ResponseMessage("You have been logout!"), HttpStatus.OK);

	}
}
