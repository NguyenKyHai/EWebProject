package com.ute.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ute.common.constants.Constants;
import com.ute.common.entity.Role;
import com.ute.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private IUserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "19110197";
		String encodedPassword = passwordEncoder.encode(rawPassword);
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userHai = new User("19110197@student.hcmute.edu.vn", encodedPassword, "Hai Nguyen");
		userHai.addRole(roleAdmin);

		User savedUser = repo.save(userHai);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNewUserWithTwoRoles() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "19110227";
		String encodedPassword = passwordEncoder.encode(rawPassword);
		User userKhanh = new User("19110227@student.hcmute.edu.vn", encodedPassword, "Khanh Tran Nguyen");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(4);

		userKhanh.addRole(roleEditor);
		userKhanh.addRole(roleAssistant);

		User savedUser = repo.save(userKhanh);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		List<User> listUsers = repo.findAll();
		if (!listUsers.isEmpty())
			listUsers.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById() {
		User user = repo.findById(1).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}

	@Test
	public void testUpdateStatus() {
		repo.updateStatus(1, Constants.STATUS_ACTIVE);
		User user = repo.findById(1).get();
		assertThat(user.getStatus()).matches(Constants.STATUS_ACTIVE);
	}

	@Test
	public void testUpdateUserRoles() {
		User userKhanh = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);

		userKhanh.getRoles().remove(roleEditor);
		userKhanh.addRole(roleSalesperson);

		repo.save(userKhanh);
	}

	@Test
	public void testUpdatePassword() {
		Integer userId = 10;
		User user = repo.findById(userId).get();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "19110197";
		String encodedPassword = passwordEncoder.encode(rawPassword);

		user.setPassword(encodedPassword);
		User updatedUser = repo.save(user);
		assertThat(updatedUser.getPassword()).isEqualTo(encodedPassword);

	}

	@Test
	public void testUpdateRole() {
		Integer userId = 1;
		Integer roleId = 3;
		User user = repo.findById(userId).get();
		user.addRole(new Role(roleId));
		user.addRole(new Role(4));
		repo.save(user);
	}

	@Test
	public void testListFirstPage() {
		int pageNumber = 1;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isEqualTo(pageSize);
	}

	@Test
	public void testGetRole() {
		Integer id = 1;
		User user = repo.findById(id).get();
		user.getRoles().forEach(role -> System.out.println(role.getName()));

	}
}
