package com.ute.admin.user;

import java.util.List;

import com.ute.common.entity.User;

public interface IUserService {
	 List<User> getAllUsers();
	 User save(User user);
	 boolean existsByEmail(String email);
	 User findUserById(Integer id);
	 void deleteUserById(Integer id);
	 void updateUserEnabledStatus(Integer id, boolean enabled);
}
