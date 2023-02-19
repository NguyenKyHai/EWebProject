package com.ute.shopping.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import com.ute.common.entity.Customer;
import com.ute.shopping.customer.ICustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
	@Autowired
	private ICustomerRepository repo;

	@Test
	public void testCreateNewCustomer() {
		Customer customer =new Customer();
		customer.setEmail("abc123345@gmail.com");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "19110197";
		String encodedPassword = passwordEncoder.encode(rawPassword);
		customer.setPassword(encodedPassword);
		customer.setFullName("abc");
		
	
		repo.save(customer);
	}
}
