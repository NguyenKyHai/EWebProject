package com.ute.shopping.product;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ute.common.entity.Product;

public interface IProductRepository extends JpaRepository<Product, Integer> {

	public Product findByName(String name);

	Boolean existsByName(String name);

//	public Page<Product> filterProduct(String name);
}
