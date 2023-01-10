package com.ute.admin.user;

import java.util.List;
import java.util.Optional;

import com.ute.common.entity.User;

public interface IUserService {
	 List<User> getAllUsers();
	 void save(User user);
	 boolean existsByEmail(String email);
	 User findUserById(Integer id);
	 void deleteUserById(Integer id);
}
