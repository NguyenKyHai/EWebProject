package com.ute.admin.user;


import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ute.common.entity.User;

@Service
@Transactional
public class UserService implements IUserService {
	
	public static final int USERS_PER_PAGE = 4;
	
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
	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);

		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();

			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}

		} else {
			encodePassword(user);
		}

		return userRepo.save(user);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}

	@Override
	public User findUserById(Integer id) {
		return userRepo.findById(id).get();
	}

	@Override
	public void deleteUserById(Integer id) {
		userRepo.deleteById(id);
	}

	@Override
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepo.updateEnabledStatus(id, enabled);
	}

	@Override
	public Page<User> listByPage(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE);
		return userRepo.findAll(pageable);
	}
	
	

}