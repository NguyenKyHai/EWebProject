package com.ute.admin.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.Product;

public interface IProductRepository extends JpaRepository<Product,Integer>{
	
	Product findByName(String name);
	Boolean existsByName(String name);
	
	
}
