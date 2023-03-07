package com.ute.admin.category;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ute.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {

	@Autowired
	private ICategoryRepository repo;

	@Test
	public void testGetAllCategories() {
		List<Category> list = repo.findAll();
		if (!list.isEmpty())
			list.stream().forEach(category -> System.out.println(category.getName()));
	}

	@Test
	public void testFindByName() {
		Category category = repo.findByName("Máy tính").get();
		if (category != null)
			System.out.println(category.getName());
	}

}
