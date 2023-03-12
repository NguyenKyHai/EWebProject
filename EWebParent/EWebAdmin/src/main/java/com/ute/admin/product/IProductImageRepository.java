package com.ute.admin.product;

import com.ute.common.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductImageRepository extends JpaRepository<ProductImage,Integer> {
}
