package com.ute.shopping.review;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.Review;

public interface IReviewRepository extends JpaRepository<Review, Integer>{

}
