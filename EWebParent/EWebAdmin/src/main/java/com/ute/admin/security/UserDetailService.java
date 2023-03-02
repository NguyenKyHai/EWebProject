package com.ute.admin.security;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ute.admin.user.IUserRepository;
import com.ute.admin.user.IUserService;
import com.ute.common.entity.User;

public class UserDetailService implements UserDetailsService {

	@Autowired
	IUserRepository userRepository;
	@Autowired
	IUserService userService;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found"));
		return UserPrinciple.build(user);
	}

	public User getCurrentUser() {
		Optional<User> user;
		String userName;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {

			userName = principal.toString();
		}

		if (userRepository.existsByEmail(userName)) {
			user = userService.findUserByEmail(userName);
		} else {

			user = Optional.of(new User());

			// user.get().setUsername("Anonymous");
		}
		return user.get();
	}
}