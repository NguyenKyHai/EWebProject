package com.ute.admin.user;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ute.common.entity.User;

@Service
@Transactional
public class UserService implements IUserService {
	@Autowired
	private IUserRepository userRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public void save(User user) {
		// TODO Auto-generated method stub
		encodePassword(user);
		userRepo.save(user);
	}

	@Override
	public boolean existsByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepo.existsByEmail(email);
	}

	@Override
	public User findUserById(Integer id) {
		// TODO Auto-generated method stub
		return userRepo.findById(id).get();
	}

	@Override
	public void deleteUserById(Integer id) {
		// TODO Auto-generated method stub
		userRepo.deleteById(id);
	}

}