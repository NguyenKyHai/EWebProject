package com.ute.shopping.product;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ute.common.entity.Product;

public interface IProductRepository extends JpaRepository<Product, Integer>{

}
