package com.ute.admin.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ute.common.entity.Product;

@Service
public class ProductService implements IProductService {

	@Autowired
	IProductRepository productRepository;

	@Override
	public List<Product> listAll() {
		return productRepository.findAll();
	}

	@Override
	public Product save(Product product) {
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		product.setUpdatedTime(new Date());

		Product updatedProduct = productRepository.save(product);
		//productRepository.updateReviewCountAndAverageRating(updatedProduct.getId());

		return updatedProduct;

	}

}
