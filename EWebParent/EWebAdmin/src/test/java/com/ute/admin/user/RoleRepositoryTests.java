package com.ute.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ute.admin.role.IRoleRepository;
import com.ute.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

	@Autowired
	IRoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role admin=new Role("ROLE_ADMIN","manage everything");
		Role saveRole=repo.save(admin);
		assertThat(saveRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles() {
		Role roleSalesperson = new Role("ROLE_SALESPERSON", "manage product price, "
				+ "customers, shipping, orders and sales report");
		
		Role roleEditor = new Role("ROLE_EDITOR", "manage categories, brands, "
				+ "products, articles and menus");
		
		Role roleShipper = new Role("ROLE_SHIPPER", "view products, view orders "
				+ "and update order status");
		
		Role roleAssistant = new Role("ROLE_ASSISTANT", "manage questions and reviews");
		
		repo.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
	}
}
