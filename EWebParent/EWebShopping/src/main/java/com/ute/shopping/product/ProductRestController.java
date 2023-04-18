package com.ute.shopping.product;

import java.util.List;
import java.util.Optional;

import com.ute.common.entity.Customer;
import com.ute.common.entity.User;
import com.ute.shopping.review.IReviewService;
import com.ute.shopping.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ute.common.entity.Product;
import com.ute.common.response.ResponseMessage;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ProductRestController {

    @Autowired
    IProductService productService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    IReviewService reviewService;

    @Autowired

    @GetMapping("/products")
    public ResponseEntity<?> listProducts() {
        List<Product> products = productService.listAll();
        if (products.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("List of users is empty!"), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        Optional<Product> product = productService.findById(id);
        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (!product.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("Product not found"), HttpStatus.NOT_FOUND);
        }

        boolean didCustomerReviewProduct = reviewService.didCustomerReviewProduct(customer, product.get().getId());
        boolean canCustomerReviewProduct = reviewService.canCustomerReviewProduct(customer, product.get().getId());
        product.get().setReviewedByCustomer(didCustomerReviewProduct);
        product.get().setCustomerCanReview(canCustomerReviewProduct);

        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @GetMapping("/products/filter")
    public Page<Product> filterAdnSortedUser(@RequestParam(defaultValue = "") String productName,
                                             @RequestParam(defaultValue = "1") List <Integer> categoryId,
                                             @RequestParam(defaultValue = "0") float minPrice,
                                             @RequestParam(defaultValue = "150000000") float maxPrice,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "20") int size,
                                             @RequestParam(defaultValue = "id") List<String> sortBy,
                                             @RequestParam(defaultValue = "DESC") Sort.Direction order) {

        return productService
                .filterProducts(productName, categoryId, minPrice, maxPrice, page, size, sortBy, order.toString());
    }

}
