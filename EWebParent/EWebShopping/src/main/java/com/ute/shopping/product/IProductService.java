package com.ute.shopping.product;

import java.util.List;
import java.util.Optional;

import com.ute.common.entity.Product;

public interface IProductService {
	List<Product> listAll();

	Optional<Product> findById(Integer id);

	Optional<Product> findByName(String name);

	void updateReviewRating(Integer productId, double oldRating);
}
