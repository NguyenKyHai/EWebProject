package com.ute.admin.product;

import java.util.List;
import java.util.Optional;

import com.ute.common.entity.Product;
import com.ute.common.entity.ProductImage;

public interface IProductService {
	List<Product> listAll();
	Optional<Product>findById(Integer id);
	Product save(Product product);
	Boolean existsByName(String name);
	Product findByName(String name);
	void saveExtraImage( ProductImage productImage);
	void deleteExtraImage(Integer id);

	List<ProductImage> listExtraImage();
}
