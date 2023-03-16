package com.ute.shopping.review;

import com.ute.common.response.ResponseMessage;
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
        review.setRating(Integer.parseInt(request.getRating()));
        reviewService.save(review);
        return new ResponseEntity<>(new ResponseMessage("Review successfully"),HttpStatus.OK);
    }

    @GetMapping("/review/{productId}")
    public ResponseEntity<?> getReview(@PathVariable Integer productId) {

        Customer customer = customUserDetailsService.getCurrentCustomer();
        Product product = productService.findById(productId).get();
        boolean didCustomerReviewProduct = reviewService.didCustomerReviewProduct(customer, product.getId());
        boolean canCustomerReviewProduct = reviewService.canCustomerReviewProduct(customer, product.getId());
        if (canCustomerReviewProduct == false) {
            return new ResponseEntity<>(new ResponseMessage("This customer is not permission to review this product"),
                    HttpStatus.BAD_REQUEST);
        }
        if (didCustomerReviewProduct == false) {
            return new ResponseEntity<>(new ResponseMessage("This customer is not comment yet"),
                    HttpStatus.BAD_REQUEST);
        }

        List<Review> reviews = reviewService.findByCustomerAndProduct(customer.getId(), product.getId());
        Review review = reviews.get(0);

        return new ResponseEntity<>(review, HttpStatus.OK);
    }


}
