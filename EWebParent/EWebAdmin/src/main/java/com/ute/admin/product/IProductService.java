package com.ute.admin.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ute.common.entity.Product;
import com.ute.common.entity.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
	List<Product> listAll();
	Optional<Product>findById(Integer id);
	Product save(Product product);
	Boolean existsByName(String name);
	Product findByName(String name);
	void saveExtraImage( ProductImage productImage);
	void deleteExtraImage(Integer id);

	List<ProductImage> listExtraImage();

	Page<Product> filterProducts(String productName, List<Integer> categoryId, BigDecimal minPrice, BigDecimal maxPrice,
								 Integer  quantity, Integer  sold, int page, int size, List<String> sortBy, String order);

}
