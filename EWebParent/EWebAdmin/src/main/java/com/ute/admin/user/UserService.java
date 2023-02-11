package com.ute.admin.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ute.admin.role.RoleService;
import com.ute.admin.utils.SortedUtil;
import com.ute.common.constants.Constants;
import com.ute.common.entity.Role;
import com.ute.common.entity.User;

@Service
@Transactional
public class UserService implements IUserService {
	
	
	
	@Autowired
	private IUserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleService roleService;

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
	
	@Override
	public Set<Role> addRoles(Set<String> strRole) {
		Set<Role> roles = new HashSet<>();
		strRole.forEach(role -> {
			switch (role) {
			case Constants.ROLE_ADMIN:
				Role adminRole = roleService.findByName(Constants.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(adminRole);
				break;
			case Constants.ROLE_SALESPERSON:
				Role salesRole = roleService.findByName(Constants.ROLE_SALESPERSON)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(salesRole);
				break;
			case Constants.ROLE_ASSISTANT:
				Role assistantRole = roleService.findByName(Constants.ROLE_ASSISTANT)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(assistantRole);
				break;
			case Constants.ROLE_SHIPPER:
				Role shipperRole = roleService.findByName(Constants.ROLE_SHIPPER)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(shipperRole);
				break;
			case Constants.ROLE_EDITOR:
				Role editorRole = roleService.findByName(Constants.ROLE_EDITOR)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(editorRole);
				break;
			}
		});
		return roles;
	}
	

}