package com.ute.admin.product;


import com.ute.common.response.ProductReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ute.common.entity.Product;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;


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

    @Query("Select p from Product p where p.sold > ?1  and p.sold< ?2 " +
            " ORDER BY p.sold DESC")
    Page<Product>productsInStock(Integer min, Integer max, Pageable pageable);

    @Query("Select p from Product p where p.sold > ?1  and p.sold< ?2 " +
            " ORDER BY p.sold DESC")
    Page<Product>bestSellingProduct(Integer min, Integer max, Pageable pageable);

}
