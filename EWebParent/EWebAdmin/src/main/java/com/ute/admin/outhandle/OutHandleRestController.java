package com.ute.admin.outhandle;

import com.ute.admin.product.IProductService;
import com.ute.common.entity.Product;
import com.ute.common.entity.ProductImage;
import com.ute.common.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
public class OutHandleRestController {
    @Autowired
    IProductService productService;

    @GetMapping("/")
    public ResponseEntity<?> listProducts() {

        return new ResponseEntity<>("Welcome to our API HDK Web Admin!", HttpStatus.OK);
    }
    @GetMapping("/add-extra-image/{id}")
    public ResponseEntity<?> addExtraImage(@PathVariable Integer id, @RequestBody Map<String,String> param) {

        Optional<Product> product = productService.findById(id);
        if(!product.isPresent()){
            return  new ResponseEntity<>(new ResponseMessage("Product not found"),HttpStatus.NOT_FOUND);
        }
        String extraImage = param.get("extraImage").trim();
        Set<ProductImage> extraProductImage = product.get().getProductImages();
        ProductImage productImage = new ProductImage();
        productImage.setExtraImage(extraImage);
        productImage.setPublicId("link");
        productService.saveExtraImage(productImage);
        extraProductImage.add(productImage);
        product.get().setProductImages(extraProductImage);
        productService.save(product.get());

        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
