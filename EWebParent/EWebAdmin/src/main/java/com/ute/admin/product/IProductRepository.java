package com.ute.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ute.common.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Integer> {

    Product findByName(String name);

    Boolean existsByName(String name);

    @Query(value =
            "select p from Product p"
                    + " where upper(p.name) like CONCAT('%',UPPER(:productName),'%')"
                    + " and p.category.id in :categoryId "
                    + " and (:quantity is null or p.sold = :quantity) "
                    + " and (:sold is null or p.sold = :sold) "
                    + " and p.price between :minPrice and :maxPrice"
    )
    Page<Product> filterProduct(@Param("productName") String productName,
                                @Param("categoryId") List<Integer> categoryId,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice,
                                @Param("quantity") Integer  quantity,
                                @Param("sold") Integer  sold,
                                Pageable pageable);

}
