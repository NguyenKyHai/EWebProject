package com.ute.admin.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.ute.common.entity.Role;
import com.ute.common.entity.User;

public interface IUserService {
	List<User> getAllUsers();

	User save(User user);

	boolean existsByEmail(String email);
	
	Optional<User> findUserById(Integer id);
	
	Optional<User> findUserByEmail(String email);
	
	void deleteUserById(Integer id);

	void updateStatus(Integer id, String status);

	Page<User> listByPage(String firstNameFilter, String lastNameFilter, 
								int page, int size,List<String> sortList, String sortOrder);
	Set<Role> addRoles(Set<String> strRole);
}
