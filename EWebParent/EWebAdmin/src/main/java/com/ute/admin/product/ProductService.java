package com.ute.admin.product;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ute.common.entity.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ute.common.entity.Product;

@Service
public class ProductService implements IProductService {

    @Autowired
    IProductRepository productRepository;

    @Autowired
    IProductImageRepository productImageRepository;

    @Override
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Integer id) {

        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {

        if (product.getMainImage() == null || product.getMainImage() == "") {
            product.setMainImage("default.png");
        }
        product.setUpdatedTime(new Date());
        Product updatedProduct = productRepository.save(product);

        return updatedProduct;
    }

    @Override
    public Boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public void saveExtraImage( ProductImage productImage) {
        productImageRepository.save(productImage);
    }

    @Override
    public void deleteExtraImage(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductImage> listExtraImage() {
        return productImageRepository.findAll();
    }

}
