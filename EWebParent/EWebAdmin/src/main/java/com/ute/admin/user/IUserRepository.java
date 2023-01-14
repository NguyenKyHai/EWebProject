package com.ute.admin.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.User;

public interface IUserRepository extends JpaRepository<User, Integer> {
	
	Boolean existsByEmail(String email);
	Optional<User> findByEmail(String email);
}
