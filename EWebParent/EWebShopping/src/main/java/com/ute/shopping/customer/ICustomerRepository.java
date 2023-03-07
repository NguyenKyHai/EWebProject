package com.ute.shopping.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.ute.common.entity.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Integer>{

	Boolean existsByEmail(String email);

	Optional<Customer> findByEmail(String email);
	
	@Query("UPDATE Customer c SET c.status = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateStatus(Integer id, String status);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	public Customer findByVerificationCode(String code);
	
	@Query("UPDATE Customer c SET c.provider = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateAuthenticationType(Integer customerId, String type);
	
	@Query("UPDATE Customer c SET c.verificationCode = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateVerifycationCode(Integer customerId, String code);
}
