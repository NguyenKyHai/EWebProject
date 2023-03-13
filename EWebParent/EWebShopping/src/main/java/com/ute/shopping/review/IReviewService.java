package com.ute.shopping.review;

import com.ute.common.entity.Customer;
import com.ute.common.entity.Review;

public interface IReviewService {
	Review save(Review review);
	boolean didCustomerReviewProduct(Customer customer, Integer productId);
	boolean canCustomerReviewProduct(Customer customer, Integer productId);
}
