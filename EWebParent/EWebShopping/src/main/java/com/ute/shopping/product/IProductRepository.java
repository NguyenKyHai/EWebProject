package com.ute.shopping.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ute.common.entity.Product;

public interface IProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    Boolean existsByName(String name);

    @Query(value = "select p from Product p"
            + " where upper(p.name) like CONCAT('%',UPPER(?1),'%')"
            + " and upper(p.category.id) like CONCAT('%',UPPER(?2),'%') "
            + "and (p.price * (100 - p.discountPercent)/100 between ?3 and ?4)"
            )
    Page<Product> filterProduct(String productName, int categoryId, float minPrice, float maxPrice, Pageable pageable);

    @Modifying
    @Query(value = "Update products set average_rating = (average_rating * review_count + ?2)/(review_count + 1), "
            + " review_count = review_count + 1 "
            + " where id = ?1"
            , nativeQuery = true)
    void updateReviewCountAndAverageRating(Integer productId, double rating);

    @Modifying
    @Query(value = "Update products set average_rating = (average_rating * review_count - ?2)/(review_count -1), "
            + " review_count = review_count - 1 "
            + " where id = ?1"
            , nativeQuery = true)
    void revertReviewRating(Integer productId, double oldRating);
}
