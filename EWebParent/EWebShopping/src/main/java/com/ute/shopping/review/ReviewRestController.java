package com.ute.shopping.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ute.common.entity.Customer;
import com.ute.common.entity.Product;
import com.ute.common.entity.Review;
import com.ute.common.request.ReviewRequest;
import com.ute.shopping.product.IProductService;
import com.ute.shopping.security.CustomUserDetailsService;

@RestController
@RequestMapping("/api")
public class ReviewRestController {

	@Autowired
	IReviewService reviewService;

	@Autowired
	IProductService productservice;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@PostMapping("/review/{id}")
	public ResponseEntity<?> createReview(@PathVariable Integer id, @RequestBody ReviewRequest request) {

		Customer customer = customUserDetailsService.getCurrentCustomer();
		Product product = productservice.findById(id).get();
		Review review = new Review();
		review.setComment(request.getComment());
		review.setRating(Integer.parseInt(request.getRating()));
		review.setProduct(product);
		review.setCustomer(customer);
		reviewService.save(review);

		return new ResponseEntity<>(review, HttpStatus.OK);
	}

}
