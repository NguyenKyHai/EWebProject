package com.ute.shopping.product;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ute.common.entity.Product;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductService implements IProductService {

	@Autowired
	IProductRepository productRepository;

	@Override
	public List<Product> listAll() {
		return productRepository.findAll();
	}
	
	@Override
	public Optional<Product> findById(Integer id) {
		
		return productRepository.findById(id);
	}

	@Override
	public Optional<Product> findByName(String name) {

		return productRepository.findByName(name);
	}

	@Override
	public void updateReviewRating(Integer productId, double oldRating) {
		productRepository.revertReviewRating(productId, oldRating);
	}

}
