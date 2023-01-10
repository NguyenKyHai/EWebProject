package com.ute.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

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
		Role roleAdmin = entityManager.find(Role.class, 2);
		User userHai = new User("19110197@student.hcmute.edu.vn", "19110197", "Hai", "Nguyen");
		userHai.addRole(roleAdmin);
		
		User savedUser = repo.save(userHai);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userKhanh = new User("19110227@student.hcmute.edu.vn", "19110227", "Khanh", "Tran Nguyen");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userKhanh.addRole(roleEditor);
		userKhanh.addRole(roleAssistant);
		
		User savedUser = repo.save(userKhanh);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User user = repo.findById(1).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userHai = repo.findById(1).get();
		userHai.setEnabled(true);
		userHai.setEmail("19110197@student.hcmute.vn");
	
		repo.save(userHai);
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
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
		
	}
}