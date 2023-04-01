package com.ute.shopping.review;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.Review;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IReviewRepository extends JpaRepository<Review, Integer>{

    @Query("SELECT COUNT(r.id) FROM Review r WHERE r.customer.id = ?1 AND "
            + "r.product.id = ?2")
    Long countByCustomerAndProduct(Integer customerId, Integer productId);

    @Query("SELECT r FROM Review r WHERE r.customer.id = ?1 AND "
            + "r.product.id = ?2")
    List<Review> findByCustomerAndProduct(Integer customerId, Integer productId);

    @Query("SELECT r FROM Review r WHERE r.product.id = ?1")
    List<Review> findByProductId( Integer productId);
}
