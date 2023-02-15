package com.ute.shopping.customer;

import java.util.List;
import java.util.Optional;
import com.ute.common.entity.Customer;

public interface ICustomerService {
	List<Customer> getAllCustomers();

	Customer save(Customer customer);

	boolean existsByEmail(String email);
	
	Optional<Customer> findCustomerById(Integer id);
	
	Optional<Customer> findCustomerByEmail(String email);
	
	void deleteCustomerById(Integer id);

	void updateStatus(Integer id, String status);
}
