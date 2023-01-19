package com.ute.admin.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ute.common.entity.User;

public interface IUserRepository extends JpaRepository<User, Integer> {
	
	Boolean existsByEmail(String email);
	Optional<User> findByEmail(String email);
	
	@Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :id")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
}
