package com.ute.admin.customer;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.ute.common.entity.Customer;
public interface ICustomerRepository extends JpaRepository<Customer, Integer>{

	Boolean existsByEmail(String email);

	Optional<Customer> findByEmail(String email);
	
	@Query("UPDATE Customer c SET c.status = :status WHERE c.id = :id")
	@Modifying
	public void updateStatus(Integer id, String status);

	@Query("UPDATE Customer c SET c.sessionString = ?2 WHERE c.id = ?1")
	@Modifying
	void updateSessionString(Integer id, String sessionString);
	
	@Query("SELECT c FROM Customer c " +
			"WHERE UPPER(c.fullName) like CONCAT('%',UPPER(?1),'%') " +
			" AND UPPER(c.email) like CONCAT('%',UPPER(?2),'%')")
	Page<Customer> filterCustomer (String fullNameFilter, String emailFilter, Pageable pageable);
}