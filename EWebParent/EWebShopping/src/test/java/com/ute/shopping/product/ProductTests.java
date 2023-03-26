package com.ute.shopping.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ute.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductTests {

	@Autowired
	IProductRepository repo;

	@Test
	public void getAll() {
		Iterable<Product> productsFilter = repo.findAll();
		productsFilter.forEach(p -> System.out.println(p.getName()));

	}
}
