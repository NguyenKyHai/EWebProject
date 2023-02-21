package com.ute.shopping.customer;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ute.common.constants.Constants;
import com.ute.common.entity.Customer;
import com.ute.common.util.MailUtil;
import com.ute.common.util.RandomString;
import com.ute.shopping.jwt.JwtTokenFilter;
import com.ute.shopping.jwt.JwtTokenUtil;
import com.ute.shopping.request.LoginRequest;
import com.ute.shopping.request.SignupRequest;
import com.ute.shopping.response.LoginResponse;
import com.ute.shopping.response.ResponseMessage;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

	@Autowired
	AuthenticationManager authManager;
	@Autowired
	JwtTokenUtil jwtUtil;
	@Autowired
	ICustomerService customerService;
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
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			Customer customer = (Customer) authentication.getPrincipal();
			String accessToken = jwtUtil.generateAccessToken(customer);
			customerService.updateStatus(customer.getId(), Constants.STATUS_ACTIVE);
			LoginResponse response = new LoginResponse(customer.getId(), customer.getEmail(), accessToken,
					customer.getFullName());

			return ResponseEntity.ok().body(response);

		} catch (BadCredentialsException ex) {
			return new ResponseEntity<>(new ResponseMessage("Please check your email or password!"),
					HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@RequestBody @Valid SignupRequest signupRequest) {

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
		customer.setAuthenticationType(Constants.AUTH_TYPE_DATABASE);
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
			customerService.updateStatus(customer.getId(), Constants.STATUS_ACTIVE);
			customerService.updateVerifycationCode(customer.getId());
			return new ResponseEntity<>(new ResponseMessage("Verify successfully"), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseMessage("Invalid code"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/customers")
	public ResponseEntity<?> getListCustomer() {
		List<Customer> listCustomers = customerService.getAllCustomers();
		if (listCustomers.isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("List of users is empty!"), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(listCustomers, HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		String jwt = jwtTokenFilter.getAccessToken(request);
		if (jwt == null)
			return new ResponseEntity<>(new ResponseMessage("Token not found"), HttpStatus.NOT_FOUND);
		Customer customer = (Customer) jwtTokenFilter.getUserDetails(jwt);
		if (customer == null)
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.NOT_FOUND);
		Customer customerCheck = customerService.findCustomerByEmail(customer.getEmail()).get();
		try {
			customerService.updateStatus(customerCheck.getId(), Constants.STATUS_LOGOUT);
			return new ResponseEntity<>(new ResponseMessage("You have been logout!"), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(new ResponseMessage("customer not found"), HttpStatus.OK);
		}
	}
}
