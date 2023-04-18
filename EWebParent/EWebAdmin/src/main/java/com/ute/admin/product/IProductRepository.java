package com.ute.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ute.common.entity.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


public interface IProductRepository extends JpaRepository<Product, Integer> {

    Product findByName(String name);

    Boolean existsByName(String name);
    @Query(value =
              "select p from Product p"
            + " where upper(p.name) like CONCAT('%',UPPER(?1),'%')"
            + " and p.category.id in ?2 "
            + "and (p.price * (100 - p.discountPercent)/100 between ?3 and ?4)"
    )
    Page<Product> filterProduct(String productName, List<Integer> categoryId, float minPrice, float maxPrice, Pageable pageable);

    @Query("Select p from Product p where (p.quantity > p.sold) and (p.quantity - p.sold < 3)")
    List<Product>productsInStock();
}
