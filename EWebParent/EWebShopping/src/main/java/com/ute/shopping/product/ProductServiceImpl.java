package com.ute.shopping.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ute.common.util.SortedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.ute.common.entity.Product;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {

    @Autowired
    IProductRepository productRepository;

    @Override
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Integer id) {

        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> findByName(String name) {

        return productRepository.findByName(name);
    }

    @Override
    public void updateReviewRating(Integer productId, double oldRating) {
        productRepository.revertReviewRating(productId, oldRating);
    }

    @Override
    public List<Product> getProductBySameCategory(Integer categoryId) {
       return productRepository.getProductBySameCategory(categoryId);
    }


    @Override
    public Page<Product> filterProducts(String productName, List<Integer> categoryId, BigDecimal minPrice, BigDecimal maxPrice,
                                        int page, int size, List<String> sortBy, String order) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(SortedUtil.createListSortOrder(sortBy, order)));

        return productRepository.filterProduct(productName, categoryId, minPrice, maxPrice, pageable);
    }

}
