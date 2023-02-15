package com.ute.shopping.customer;

import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ute.common.constants.Constants;
import com.ute.common.entity.Customer;
import com.ute.shopping.jwt.JwtTokenUtil;
import com.ute.shopping.request.LoginRequest;
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

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {

//		Optional<Customer> customerCheck = customerService.findCustomerByEmail(request.getEmail());
//
//		if (customerCheck.get().getStatus().equals(Constants.STATUS_BLOCKED)) {
//			return new ResponseEntity<>(new ResponseMessage("The customer have been blocked"), HttpStatus.BAD_REQUEST);
//		}

		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			Customer customer = (Customer) authentication.getPrincipal();
			String accessToken = jwtUtil.generateAccessToken(customer);
			customerService.updateStatus(customer.getId(),Constants.STATUS_ACTIVE);
			LoginResponse response = new LoginResponse(customer.getId(),customer.getEmail(), accessToken, customer.getFirstName(),
					customer.getLastName());

			return ResponseEntity.ok().body(response);

		} catch (BadCredentialsException ex) {
			return new ResponseEntity<>(new ResponseMessage("Please check your email or password!"),
					HttpStatus.UNAUTHORIZED);
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

	@PostMapping("/logout/{id}")
	public ResponseEntity<?> logout(@PathVariable Integer id) {
		try {
			customerService.updateStatus(id, Constants.STATUS_LOGOUT);
			return new ResponseEntity<>(new ResponseMessage("You have been logout!"), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(new ResponseMessage("customer not found"), HttpStatus.OK);
		}
	}
}
