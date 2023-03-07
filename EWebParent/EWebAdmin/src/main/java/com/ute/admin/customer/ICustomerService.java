package com.ute.admin.customer;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import com.ute.common.entity.Customer;

public interface ICustomerService {
	
	List<Customer> getAllCustomers();

	Customer save(Customer customer);

	boolean existsByEmail(String email);
	
	Optional<Customer> findCustomerById(Integer id);
	
	Optional<Customer> findCustomerByEmail(String email);
	
	void updateStatus(Integer id, String status);
	
	Page<Customer> filterCustomers(String fullNameFilter, int page, int size, List<String> sortBy, String order);
	
	void deleteCustomerById(Integer id);

}
