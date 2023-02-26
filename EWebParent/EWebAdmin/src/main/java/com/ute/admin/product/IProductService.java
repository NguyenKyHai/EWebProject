package com.ute.admin.product;

import java.util.List;

import com.ute.common.entity.Product;

public interface IProductService {
	List<Product> listAll();

	Product save(Product product);
}
