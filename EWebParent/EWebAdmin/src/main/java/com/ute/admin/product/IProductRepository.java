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
    @Query(value = "select p from Product p"
            + " where upper(p.name) like CONCAT('%',UPPER(?1),'%')"
            + " and upper(p.category.id) like CONCAT('%',UPPER(?2),'%') "
            + "and (p.price * (100 - p.discountPercent)/100 between ?3 and ?4)"
    )
    Page<Product> filterProduct(String productName, int categoryId, float minPrice, float maxPrice, Pageable pageable);
    @Query(value = "Select p.recommend from products p join order_details d on p.id=d.product_id join orders o on d.order_id = o.id " +
            "Where o.status = ?2 and o.customer_id = ?1", nativeQuery = true)
    List<Map<String, String>> findRecommendProduct(Integer customerId, String status);
}
