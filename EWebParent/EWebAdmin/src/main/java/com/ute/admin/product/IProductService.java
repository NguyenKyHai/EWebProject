package com.ute.admin.product;

import java.util.List;
import java.util.Optional;

import com.ute.common.entity.Product;

public interface IProductService {
	List<Product> listAll();
	Optional<Product>findById(Integer id);
	Product save(Product product);
	Boolean existsByName(String name);
	public Product findByName(String name);
}
