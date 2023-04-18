package com.ute.shopping.review;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ute.common.constants.Constants;
import com.ute.common.entity.Customer;
import com.ute.shopping.order.IOrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ute.common.entity.Review;
import com.ute.shopping.product.IProductRepository;

@Service
@Transactional
public class ReviewServiceImpl implements IReviewService {

    @Autowired
    IReviewRepository reviewRepository;

    @Autowired
    IProductRepository productRepository;

    @Autowired
    IOrderDetailRepository orderDetailRepository;

    @Override
    public Review save(Review review) {
        review.setReviewTime(new Date());
        Review savedReview = reviewRepository.save(review);

        Integer productId = savedReview.getProduct().getId();
        productRepository.updateReviewCountAndAverageRating(productId, savedReview.getRating());

        return savedReview;
    }

    @Override
    public Optional<Review> findById(Integer id) {
        return reviewRepository.findById(id);
    }

    @Override
    public boolean didCustomerReviewProduct(Customer customer, Integer productId) {
        Long count = reviewRepository.countByCustomerAndProduct(customer.getId(), productId);
        return count > 0;
    }

    @Override
    public boolean canCustomerReviewProduct(Customer customer, Integer productId) {
        Long count = orderDetailRepository.countByProductAndCustomerAndOrderStatus(productId,
                customer.getId(), Constants.ORDER_STATUS_DELIVERED);
        return count > 0;
    }

    @Override
    public List<Review> findByCustomerAndProduct(Integer customerId, Integer productId) {
      return reviewRepository.findByCustomerAndProduct(customerId, productId);
    }

    @Override
    public List<Review> findByProductId(Integer productId) {
        return reviewRepository.findByProductId(productId);
    }

}
