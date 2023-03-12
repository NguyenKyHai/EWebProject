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
	
	Customer findByVerificationCode(String code);
	
	void updateStatus(Integer id, String status);
	void updateSessionString(Integer id, String sessionString);
	
	void updateAuthenticationType(Integer customerId, String type);
	
	void updateVerificationCode(Integer customerId, String code);
	
	void deleteCustomerById(Integer id);

}
