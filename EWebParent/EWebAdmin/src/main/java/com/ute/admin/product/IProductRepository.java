package com.ute.admin.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.ute.common.entity.Product;

public interface IProductRepository extends JpaRepository<Product,Integer>{
	
	public Product findByName(String name);
	
}
