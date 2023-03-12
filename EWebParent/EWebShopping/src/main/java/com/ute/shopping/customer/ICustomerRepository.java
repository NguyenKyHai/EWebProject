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
	void updateStatus(Integer id, String status);

	@Query("UPDATE Customer c SET c.sessionString = ?2 WHERE c.id = ?1")
	@Modifying
	void updateSessionString(Integer id, String sessionString);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	Customer findByVerificationCode(String code);
	
	@Query("UPDATE Customer c SET c.provider = ?2 WHERE c.id = ?1")
	@Modifying
	void updateAuthenticationType(Integer customerId, String type);
	
	@Query("UPDATE Customer c SET c.verificationCode = ?2 WHERE c.id = ?1")
	@Modifying
	void updateVerificationCode(Integer customerId, String code);
}
