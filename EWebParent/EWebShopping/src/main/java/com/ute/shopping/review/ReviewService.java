package com.ute.shopping.review;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ute.common.entity.Review;
import com.ute.shopping.product.IProductRepository;

@Service
@Transactional
public class ReviewService implements IReviewService {

	@Autowired
	IReviewRepository reviewRepository;
	
	@Autowired
	IProductRepository productRepository;

	@Override
	public Review save(Review review) {
		review.setReviewTime(new Date());
		Review savedReview = reviewRepository.save(review);
		
		Integer productId = savedReview.getProduct().getId();		
		productRepository.updateReviewCountAndAverageRating(productId, savedReview.getRating());
		
		return savedReview;
	}

}
