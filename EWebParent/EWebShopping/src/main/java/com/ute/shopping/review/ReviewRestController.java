package com.ute.shopping.review;

import com.ute.common.response.ResponseMessage;
import com.ute.common.response.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ute.common.entity.Customer;
import com.ute.common.entity.Product;
import com.ute.common.entity.Review;
import com.ute.common.request.ReviewRequest;
import com.ute.shopping.product.IProductService;
import com.ute.shopping.security.CustomUserDetailsService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReviewRestController {

    @Autowired
    IReviewService reviewService;

    @Autowired
    IProductService productService;


    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/review/{productId}")
    public ResponseEntity<?> createReview(@PathVariable Integer productId, @RequestBody ReviewRequest request) {

        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer.isBlockAccount()) {
            return new ResponseEntity<>(new ResponseMessage("Your account is blocked."),
                    HttpStatus.UNAUTHORIZED);
        }

        Product product = productService.findById(productId).get();
        boolean canCustomerReviewProduct = reviewService.canCustomerReviewProduct(customer, product.getId());
        if (canCustomerReviewProduct == false) {
            return new ResponseEntity<>(new ResponseMessage("This customer is not permission to review this product"),
                    HttpStatus.BAD_REQUEST);
        }
        boolean didCustomerReviewProduct = reviewService.didCustomerReviewProduct(customer, product.getId());
        Review review = new Review();
        if (didCustomerReviewProduct == false) {
            review.setProduct(product);
            review.setCustomer(customer);
        } else {
            List<Review> reviews = reviewService.findByCustomerAndProduct(customer.getId(), product.getId());
            review = reviews.get(0);
            if (product.getReviewCount() == 1) {
                product.setAverageRating(0);
                product.setReviewCount(0);
            } else {
                productService.updateReviewRating(productId, product.getAverageRating());
            }
            review.setUpdateReviewTime(new Date());
        }
        review.setComment(request.getComment());
        String rating = request.getRating();
        if (rating == null) rating = "5";
        review.setRating(Integer.parseInt(rating));
        reviewService.save(review);
        return new ResponseEntity<>(new ResponseMessage("Review successfully"), HttpStatus.OK);
    }

    @GetMapping("/reviewByCustomer/{productId}")
    public ResponseEntity<?> getReview(@PathVariable Integer productId) {

        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer.isBlockAccount()) {
            return new ResponseEntity<>(new ResponseMessage("Your account is blocked."),
                    HttpStatus.UNAUTHORIZED);
        }
        Product product = productService.findById(productId).get();
        List<Review> reviews = reviewService.findByCustomerAndProduct(customer.getId(), product.getId());
        if (reviews.isEmpty()) {
            return new ResponseEntity<>(new Review(), HttpStatus.OK);
        }
        Review review = reviews.get(0);
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setComment(review.getComment());
        response.setRating(String.valueOf(review.getRating()));
        response.setReviewTime(review.getReviewTime().toString());
        response.setUpdateReviewTime(review.getUpdateReviewTime().toString());
        response.setCustomerName(review.getCustomer().getFullName());
        response.setCustomerPhoto(review.getCustomer().getPhotos());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/review/{productId}")
    public ResponseEntity<?> getReviewByProductId(@PathVariable Integer productId) {
        List<Review> reviews = reviewService.findByProductId(productId);
        List<ReviewResponse> response = new ArrayList<>();

        if(!reviews.isEmpty()) {
            for (Review review : reviews) {
                ReviewResponse r = new ReviewResponse();
                r.setId(review.getId());
                r.setComment(review.getComment());
                r.setRating(String.valueOf(review.getRating()));
                r.setReviewTime(review.getReviewTime().toString());
                r.setUpdateReviewTime(review.getUpdateReviewTime().toString());
                r.setCustomerName(review.getCustomer().getFullName());
                r.setCustomerPhoto(review.getCustomer().getPhotos());
                response.add(r);
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
