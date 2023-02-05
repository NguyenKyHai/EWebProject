package com.ute.admin.user;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ute.admin.utils.SortedUtil;
import com.ute.common.entity.User;

@Service
@Transactional
public class UserService implements IUserService {
	
	
	
	@Autowired
	private IUserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);

		if (isUpdatingUser) {
			User existingUser = userRepository.findById(user.getId()).get();

			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}

		} else {
			encodePassword(user);
		}

		return userRepository.save(user);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public User findUserById(Integer id) {
		return userRepository.findById(id).get();
	}

	@Override
	public void deleteUserById(Integer id) {
		userRepository.deleteById(id);
	}

	@Override
	public void updateStatus(Integer id, String status) {
		userRepository.updateStatus(id, status);
	}

	@Override
	public Page<User> listByPage(String firstNameFilter, String lastNameFilter, 
								 int page, int size, List<String> sortList, String sortOrder) {
		
		 Pageable pageable = PageRequest.of(page-1, size, Sort.by(SortedUtil.createSortOrder(sortList, sortOrder)));
	
		
		return userRepository.findByFirstNameLikeAndLastNameLike(firstNameFilter, lastNameFilter, pageable);
	}
	
}