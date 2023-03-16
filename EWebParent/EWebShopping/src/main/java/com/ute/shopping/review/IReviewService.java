package com.ute.shopping.review;

import com.ute.common.entity.Customer;
import com.ute.common.entity.Review;

import java.util.List;
import java.util.Optional;

public interface IReviewService {
	Review save(Review review);

	Optional<Review> findById(Integer id);
	boolean didCustomerReviewProduct(Customer customer, Integer productId);
	boolean canCustomerReviewProduct(Customer customer, Integer productId);

	List<Review> findByCustomerAndProduct(Integer customerId, Integer productId);
}
