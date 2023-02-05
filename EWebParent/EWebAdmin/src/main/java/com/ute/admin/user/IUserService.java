package com.ute.admin.user;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ute.common.entity.User;

public interface IUserService {
	List<User> getAllUsers();

	User save(User user);

	boolean existsByEmail(String email);

	User findUserById(Integer id);

	void deleteUserById(Integer id);

	void updateStatus(Integer id, String status);

	public Page<User> listByPage(String firstNameFilter, String lastNameFilter, 
								int page, int size,List<String> sortList, String sortOrder);
}
