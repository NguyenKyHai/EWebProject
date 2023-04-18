package com.ute.shopping.product;

import java.util.List;
import java.util.Optional;
import com.ute.common.entity.Product;
import org.springframework.data.domain.Page;

public interface IProductService {
	List<Product> listAll();

	Optional<Product> findById(Integer id);

	Optional<Product> findByName(String name);

	void updateReviewRating(Integer productId, double oldRating);

	Page<Product> filterProducts(String productName, List<Integer> categoryId, float minPrice, float maxPrice,
								 int page, int size, List<String> sortBy, String order);
}
