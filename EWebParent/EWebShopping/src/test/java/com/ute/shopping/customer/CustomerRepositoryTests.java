package com.ute.shopping.customer;

import com.ute.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import com.ute.common.entity.Customer;

import static org.assertj.core.api.Assertions.assertThat;

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
	
	@Test
	public void testFindByVerificationCode() {
		Customer customer = repo.findByVerificationCode("83506238");
		System.out.println(customer.getEmail());
	}

	@Test
	public void testUpdatePassword() {
		Integer customerId = 2;
		Customer customer = repo.findById(customerId).get();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "19110227";
		String encodedPassword = passwordEncoder.encode(rawPassword);

		customer.setPassword(encodedPassword);
		Customer updatedUser = repo.save(customer);
		assertThat(updatedUser.getPassword()).isEqualTo(encodedPassword);

	}
}
