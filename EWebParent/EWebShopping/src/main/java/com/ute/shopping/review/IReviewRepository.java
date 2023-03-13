package com.ute.shopping.review;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.Review;
import org.springframework.data.jpa.repository.Query;

public interface IReviewRepository extends JpaRepository<Review, Integer>{

    @Query("SELECT COUNT(r.id) FROM Review r WHERE r.customer.id = ?1 AND "
            + "r.product.id = ?2")
    Long countByCustomerAndProduct(Integer customerId, Integer productId);
}
