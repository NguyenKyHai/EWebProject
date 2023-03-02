package com.ute.admin.user;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ute.common.entity.User;

public interface IUserRepository extends JpaRepository<User, Integer> {
	
	Boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	@Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
	@Modifying
	public void updateStatus(Integer id, String status);

	@Query("SELECT u FROM User u WHERE UPPER(u.fullName) like CONCAT('%',UPPER(?1),'%')")
	Page<User> findByFirstNameLikeAndLastNameLike(String fullNameFilter, Pageable pageable);

}
