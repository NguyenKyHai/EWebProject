package com.ute.shopping.security;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ute.common.entity.Customer;
import com.ute.shopping.customer.ICustomerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	ICustomerRepository customerRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer user = customerRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

		return UserPrincipal.create(user);
	}

	public Customer getCurrentCustomer() {
		Optional<Customer> customer;
		String userName;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}

		if (customerRepository.existsByEmail(userName)) {
			customer = customerRepository.findByEmail(userName);
		} else {

			customer = Optional.of(new Customer());

			// user.get().setUsername("Anonymous");
		}
		return customer.get();
	}

}